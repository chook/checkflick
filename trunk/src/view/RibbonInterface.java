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

/**
 * This is the GUI interface class
 * These are the function that the controller is calling to paint 
 * the GUI from the GUI thread.
 * @author Chook & Maayan
 *
 */
public class RibbonInterface {
	/**
	 * Update the movie tab
	 * @param movie - The movie to show
	 */
	public static void settingMovieTab(MovieEntity movie) {
		CheckFlickGUI.updateMovieTab(movie);
	}

	/**
	 * Update the person tab
	 * @param person - The person to update
	 */
	public static void settingPersonTab(PersonEntity person) {
		CheckFlickGUI.updatePersonTab(person);
	}

	/**
	 * Show the person extra data
	 * @param result - The data to show
	 * @param type - Type of data
	 * @param id - Person id
	 */
	public static void drawPersonData(List<AbsType> result,
			PersonDataEnum type, int id) {
		CheckFlickGUI.drawPersonData(result, type, id);
	}

	/**
	 * Draw the movie extra data
	 * @param result - The data to show
	 * @param type - The type
	 * @param id - The movie id
	 */
	public static void drawMovieData(List<AbsType> result, MovieDataEnum type,
			int id) {
		CheckFlickGUI.drawMovieData(result, type, id);
	}

	/**
	 * Show the results of the movie search
	 * @param list - List of movies
	 * @param search - The search entities
	 */
	public static void drawMovieSearch(List<DatedEntity> list,
			SearchEntitiesEnum search) {
		CheckFlickGUI.drawSearchMovieTable(list, search);
	}

	/**
	 * Show the persons search result
	 * @param list - The list of persons
	 * @param search - The search entities
	 */
	public static void drawPersonSearch(List<DatedEntity> list,
			SearchEntitiesEnum search) {
		CheckFlickGUI.drawSearchPersonTable(list, search);
	}

	/**
	 * The person search (when you look them up from the movies - add crew/cast)
	 * @param list - List of persons
	 * @param search - Search entities
	 * @param id - The id of the movie
	 * @param update - True if update, False if not
	 */
	public static void drawPersonSearch(List<DatedEntity> list,
			SearchEntitiesEnum search, int id, boolean update) {
		CheckFlickGUI.peopleToInsertTable(list, search, id, update);
	}

	/**
	 * Show the insert a movie 
	 * @param id - The movie id
	 */
	public static void InsertMovie(int id) {
		CheckFlickGUI.drawMoreInsertMovie(id);
	}

	/**
	 * Insert extra data about a movie
	 * @param id - The movie id
	 * @param update - True if update, False otherwise
	 * @param type - Type of data
	 */
	public static void InsertMovieExtraData(int id, boolean update,
			MovieDataEnum type) {
		if (update)
			CheckFlickGUI.redrawMovieTable(id, type);
		else
			CheckFlickGUI.drawInsertDataSuccess(id > 0);
	}

	/**
	 * Update a movie
	 * @param id - The movie id
	 */
	public static void updateMovie(int id) {
		CheckFlickGUI.drawUpdateDataSuccess(id > 0);
	}

	/**
	 * Inserts a person into the db
	 * @param id - The person id
	 */
	public static void InsertPerson(int id) {
		CheckFlickGUI.drawMoreInsertPerson(id);
	}
	
	/**
	 * Insert more person data
	 * @param id - Person id
	 * @param update - True if update, False otherwise
	 * @param type - Type of data to insert
	 */
	public static void InsertPersonExtraData(int id, boolean update,
			PersonDataEnum type) {
		if (update)
			CheckFlickGUI.redrawPersonTable(id, type);
		else
			CheckFlickGUI.drawInsertDataSuccess(id > 0);
	}

	/**
	 * Update person
	 * @param id - Person id
	 */
	public static void updatePerson(int id) {
		CheckFlickGUI.drawUpdateDataSuccess(id > 0);
	}

	/**
	 * Set the list of id-names
	 * @param list - The list
	 * @param type - The list type
	 */
	public static void SetNamedList(List<NamedEntity> list,
			NamedEntitiesEnum type) {
		CheckFlickGUI.setList(list, type);
	}

	/**
	 * Delete movie extra data
	 * @param result - True if success, False otherwise
	 * @param id - The movie id
	 * @param type - The movie data type
	 */
	public static void deleteMovieExtraData(boolean result, int id,
			MovieDataEnum type) {
		deleteEntity(result);
		CheckFlickGUI.redrawMovieTable(id, type);
	}

	/**
	 * Delete a person extra data
	 * @param result - True if delete successful, False otherwise
	 * @param id - The person id
	 * @param type - The person type
	 */
	public static void deletePersonExtraData(boolean result, int id,
			PersonDataEnum type) {
		deleteEntity(result);
		CheckFlickGUI.redrawPersonTable(id, type);
	}

	/**
	 * Deletes an entity
	 * @param ok - True if delete successfuly, False otherwise
	 */
	public static void deleteEntity(boolean ok) {
		CheckFlickGUI.drawDeleteDataFailure(ok);
	}

	/**
	 * Send the finish import to the GUI
	 * @param importIntoDB - True if success, False otherwise
	 */
	public static void finishImport(boolean importIntoDB) {
		CheckFlickGUI.handleFinishImport(importIntoDB);
	}
}
