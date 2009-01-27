package view;

import org.eclipse.swt.widgets.Display;

public class RibbonInterface {
	protected RibbonInterface(Display display) {
	}
	
	public static void settingShellText(final String text) {
		SampleRibbonClass.updateShellText(text);		
	}
}
