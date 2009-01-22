package view;

public class SampleRibbonThread extends Thread  {
	//RibbonTester tester = null;
	SampleRibbonClass rc = null;
	@Override
	public void run() {
		String[] a = {"",""};
		//RibbonTester.main(a);
		
		rc.main(a);
	}
}
