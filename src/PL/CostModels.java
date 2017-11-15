package PL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import commons.EModel;
import commons.EModelComparator;

public class CostModels {

	public static double costTech1Model1(int temporalHorizon2, int j, double penetration,
			double population, double gPON_PORTS, double fiberCost2,
			double trenchCost2, double nearestRouter, double civilEngineering2,
			double mSAN_models, int maintenance) {
		
		int Maintenance = 300 ;
        //double Cz_1 = Maintenance * j ;
        double currentClients = Math.ceil(penetration * population) ;
        
        // Computing MSAN Costs
        double Cz_MSAN = C_MSAN(currentClients) ;
        // Computing Link costs (Nearest Router is in meters)
        double Cz_NRouter_MSAN = 0 ;
        
    	// Computing Fiber related costs 
    	Cz_NRouter_MSAN += nearestRouter/1000 * (currentClients / gPON_PORTS * fiberCost2);
    	// Computing trenching related costs
    	Cz_NRouter_MSAN += nearestRouter *(trenchCost2 * civilEngineering2) ;
    	// Computing all costs
        double Cz_1 = Cz_MSAN + Cz_NRouter_MSAN ;
        // Marketing costs (standard value 40%)
    	Cz_1 = Cz_1 *(1 + 0.6) ;
        return  (int)Cz_1;

	}
	
	public static double costTech2Model1(int TemporalHorizon, int j, double penetration,
			double population, double gPON_PORTS, double FiberCost,
			double TrenchCost, double nearestRouter, double CivilEngineering,
			double miniMSANCost, double pC_Existing, double meanHouseWidth,
			double meanHouseLength, double urbanization, double housesInRow,
			double housesInColumn, double mSAN_models, int maintenance) {
		int Maintenance = 1000 ;
        //double Cz_2 = Maintenance * j ;
        double currentClients = penetration * population ;
        
        // Computing costs
        double Cz_2 = 0 ;
        	
        	// Equipments related costs
        	Cz_2 = pC_Existing * miniMSANCost ;
        	// Mean Distance
        	
        	double D_moy = D_mean(meanHouseWidth, meanHouseLength, urbanization, housesInRow,
                       housesInColumn) ;
        	double random = new Random().nextDouble();
			int start = 600 ;
			int end  = 1000 ;
			double val = start + (random * (end - start));
			D_moy = Math.ceil(val);
			D_moy = 700 ;
        	// Fiber related costs
        	Cz_2 += D_moy * (currentClients / gPON_PORTS * FiberCost )/1000 ;
        	// Trenching related costs
        	//Cz_2 += D_moy * TrenchCost * CivilEngineering ;
    	
        	Cz_2 += costTech1Model1(TemporalHorizon, j, penetration, population, gPON_PORTS,
                             FiberCost, TrenchCost, nearestRouter,
                             CivilEngineering, mSAN_models, Maintenance) ;
    	// Marketing costs (standard value 60%)
    	Cz_2 = Cz_2 *(1 + 0.6) ;
        	

        return  (int)Cz_2;
	}
	
	public static double costTech3Model1(int temporalHorizon2, int j, double penetration,
			double population, double FiberCost,double TrenchCost, double verticalHouses,
			double horizontalHouses, double c_ONT_Unitary2, int maintenance) {
		int Maintenance = 1000 ;
        //double Cz_3 = Maintenance * j ;
		double currentClients = penetration * population ;
		
		// Computing costs
		double Cz_3 = 0 ;
			// Computing equipment related costs
        	double Cz_31 = currentClients * c_ONT_Unitary2 ;
        	// Computing link related costs
        		// Vertical links
        		double Cz_32 = verticalHouses * 11 * FiberCost/1000 ;
        		// Horizontal links
        		double D_moy = 700 ;
        		double Cz_33 = horizontalHouses * D_moy*TrenchCost/1000 ;
        	
        	Cz_3 = Cz_31 + Cz_32 + Cz_33 ;
    		// Marketing costs (standard value 40%)
        	Cz_3 = Cz_3 *(1 + 0.6) ;
        	System.out.println(Cz_3);
        return (int)Cz_3;
	}
	
	public static double C_MSAN(double population) {
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
	
	private static double D_mean(double meanHouseWidth, double meanHouseLength,
			double urbanization, double housesInRow, double housesInColumn) {
		
	    if (urbanization == 0){
	    	return SSSS(meanHouseWidth,meanHouseLength,housesInRow,housesInColumn)/100.0 ;
	    }
	    else if (urbanization == 1) {
	    	return DSSS(meanHouseWidth,meanHouseLength,housesInRow,housesInColumn)/100.0 ;
		}
	    else if (urbanization == 2) {
	    	return SSSnS(meanHouseWidth,meanHouseLength,housesInRow,housesInColumn)/100.0 ;
		}
	    else if (urbanization == 3) {
	    	return DSSnS(meanHouseWidth,meanHouseLength,housesInRow,housesInColumn)/100.0 ;
		}
	    else {
			return 0 ;
		}
	}
	
	private static double DSSnS(double L1, double L2,double N, double M) {
		
		return SSSnS(L1,L2,N,M) + N*M*L1;
	}

	private static double SSSnS(double L1, double L2,double N, double M) {
		return L2*(M*(N+1)*L1 + M*(N+1)*(N+2)*L2);
	}

	private static double DSSS(double L1, double L2,double N, double M) {
		return SSSS(L1,L2,N,M) + 2*Math.ceil(M/4)*Math.ceil(N/4)*L1;
	}

	private static double SSSS(double L1, double L2,double N, double M) {
		double val1 = Math.ceil(N/4) + 1 ;
		double val2 = Math.ceil(M/4) + 1 ;
		
		return 4*L1*L2*val1*(val1 + 1)/2*val2*(val2 + 1)/2 ;

	}
}
