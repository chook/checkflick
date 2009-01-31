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
		SampleRibbonClass.updateShellText(text);		
	}
	public static void settingMovieTab(MovieEntity movie){
		SampleRibbonClass.updateMovieTab(movie);
	}
	public static void settingPersonTab(PersonEntity person){
		SampleRibbonClass.updatePersonTab(person);
	}
	public static void drawPersonData(List<AbsType> result , PersonDataEnum type){
		SampleRibbonClass.drawPersonData(result, type);
	}
	public static void drawMovieData(List<AbsType> result , MovieDataEnum type){
		SampleRibbonClass.drawMovieData(result, type);
	}
	public static void drawMovieSearch(List<DatedEntity> list, SearchEntitiesEnum search) {
		SampleRibbonClass.drawSearchMovieTable(list, search);
	}
	public static void drawMovieSearch(List<DatedEntity> list, SearchEntitiesEnum search ,int id) {
		//	SampleRibbonClass.drawSearchMovieTable(list, search , id);
	}
	public static void drawPersonSearch(List<DatedEntity> list, SearchEntitiesEnum search ) {
		SampleRibbonClass.drawSearchPersonTable(list, search);
	}
	public static void drawPersonSearch(List<DatedEntity> list, SearchEntitiesEnum search , int id) {
		SampleRibbonClass.peopleToInsertTable(list, search , id);
	}
	public static void InsertMovie(int id){
		SampleRibbonClass.drawMoreInsertMovie(id);
	}
	
	public static void InsertMovieExtraData(int id){
		SampleRibbonClass.drawInsertDataSuccess();
	}
	public static void updateMovie(int id){
		SampleRibbonClass.drawUpdateDataSuccess();
	}
	public static void InsertPerson(int id){
		SampleRibbonClass.drawMoreInsertPerson(id);
	}
	public static void InsertPersonExtraData(int id){
		SampleRibbonClass.drawInsertDataSuccess();
	}
	public static void updatePerson(int id){
		SampleRibbonClass.drawUpdateDataSuccess();
	}
	public static void SetNamedList(List<NamedEntity> list, NamedEntitiesEnum type) {
		SampleRibbonClass.setList(list, type);
	}

}
