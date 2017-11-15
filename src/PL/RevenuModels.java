package PL;

public class RevenuModels {
	
	
	public static double revenusModel0(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		
		double R_tza_ = 0 ;
		
		
		int potentialCustomers = (int) (population*penetration) ;
		/*System.out.println("Population= " + population + " , " +
						   "Penetration= " + penetration + " , " +
						   "Effective= " + potentialCustomers
							);*/
		
		
		for (int o = j + 1 ; o < temporalHorizon;o++){
			// Yearly revenus of all offers
			/*System.out.println("Year " + o);**/
			double R_tza = 0 ;
			double R_tzaADSL = 0 ;
			for (int l = 0 ; l < offers.length;l++){
				
				// Revenus per Offer
				// Revenus per Offer without previous technologies 
				R_tza +=  (int)(potentialCustomers * alpha(l, socialClass, k) * 
											 PriceOffer(l,socialClass,k)) ;
				
				// Revenus per Offer of the previous technologies 
                R_tzaADSL += (int)(potentialCustomers * alpha( l, socialClass, -1) * 
                							PriceOffer(l,socialClass,k)) ;
                /*System.out.println("Clients= " + (int)(potentialCustomers * alpha( l, socialClass, 0) ) + " Revenus= " + (int)(potentialCustomers * alpha(l, socialClass, k) * 
											 PriceOffer(l,socialClass,k)));*/
                
                System.out.println((int)(potentialCustomers * alpha(l, socialClass, k) * 
											 PriceOffer(l,socialClass,k)));
                
			}
			System.out.println("#################################");
			
			double earlyAdoptersRate = 0.4 ;
			R_tza_ += earlyAdoptersRate*R_tza + (1  - earlyAdoptersRate )* includeADSLRevenus * R_tzaADSL*(1 - (o-1)*0.2) ;
		}

		R_tza_ = 12*R_tza_/(int)(temporalHorizon - (j + 1)) ;
		return  (int)R_tza_;
		
	}
	public static double revenusModel1(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		double R_tza_ = 0 ;
		int potentialCustomers = (int) (population*penetration) ;
		double pf = 40 ;
		R_tza_ = potentialCustomers * pf ;
		return 12* R_tza_ ;
		
	}
	
	public static double revenusModel2(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		double R_tza_ = 0 ;
		int potentialCustomers = (int) (population*penetration) ;
		for (int l = 0 ; l < offers.length;l++){
			R_tza_ += (int)(potentialCustomers * alpha(l, socialClass, k) * 
					 PriceOffer(l,socialClass,k)) ;
		}
		
		return 12* R_tza_ ;
		
	}
	
	public static double revenusModel3(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		double R_tza_ = 0 ;
		int potentialCustomers = (int) (population*penetration) ;
		double R_tza_previous = 0 ;
		for (int o = 0 ; o < j;o++){
			for (int l = 0 ; l < offers.length;l++){
				R_tza_previous += (int)(potentialCustomers * alpha(l, socialClass, -1) * 
					 PriceOffer(l,socialClass,-1))/j ;
			}
		}
		
		for (int l = 0 ; l < offers.length;l++){
			R_tza_ += (int)(potentialCustomers * alpha(l, socialClass, k) * 
					 PriceOffer(l,socialClass,k)) ;
		}
		
		R_tza_ = 0.1*R_tza_previous + 0.9 * R_tza_ ;
		return 12* R_tza_ ;
		
	}
	
	public static double revenusModel4(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		double R_tza_ = 0 ;
		int potentialCustomers = (int) (population*penetration) ;
		double R_tza_previous = 0 ;
		for (int o = 0 ; o < j;o++){
			for (int l = 0 ; l < offers.length;l++){
				R_tza_previous += (int)(potentialCustomers * alpha(l, socialClass, -1) * 
					 PriceOffer(l,socialClass,-1))/j ;
			}
		}
		
		double R_tza_next = 0 ;
		
		for (int o = j + 2 ; o < temporalHorizon;o++){
			for (int l = 0 ; l < offers.length;l++){
				R_tza_next += (int)(potentialCustomers * alpha(l, socialClass, k) * 
					 PriceOffer(l,socialClass,k)) ;
			}
			
		}
		
		R_tza_ = 0.1*R_tza_previous + 0.8 * R_tza_  + 0.1*R_tza_next;
		return 12* R_tza_ ;
		
	}
	
	
	public static double revenusModel5(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		double R_tza_ = 0 ;
		int potentialCustomers = (int) (population*penetration) ;
		
		
		for (int o = j + 1 ; o < temporalHorizon;o++){
			for (int l = 0 ; l < offers.length;l++){
				R_tza_ += (int)(potentialCustomers * alpha(l, socialClass, k) * 
						 PriceOffer(l,socialClass,k)) ;
			}
		}
		
		return 12* R_tza_ ;
		
	}
	
	public static double revenusModel6(int j, int temporalHorizon,
			double includeADSLRevenus, double socialClass, int k,
			double[] offers, double population,double penetration) {
		
		double R_tza_ADSL = 0 ;
		double R_tza_ = 0 ;
		double adoptersRate = 0.3 ;
		double adoptersEvolutionRate = 0.05 ;
		int potentialCustomers = (int) (population*penetration) ;
		
		for (int l = 0 ; l < offers.length;l++){
			R_tza_ += (int)(potentialCustomers * alpha(l, socialClass, -1) * 
					 PriceOffer(l,socialClass,-1)) ;
		}
		
		
		R_tza_ADSL *= (1 - adoptersRate) *(1 - j *adoptersEvolutionRate) ;
		
		for (int l = 0 ; l < offers.length;l++){
			R_tza_ += (int)(potentialCustomers * alpha(l, socialClass, k) * 
					 PriceOffer(l,socialClass,k)) ;
		}
		
		R_tza_ *= adoptersRate *(1 + j *adoptersEvolutionRate);
		return 12* (R_tza_ + R_tza_ADSL);
		
	}
	
	public static double alpha(double offer, double socialClass, int k) {
		
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
	
	public static double PriceOffer( double offer, double socialClass,int k) {
		
		double [] tab = {35, 45, 50, 60, 75, 75, 75, 100, 150} ;
		return tab[(int) offer] ;
	}

}
