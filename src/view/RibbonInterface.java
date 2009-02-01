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
	public static void drawPersonData(List<AbsType> result , PersonDataEnum type , int id){
		CheckFlickGUI.drawPersonData(result, type , id);
	}
	public static void drawMovieData(List<AbsType> result , MovieDataEnum type, int id){
		CheckFlickGUI.drawMovieData(result, type , id);
	}
	public static void drawMovieSearch(List<DatedEntity> list, SearchEntitiesEnum search) {
		CheckFlickGUI.drawSearchMovieTable(list, search);
	}

	public static void drawPersonSearch(List<DatedEntity> list, SearchEntitiesEnum search ) {
		CheckFlickGUI.drawSearchPersonTable(list, search);
	}
	public static void drawPersonSearch(List<DatedEntity> list, SearchEntitiesEnum search , int id , boolean update) {
		CheckFlickGUI.peopleToInsertTable(list, search , id ,update);
	}
	public static void InsertMovie(int id){
		CheckFlickGUI.drawMoreInsertMovie(id);
	}
	
	public static void InsertMovieExtraData(int id , boolean update , MovieDataEnum type){
		CheckFlickGUI.drawInsertDataSuccess(id>0);
		if (update)
			CheckFlickGUI.redrawMovieTable(id , type );
	}
	public static void updateMovie(int id){
		CheckFlickGUI.drawUpdateDataSuccess(id>0);
	}
	public static void InsertPerson(int id){
		CheckFlickGUI.drawMoreInsertPerson(id);
	}
	public static void InsertPersonExtraData(int id , boolean update, PersonDataEnum type){
		CheckFlickGUI.drawInsertDataSuccess(id>0);
		if (update)
			CheckFlickGUI.redrawPersonTable(id , type );
	}
	public static void updatePerson(int id){
		CheckFlickGUI.drawUpdateDataSuccess(id>0);
	}
	public static void SetNamedList(List<NamedEntity> list, NamedEntitiesEnum type) {
		CheckFlickGUI.setList(list, type);
	}
	public static void deleteMovieExtraData(boolean result, int id, MovieDataEnum type){
		deleteEntity(result);
		CheckFlickGUI.redrawMovieTable(id , type );
	}
	public static void deletePersonExtraData(boolean result, int id, PersonDataEnum type){
		deleteEntity(result);
		CheckFlickGUI.redrawPersonTable(id , type );
	}
	public static void deleteEntity(boolean ok ) {
		CheckFlickGUI.drawDeleteDataFailure(ok);
	}
	public static void finishImport(boolean importIntoDB) {
		CheckFlickGUI.handleFinishImport(importIntoDB);
	}
}
