package view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import com.hexapixel.widgets.generic.ImageCache;
import com.hexapixel.widgets.generic.Utils;
import com.hexapixel.widgets.ribbon.ButtonSelectGroup;
import com.hexapixel.widgets.ribbon.RibbonButton;
import com.hexapixel.widgets.ribbon.RibbonGroup;
import com.hexapixel.widgets.ribbon.RibbonGroupSeparator;
import com.hexapixel.widgets.ribbon.RibbonShell;
import com.hexapixel.widgets.ribbon.RibbonTab;
import com.hexapixel.widgets.ribbon.RibbonTabFolder;
import com.hexapixel.widgets.ribbon.RibbonTooltip;
import controller.AppData;
import controller.DataManager;
import controller.entity.AbsType;
import controller.entity.CastingRelation;
import controller.entity.DatedEntity;
import controller.entity.MovieEntity;
import controller.entity.NamedEntity;
import controller.entity.PersonEntity;
import controller.entity.Relation;
import controller.enums.MovieDataEnum;
import controller.enums.NamedEntitiesEnum;
import controller.enums.PersonDataEnum;
import controller.enums.SearchEntitiesEnum;
import controller.filter.AbsFilter;
import controller.thread.ThreadPool;

public class CheckFlickGUI {
	static RibbonShell shell;
	static Display display;
	static Composite searchByMovie;
	static Composite searchByPerson;
	static Composite insertMovie;
	static Composite insertPerson;
	static Composite resultsMovieTable;
	static Composite resultsPersonTable;
	static Composite personButtons;
	static Composite movieButtons;
	static Composite entityDetails;
	static ExpandBar moreInsert;
	static RibbonTab movieTab;
	static RibbonTab personTab;
	static String[] genresString;
	static List<NamedEntity> genresList;
	static List<NamedEntity> colorList;
	static List<NamedEntity> countriesList;
	static List<NamedEntity> langList;
	static List<NamedEntity> rolesList;
	static DataManager dm = null;//DataManager.getInstance();
	static ThreadPool pool = null;
	static ExpandBar bar;
	static ExpandItem otherResults;
	static AppData settings = null; 
	public static void main(String args []) {
		AppData.getInstance().parseINIFile("ini\\checkflick.ini");
		pool = new ThreadPool(AppData.getInstance().getMaxThreads());
		dm = new DataManager();
		display = new Display();
		try {
			genresList = new ArrayList<NamedEntity>();
			genresList.add(new NamedEntity(0,""));
			colorList = new ArrayList<NamedEntity>();
			colorList.add(new NamedEntity(0,""));
			countriesList = new ArrayList<NamedEntity>();
			countriesList.add(new NamedEntity(0,""));
			langList = new ArrayList<NamedEntity>();
			langList.add(new NamedEntity(0,""));
			rolesList = new ArrayList<NamedEntity>();
			rolesList.add(new NamedEntity(0,""));
			
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.COUNTRIES));
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.GENRES));
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.LANGUAGES));
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.PRODUCTION_ROLES));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		CheckFlickGUI app = new CheckFlickGUI();
		app.createShell();
		
		Utils.centerDialogOnScreen(shell.getShell());
		app.shell.open();
		
		while (!app.shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
	}
	
	public static void updateShellText(final String text) {
		display.asyncExec(new Runnable() {
			public void run() {
				shell.setText(text);		
			}
		});
	}
	protected static void updateMovieTab(final MovieEntity movie){
		display.asyncExec(new Runnable() {
			public void run() {
				String tabName = movie.getName().substring(0, Math.min(movie.getName().length(), 8));
				final RibbonTabFolder tabs = shell.getRibbonTabFolder();
				movieTab = new RibbonTab(tabs,tabName);
				ShowMovieResult(movieTab , movie);
				tabs.selectTab(movieTab);
				resultsMovieTable.setVisible(false);
			}
		});
	}
	protected static void updatePersonTab(final PersonEntity person){
		display.asyncExec(new Runnable() {
			public void run() {
				String tabName = person.getName().substring(0, Math.min(person.getName().length(), 8));
				final RibbonTabFolder tabs = shell.getRibbonTabFolder();
				personTab = new RibbonTab(tabs, tabName);
				ShowPersonResult(personTab, person);
				tabs.selectTab(personTab);
				resultsPersonTable.setVisible(false);
			}
		});
	}
	protected static void drawPersonData(final List<AbsType> result ,final PersonDataEnum type , final int personId){
		display.asyncExec(new Runnable() {
			public void run() {
				Image image = ImageCache.getImage("book_48.png"); ;
				String[] titles= new String[3];
				String toGet = "name";
				titles[0]="";
				titles[2]="";
				boolean cast = false;
				List<NamedEntity> list = null;
				String message = "";
				String title = "";
				switch(type){
				case PERSON_AKAS:{
					image = ImageCache.getImage("book_48.png");
					title = "AKA Names For The Person";
					titles[1] = "Name";
					toGet = "name";
					break;
				}
				case PERSON_ROLES:{
					image = ImageCache.getImage("spanner_48.png");
					title = "The Roles Of This Person";
					titles[2] = "Movie Name";
					titles[1]="Role";
					toGet = "type";
					list = rolesList;
					message = "No roles for this person. Adding roles is done by choosing movie's cast.";
					cast = true;
					break;
				}
				case PERSON_QUOTES:{
					image = ImageCache.getImage("speech_bubble_48.png");
					title = "Famous Quotes Of The Person";
					titles[1] = "Quote";
					toGet = "name";
					break;
				}
				
				}
				if ((personButtons != null) && !(personButtons.isDisposed())){
					personButtons.dispose();
				}
				personButtons = new Composite (bar, SWT.FILL);
				if (result.size()>0){
					if ((otherResults != null) && !(otherResults.isDisposed()))
						otherResults.dispose();
					final Table table = new Table (personButtons, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
					table.setLinesVisible (true);
					table.setHeaderVisible (true);
					GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
					data.heightHint = 150;
					table.setLayoutData(data);
					for (int i=0; i<titles.length; i++) {
						TableColumn column = new TableColumn (table, SWT.NONE);
						column.setText (titles [i]);
					}	
					final int count = result.size();
					System.out.println(count);
					Map<String, String> map = null;
					for (int i=0; i<count; i++) {
						if (result.get(i)!=null){
							map = result.get(i).toStringMap();
							TableItem item = new TableItem (table, SWT.NONE);
							item.setText (0, String.valueOf(i+1));
							if (list != null) {
								if (getName(list , map.get(toGet))!=null)
									item.setText (1, getName(list , map.get(toGet)));
							}
							else
								item.setText (1, map.get(toGet));
							if (titles[2]!=""){
								if (map.get("name")!=null)
									item.setText (2, map.get("name"));
							}
						}
					}
					for (int i=0; i<titles.length; i++) {
						table.getColumn (i).pack ();
					}
					if (!cast){
						table.getColumn(2).setWidth(0);
						table.getColumn(2).setResizable(false);
					}
					GridLayout layout = new GridLayout (2,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					personButtons.setLayout(layout);
					if (!cast){
						Label label = new Label(personButtons , SWT.NONE);
						Button add = new Button(personButtons , SWT.PUSH);
						add.setText("Add");
						add.addSelectionListener(new SelectionListener() {
							public void widgetDefaultSelected(SelectionEvent e) {	
							}
							public void widgetSelected(SelectionEvent e){
								openPersonAddWindow(type , personId);
							}
						});
						Button delete = new Button(personButtons , SWT.PUSH);
						delete.setText("Delete");
						delete.addSelectionListener(new SelectionListener() {
							public void widgetDefaultSelected(SelectionEvent e) {	
							}
							public void widgetSelected(SelectionEvent e){
								int[] t= table.getSelectionIndices();
								if (t.length==0)
									okMessageBox("Please select a row to delete.");
								else{
									for (int i=0; i< t.length; i++){
										AbsType del = new NamedEntity(personId, table.getItem(t[i]).getText(1));
										try {
											pool.execute(DataManager.deletePersonEntity(type, del , personId));
										} catch (InterruptedException ev) {
											// TODO Auto-generated catch block
											ev.printStackTrace();
										}
		
									}
								}
							}
						});
					}
					otherResults = new ExpandItem(bar, SWT.NONE, 1);
					otherResults.setText(title);
					otherResults.setHeight(personButtons.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					otherResults.setControl(personButtons);
					otherResults.setImage(image);
					otherResults.setExpanded(true);
					entityDetails.setSize(shell.getShell().getSize().x-5, shell.getShell().getSize().y-150);
				}
				else{
					if (cast)
					okMessageBox(message);
					else{
						GridLayout layout = new GridLayout (1,false);
						layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
						layout.verticalSpacing = 10;
						personButtons.setLayout(layout);
						Button add = new Button(personButtons , SWT.PUSH);
						add.setText("Add");
						add.addSelectionListener(new SelectionListener() {
							public void widgetDefaultSelected(SelectionEvent e) {	
							}
							public void widgetSelected(SelectionEvent e){
								openPersonAddWindow(type , personId);
							}
						});
						if ((otherResults != null) && !(otherResults.isDisposed()))
							otherResults.dispose();
						otherResults = new ExpandItem(bar, SWT.NONE, 1);
						otherResults.setText(title);
						otherResults.setHeight(personButtons.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
						otherResults.setControl(personButtons);
						otherResults.setImage(image);
						otherResults.setExpanded(true);
						entityDetails.setSize(shell.getShell().getSize().x-5, shell.getShell().getSize().y-150);
					}
				}
			}
		});
	}
	
	protected static void drawMovieData(final List<AbsType> result ,final MovieDataEnum type , final int movieId){
		display.asyncExec(new Runnable() {
			public void run() {
				Image image = ImageCache.getImage("book_48.png"); ;
				String[] titles= new String[4];
				String toGet = "name";
				titles[0]="";
				titles[2] ="";
				titles[3] = "";
				String title = "";
				boolean cast = false;
				boolean combo = false;
				List<NamedEntity> list = null;
				switch(type){
				case MOVIE_COUNTRIES:
					list = countriesList;
					image = ImageCache.getImage("globe_48.png");
					title ="Movie's Countries";
					titles[1]="Country";
					toGet ="secondaryId";
					combo = true;
					break;
				case MOVIE_LANGUAGES:
					list = langList;
					image = ImageCache.getImage("furl_48.png");
					title = "Movie's Languages";
					titles[1] = "Language";
					toGet ="secondaryId";
					combo = true;
					break;
				case MOVIE_QUOTES:
					image = ImageCache.getImage("speech_bubble_48.png");
					title = "Famous Quotes From The Movie";
					titles[1] = "Quote";
					break;
				case MOVIE_GENRES:
					list = genresList;
					image = ImageCache.getImage("pie_chart_48.png");
					title = "Movie's Genres";
					titles[1] = "Genre";
					toGet ="secondaryId";
					combo = true;
					break;
				case MOVIE_CAST:
					image = ImageCache.getImage("users_two_48.png");
					title = "Movie's Cast";
					titles[1] = "Name";
					titles[2] = "Production Role";
					cast = true;
					break;
				}
				final boolean finalCast = cast;
				final boolean finalCombo = combo;
				final List<NamedEntity> finalList = list;
				if ((movieButtons != null) && !(movieButtons.isDisposed())){
					movieButtons.dispose();
				}
				movieButtons = new Composite (bar, SWT.FILL);
				if (result.size()>0){
					final Table table = new Table (movieButtons, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
					table.setLinesVisible (true);
					table.setHeaderVisible (true);
					GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
					data.heightHint = 200;
					table.setLayoutData(data);
					for (int i=0; i<titles.length; i++) {
						TableColumn column = new TableColumn (table, SWT.NONE);
						column.setText(titles[i]);
					}	
					final int count = result.size();
					System.out.println(count);
					Map<String, String> map = null;
					for (int i=0; i<count; i++) {
						if (result.get(i)!= null){
							map = result.get(i).toStringMap();
							TableItem item = new TableItem (table, SWT.NONE);
							item.setText (0, String.valueOf(i+1));
							if (list!=null){
								if (getName(list , map.get(toGet))!=null)
									item.setText (1, getName(list , map.get(toGet)));
							}
							else
								if (map.get(toGet)!=null)
									item.setText (1, map.get(toGet));
							if (cast){
								if (getName(rolesList , map.get("type"))!=null){
									item.setText (2, getName(rolesList , map.get("type")));
									item.setText(3 , map.get("id"));
								}
							}
						}
					}
					for (int i=0; i<titles.length; i++) {
						table.getColumn (i).pack ();
					}
					if(!cast){
							table.getColumn(2).setWidth(0);
							table.getColumn(2).setResizable(false);
					}
					table.getColumn(3).setWidth(0);
					table.getColumn(3).setResizable(false);
					GridLayout layout = new GridLayout (2,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					movieButtons.setLayout(layout);
					Label label = new Label(movieButtons , SWT.NONE);
					Button add = new Button(movieButtons , SWT.PUSH);
					add.setText("Add");
					add.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {	
						}
						public void widgetSelected(SelectionEvent e){
							if (!finalCombo)
								openMovieAddWindow(type , movieId);
							else
								openMovieAddFromListWindow(type , movieId);
						}
					});
					Button delete = new Button(movieButtons , SWT.PUSH);
					delete.setText("Delete");
					delete.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {	
						}
						public void widgetSelected(SelectionEvent e){
							int[] t= table.getSelectionIndices();
							if (t.length==0)
								okMessageBox("Please select a row to delete.");
							else{
								if (finalCast){
									for (int i=0; i< t.length; i++){
										int secondId = Integer.parseInt(table.getItem(t[i]).getText(3));
										int role = Integer.parseInt(getID(rolesList, table.getItem(t[i]).getText(2)));
										AbsType del = new CastingRelation(secondId, movieId, role);
										try {
											pool.execute(DataManager.deleteMovieEntity(type, del , movieId));
										} catch (InterruptedException ev) {
											// TODO Auto-generated catch block
											ev.printStackTrace();
										}
									}
								}
								if (finalCombo){
									for (int i=0; i< t.length; i++){
										int secondId = Integer.parseInt(getID( finalList,table.getItem(t[i]).getText(1)));
										AbsType del = new Relation(movieId, secondId);
										try {
											pool.execute(DataManager.deleteMovieEntity(type, del , movieId));
										} catch (InterruptedException ev) {
											// TODO Auto-generated catch block
											ev.printStackTrace();
										}
									}
								}
								else{
									for (int i=0; i< t.length; i++){
										AbsType del = new NamedEntity(movieId, table.getItem(t[i]).getText(1));
										try {
											pool.execute(DataManager.deleteMovieEntity(type, del , movieId));
										} catch (InterruptedException ev) {
											// TODO Auto-generated catch block
											ev.printStackTrace();
										}
									}
								}
							}
						}
					});
					if ((otherResults != null) && !(otherResults.isDisposed()))
						otherResults.dispose();
					otherResults = new ExpandItem(bar, SWT.NONE, 1);
					otherResults.setText(title);
					otherResults.setHeight(movieButtons.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					otherResults.setControl(movieButtons);
					otherResults.setImage(image);
					otherResults.setExpanded(true);
					entityDetails.setSize(shell.getShell().getSize().x-5, (shell.getShell().getSize().y)-150);
				}
				else{
					GridLayout layout = new GridLayout (1,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					movieButtons.setLayout(layout);
					Button add = new Button(movieButtons , SWT.PUSH);
					add.setText("Add");
					add.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {	
						}
						public void widgetSelected(SelectionEvent e){
							if (!finalCombo)
								openMovieAddWindow(type , movieId);
							else
								openMovieAddFromListWindow(type , movieId);
						}
					});
					if ((otherResults != null) && !(otherResults.isDisposed()))
						otherResults.dispose();
					otherResults = new ExpandItem(bar, SWT.NONE, 1);
					otherResults.setText(title);
					otherResults.setHeight(movieButtons.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					otherResults.setControl(movieButtons);
					otherResults.setImage(image);
					otherResults.setExpanded(true);
					entityDetails.setSize(shell.getShell().getSize().x-5, (shell.getShell().getSize().y)-150);

				}
			}
		});
	}
	
	protected static void setList(final List<NamedEntity> list, final NamedEntitiesEnum type) {
		display.asyncExec(new Runnable() {
			public void run() {
				System.out.println("Got list: " + type.toString());
			
				switch(type) {
				case GENRES:
					genresList = list;
					break;
				case COUNTRIES:
					countriesList = list;
					break;
				case PRODUCTION_ROLES:
					rolesList = list;
					break;
				case LANGUAGES:
					langList = list;
					break;
				}
			}
		});
	}
	
	private void createShell() {
		shell = new RibbonShell(display);
		shell.setText("DB Project, TAU 2009");
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		shell.setSize(new Point(monitor_bounds.width-100,monitor_bounds.height-100));
		
		shell.getShell().setMinimumSize(new Point(monitor_bounds.width-100,monitor_bounds.height-100));
		//closing the program.
		shell.getShell().addListener(SWT.Close, new Listener(){
			public void handleEvent(Event e){    			
    		/*	switch(yesNoMessageBox("Are you sure you want to exit?")){
    				case(SWT.YES):{shell.getShell().dispose();}
    				case(SWT.NO):{
    					e.doit = false;
	    				
    				}
    			}*/
    			pool.stopRequestAllWorkers();
			}
		});

		// Tab folder
		RibbonTabFolder tabs = shell.getRibbonTabFolder();
			
		// Tabs
		RibbonTab searchTab = new RibbonTab(tabs, "Search");
		RibbonTab insertTab = new RibbonTab(tabs, "Insert");	
		
		// Tooltips
		RibbonTooltip searchToolTip = new RibbonTooltip("Search For", "Please click on one of the buttons to search."); 
		RibbonTooltip insertToolTip = new RibbonTooltip("Insert", "Please click on one of the buttons to insert movie/person."); 
		RibbonTooltip importToolTip = new RibbonTooltip("Import The Database", "Please click the button to import.\n \\bWARNING: this could take a while."); 

		// Search tab
		RibbonGroup searching = new RibbonGroup(searchTab, "Search For" , searchToolTip);
		RibbonButton movieSearch = new RibbonButton(searching, ImageCache.getImage("camera_48.png"), " \nMovie", RibbonButton.STYLE_TWO_LINE_TEXT);
		new RibbonGroupSeparator(searching);
		RibbonButton personSearch = new RibbonButton(searching, ImageCache.getImage("user_48.png"), " \nPerson", RibbonButton.STYLE_TWO_LINE_TEXT);
		
		searchByMovie = new Composite(shell.getShell(),SWT.BORDER);
		searchByMovie.setBackground(shell.getShell().getBackground());
		searchByMovie.setLocation(2, 145);
		searchByMovie.setVisible(false);
		
		searchByPerson = new Composite(shell.getShell(),SWT.BORDER);
		searchByPerson.setBackground(shell.getShell().getBackground());
		searchByPerson.setVisible(false);
		
		resultsMovieTable = new Composite(shell.getShell(),SWT.NONE);
		resultsMovieTable.setVisible(false);
		resultsMovieTable.setBackground(shell.getShell().getBackground());
		
		resultsPersonTable = new Composite(shell.getShell(),SWT.NONE);
		resultsPersonTable.setBackground(shell.getShell().getBackground());
		resultsPersonTable.setVisible(false);
		
		insertMovie = new Composite(shell.getShell(),SWT.BORDER);
		insertMovie.setBackground(shell.getShell().getBackground());
		insertMovie.setLocation(2, 145);
		insertMovie.setVisible(false);
		
		insertPerson = new Composite(shell.getShell(),SWT.BORDER);
		insertPerson.setBackground(shell.getShell().getBackground());
		insertPerson.setLocation(2, 145);
		insertPerson.setVisible(false);
		
		// The Insert Tab
		// Insert Tab
		RibbonGroup inserting = new RibbonGroup(insertTab, "Insert" , insertToolTip);
		RibbonButton movieInsert = new RibbonButton(inserting, ImageCache.getImage("camera_48.png"), " \nMovie", RibbonButton.STYLE_TWO_LINE_TEXT);
		new RibbonGroupSeparator(inserting);
		RibbonButton personInsert = new RibbonButton(inserting, ImageCache.getImage("user_48.png"), " \nPerson", RibbonButton.STYLE_TWO_LINE_TEXT);
		RibbonGroup importing = new RibbonGroup(insertTab, "Import", importToolTip);
		RibbonButton importButton = new RibbonButton(importing , ImageCache.getImage("star_48.png"), " \nImport", RibbonButton.STYLE_TWO_LINE_TEXT);
		
		ButtonSelectGroup group = new ButtonSelectGroup();
			
		movieInsert.setButtonSelectGroup(group);
		personInsert.setButtonSelectGroup(group);
		movieSearch.setButtonSelectGroup(group);
		personSearch.setButtonSelectGroup(group);
		importButton.setButtonSelectGroup(group);
		
		movieSearch.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
			public void widgetSelected(SelectionEvent e){
				if ((searchByMovie!= null) && !(searchByMovie.isDisposed()))
					searchByMovie.dispose();
				if ((searchByPerson!= null) && !(searchByPerson.isDisposed()))	
					searchByPerson.dispose();
				if ((insertMovie!= null) && !(insertMovie.isDisposed()))
					insertMovie.dispose();
				if ((insertPerson!= null) && !(insertPerson.isDisposed()))	
					insertPerson.dispose();
				if ((movieButtons != null) && !(movieButtons.isDisposed()))
					movieButtons.dispose();
				if ((personButtons != null) && !(personButtons.isDisposed()))
					personButtons.dispose();
				if ((otherResults != null) && !(otherResults.isDisposed()))
					otherResults.dispose();
				if ((resultsMovieTable!=null) && (resultsMovieTable.isDisposed())) 
					resultsMovieTable.dispose();
				if ((resultsPersonTable!=null) && (resultsPersonTable.isDisposed())) 
					resultsPersonTable.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				if ((bar!=null)&& !(bar.isDisposed()))
					bar.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				searchByMovie = new Composite(shell.getShell(),SWT.BORDER);
				searchByMovie.setBackground(shell.getShell().getBackground());
				searchByMovie(searchByMovie);
			}
		});
		
		personSearch.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if ((searchByMovie!= null) && !(searchByMovie.isDisposed()))
					searchByMovie.dispose();
				if ((searchByPerson!= null) && !(searchByPerson.isDisposed()))	
					searchByPerson.dispose();
				if ((insertMovie!= null) && !(insertMovie.isDisposed()))
					insertMovie.dispose();
				if ((insertPerson!= null) && !(insertPerson.isDisposed()))	
					insertPerson.dispose();
				if ((movieButtons != null) && !(movieButtons.isDisposed()))
					movieButtons.dispose();
				if ((personButtons != null) && !(personButtons.isDisposed()))
					personButtons.dispose();
				if ((otherResults != null) && !(otherResults.isDisposed()))
					otherResults.dispose();
				if ((resultsMovieTable!=null) && (resultsMovieTable.isDisposed())) 
					resultsMovieTable.dispose();
				if ((resultsPersonTable!=null) && (resultsPersonTable.isDisposed())) 
					resultsPersonTable.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				if ((bar!=null)&& !(bar.isDisposed()))
					bar.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				searchByPerson = new Composite(shell.getShell(),SWT.BORDER);
				searchByPerson.setBackground(shell.getShell().getBackground());
				searchByPerson(searchByPerson);
			}	
		});
		movieInsert.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if ((searchByMovie!= null) && !(searchByMovie.isDisposed()))
					searchByMovie.dispose();
				if ((searchByPerson!= null) && !(searchByPerson.isDisposed()))	
					searchByPerson.dispose();
				if ((insertMovie!= null) && !(insertMovie.isDisposed()))
					insertMovie.dispose();
				if ((insertPerson!= null) && !(insertPerson.isDisposed()))	
					insertPerson.dispose();
				if ((movieButtons != null) && !(movieButtons.isDisposed()))
					movieButtons.dispose();
				if ((personButtons != null) && !(personButtons.isDisposed()))
					personButtons.dispose();
				if ((otherResults != null) && !(otherResults.isDisposed()))
					otherResults.dispose();
				if ((resultsMovieTable!=null) && (resultsMovieTable.isDisposed())) 
					resultsMovieTable.dispose();
				if ((resultsPersonTable!=null) && (resultsPersonTable.isDisposed())) 
					resultsPersonTable.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				if ((bar!=null)&& !(bar.isDisposed()))
					bar.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				insertMovie = new Composite(shell.getShell(),SWT.BORDER);
				insertMovie.setBackground(shell.getShell().getBackground());
				insertMovie(insertMovie);
			}	
		});
		personInsert.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if ((searchByMovie!= null) && !(searchByMovie.isDisposed()))
					searchByMovie.dispose();
				if ((searchByPerson!= null) && !(searchByPerson.isDisposed()))	
					searchByPerson.dispose();
				if ((insertMovie!= null) && !(insertMovie.isDisposed()))
					insertMovie.dispose();
				if ((insertPerson!= null) && !(insertPerson.isDisposed()))	
					insertPerson.dispose();
				if ((movieButtons != null) && !(movieButtons.isDisposed()))
					movieButtons.dispose();
				if ((personButtons != null) && !(personButtons.isDisposed()))
					personButtons.dispose();
				if ((otherResults != null) && !(otherResults.isDisposed()))
					otherResults.dispose();
				if ((resultsMovieTable!=null) && (resultsMovieTable.isDisposed())) 
					resultsMovieTable.dispose();
				if ((resultsPersonTable!=null) && (resultsPersonTable.isDisposed())) 
					resultsPersonTable.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				if ((bar!=null)&& !(bar.isDisposed()))
					bar.dispose();
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				insertPerson = new Composite(shell.getShell(),SWT.BORDER);
				insertPerson.setBackground(shell.getShell().getBackground());
				insertPerson(insertPerson);
			}	
		});
	}				
	
	public static void searchByMovie(Composite search){
		Calendar toDay = Calendar.getInstance();
		final int year = toDay.get(Calendar.YEAR);
		search.setLocation(2,145);
		search.setLayout(new FillLayout());
		if ((bar!= null) && !(bar.isDisposed()))
				bar.dispose();
		bar = new ExpandBar (search, SWT.V_SCROLL);
		bar.setBackground( new Color(display , 177 ,200 , 231));
		Image image = ImageCache.getImage("search_48.png");
		// First item
		final Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout (7,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout); 
		Label label = new Label(composite,SWT.NONE);
		label.setText("Movie Name");
		final Button checkWildCard = new Button(composite, SWT.CHECK);
		checkWildCard.setText("Wildcard");
		checkWildCard.setSelection(true);
		final Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Movie Year	From");
		final Spinner yearFrom = new Spinner (composite, SWT.BORDER);
		yearFrom.setMinimum(1880);
		yearFrom.setMaximum(year+100);
		yearFrom.setSelection(1880);
		yearFrom.setPageIncrement(1);
		yearFrom.pack();
		label= new Label(composite,SWT.NONE);
		label.setText("To");
		final Spinner yearTo = new Spinner (composite, SWT.BORDER);
		yearTo.setMinimum(1880);
		yearTo.setMaximum(year+100);
		yearTo.setSelection(year+100);
		yearTo.setPageIncrement(1);
		yearTo.pack();
		Label movieGenres = new Label(composite ,SWT.NONE);
		movieGenres.setText("Movie Genre");
		final Combo genresCombo = new Combo (composite, SWT.READ_ONLY);
		String[] genresString= new String[genresList.size()+1];
		genresString[0]= "";
		for (int i=0; i<genresList.size(); i++){
			genresString[i+1]=genresList.get(i).getName();
		}
		genresCombo.setItems (genresString);
		label = new Label(composite,SWT.NONE);
		label.setText("Movie Language");
		final Combo langCombo = new Combo(composite ,SWT.READ_ONLY);
		String[] langString= new String[langList.size()+1];
		langString[0]="";
		for (int i=0; i<langList.size(); i++){
			langString[i+1]=langList.get(i).getName();
		}
		langCombo.setItems(langString);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		langCombo.setLayoutData(gridData);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Search for movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		item0.setExpanded(true);
		bar.setSpacing(8);
		search.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/3);
		//listener for the search button
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if ((resultsPersonTable != null) && !(resultsPersonTable.isDisposed()))
					resultsPersonTable.dispose();
				if ((resultsMovieTable != null) && !(resultsMovieTable.isDisposed()))
					resultsMovieTable.dispose();
				resultsMovieTable = new Composite(shell.getShell(),SWT.NONE);
				resultsMovieTable.setBackground(shell.getShell().getBackground());
				resultsMovieTable.setLayout(new GridLayout());
				//creating the filter to search by
				List<AbsFilter> list = new ArrayList<AbsFilter>();;
				System.out.println(nameText.getText());
				if (nameText.getText()!= "" && nameText.getText().length() > 0){
					// Search without wildcard
					if(checkWildCard.getSelection())
						list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_NAME_WILDCARD, nameText.getText()));
					else
						list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_NAME, nameText.getText()));
				}
				if (genresCombo.getText() != ""){
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_GENRE,getID(genresList , genresCombo.getText()) ));
				}
				if (langCombo.getText() != ""){
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_LANGUAGES,getID(langList , langCombo.getText()) ));
				}
				//search by year only if the years parameters were changed
				if ((Integer.parseInt(yearFrom.getText())!= 1880) || ((Integer.parseInt(yearTo.getText())!= (year+100))))
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_YEAR, yearFrom.getText() , yearTo.getText()));
				System.out.println(list.toString());

				//search for movies
				try {
					pool.execute(DataManager.search(SearchEntitiesEnum.MOVIES, list));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		/* searchByMovie.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event arg0) {
				searchByMovie.redraw();
			}
		 });*/
	}
	public static void searchByPerson(Composite search){
		search.setLocation(2, 145);
		search.setLayout(new FillLayout());
		if ((bar != null)&& !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (search, SWT.V_SCROLL);
		bar.setBackground( new Color(display , 177 ,200 , 231));
		Image image = ImageCache.getImage("search_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (7,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		//to add- search by origin country, age-range 
		Label label= new Label(composite,SWT.NONE);
		label.setText("Person Name");
		final Button checkWildCard = new Button(composite, SWT.CHECK);
		checkWildCard.setText("Wildcard");
		checkWildCard.setSelection(true);
		final Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Age Range	From");
		final Spinner ageFrom = new Spinner (composite, SWT.BORDER);
		ageFrom.setMinimum(0);
		ageFrom.setMaximum(100);
		ageFrom.setSelection(0);
		ageFrom.setPageIncrement(1);
		ageFrom.pack();
		label = new Label(composite , SWT.NONE);
		label.setText("To");
		final Spinner ageTo = new Spinner (composite, SWT.BORDER);
		ageTo.setMinimum(0);
		ageTo.setMaximum(100);
		ageTo.setSelection(100);
		ageTo.setPageIncrement(1);
		ageTo.pack();
		label = new Label(composite ,SWT.NONE);
		label.setText("Production Role");
		final Combo rolesCombo = new Combo (composite, SWT.READ_ONLY);
		String[] rolesString= new String[rolesList.size()+1];
		rolesString[0]="";
		for (int i=0; i<rolesList.size(); i++){
			rolesString[i+1]=rolesList.get(i).getName();
		}
		rolesCombo.setItems (rolesString);
		label= new Label(composite, SWT.NONE);
		label.setText("Origin Country");
		final Combo countryCombo = new Combo (composite, SWT.READ_ONLY);
		String[] countryString= new String[countriesList.size()+1];
		countryString[0]="";
		for (int i=0; i<countriesList.size(); i++){
			countryString[i+1]=countriesList.get(i).getName();
		}
		countryCombo.setItems (countryString);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		countryCombo.setLayoutData(gridData);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Search for person");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		item0.setExpanded(true);
		bar.setSpacing(8);
		//searchByActor.setVisible(checked);
		search.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/3);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if ((resultsPersonTable != null) && !(resultsPersonTable.isDisposed()))
					resultsPersonTable.dispose();
				if ((resultsMovieTable != null) && !(resultsMovieTable.isDisposed()))
					resultsMovieTable.dispose();
				resultsPersonTable = new Composite(shell.getShell(),SWT.NONE);
				resultsPersonTable.setBackground(shell.getShell().getBackground());
				resultsPersonTable.setLayout(new GridLayout());
				
				//creating the filter to search by
				List<AbsFilter> list = new ArrayList<AbsFilter>();
				System.out.println(nameText.getText());
				if (nameText.getText()!= "" && nameText.getText().length() > 0){
					if(checkWildCard.getSelection())
						list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME_WILDCARD, nameText.getText()));
					else
						list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME, nameText.getText()));
				}
				if (rolesCombo.getText() != ""){
					list.add(dm.getFilter(SearchEntitiesEnum.PERSON_PRODUCTION_ROLE,getID(rolesList , rolesCombo.getText()) ));
				}
				if (countryCombo.getText() != ""){
					list.add(dm.getFilter(SearchEntitiesEnum.PERSON_ORIGIN_COUNTRY,getID(countriesList , countryCombo.getText()) ));
				}
				
				//search by age only if the ages parameters were changed
				if ((Integer.parseInt(ageFrom.getText())!= 0) || ((Integer.parseInt(ageTo.getText())!= (100))))
					list.add(dm.getFilter(SearchEntitiesEnum.PERSON_AGE, ageFrom.getText() , ageTo.getText()));
				System.out.println(list.toString());
				//search for persons
				
				try {
					pool.execute(DataManager.search(SearchEntitiesEnum.PERSONS, list ));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}			
		});

	}
	//message box that is opened whenever Yes/No/Cancel question is asked
	static public int yesNoMessageBox(String q){	
		shell.getShell().setEnabled(false);
		MessageBox mb = new MessageBox(shell.getShell(), SWT.YES | SWT.NO); 
		mb.setMessage(q);
		int answer = mb.open();	
		shell.getShell().setEnabled(true);
		return answer;		
	}
	//message box that is opened whenever OK statement is asked
	static public int okMessageBox(String q){	
		shell.getShell().setEnabled(false);
		MessageBox mb = new MessageBox(shell.getShell(), SWT.OK); 
		mb.setMessage(q);
		int answer = mb.open();	
		shell.getShell().setEnabled(true);
		return answer;		
	}
	public static void ShowMovieResult(final RibbonTab tab, final MovieEntity movie){
		searchByMovie.setVisible(false);
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 

		// Groups

		// Movie tab
		RibbonGroup generalInfo = new RibbonGroup(tab, "General Info" , toolTip);
		RibbonButton general = new RibbonButton(generalInfo, ImageCache.getImage("book_48.png"), " \nInformation", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonGroup results = new RibbonGroup(tab, "More Details" , toolTip);
		RibbonButton countries = new RibbonButton(results, ImageCache.getImage("globe_48.png"), " \nCountries", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton languages = new RibbonButton(results, ImageCache.getImage("furl_48.png"), " \nLanguages", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton quotes = new RibbonButton(results, ImageCache.getImage("speech_bubble_48.png"), " \nQuotes", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton genres = new RibbonButton(results, ImageCache.getImage("pie_chart_48.png"), " \nGenres", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonGroup personsGroup = new RibbonGroup(tab, "Cast" , toolTip);
		RibbonButton persons = new RibbonButton(personsGroup, ImageCache.getImage("users_two_48.png"), " \nPersons", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		ButtonSelectGroup group = new ButtonSelectGroup();
		general.setButtonSelectGroup(group);
		countries.setButtonSelectGroup(group);
		genres.setButtonSelectGroup(group);
		languages.setButtonSelectGroup(group);
		quotes.setButtonSelectGroup(group);
		persons.setButtonSelectGroup(group);
		drawGeneralInformationMovie(movie , tab.getIndex());
		
		countries.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationMovie( movie, tab.getIndex());
				entityDetails.setVisible(true);
				movieButtonsResults(movie.getId() , MovieDataEnum.MOVIE_COUNTRIES);
			}
		});
		languages.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationMovie( movie, tab.getIndex());
				entityDetails.setVisible(true);
				movieButtonsResults(movie.getId() , MovieDataEnum.MOVIE_LANGUAGES);
		}});
		quotes.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationMovie( movie, tab.getIndex());
				entityDetails.setVisible(true);
				movieButtonsResults(movie.getId() , MovieDataEnum.MOVIE_QUOTES);
			}
		});
		genres.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationMovie( movie, tab.getIndex());
				entityDetails.setVisible(true);
				movieButtonsResults(movie.getId() , MovieDataEnum.MOVIE_GENRES);
			}
		});
		persons.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationMovie( movie, tab.getIndex());
				entityDetails.setVisible(true);
				movieButtonsResults(movie.getId() , MovieDataEnum.MOVIE_CAST);
			}
		});
		general.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationMovie( movie, tab.getIndex());
			}
		});

	}
	
	public static void ShowPersonResult(final RibbonTab tab,final PersonEntity person){
		searchByPerson.setVisible(false);
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
		// Person Tab
		RibbonGroup generalInfo = new RibbonGroup(tab, "General Info" , toolTip);
		RibbonButton general = new RibbonButton(generalInfo, ImageCache.getImage("book_48.png"), " \nInformation", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonGroup results = new RibbonGroup(tab, "More Details" , toolTip);
		RibbonButton aka = new RibbonButton(results, ImageCache.getImage("book_48.png"), " \nAKA names", RibbonButton.STYLE_TWO_LINE_TEXT | RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton role = new RibbonButton(results, ImageCache.getImage("camera_48.png"), " \nRoles", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton quotes = new RibbonButton(results, ImageCache.getImage("speech_bubble_48.png"), " \nQuotes", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);

		ButtonSelectGroup group = new ButtonSelectGroup();
		general.setButtonSelectGroup(group);
		aka.setButtonSelectGroup(group);
		role.setButtonSelectGroup(group);
		quotes.setButtonSelectGroup(group);
		drawGeneralInformationPerson(person , tab.getIndex());
	
		aka.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationPerson(person ,tab.getIndex());
				personButtonsResults(person.getId() , PersonDataEnum.PERSON_AKAS);
			}			
		});
		role.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationPerson(person , tab.getIndex());
				personButtonsResults(person.getId() , PersonDataEnum.PERSON_ROLES);
			}			
		});
		quotes.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationPerson(person,tab.getIndex());
				personButtonsResults(person.getId() , PersonDataEnum.PERSON_QUOTES);
			}
		});
		general.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				drawGeneralInformationPerson(person, tab.getIndex());
			}
		});
	
	}
	static protected void personButtonsResults(int id, PersonDataEnum type){
		try {
			pool.execute(DataManager.getPersonData(type, id));
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static protected void movieButtonsResults(int id, MovieDataEnum type){
		try {
			pool.execute(DataManager.getMovieData(type, id));
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertMovie(Composite insert){
		Calendar toDay = Calendar.getInstance();
		final int year = toDay.get(Calendar.YEAR);
		insert.setLocation(2,145);
		insert.setLayout(new FillLayout(1));
		if ((bar!= null) && !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (insert, SWT.V_SCROLL);
		bar.setBackground( new Color(display , 177 ,200 , 231));
		Image image = ImageCache.getImage("add_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout); 
		Label label = new Label(composite,SWT.NONE);
		label.setText("Movie Name:");
		final Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Movie Year:");
		final Text yearText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Running Time:");
		final Text timeText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Plot:");
		final Text plotText = new Text(composite ,SWT.MULTI|SWT.BORDER| SWT.V_SCROLL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 5;
		gridData.verticalAlignment = GridData.FILL;
		gridData.verticalSpan = 2;
		plotText.setLayoutData(gridData);
		label = new Label(composite , SWT.NONE);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Insert");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Insert New Movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		item0.setExpanded(true);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				boolean valid = true ;
				int mYear = 0;
				int time = 0;
				String plot = null;
				if ((nameText.getText() == "" )||(yearText.getText()== "") )
					okMessageBox("Please insert movie name and movie year.");
				else{
					try{
						mYear =Integer.parseInt(yearText.getText());
						if ((mYear < 1880) || (mYear >year+100)){
							okMessageBox("Year is not valid. Must be between 1880 and " +(year+100)+".");
							valid = false;
						}
					}
					catch (NumberFormatException nfe){
						okMessageBox("Year must be a number.");
						valid = false;
					}
					if (timeText.getText() != ""){
						try{
							time = Integer.parseInt(timeText.getText());
							if (time < 0){
								okMessageBox("Running time must be a positive number.");
								valid = false;
							}
						}
						catch (NumberFormatException nfe){
							okMessageBox("Running time must be a number.");
							valid = false;
						}
					}
					if (plotText.getText() != "")
						plot = plotText.getText();
					if (valid){
						AbsType movie = new MovieEntity(0, nameText.getText(),mYear,null , null, time ,null, plot);
						try {
							pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE, movie , false));
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		bar.setSpacing(8);
		insert.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/3);
	}
	public void insertPerson(Composite insert){
		
		Calendar toDay = Calendar.getInstance();
		final int year = toDay.get(Calendar.YEAR);
		insert.setLocation(2,145);
		insert.setLayout(new FillLayout(1));
		if ((bar!= null) && !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (insert, SWT.V_SCROLL);
		bar.setBackground( new Color(display , 177 ,200 , 231));
		Image image = ImageCache.getImage("add_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout); 
		Label label = new Label(composite,SWT.NONE);
		label.setText("Person Name:");
		final Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Year Of Birth:");
		final Text birthText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite ,SWT.NONE);
		label.setText("Year Of Death:");
		final Text deathText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Origin City:");
		final Text cityText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite ,SWT.NONE);
		label.setText("Origin Country:");
		final Combo countryCombo = new Combo (composite, SWT.READ_ONLY);
		String[] countryString= new String[countriesList.size()+1];
		countryString[0]="";
		for (int i=0; i<countriesList.size(); i++){
			countryString[i+1]=countriesList.get(i).getName();
		}
		countryCombo.setItems (countryString);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		countryCombo.setLayoutData(gridData);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Insert");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Insert New Person");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		item0.setExpanded(true);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				boolean valid = true ;
				String city = null;
				int country = 0;
				int bYear = 0;
				int dYear = 0;
				
				if ((nameText.getText() == "")||(birthText.getText()=="")||(countryCombo.getText()==""))
					okMessageBox("Please insert person name, year of birth and origin country.");
				else{
					try{
						bYear =Integer.parseInt(birthText.getText());
						if ((bYear < 1800) || (bYear >year)){
							okMessageBox("Birth year is not valid. Must be between 1800 and " +(year)+".");
							valid = false;
						}
					}
					catch (NumberFormatException nfe){
						okMessageBox("Birth year must be a number.");
						valid = false;
					}
					if (deathText.getText()!=""){
						try{
							dYear =Integer.parseInt(deathText.getText());
							if ((dYear < bYear) || (dYear >year)){
								okMessageBox("Death year is not valid. Must be between the birth year and " +(year)+".");
								valid = false;
							}
						}
						catch (NumberFormatException nfe){
							okMessageBox("Death year must be a number.");
							valid = false;
						}
					}
					if (cityText.getText() != ""){
						city = cityText.getText();
					}
					String id = getID(countriesList, countryCombo.getText());
					if (id != null)
						country = Integer.parseInt(id);
					else{
						okMessageBox("Origin country is not valid.");
						valid = false;
					}
					if (valid){
						AbsType person = new PersonEntity(0,nameText.getText() ,bYear , city , country , dYear);
						try {
							pool.execute(DataManager.insertPersonData(PersonDataEnum.PERSON, person , false));
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		bar.setSpacing(8);
		insert.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/3);
	}
	
	static private String getID(List<NamedEntity> list , String name){
		String id=null;
		for (int i=0; i<list.size(); i++){
			if (name.compareTo(list.get(i).getName())==0){
				id = String.valueOf(list.get(i).getId());
				return id;
			}
		}
		return id;
	}
	static private String getName(List<NamedEntity> list , String id){
		String name=null;
		if (id != null){
			for (int i=0; i<list.size(); i++){
				if (id.compareTo(String.valueOf(list.get(i).getId())) ==0){
					name = String.valueOf(list.get(i).getName());
					return name;
				}
			}
		}
		//else return "";
		return name;
	}
	static private void drawGeneralInformationPerson(final PersonEntity person , final int tabIndex){
		if ((searchByMovie!= null) && !(searchByMovie.isDisposed()))
			searchByMovie.dispose();
		if ((searchByPerson!= null) && !(searchByPerson.isDisposed()))	
			searchByPerson.dispose();
		if ((insertMovie!= null) && !(insertMovie.isDisposed()))
			insertMovie.dispose();
		if ((insertPerson!= null) && !(insertPerson.isDisposed()))	
			insertPerson.dispose();
		if ((movieButtons != null) && !(movieButtons.isDisposed()))
			movieButtons.dispose();
		if ((personButtons != null) && !(personButtons.isDisposed()))
			personButtons.dispose();
		if ((otherResults != null) && !(otherResults.isDisposed()))
			otherResults.dispose();
		if ((resultsMovieTable!=null) && (resultsMovieTable.isDisposed())) 
			resultsMovieTable.dispose();
		if ((resultsPersonTable!=null) && (resultsPersonTable.isDisposed())) 
			resultsPersonTable.dispose();
		if ((entityDetails!= null) && !(entityDetails.isDisposed()))
			entityDetails.dispose();
		if ((bar!=null)&& !(bar.isDisposed()))
			bar.dispose();
		if ((entityDetails!= null) && !(entityDetails.isDisposed()))
			entityDetails.dispose();
		
		entityDetails = new Composite(shell.getShell(),SWT.BORDER);
		entityDetails.setLocation(2, 145);
		entityDetails.setLayout(new FillLayout());
		bar = new ExpandBar (entityDetails, SWT.V_SCROLL);
		bar.setBackground( new Color(display , 177 ,200 , 231));
		Image image = ImageCache.getImage("paper_content_48.png");
		
		// general information
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label label = new Label(composite,SWT.NONE);
		label.setText("Person Name:");
		final Text nameText = new Text(composite ,SWT.FILL);
		nameText.setText(person.getName());
		label = new Label(composite,SWT.NONE);
		label.setText("Year Of Birth: ");
		final Text bYearText = new Text(composite ,SWT.FILL);
		if (person.getYearOfBirth()!= 0)
			bYearText.setText(String.valueOf(person.getYearOfBirth()));
		final String oldBYear = String.valueOf(person.getYearOfBirth());
		label = new Label(composite,SWT.NONE);
		label.setText("Origin City: ");
		final Text cityText = new Text(composite ,SWT.FILL);
		if (person.getCityOfBirth()!= null)
			cityText.setText(person.getCityOfBirth());
		label = new Label(composite,SWT.NONE);
		label.setText("Origin Country: ");
		final Combo countryCombo = new Combo(composite ,SWT.READ_ONLY);
		if (person.getCountryOfBirth()!= 0)
			countryCombo.setText(getName(countriesList , String.valueOf(person.getCountryOfBirth())));
		String[] countryString= new String[countriesList.size()];
		int selected = 0;
		for (int i=0; i<countriesList.size(); i++){
			countryString[i]=countriesList .get(i).getName();
			if ( person.getCountryOfBirth() == countriesList.get(i).getId())
				selected = i;
		}
		countryCombo.setItems(countryString);
		countryCombo.select(selected);
		label = new Label(composite,SWT.NONE);
		label.setText("Year Of Death: ");
		final Text dYearText = new Text(composite ,SWT.FILL);
		if (person.getYearOfDeath()!= 0)
			dYearText.setText(String.valueOf(person.getYearOfDeath()));
		final String oldDYear = String.valueOf(person.getYearOfDeath());
		
		label= new Label(composite, SWT.NONE);
		label= new Label(composite, SWT.NONE);

		Button save = new Button (composite, SWT.PUSH);
		save.setText("Save");
		save.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				Calendar toDay = Calendar.getInstance();
				final int year = toDay.get(Calendar.YEAR);
				String newName = person.getName(); 
				int newBYear = person.getYearOfBirth(); 
				int newDYear = person.getYearOfDeath();
				int newCountry = person.getCountryOfBirth();
				String newCity = person.getCityOfBirth();
				boolean valid = true;
				boolean update = false;
				if (newName.compareTo(nameText.getText()) != 0){
					if (nameText.getText()!=""){
						newName = nameText.getText();
						update = true;
					}
					else{
						okMessageBox("You can't leave the name of the movie empty.");
						valid = false;
					}
				}
				if (oldBYear.compareTo(bYearText.getText())!= 0){
					if (bYearText.getText()!= ""){
						try{
							newBYear = Integer.parseInt(bYearText.getText());
							update = true;
							if ((newBYear< 1800) || (newBYear>year)){
								okMessageBox("Birth year must be between 1800 and "+year+".");
								valid = false;
							}
						}
						catch(NumberFormatException nfe){
							okMessageBox("The birth year must be a number.");
							valid = false;
						}
					}
					else
						okMessageBox("You can't leave the year of the movie empty.");
				}
				if (oldDYear.compareTo(dYearText.getText())!= 0){
					if (dYearText.getText()!= ""){
						try{
							newBYear = Integer.parseInt(bYearText.getText());
							update = true;
							if ((newDYear< newBYear) || (newDYear>year)){
								okMessageBox("Birth year must be between the birth year and "+year+".");
								valid = false;
							}
						}
						catch(NumberFormatException nfe){
							okMessageBox("The birth year must be a number.");
							valid = false;
						}
					}
				}
				
				if (newCountry != Integer.parseInt(getID(countriesList, countryCombo.getText()))){
					newCountry = Integer.parseInt(getID(countriesList, countryCombo.getText()));
					update = true;
				}
				
				if (newCity!= null){		
					if (newCity.compareTo(cityText.getText())!= 0){
						newCity = cityText.getText();
						update = true;
					}
				}
				else{
					if (cityText.getText()!=""){
						newCity = cityText.getText();
						update = true;	
					}
				}
				if (update && valid){
					person.setCityOfBirth(newCity);
					person.setCountryOfBirth(newCountry);
					person.setName(newName);
					person.setYearOfBirth(newBYear);
					person.setYearOfDeath(newDYear);
					try {
						pool.execute(DataManager.updatePersonData(PersonDataEnum.PERSON, person));
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					if (!update)
						okMessageBox("You didn't change anything.");
				}
			}
				
		});
		Button reset = new Button(composite , SWT.PUSH);
		reset.setText("Reset");
		reset.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				nameText.setText(person.getName());
				if (person.getYearOfBirth()!= 0)
					bYearText.setText(String.valueOf(person.getYearOfBirth()));
				else
					bYearText.setText("");
				if (person.getYearOfDeath()!= 0)
					dYearText.setText(String.valueOf(person.getYearOfDeath()));
				else
					dYearText.setText("");
				int selected = 0;
				for (int i=0; i<countriesList.size(); i++){
					if ( person.getCountryOfBirth() == countriesList.get(i).getId())
						selected = i;
				}
				countryCombo.select(selected);
				if (person.getCityOfBirth()!= null)
					cityText.setText(person.getCityOfBirth());
				else
					cityText.setText("");
				
			}
		});
		label = new Label(composite,SWT.NONE);
		label = new Label(composite,SWT.NONE);
		Button delete = new Button(composite , SWT.PUSH);
		delete.setText("Delete Person");
		delete.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				switch(yesNoMessageBox("Are you sure you want to delete this person?")){
				case (SWT.YES):{
					try {
						pool.execute(DataManager.deletePersonEntity(PersonDataEnum.PERSON, person , person.getId()));
						RibbonTab current = shell.getRibbonTabFolder().getSelectedTab();
						entityDetails.setVisible(false);
						shell.getRibbonTabFolder().selectPrevTab();
						List<RibbonTab> tabList = shell.getRibbonTabFolder().getTabs();
						tabList.remove(current.getIndex());
		                shell.getRibbonTabFolder().redraw();
					} catch (InterruptedException ev) {
						// TODO Auto-generated catch block
						ev.printStackTrace();
					}
				}
				case (SWT.NO):{} //do nothing
				}
			}
		});
		
		Button close = new Button(composite , SWT.PUSH);
		close.setText("Close Tab");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("General Information About The Person");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
		bar.setSpacing(8);
		entityDetails.setSize(shell.getShell().getSize().x-5, shell.getShell().getSize().y/4);
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				RibbonTab current = shell.getRibbonTabFolder().getTabs().get(tabIndex);
				entityDetails.setVisible(false);
				if (tabIndex == shell.getRibbonTabFolder().getSelectedTab().getIndex())
					shell.getRibbonTabFolder().selectPrevTab();
				List<RibbonTab> tabList = shell.getRibbonTabFolder().getTabs();
				tabList.remove(current.getIndex());
                shell.getRibbonTabFolder().redraw();
			}			
		});
		
	}
	static private void drawGeneralInformationMovie(final MovieEntity movie , final int tabIndex){
		if ((searchByMovie!= null) && !(searchByMovie.isDisposed()))
			searchByMovie.dispose();
		if ((searchByPerson!= null) && !(searchByPerson.isDisposed()))	
			searchByPerson.dispose();
		if ((insertMovie!= null) && !(insertMovie.isDisposed()))
			insertMovie.dispose();
		if ((insertPerson!= null) && !(insertPerson.isDisposed()))	
			insertPerson.dispose();
		if ((movieButtons != null) && !(movieButtons.isDisposed()))
			movieButtons.dispose();
		if ((personButtons != null) && !(personButtons.isDisposed()))
			personButtons.dispose();
		if ((otherResults != null) && !(otherResults.isDisposed()))
			otherResults.dispose();
		if ((entityDetails!= null) && !(entityDetails.isDisposed()))
			entityDetails.dispose();
		if ((bar!=null)&& !(bar.isDisposed()))
			bar.dispose();
		
		entityDetails = new Composite(shell.getShell(),SWT.BORDER);
		entityDetails.setLocation(2, 145);
		entityDetails.setLayout(new FillLayout());
		bar = new ExpandBar (entityDetails, SWT.V_SCROLL);
		bar.setBackground( new Color(display , 177 ,200 , 231));
		Image image = ImageCache.getImage("paper_content_48.png");
		
		// general information
		final Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		
		final ControlEditor editor = new ControlEditor(composite);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		
		Label label = new Label(composite,SWT.NONE);
		label.setText("Movie Name:");
		final Text nameText = new Text(composite ,SWT.FILL);
		if (movie.getName() != null)
			nameText.setText(movie.getName());
		label = new Label(composite,SWT.NONE);
		label.setText("Movie Year:");
		final Text yearText = new Text(composite ,SWT.FILL);
		if (movie.getYear()!= 0)
			yearText.setText(String.valueOf(movie.getYear()));
		final String oldYear = String.valueOf(movie.getYear());
		label = new Label(composite ,SWT.NONE);
		label.setText("Running Time:");
		final Text timeText = new Text(composite ,SWT.FILL);
		if (movie.getRunningTime()!=0)
			timeText.setText(String.valueOf(movie.getRunningTime()));
		final String oldTime = String.valueOf(movie.getRunningTime());
		label = new Label(composite,SWT.NONE);
		label.setText("Plot: ");
		final Text plotText = new Text(composite ,SWT.WRAP|SWT.V_SCROLL|SWT.MULTI);
		if (movie.getPlot()!= null)
			plotText.setText(movie.getPlot());
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 5;
		gridData.verticalAlignment = GridData.FILL;
		gridData.verticalSpan = 2;
		plotText.setLayoutData(gridData);
		label = new Label(composite ,SWT.NONE);
		Button save = new Button (composite, SWT.PUSH);
		save.setText("Save");
		save.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				Calendar toDay = Calendar.getInstance();
				final int year = toDay.get(Calendar.YEAR);
				String newName = movie.getName();
				int newYear = movie.getYear();
				int newTime = movie.getRunningTime();
				String newPlot = movie.getPlot();
				boolean valid = true;
				boolean update = false;
				if (newName.compareTo(nameText.getText()) != 0){
					if (nameText.getText()!=""){
						newName = nameText.getText();
						update = true;
					}
					else{
						okMessageBox("You can't leave the name of the movie empty.");
						valid = false;
					}
				}
				if (oldYear.compareTo(yearText.getText())!= 0){
					if (yearText.getText()!= ""){
						try{
							newYear = Integer.parseInt(yearText.getText());
							update = true;
							if ((newYear< 1880) || (newYear>year+100)){
								okMessageBox("Year must be between 1880 and "+(year+100)+".");
								valid = false;
							}
						}
						catch(NumberFormatException nfe){
							okMessageBox("The year must be a number.");
							valid = false;
						}
					}
					else
						okMessageBox("You can't leave the year of the movie empty.");
				}
				if (oldTime.compareTo(timeText.getText())!= 0){
					if (timeText.getText()!=""){
						try{
							newTime = Integer.parseInt(timeText.getText());
							update = true;
							if (newTime<0){
								okMessageBox("The running time must be larger than 0.");
								valid = false;
							}
						}
						catch (NumberFormatException nfe){
							okMessageBox("Running time must be a number.");
							valid = false;
						}
					}
					else{
						newTime = 0;
						update = true;
					}
				}
				if (newPlot != null){		
					if (newPlot.compareTo(plotText.getText())!= 0){
						newPlot = plotText.getText();
						update = true;
					}
				}
				else{
					if (plotText.getText()!=""){
						newPlot = plotText.getText();
						update = true;	
					}
				}
				if (update && valid){
					movie.setName(newName);
					movie.setPlot(newPlot);
					movie.setYear(newYear);
					movie.setRunningTime(newTime);
					try {
						pool.execute(DataManager.updateMovieData(MovieDataEnum.MOVIE, movie));
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					if (!update)
						okMessageBox("You didn't change anything.");
				}
			}
				
		});
		Button reset = new Button(composite , SWT.PUSH);
		reset.setText("Reset");
		reset.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				nameText.setText(movie.getName());
				if (movie.getYear()!= 0)
					yearText.setText(String.valueOf(movie.getYear()));
				else
					yearText.setText("");
				if (movie.getRunningTime()!=0)
					timeText.setText(String.valueOf(movie.getRunningTime()));
				else
					timeText.setText("");
				if (movie.getPlot()!= null)
					plotText.setText(movie.getPlot());
				else
					plotText.setText("");
				
			}
		});
		label = new Label(composite,SWT.NONE);
		label = new Label(composite,SWT.NONE);
		Button delete = new Button(composite , SWT.PUSH);
		delete.setText("Delete Movie");
		delete.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				switch(yesNoMessageBox("Are you sure you want to delete this movie?")){
				case (SWT.YES):{
					try {
						pool.execute(DataManager.deleteMovieEntity(MovieDataEnum.MOVIE, movie , movie.getId()));
						RibbonTab current = shell.getRibbonTabFolder().getSelectedTab();
						entityDetails.setVisible(false);
						shell.getRibbonTabFolder().selectPrevTab();
						List<RibbonTab> tabList = shell.getRibbonTabFolder().getTabs();
						tabList.remove(current.getIndex());
		                shell.getRibbonTabFolder().redraw();
					} catch (InterruptedException ev) {
						// TODO Auto-generated catch block
						ev.printStackTrace();
					}
				}
				case (SWT.NO):{} //do nothing
				}
			}
		});
		
		Button close = new Button(composite , SWT.PUSH);
		close.setText("Close Tab");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("General Information About The Movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				RibbonTab current = shell.getRibbonTabFolder().getTabs().get(tabIndex);
				entityDetails.setVisible(false);
				if (tabIndex == shell.getRibbonTabFolder().getSelectedTab().getIndex())
					shell.getRibbonTabFolder().selectPrevTab();
				List<RibbonTab> tabList = shell.getRibbonTabFolder().getTabs();
				tabList.remove(current.getIndex());
                shell.getRibbonTabFolder().redraw();;
			}			
		});
		bar.setSpacing(8);
		entityDetails.setSize(shell.getShell().getSize().x-5, (shell.getShell().getSize().y)/4);
	}
	static protected void drawSearchMovieTable(final List<DatedEntity> searched, final SearchEntitiesEnum search) {
		display.asyncExec(new Runnable() {
			public void run() {
				//creating the search results table
				final int count = searched.size();
				System.out.println(count);
				if (count > 0){
					final Table table = new Table (resultsMovieTable, SWT.MULTI| SWT.BORDER|SWT.FULL_SELECTION);
					resultsMovieTable.setBackground( new Color(display , 177 ,200 , 231));
					table.setLinesVisible (true);
					table.setHeaderVisible (true);
					GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
					data.heightHint = 200;
					table.setLayoutData(data);
					String[] titles = {" ", "Name", "Year", ""};
					for (int i=0; i<titles.length; i++) {
						TableColumn column = new TableColumn (table, SWT.NONE);
						column.setText (titles [i]);
					}	
					for (int i=0; i<count; i++) {
						TableItem item = new TableItem (table, SWT.NONE);
						item.setText (0, String.valueOf(i+1));
						item.setText (1, searched.get(i).getName());
						item.setText (2, String.valueOf(searched.get(i).getYear()));
						item.setText (3, String.valueOf(searched.get(i).getId()));
					}
					for (int i=0; i<titles.length; i++) {
						table.getColumn (i).pack ();
					}
					table.getColumn(3).setWidth(0);
					table.getColumn(3).setResizable(false);
					table.addListener(SWT.MouseDoubleClick, new Listener() {
						public void handleEvent(Event event) {
							Point pt = new Point(event.x, event.y);
							TableItem item = table.getItem(pt);
							if (item == null)
								return;
							for (int i = 0; i < count; i++) {
								Rectangle rect = item.getBounds(i);
								if (rect.contains(pt)) {
									int index = table.indexOf(item);
									int id = Integer.parseInt(table.getItem(index).getText(3));
									try {
										pool.execute(DataManager.getMovieById(id));
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					});
					resultsMovieTable.setLocation(2,  145+ shell.getShell().getSize().y/4);
					resultsMovieTable.setSize(shell.getShell().getSize().x-5, shell.getShell().getSize().y/2);
				}
				else{ // if there were no results
					okMessageBox("No results. Please change your choises and try again.");
				}
			}
		});
	}
	
	static protected void drawSearchPersonTable(final List<DatedEntity> searched, final SearchEntitiesEnum search) {
		display.asyncExec(new Runnable() {
			public void run() {
				final int count = searched.size();
				//creating the search results table
				if (count > 0){
					final Table table = new Table (resultsPersonTable, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
					resultsPersonTable.setBackground( new Color(display , 177 ,200 , 231));
					table.setLinesVisible (true);
					table.setHeaderVisible (true);
					GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
					data.heightHint = 200;
					table.setLayoutData(data);
					String[] titles = {" ", "Name", "Birth Year", ""};
					for (int i=0; i<titles.length; i++) {
						TableColumn column = new TableColumn (table, SWT.NONE);
						column.setText (titles [i]);
					}	
					for (int i=0; i<count; i++) {
						TableItem item = new TableItem (table, SWT.NONE);
						item.setText (0, String.valueOf(i+1));
						item.setText (1, searched.get(i).getName());
						item.setText (2, String.valueOf(searched.get(i).getYear()));
						item.setText (3, String.valueOf(searched.get(i).getId()));
					}
					for (int i=0; i<titles.length; i++) {
						table.getColumn (i).pack ();
					}	
					table.getColumn(3).setWidth(0);
					table.getColumn(3).setResizable(false);
					table.addListener(SWT.MouseDoubleClick, new Listener() {
						public void handleEvent(Event event) {
							Point pt = new Point(event.x, event.y);
							TableItem item = table.getItem(pt);
							if (item == null)
								return;
							for (int i = 0; i < count; i++) {
								Rectangle rect = item.getBounds(i);
								if (rect.contains(pt)) {
									int index = table.indexOf(item);
									int id = Integer.parseInt(table.getItem(index).getText(3));
									try {
										pool.execute(DataManager.getPersonById(id));
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					});
					resultsPersonTable.setLocation(2,  145+ shell.getShell().getSize().y/4);
					resultsPersonTable.setSize(shell.getShell().getSize().x-5, shell.getShell().getSize().y/2);
				}
				else{ // if there were no results
					switch(okMessageBox("No results. Please change your choises and try again.")){
					case(SWT.OK):{}
					}
				}
			}
		});
	}
	protected static void drawMoreInsertMovie(final int id){
		display.asyncExec(new Runnable() {
			public void run() {
				//genres- 2nd item
				if ((moreInsert!= null) && !(moreInsert.isDisposed()))
						moreInsert.dispose();
				
				if(id == -1) {
					okMessageBox("There was a problem inserting the movie. Sorry for the inconvenience :)");
				}
				else {
					moreInsert = new ExpandBar(insertMovie, SWT.V_SCROLL);
					moreInsert.setBackground( new Color(display , 177 ,200 , 231));
					Composite composite = new Composite (moreInsert, SWT.NONE);
					GridLayout layout = new GridLayout (6,false);
					Image image = ImageCache.getImage("pie_chart_48.png");
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					Label label = new Label(composite ,SWT.NONE);
					label.setText("Genre:");
					final Combo genresCombo = new Combo (composite, SWT.READ_ONLY);
					String[] genresString= new String[genresList.size()];
					for (int i=0; i<genresList.size(); i++){
						genresString[i]=genresList.get(i).getName();
					}
					genresCombo.setItems (genresString);
					Button genreButton = new Button (composite, SWT.PUSH);
					genreButton.setText("Add");
					genreButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(genresCombo.getText() != null && genresCombo.getText() != "") {
								AbsType t = new Relation(id, Integer.parseInt(getID(genresList, genresCombo.getText())));
								try {
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_GENRES, t , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					
					ExpandItem item1 = new ExpandItem(moreInsert, SWT.NONE, 0);
					item1.setText("Insert Movie's Genres");
					item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item1.setControl(composite);
					item1.setImage(image);
					item1.setExpanded(false);
					
					//languages- 3rd item
					composite = new Composite (moreInsert, SWT.NONE);
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					image = ImageCache.getImage("furl_48.png");
					label = new Label(composite,SWT.NONE);
					label.setText("Language:");
					final Combo langCombo = new Combo(composite ,SWT.READ_ONLY);
					String[] langString= new String[langList.size()];
					for (int i=0; i<langList.size(); i++){
						langString[i]=langList.get(i).getName();
					}
					langCombo.setItems(langString);
					Button langButton = new Button (composite, SWT.PUSH);
					langButton.setText("Add");
					langButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(langCombo.getText() != null && langCombo.getText() != "") {
								AbsType t = new Relation(id, Integer.parseInt(getID(langList, langCombo.getText())));
								try {
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_LANGUAGES, t , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					
					ExpandItem item2 = new ExpandItem(moreInsert, SWT.NONE, 1);
					item2.setText("Insert Movie's Languages");
					item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item2.setControl(composite);
					item2.setImage(image);
					item2.setExpanded(false);
					
					//countries - 4th item
					composite = new Composite (moreInsert, SWT.NONE);
					image =ImageCache.getImage("globe_48.png");
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
					label.setText("Country:");
					final Combo countryCombo = new Combo(composite ,SWT.READ_ONLY);
					String[] countryString= new String[countriesList.size()];
					for (int i=0; i<countriesList.size(); i++){
						countryString[i]=countriesList.get(i).getName();
					}
					countryCombo.setItems(countryString);
					Button countryButton = new Button (composite, SWT.PUSH);
					countryButton.setText("Add");
					countryButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(countryCombo.getText() != null && countryCombo.getText() != "") {
								AbsType t = new Relation(id, Integer.parseInt(getID(countriesList, countryCombo.getText())));
								try {
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_COUNTRIES, t , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					
					ExpandItem item3 = new ExpandItem(moreInsert, SWT.NONE, 2);
					item3.setText("Insert Movie's Countries");
					item3.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item3.setControl(composite);
					item3.setImage(image);
					item3.setExpanded(false);
					
					
					//quotes - 5th item
					composite = new Composite (moreInsert, SWT.NONE);
					image = ImageCache.getImage("speech_bubble_48.png");
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
					label.setText("Quote:");
					final Text quoteText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
					Button quoteButton = new Button (composite, SWT.PUSH);
					quoteButton.setText("Add");
					quoteButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(quoteText.getText() != "" ) {
								AbsType t = new NamedEntity(id, quoteText.getText());
								try {
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_QUOTES, t , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item4 = new ExpandItem(moreInsert, SWT.NONE, 3);
					item4.setText("Insert Movie's Quotes");
					item4.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item4.setControl(composite);
					item4.setImage(image);
					item4.setExpanded(false);
					
					//cast
					composite = new Composite (moreInsert, SWT.NONE);
					image = ImageCache.getImage("users_two_48.png");
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
					label.setText("Person Name:");
					final Text castText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
					Button castButton = new Button (composite, SWT.PUSH);
					castButton.setText("Search");
					castButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							List<AbsFilter> list = new ArrayList<AbsFilter>();
							//System.out.println(castText.getText());
							if (castText.getText()!= ""){
								list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME, castText.getText()));
							/*	AbsType t = new NamedEntity(id, akaText.getText());*/
								try {
									pool.execute(DataManager.search(SearchEntitiesEnum.PERSONS, list , id , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item5 = new ExpandItem(moreInsert, SWT.NONE, 4);
					item5.setText("Insert Movie's Cast");
					item5.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item5.setControl(composite);
					item5.setImage(image);
					item5.setExpanded(false);
					
					moreInsert.setSpacing(8);
					insertMovie.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)-150);
				}
			}
		});
	}
	protected static void drawMoreInsertPerson(final int id){
		display.asyncExec(new Runnable() {
			public void run() {
				if ((moreInsert!= null) && !(moreInsert.isDisposed()))
						moreInsert.dispose();
				if(id == -1) {
					okMessageBox("There was a problem inserting the person. Sorry for the inconvenience :)");
				}
				else{
					moreInsert = new ExpandBar(insertPerson, SWT.V_SCROLL);
					moreInsert.setBackground(new Color(display , 177 ,200 , 231));
					Composite composite = new Composite (moreInsert, SWT.NONE);
					GridLayout layout = new GridLayout (6,false);
					Image image = ImageCache.getImage("book_48.png");
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					//aka names- 2nd item
					Label label = new Label(composite ,SWT.NONE);
					label.setText("AKA Name:");
					final Text akaText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
					Button akaButton = new Button (composite, SWT.PUSH);
					akaButton.setText("Add");
					akaButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(akaText.getText() != "" ) {
								AbsType t = new NamedEntity(id, akaText.getText());
								try {
									pool.execute(DataManager.insertPersonData(PersonDataEnum.PERSON_AKAS, t , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item1 = new ExpandItem(moreInsert, SWT.NONE, 0);
					item1.setText("Insert AKA Names For The Person");
					item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item1.setControl(composite);
					item1.setImage(image);
					item1.setExpanded(false);
					
					//quotes - 3rd item
					composite = new Composite (moreInsert, SWT.NONE);
					image = ImageCache.getImage("speech_bubble_48.png");
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
					label.setText("Quote:");
					final Text quoteText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
					Button quoteButton = new Button (composite, SWT.PUSH);
					quoteButton.setText("Add");
					quoteButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(quoteText.getText() != "" ) {
								AbsType t = new NamedEntity(id, quoteText.getText());
								try {
									pool.execute(DataManager.insertPersonData(PersonDataEnum.PERSON_QUOTES, t , false));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item3 = new ExpandItem(moreInsert, SWT.NONE, 1);
					item3.setText("Insert Person's Quotes");
					item3.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item3.setControl(composite);
					item3.setImage(image);
					item3.setExpanded(false);

					moreInsert.setSpacing(8);
					insertPerson.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/2+100);
				}
			}
		});
	}
	static protected void peopleToInsertTable(final List<DatedEntity> searched, final SearchEntitiesEnum search ,final int movieId , final boolean update) {
		display.asyncExec(new Runnable() {
			public void run() {
				final int count = searched.size();
				//creating the search results table
				if (count > 0){
					castInsertWindow(searched , search , movieId , update);	
				}
				else{ // if there were no results
					switch(okMessageBox("No such person. Please try again.")){
					case(SWT.OK):{}
					}
				}
			}
		});
	}
	static protected void castInsertWindow(final List<DatedEntity> searched, SearchEntitiesEnum search , final int id , final boolean update){	
		shell.getShell().setEnabled(false);
		//final RibbonShell personResults = new RibbonShell(display);	
		final Shell personResults = new Shell(SWT.CLOSE);
		Color bgColor = new Color(display , 177 ,200 , 231);
		personResults.setBackground(bgColor);
		Rectangle monitor_bounds = personResults.getShell().getMonitor().getBounds();
		personResults.setSize(new Point(monitor_bounds.width/4,
		                        monitor_bounds.height/2));		
		personResults.setText("Persons List");		
		GridLayout layout = new GridLayout(2 , false);
		personResults.setLayout(layout);
		
		final Table table = new Table (personResults, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.NONE, SWT.NONE, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {" ", "Name", "Age", ""};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	
		for (int i=0; i<searched.size(); i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, String.valueOf(i+1));
			item.setText (1, searched.get(i).getName());
			item.setText (2, String.valueOf(searched.get(i).getYear()));
			item.setText (3, String.valueOf(searched.get(i).getId()));
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		table.getColumn(3).setWidth(0);
		table.getColumn(3).setResizable(false);
		final Combo rolesCombo = new Combo (personResults, SWT.FILL|SWT.READ_ONLY);
		String[] rolesString= new String[rolesList.size()];
		for (int i=0; i<rolesList.size(); i++){
			rolesString[i]=rolesList.get(i).getName();
		}
		rolesCombo.setItems (rolesString);
		Label label = new Label(personResults , SWT.NONE);
		label.setBackground(bgColor);
		label.setText("Actor's Role:");
		final Text roleText = new Text(personResults , SWT.BORDER);
		label = new Label(personResults , SWT.NONE);
		label.setBackground(bgColor);
		label.setText("Actor's Rank:");
		final Text rankText = new Text(personResults , SWT.BORDER);
		Button add = new Button(personResults, SWT.PUSH);
		add.setText("Add");
		add.setBackground(bgColor);
		Button close = new Button(personResults, SWT.PUSH);
		close.setText("Close");
		close.setBackground(bgColor);
		add.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				int[] t= table.getSelectionIndices();
				if ((rolesCombo.getText()=="")||(t.length==0))
					okMessageBox("Please select a person and a production role.");
				else{
					int secondId;
					int actors =Integer.parseInt(getID(rolesList, "Actors"));
					int role = Integer.parseInt(getID(rolesList, rolesCombo.getText()));
					if (role != actors){
						for (int i = 0 ; i<t.length; i++){
							secondId = Integer.parseInt(table.getItem(t[i]).getText(3));
							AbsType relation = new CastingRelation(secondId, id, role);
							try {
								pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_CAST,relation , update ));
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						table.deselectAll();
					}
					else{
						boolean valid = true;
						if ((roleText.getText()=="")|| (rankText.getText()=="")){
							okMessageBox("If you want to insert an actor you need to write his role and rank.");
						}
						else{
							String part = roleText.getText();
							int rank = 0;
							try{
								rank = Integer.parseInt(rankText.getText());
							}
							catch (NumberFormatException nfe){
								okMessageBox("The rank must be a number!");
								valid = false;
							}
							if (valid){
								for (int i = 0 ; i<t.length; i++){
									secondId = Integer.parseInt(table.getItem(t[i]).getText(3));
									AbsType relation = new CastingRelation(secondId, id, role , true , part , rank);
									try {
										pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_CAST,relation , false));
										shell.getShell().setEnabled(true);	
										personResults.close();
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}
								table.deselectAll();
							}
						}
					}
				}
			}
		});
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shell.getShell().setEnabled(true);	
				personResults.close();
			}
		});
		personResults.addDisposeListener( new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				shell.getShell().setEnabled(true);}			
		});
		personResults.open();
		
	}
	static protected void openPersonAddWindow(final PersonDataEnum type , final int personId){
		shell.getShell().setEnabled(false);
		//final RibbonShell personResults = new RibbonShell(display);	
		final Shell addToPerson = new Shell(SWT.CLOSE);
		Color bgColor = new Color(display , 177 ,200 , 231);
		addToPerson.setBackground(bgColor);
		Rectangle monitor_bounds = addToPerson.getShell().getMonitor().getBounds();
		addToPerson.setSize(new Point(monitor_bounds.width/5,100));		
		addToPerson.setText("Add To Person");		
		GridLayout layout = new GridLayout(2 , false);
		
		addToPerson.setLayout(layout);
		
		Label label = new Label(addToPerson , SWT.NONE);
		label.setBackground(bgColor);
		final Text text = new Text(addToPerson , SWT.FILL|SWT.BORDER);
		switch(type){
		case PERSON_AKAS:{
			label.setText("AKA name:");
			break;
		}
		case PERSON_QUOTES:{
			label.setText("Quote:");
			break;
		}
		}
		Button add = new Button(addToPerson, SWT.PUSH);
		add.setText("Add");
		add.setBackground(bgColor);
		Button close = new Button(addToPerson, SWT.PUSH);
		close.setText("Close");
		close.setBackground(bgColor);
		add.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if (text.getText()!=""){
					AbsType t = new NamedEntity(personId, text.getText());
					try {
						pool.execute(DataManager.insertPersonData(type, t , true));
						shell.getShell().setEnabled(true);	
						addToPerson.close();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				else{
					okMessageBox("Please insert information to add.");
				}
			}
		});
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shell.getShell().setEnabled(true);	
				addToPerson.close();
			}
		});
		addToPerson.addDisposeListener( new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				shell.getShell().setEnabled(true);
			}			
		});
		addToPerson.open();
	}
	static protected void openMovieAddWindow(final MovieDataEnum type , final int id){
		shell.getShell().setEnabled(false);
		//final RibbonShell personResults = new RibbonShell(display);	
		final Shell addToMovie = new Shell(SWT.CLOSE);
		Color bgColor = new Color(display , 177 ,200 , 231);
		addToMovie.setBackground(bgColor);
		Rectangle monitor_bounds = addToMovie.getShell().getMonitor().getBounds();
		addToMovie.setSize(new Point(monitor_bounds.width/5,100));		
		addToMovie.setText("Add To Movie");		
		GridLayout layout = new GridLayout(2 , false);
		String buttonString = "Add";
		addToMovie.setLayout(layout);
		Label label = new Label(addToMovie , SWT.NONE);
		label.setBackground(bgColor);
		final Text text = new Text(addToMovie , SWT.FILL|SWT.BORDER);
		switch(type){
		case MOVIE_QUOTES:{
			label.setText("Quote:");
			break;
		}
		case MOVIE_CAST:{
			label.setText("Cast:");
			buttonString = "Search";
			break;
		}
		}
		Button add = new Button(addToMovie, SWT.PUSH);
		add.setText(buttonString);
		add.setBackground(bgColor);
		Button close = new Button(addToMovie, SWT.PUSH);
		close.setText("Close");
		close.setBackground(bgColor);
		add.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				
				if (text.getText()!=""){
					if (type.equals(MovieDataEnum.MOVIE_CAST)){
						List<AbsFilter> list = new ArrayList<AbsFilter>();
						list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME, text.getText()));
						try {
							pool.execute(DataManager.search(SearchEntitiesEnum.PERSONS, list , id , true));
							shell.getShell().setEnabled(true);	
							addToMovie.close();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					else{
						AbsType t = new NamedEntity(id, text.getText());
						try {
							pool.execute(DataManager.insertMovieData(type, t , true));
							shell.getShell().setEnabled(true);	
							addToMovie.close();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				else{
					okMessageBox("Please insert information to add.");
				}
			}
		});
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shell.getShell().setEnabled(true);	
				addToMovie.close();
			}
		});
		addToMovie.addDisposeListener( new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				shell.getShell().setEnabled(true);
			}			
		});
		addToMovie.open();
	}
	static protected void openMovieAddFromListWindow(final MovieDataEnum type , final int id){
		shell.getShell().setEnabled(false);
		//final RibbonShell personResults = new RibbonShell(display);	
		final Shell addToMovie = new Shell(SWT.CLOSE);
		Color bgColor = new Color(display , 177 ,200 , 231);
		addToMovie.setBackground(bgColor);
		Rectangle monitor_bounds = addToMovie.getShell().getMonitor().getBounds();
		addToMovie.setSize(new Point(monitor_bounds.width/5,100));		
		addToMovie.setText("Add To Movie");		
		GridLayout layout = new GridLayout(2 , false);
		addToMovie.setLayout(layout);
		Label label = new Label(addToMovie , SWT.NONE);
		label.setBackground(bgColor);
		final Combo combo = new Combo(addToMovie , SWT.FILL|SWT.BORDER|SWT.READ_ONLY);
		
		String[] listString;
		List<NamedEntity> list = countriesList;
		switch(type){
		case MOVIE_COUNTRIES:{
			label.setText("Country:");
			break;
		}
		case MOVIE_GENRES:{
			label.setText("Genre:");
			list = genresList;
			break;
		}
		case MOVIE_LANGUAGES:{
			label.setText("Language:");
			Point size = new Point(monitor_bounds.width/5,combo.getSize().y);
			combo.setSize(size);
			list = langList;
			break;
		}
		}
		listString= new String[list.size()];
		for (int i=0; i<list.size(); i++){
			listString[i]=list.get(i).getName();
		}
		final List<NamedEntity> finalList = list;
		combo.setItems (listString);
		Button add = new Button(addToMovie, SWT.PUSH);
		add.setText("Add");
		add.setBackground(bgColor);
		Button close = new Button(addToMovie, SWT.PUSH);
		close.setText("Close");
		close.setBackground(bgColor);
		add.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				
				if (combo.getText()!=""){
					AbsType t = new Relation(id, Integer.parseInt(getID(finalList, combo.getText())));
					try {
						pool.execute(DataManager.insertMovieData(type, t , true));
						shell.getShell().setEnabled(true);	
						addToMovie.close();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				else{
					okMessageBox("Please insert information to add.");
				}
			}
		});
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				shell.getShell().setEnabled(true);	
				addToMovie.close();
			}
		});
		addToMovie.addDisposeListener( new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				shell.getShell().setEnabled(true);
			}			
		});
		addToMovie.open();
	}

	static protected void redrawPersonTable(final int id ,final PersonDataEnum type){
		display.asyncExec(new Runnable() {
			public void run() {
				personButtonsResults(id , type);
			}
		});
	}
	static protected void redrawMovieTable(final int id ,final MovieDataEnum type){
		display.asyncExec(new Runnable() {
			public void run() {
				movieButtonsResults(id , type);
			}
		});
	}

	static protected void drawInsertDataSuccess(final boolean ok) {
		display.asyncExec(new Runnable() {
			public void run() {
				if (ok)
					okMessageBox("Successfuly added");	
				else
					okMessageBox("There was a problem inserting this entity. Sorry for the inconvenience :)");
			}
		});
	}
	static protected void drawUpdateDataSuccess(final boolean ok) {
		display.asyncExec(new Runnable() {
			public void run() {
				if (ok)
					okMessageBox("Successfuly updated");
				else
					okMessageBox("There was a problem updating this entity. Sorry for the inconvenience :)");
			}
		});
	}
	static protected void drawDeleteDataFailure(final boolean result) {
		display.asyncExec(new Runnable() {
			public void run() {
				if (!result)
					okMessageBox("There was a problem deleting the entity. Sorry for the inconvenience :)");		
			}
		});
	}
}