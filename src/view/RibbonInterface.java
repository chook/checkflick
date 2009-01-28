package view;

import org.eclipse.swt.widgets.Display;

import controller.entity.MovieEntity;

public class RibbonInterface {
	protected RibbonInterface(Display display) {
	}
	
	public static void settingShellText(final String text) {
		SampleRibbonClass.updateShellText(text);		
	}
	public static void settingMovieTab(MovieEntity movie){
		SampleRibbonClass.updateMovieTab(movie);
	}

}
