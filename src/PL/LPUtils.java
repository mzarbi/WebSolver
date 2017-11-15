package PL;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import commons.EModel;
import commons.EModelComparator;

public class LPUtils {
	
	public double[][][] costs;
	public double[][][] revenus;
	public double[][][] ROI;
	
	public int RegionCount;
	
	public int TemporalHorizon;
	public int TechCount;
	
	public double annualBudget;
	public int ppy;
	public double [][] regions ;
	public double C_ONT_Unitary  ;
	public double FiberCost  ; 
	public double TrenchCost  ;
	public double CivilEngineering  ;
	public double miniMSANCost  ;
	public double [] offers ;
	
	public LPUtils(int RegC,int TemH,int TechC,
				   double annualBudget2, int ppY,double[][] regs,
				   double OntC,double FC,double TC, double mmC){
		
		RegionCount = RegC ;
		TemporalHorizon = TemH ;
		TechCount = TechC ;
		annualBudget = annualBudget2 ;
		ppy = ppY ;
		regions = regs ;
		C_ONT_Unitary = OntC ;
		FiberCost = FC ;
		TrenchCost = TC ;
		miniMSANCost = mmC ;
		
		init() ;
		
	}
	
	public LPUtils(){
		
	}
	
	public double [][][][] Conv(double [][][] revenu,double [][][] costs){
		double [][][][] tmp = new double [RegionCount][TemporalHorizon][TechCount][2] ;
		
		for (int i = 0 ; i < RegionCount; i++){
			for (int j = 0 ; j < TemporalHorizon; j++){
				for (int k = 0 ; k < TechCount; k++){
					tmp[i][j][k][0] = revenu[i][j][k] ;
					tmp[i][j][k][1] = costs[i][j][k] ;
				}
			}
		}
		
		return tmp ;
	}
	
	
	public void init(){
		/*
		 * cette fonction initialise l'exemple par défault.
		 */
		offers = new double [] {1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 };
		
		costs = computeCosts() ;
		revenus = computeRevenus() ;
		ROI = computeROI(costs,revenus) ;
		
		//new Utils().printRegions(0,regions) ;
		//new Utils().printRegions(1,regions) ;
		//new Utils().printRegions(2,regions) ;
		
		
	}
	
	
	public double [][][] computeCosts(){
		/*
		 * Cette fonction permet de calculer les couts
		 */
		
		double [][][] costs = new double[RegionCount][TemporalHorizon][TechCount] ;
		
		for (int i = 0 ; i < RegionCount; i++){
			
			
			double penetration = regions[i][0] ;
			double population = regions[i][1] ;
			double GPON_PORTS = regions[i][2] ;
			double NearestRouter = regions[i][3] ;
			double MSAN_models = 0 ;

            double PC_Existing = regions[i][4] ;
            double MeanHouseWidth = regions[i][5] ;
            double MeanHouseLength = regions[i][6] ;
            double Urbanization = regions[i][7] ;
            double HousesInRow = regions[i][8] ;
            double HousesInColumn = regions[i][9] ;

            double VerticalHouses = regions[i][10] ;
            double HorizontalHouses = regions[i][11] ;

			for (int j = 0 ; j < TemporalHorizon; j++){
				for (int k = 0 ; k < TechCount; k++){
					double cost = 0 ;
                    if (k == 0){
                    	int Maintenance = 300 ;
                        cost = this.costsTech1(TemporalHorizon,
                                               j,
                                               penetration,
                                               population,
                                               GPON_PORTS,
                                               FiberCost,
                                               TrenchCost,
                                               NearestRouter,
                                               CivilEngineering,
                                               MSAN_models,
                                               Maintenance) ;
                        cost = (int)(cost * (2 - Math.exp(j*(-0.1)) )) ;
                        cost = cost * ( 1  + 0.2) ;
                    }
                    else if (k == 1) {
                    	int Maintenance = 400 ;
                        cost = this.costsTech2(TemporalHorizon,
                                               j,
                                               penetration,
                                               population,
                                               GPON_PORTS,
                                               FiberCost,
                                               TrenchCost,
                                               NearestRouter,
                                               CivilEngineering,
                                               miniMSANCost,
                                               PC_Existing,
                                               MeanHouseWidth,
                                               MeanHouseLength,
                                               Urbanization,
                                               HousesInRow,
                                               HousesInColumn,
                                               MSAN_models,
                                               Maintenance) ;
                        cost = (int)(cost * (2 - Math.exp(j*(-0.1)) )) ;
                        cost = cost * ( 1  + 0.2) ;
					}    
                    else if ( k == 2){
                    	int Maintenance = 500 ;
                    	cost = this.costsTech3(TemporalHorizon,
                                j,
                                penetration,
                                population,
                                FiberCost,
                                TrenchCost,
                                VerticalHouses,
                                HorizontalHouses,
                                C_ONT_Unitary,
                                Maintenance) ;
                    	cost = (int)(cost * (2 - Math.exp(j*(-0.1)) )) ;
                    	cost = cost * ( 1  + 0.2) ;
                    }
                    else{
                    	cost = 0 ;
                    }
                    costs[i][j][k] = cost ;
				}
				
			}
			
		}
		return costs;
		
	}
	
	public double[][][] computeROI(double [][][] costs, double [][][] revenus) {
		/*
		 * Cette fonction permet de claculer les ROI
		 */
		double [][][] ROI = new double[RegionCount][TemporalHorizon][TechCount] ;
		for (int i = 0 ; i < RegionCount; i++){
			for (int j = 0 ; j < TemporalHorizon; j++){
				for (int k = 0 ; k < TechCount; k++){
					ROI[i][j][k] = revenus[i][j][k] - costs[i][j][k] ;
				}
			}
		}
		return ROI ;
	}

	public double[][][] computeRevenus() {
		/*
		 * Cette fonction permet de calculer les revenus
		 */
		double [][][] revenus = new double[RegionCount][TemporalHorizon][TechCount] ;
		
		for (int i = 0 ; i < RegionCount; i++){
			double population = regions[i][1] ;
			double penetration = regions[i][0] ;
            double includeADSLRevenus = regions[i][12] ;
            double socialClass = regions[i][13] ;
			for (int j = 0 ; j < TemporalHorizon; j++){
				for (int k = 0 ; k < TechCount; k++){
					double revenu = this.revenus(  j,
                            TemporalHorizon,
                            includeADSLRevenus,
                            socialClass,
                            k,
                            offers,
                            population,
                            penetration) ;
                    
                    revenus[i][j][k] = revenu ;
				}
				
			}
			
		}
		return revenus;
	}

	
	
	private double revenus(int j, int temporalHorizon2,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers2, double population,double penetration) {
		
		//return revenusModel1(j, temporalHorizon2, includeADSLRevenus, socialClass, k, offers2, population, penetration) ;
		//return revenusModel2(j, temporalHorizon2, includeADSLRevenus, socialClass, k, offers2, population, penetration) ;
		//return revenusModel3(j, temporalHorizon2, includeADSLRevenus, socialClass, k, offers2, population, penetration) ;
		//return revenusModel4(j, temporalHorizon2, includeADSLRevenus, socialClass, k, offers2, population, penetration) ;
		//return revenusModel5(j, temporalHorizon2, includeADSLRevenus, socialClass, k, offers2, population, penetration) ;
		return RevenuModels.revenusModel6(j, temporalHorizon2, includeADSLRevenus, socialClass, k, offers2, population, penetration) ;

	}

	private double PriceOffer( double offer, double socialClass,int k) {
		
		double [] tab = {35, 45, 50, 60, 75, 75, 75, 100, 150} ;
		return tab[(int) offer] ;
	}

	private double alpha(double offer, double socialClass, int k) {
		
		double [][] tab0 = {{0.2, 0.2, 0.6, 0, 0, 0, 0, 0, 0},
	            {0.2, 0.2, 0.6, 0, 0, 0, 0, 0, 0},
	            {0.2, 0.2, 0.6, 0, 0, 0, 0, 0, 0},
	            {0.2, 0.2, 0.6, 0, 0, 0, 0, 0, 0}
		} ;
		
		double [][] tab1 = {{0, 0, 0, 0, 1, 0, 0, 0, 0},
	            {0.05, 0.05, 0.1, 0.1, 0.7, 0, 0, 0, 0},
	            {0.1, 0.1, 0.2, 0.3, 0.3, 0, 0, 0, 0},
	            {0.6, 0.2, 0.1, 0.1, 0, 0, 0, 0, 0}
		} ;
	    double [][] tab2 = {{0, 0, 0, 0, 0.2, 0.8, 0, 0, 0},
	            {0.05, 0.05, 0.1, 0.1, 0.2, 0.5, 0, 0, 0},
	            {0.05, 0.15, 0.1, 0.2, 0.4, 0.1, 0, 0, 0},
	            {0.5, 0.2, 0.15, 0.15, 0, 0, 0, 0, 0}
	    };
	   double [][] tab3 = {{0, 0, 0, 0, 0, 0.1, 0.5, 0.2, 0.2},
	            {0, 0, 0, 0.1, 0.3, 0.2, 0.25, 0.1, 0.05},
	            {0, 0.1, 0.2, 0.3, 0.2, 0.1, 0.1, 0, 0},
	            {0.4, 0.2, 0.15, 0.15, 0.1, 0, 0, 0, 0}
	   };
	   if (k == -1){
	    	return tab0[(int) socialClass][(int) offer] ;
	    }
	    if (k == 0){
	    	return tab1[(int) socialClass][(int) offer] ;
	    }
	    else if (k == 1){
	    	return tab1[(int) socialClass][(int) offer] ;
	    }
	    else if (k == 2){
	    	return tab2[(int) socialClass][(int) offer] ;
	    }
	    else{
	    	return 0 ;
	    }
	}

	private double costsTech1(int temporalHorizon2, int j, double penetration,
			double population, double gPON_PORTS, double fiberCost2,
			double trenchCost2, double nearestRouter, double civilEngineering2,
			double mSAN_models, int maintenance) {
		
		return CostModels.costTech1Model1(temporalHorizon2, j, penetration,
				 population,  gPON_PORTS, fiberCost2,
				 trenchCost2,  nearestRouter,  civilEngineering2,
				 mSAN_models,  maintenance) ;
		
	}


	private double costsTech2(int temporalHorizon2, int j, double penetration,
			double population, double gPON_PORTS, double fiberCost2,
			double trenchCost2, double nearestRouter, double civilEngineering2,
			double miniMSANCost2, double pC_Existing, double meanHouseWidth,
			double meanHouseLength, double urbanization, double housesInRow,
			double housesInColumn, double mSAN_models, int maintenance) {
		
		return CostModels.costTech2Model1(temporalHorizon2, j, penetration,
				population, gPON_PORTS, fiberCost2,
				trenchCost2, nearestRouter, civilEngineering2,
				miniMSANCost2, pC_Existing, meanHouseWidth,
				meanHouseLength, urbanization, housesInRow,
				housesInColumn, mSAN_models, maintenance) ;
	}
	

	private double costsTech3(int temporalHorizon2, int j, double penetration,
			double population, double FiberCost,double TrenchCost,double verticalHouses,
			double horizontalHouses, double c_ONT_Unitary2, int maintenance) {
		
		return CostModels.costTech3Model1(temporalHorizon2, j, penetration,
				 population,  FiberCost,TrenchCost,verticalHouses,
				 horizontalHouses,  c_ONT_Unitary2,  maintenance) ;
			
	}
	
	private double C_MSAN(double population) {
		Object [][] models = {
		          {"Huawei", 50000, 15000},
		          {"Huawei", 100000, 30000},
		          {"SAGEM", 100000, 32000},
		          {"SAGEM", 90000, 20000},
		          {"SAGEM", 140000, 40000},
		          {"SAGEM", 1200000, 30000}
			} ;
		ArrayList<EModel> tmp = new ArrayList<EModel>() ;
		
		
		for (int i = 0 ; i < models.length;i++){
			int pop = (int) models[i][1] ;
			if (pop > population){
				EModel tmp_ = new EModel() ;
				tmp_.setName((String) models[i][0]);
				tmp_.setCapacity((int) models[i][1]);
				tmp_.setPrice((int) models[i][2]);
				tmp.add(tmp_) ;
			}
		}
		
		Collections.sort(tmp, new EModelComparator());
			
		
		return tmp.get(0).getPrice();
	}
	
	
		
	public void displayCosts(int i, int j , int k ) {
		
		double population = this.regions[i][1] ;
		double penetration = this.regions[i][0] ;
		int potentialCustomers = (int) (population*penetration) ;
		System.out.println("Population= " + population + " , " +
						   "Penetration= " + penetration + " , " +
						   "Effective= " + potentialCustomers
							);
		System.out.println(this.costs[i][j][k]);
	}
	
	public void display(int i, int j , int k ) {
		
		double population = this.regions[i][1] ;
		double penetration = this.regions[i][0] ;
		int potentialCustomers = (int) (population*penetration) ;
		System.out.println("Population= " + population + " , " +
						   "Penetration= " + penetration + " , " +
						   "Effective= " + potentialCustomers + " , " +
							"Costs= " + this.costs[i][j][k] + " , " +
						    "Revenus= " + this.revenus[i][j][k]  + " , " +
							"ROI= " + (this.revenus[i][j][k]  - this.costs[i][j][k]));
	}
	
	public void displayRevenus(int i, int j , int k ) {
		double population = this.regions[i][1] ;
		double penetration = this.regions[i][0] ;
		int potentialCustomers = (int) (population*penetration) ;
		System.out.println("Population= " + population + " , " +
						   "Penetration= " + penetration + " , " +
						   "Effective= " + potentialCustomers
							);
		System.out.println(this.revenus[i][j][k]);
	}
	
	public double[][] generate(int RegionsCount){
		
		/*
		 * Cette fonction permet de générer un ensemble de régions
		 */
		double [][] regions = new double [RegionsCount][15];
		for (int i = 0 ; i < RegionsCount ; i++){
			
			double random = new Random().nextDouble();
			double start = 0 ;
			double end  = 0.1 ;
			
			double val = start + (random * (end - start));
			regions[i][0] = val;
			
			random = new Random().nextDouble();
			start = 100000 ;
			end  = 30000 ;
			val = start + (random * (end - start));
			regions[i][1] = Math.ceil(val);
			
			regions[i][2] = 64;
			
			
			// Nearest Router distance
			random = new Random().nextDouble();
			start = 600 ;
			end  = 1000 ;
			val = start + (random * (end - start));
			regions[i][3] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 7000 ;
			end  = 5000 ;
			val = start + (random * (end - start));
			regions[i][4] = 0;
			
			random = new Random().nextDouble();
			start = 2 ;
			end  = 10 ;
			val = start + (random * (end - start));
			regions[i][5] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 15 ;
			end  = 25 ;
			val = start + (random * (end - start));
			regions[i][6] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 15 ;
			end  = 25 ;
			val = start + (random * (end - start));
			regions[i][7] = Math.ceil(val);
            
			random = new Random().nextDouble();
			start = 4 ;
			end  = 1 ;
			val = start + (random * (end - start));
			regions[i][8] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 15 ;
			end  = 100 ;
			val = start + (random * (end - start));
			regions[i][9] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 15 ;
			end  = 100 ;
			val = start + (random * (end - start));
			regions[i][10] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 5 ;
			end  = 20 ;
			val = start + (random * (end - start));
			regions[i][11] = Math.ceil(val);

			random = new Random().nextDouble();
			start = 20 ;
			end  = 70 ;
			val = start + (random * (end - start));
			regions[i][12] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 0 ;
			end  = 1 ;
			val = start + (random * (end - start));
			
			regions[i][13] = Math.ceil(val);
			
			random = new Random().nextDouble();
			start = 0 ;
			end  = 1 ;
			val = start + (random * (end - start));
			
			regions[i][14] = Math.ceil(val);
           
		}
		return regions ;
	}
}
