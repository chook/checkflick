package view;

import java.util.List;
import controller.entity.AbsType;
import controller.entity.DatedEntity;
import controller.entity.MovieEntity;
import controller.entity.NamedEntity;
import controller.entity.PersonEntity;
import controller.enums.MovieDataEnum;
import controller.enums.NamedEntitiesEnum;
import controller.enums.PersonDataEnum;
import controller.enums.SearchEntitiesEnum;

public class RibbonInterface {
	public static void settingShellText(final String text) {
		CheckFlickGUI.updateShellText(text);		
	}
	public static void settingMovieTab(MovieEntity movie){
		CheckFlickGUI.updateMovieTab(movie);
	}
	public static void settingPersonTab(PersonEntity person){
		CheckFlickGUI.updatePersonTab(person);
	}
	public static void drawPersonData(List<AbsType> result , PersonDataEnum type){
		CheckFlickGUI.drawPersonData(result, type);
	}
	public static void drawMovieData(List<AbsType> result , MovieDataEnum type){
		CheckFlickGUI.drawMovieData(result, type);
	}
	public static void drawMovieSearch(List<DatedEntity> list, SearchEntitiesEnum search) {
		CheckFlickGUI.drawSearchMovieTable(list, search);
	}
	public static void drawMovieSearch(List<DatedEntity> list, SearchEntitiesEnum search ,int id) {
		//	SampleRibbonClass.drawSearchMovieTable(list, search , id);
	}
	public static void drawPersonSearch(List<DatedEntity> list, SearchEntitiesEnum search ) {
		CheckFlickGUI.drawSearchPersonTable(list, search);
	}
	public static void drawPersonSearch(List<DatedEntity> list, SearchEntitiesEnum search , int id) {
		CheckFlickGUI.peopleToInsertTable(list, search , id);
	}
	public static void InsertMovie(int id){
		CheckFlickGUI.drawMoreInsertMovie(id);
	}
	
	public static void InsertMovieExtraData(int id){
		CheckFlickGUI.drawInsertDataSuccess();
	}
	public static void updateMovie(int id){
		CheckFlickGUI.drawUpdateDataSuccess();
	}
	public static void InsertPerson(int id){
		CheckFlickGUI.drawMoreInsertPerson(id);
	}
	public static void InsertPersonExtraData(int id){
		CheckFlickGUI.drawInsertDataSuccess();
	}
	public static void updatePerson(int id){
		CheckFlickGUI.drawUpdateDataSuccess();
	}
	public static void SetNamedList(List<NamedEntity> list, NamedEntitiesEnum type) {
		CheckFlickGUI.setList(list, type);
	}

}
