package view;

import java.util.List;

import org.eclipse.swt.widgets.Display;

import controller.entity.AbsType;
import controller.entity.MovieEntity;
import controller.entity.PersonEntity;
import controller.enums.MovieDataEnum;
import controller.enums.PersonDataEnum;

public class RibbonInterface {
	protected RibbonInterface(Display display) {
	}
	
	public static void settingShellText(final String text) {
		SampleRibbonClass.updateShellText(text);		
	}
	public static void settingMovieTab(MovieEntity movie){
		SampleRibbonClass.updateMovieTab(movie);
	}
	public static void settingPersonTab(PersonEntity person){
		SampleRibbonClass.updatePersonTab(person);
	}
	public static void drawPersonData(List<AbsType> result , PersonDataEnum type){
		SampleRibbonClass.drawPersonData(result , type);
	}
	public static void drawMovieData(List<AbsType> result , MovieDataEnum type){
		SampleRibbonClass.drawMovieData(result , type);
	}
	public static void InsertMovie(int id){
		SampleRibbonClass.drawMoreInsertMovie(id);
	}

}
