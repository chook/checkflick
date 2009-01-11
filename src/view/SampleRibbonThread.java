package view;

import com.hexapixel.widgets.ribbon.RibbonTester;

public class SampleRibbonThread extends Thread  {
	//RibbonTester tester = null;
	
	@Override
	public void run() {
		String[] a = {"",""};
		RibbonTester.main(a);
	}
}
