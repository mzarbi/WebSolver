import PL.LPUtils;
import PL.TestBench;

public class main {

	public static void main(String[] args) {
		
		LPUtils lpu = new LPUtils() ;
		TestBench.init2(lpu);
		lpu.display(0, 0, 2);
		TestBench.request(lpu);
		
	}
}
