package view;

public class SampleRibbonThread extends Thread  {
	//RibbonTester tester = null;
	@Override
	public void run() {
		String[] a = {"",""};
		SampleRibbonClass.main(a);
	}
}
