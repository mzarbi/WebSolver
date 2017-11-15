package PL;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import SPwebClients.FullHttpResponse;
import SPwebClients.HttpRequester;

public class TestBench {


	
	public static void init2(LPUtils lpu) {
		// Configurations
		lpu.RegionCount = 6 ;
		lpu.TemporalHorizon = 3 ;
		lpu.TechCount = 3 ;
		lpu.annualBudget = 10000000 ;
		lpu.ppy = 5 ;
		/*
		lpu.regions = new double [][]{{0.24, 36818, 64, 700, 2, 13.4, 14.4, 2, 105, 105, 935, 10100, 1, 1},
									  {0.3, 40864, 64, 700, 2, 14, 15, 2, 105, 105, 3699, 11147, 1, 1},
									  {0.3, 16499, 64, 700, 2, 14, 15, 2, 65, 65, 659, 4243, 1, 1},
									  {0.3, 11386, 64, 700, 2, 14, 15, 1, 55, 55, 885, 3135, 1, 2},
									  {0.3, 13440, 64, 700, 2, 14, 15, 2, 60, 60, 736, 3659, 1, 2},
									  {0.3, 114486, 64, 700, 2, 14, 15, 1, 180, 180, 7275, 32498, 1, 2},
									  {0.3, 7830, 64, 700, 2, 14, 15, 1, 48, 48, 418, 2376, 1, 1},
									  {0.3, 6120, 64, 700, 2, 14, 15, 2, 43, 43, 356, 1924, 1, 1},
									  {0.3, 21749, 64, 700, 2, 14, 15, 2, 73, 73, 2100, 5387, 1, 2}};
									  */
		
		lpu.regions = new double [][]{
									  {0.3, 16499, 64, 1600, 2, 14, 15, 2, 65, 65, 659, 4243, 1, 1},
									  {0.3, 11386, 64, 1200, 2, 14, 15, 1, 55, 55, 885, 3135, 1, 2},
									  {0.3, 13440, 64, 1400, 2, 14, 15, 2, 60, 60, 736, 3659, 1, 2},
									  {0.3, 7830, 64, 800, 2, 14, 15, 1, 48, 48, 418, 2376, 1, 1},
									  {0.3, 6120, 64, 700, 2, 14, 15, 2, 43, 43, 356, 1924, 1, 1},
									  {0.3, 21749, 64, 1800, 2, 14, 15, 2, 73, 73, 2100, 5387, 1, 2}};
		// Add Regions
		//lpu.regions = Connector.loadDataSet("data1");
		
		// Add Costs
		lpu.C_ONT_Unitary = 60 ;
		lpu.FiberCost = 150 ;
		lpu.TrenchCost = 500 ;
		lpu.miniMSANCost = 1500 ;
		lpu.CivilEngineering = 1 ;
		lpu.offers = new double [] {1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 };
		
		lpu.costs = lpu.computeCosts() ;
		lpu.revenus = lpu.computeRevenus() ;
		lpu.ROI = lpu.computeROI(lpu.costs,lpu.revenus) ;
		
	}
	
	public static void request(LPUtils lpu) {
		
		double[][][][] tab = lpu.Conv(lpu.revenus , lpu.costs) ;
		
		HttpRequester httpRequester = new HttpRequester();
		String url = "http://localhost:5000/Simulation" ;
        Map<String, String> params = new LinkedHashMap<String, String>();
		
        String uuid = UUID.randomUUID().toString();
        params.put("session",uuid) ;
       
       
        String data = "" ;
        System.out.println(Arrays.deepToString(tab));
        data += Arrays.deepToString(tab) + "&" ;
        data += String.valueOf(lpu.annualBudget) + "&" ;
        data += String.valueOf(lpu.ppy)  ;
        params.put("data", data) ;
		FullHttpResponse response = httpRequester.call(url, "POST", params);
		
		
		String resp = response.getResponse() ;
		
		System.out.println(resp);
	}
}
