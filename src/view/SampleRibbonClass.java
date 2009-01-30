package view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.LanguageCallback;

import oracle.net.aso.i;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import com.hexapixel.widgets.generic.ImageCache;
import com.hexapixel.widgets.generic.Utils;
import com.hexapixel.widgets.ribbon.ButtonSelectGroup;
import com.hexapixel.widgets.ribbon.QuickAccessShellToolbar;
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

public class SampleRibbonClass {
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
			
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.COLOR_INFOS));
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.COUNTRIES));
			pool.stopRequestIdleWorkers();
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.GENRES));
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.LANGUAGES));
			pool.stopRequestIdleWorkers();
			pool.execute(DataManager.getAllNamedEntities(NamedEntitiesEnum.PRODUCTION_ROLES));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		SampleRibbonClass app = new SampleRibbonClass();
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
				final RibbonTabFolder tabs = shell.getRibbonTabFolder();
				personTab = new RibbonTab(tabs, "Person");
				ShowPersonResult(personTab, person);
				tabs.selectTab(personTab);
				resultsPersonTable.setVisible(false);
			}
		});
	}
	protected static void drawPersonData(final List<AbsType> result ,final PersonDataEnum type){
		display.asyncExec(new Runnable() {
			public void run() {
				Image image = ImageCache.getImage("book_48.png"); ;
				String[] titles= new String[2];
				String toGet = "name";
				titles[0]="";
				List<NamedEntity> list = null;
				String message = "";
				String title = "";
				switch(type){
				case PERSON_AKAS:{
					image = ImageCache.getImage("book_48.png");
					title = "AKA Names For The Person";
					titles[1] = "Name";
					toGet = "name";
					message = "No AKA names for this person.";
					break;
				}
				case PERSON_ROLES:{
					image = ImageCache.getImage("spanner_48.png");
					title = "The Roles Of This Person";
					titles[1]="Role";
					toGet = "role";
					list = rolesList;
					message = "No roles for this person.";
					break;
				}
				case PERSON_QUOTES:{
					image = ImageCache.getImage("speech_bubble_48.png");
					title = "Famous Quotes Of The Person";
					titles[1] = "Quote";
					toGet = "name";
					message = "No quotes for this person";
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
					data.heightHint = 200;
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
						}
					}
					for (int i=0; i<titles.length; i++) {
						table.getColumn (i).pack ();
					}
					GridLayout layout = new GridLayout (3,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					personButtons.setLayout(layout);
					otherResults = new ExpandItem(bar, SWT.NONE, 1);
					otherResults.setText(title);
					otherResults.setHeight(personButtons.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					otherResults.setControl(personButtons);
					otherResults.setImage(image);
					otherResults.setExpanded(true);
				}
				else{
					switch(okMessageBox(message)){
					case(SWT.OK):{}
					}
				}
			}
		});
	}
	
	protected static void drawMovieData(final List<AbsType> result ,final MovieDataEnum type){
		display.asyncExec(new Runnable() {
			public void run() {
				Image image = ImageCache.getImage("book_48.png"); ;
				String[] titles= new String[3];
				String toGet = "name";
				titles[0]="";
				titles[2] ="";
				String title = "";
				boolean cast = false;
				String message = "";
				List<NamedEntity> list = null;
				switch(type){
				case MOVIE_AKAS:
					image = ImageCache.getImage("book_48.png");
					title = "AKA Names For The Movie";
					titles[1] = "Name";
					message = "No AKA names for this movie.";
					break;
				case MOVIE_CONNECTIONS:
					image = ImageCache.getImage("google_48.png");
					title = "Movie's Connections To Other Movies";
					titles[1]="Name";
					message = "This movie is not connected to any other movie.";
					break;
				case MOVIE_COUNTRIES:
					list = countriesList;
					image = ImageCache.getImage("globe_48.png");
					title ="Movie's Countries";
					titles[1]="Country";
					toGet ="secondaryId";
					message = "No countries for this movie.";
					break;
				case MOVIE_LANGUAGES:
					list = langList;
					image = ImageCache.getImage("furl_48.png");
					title = "Movie's Languages";
					titles[1] = "Language";
					toGet ="secondaryId";
					message = "No languages for this movie.";
					break;
				case MOVIE_GOOFS:
					image = ImageCache.getImage("smile_grin_48.png");
					title = "Movie's Goofs";
					titles[1] = "Goof";
					message = "No goofs for this movie.";
					break;
				case MOVIE_QUOTES:
					image = ImageCache.getImage("speech_bubble_48.png");
					title = "Famous Quotes From The Movie";
					titles[1] = "Quote";
					message = "No quotes for this movie.";
					break;
				case MOVIE_GENRES:
					list = genresList;
					image = ImageCache.getImage("pie_chart_48.png");
					title = "Movie's Genres";
					titles[1] = "Genre";
					toGet ="secondaryId";
					message = "No genres for this movie.";
					break;
				case MOVIE_CAST:
					image = ImageCache.getImage("users_two_48.png");
					title = "Movie's Cast";
					titles[1] = "Name";
					titles[2] = "Production Role";
					cast = true;
					message ="No cast for this movie.";
					break;
				}
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
								//else item.setText(1, "null");
							}
							else
								if (map.get(toGet)!=null)
									item.setText (1, map.get(toGet));
							if (cast){
								if (getName(rolesList , map.get("type"))!=null)
									item.setText (2, getName(rolesList , map.get("type")));
							}
						}
					}
					for (int i=0; i<titles.length; i++) {
						table.getColumn (i).pack ();
					}
					GridLayout layout = new GridLayout (3,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					movieButtons.setLayout(layout);
					if ((otherResults != null) && !(otherResults.isDisposed()))
						otherResults.dispose();
					otherResults = new ExpandItem(bar, SWT.NONE, 1);
					otherResults.setText(title);
					otherResults.setHeight(movieButtons.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					otherResults.setControl(movieButtons);
					otherResults.setImage(image);
					otherResults.setExpanded(true);
				}
				else{
					switch(okMessageBox(message)){
					case(SWT.OK):{}
					}
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
				case COLOR_INFOS:
					colorList = list;
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
				/*genresList = dm.getAllNamedEntities(NamedEntitiesEnum.GENRES);
				colorList = dm.getAllNamedEntities(NamedEntitiesEnum.COLOR_INFOS);
				langList = dm.getAllNamedEntities(NamedEntitiesEnum.LANGUAGES);
				rolesList =dm.getAllNamedEntities(NamedEntitiesEnum.PRODUCTION_ROLES);
				countriesList = dm.getAllNamedEntities(NamedEntitiesEnum.COUNTRIES);*/
			}
		});
	}
	
	private void createShell() {
		shell = new RibbonShell(display);
		shell.setButtonImage(ImageCache.getImage("selection_recycle_24.png"));
		//Shell shell = new Shell(display);
		shell.setText("DB Project, TAU 2009");
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		shell.setSize(new Point(monitor_bounds.width-100,monitor_bounds.height-100));
		//shell.setMaximized(true);
		//shell.setSize(570, 550);
		
		//closing the program.
		shell.getShell().addListener(SWT.Close, new Listener(){
			public void handleEvent(Event e){    			
    			switch(yesNoMessageBox("Are you sure you want to exit?")){
    				case(SWT.YES):{shell.getShell().dispose();}
    				case(SWT.NO):{e.doit = false;}
    			}
    			pool.stopRequestAllWorkers();
			}
		});
		
		QuickAccessShellToolbar mtb = shell.getToolbar();
		RibbonButton mtbtb1 = new RibbonButton(mtb, ImageCache.getImage("gear_ok_16.gif"), null, SWT.NONE);
		RibbonButton mtbtb2 = new RibbonButton(mtb, ImageCache.getImage("gantt_16.gif"), null, SWT.NONE);
		shell.setBigButtonTooltip(new RibbonTooltip("Big", "I'm the tooltip for the big button"));
		mtb.setArrowTooltip(new RibbonTooltip("Oh", "Jeez"));
		
		Menu shellMenu = shell.getBigButtonMenu();
		MenuItem btest = new MenuItem(shellMenu, SWT.POP_UP);
		btest.setText("Testing a menu");
		
		shell.addBigButtonListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				System.err.println("Clicked big button");
				shell.showBigButtonMenu();
			}
			
		});		
		//getting the lists from the DB
		//DataManager dm = DataManager.getInstance();
		
		// Tab folder
		RibbonTabFolder tabs = shell.getRibbonTabFolder();
		
		tabs.setHelpImage(ImageCache.getImage("questionmark.gif"));
		tabs.getHelpButton().setToolTip(new RibbonTooltip("Title", "Get Help Using Whatever This Is"));
		
		// Tabs
		RibbonTab searchTab = new RibbonTab(tabs, "Search");
		RibbonTab insertTab = new RibbonTab(tabs, "Insert");	
		//RibbonTab triviaTab = new RibbonTab(tabs, "Trivia");
		RibbonTab nadavTab = new RibbonTab(tabs, "Nadav");
		
		// Tooltip
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
	
		// Groups

		// Search tab
		RibbonGroup searching = new RibbonGroup(searchTab, "Search For" , toolTip);
		RibbonButton movieSearch = new RibbonButton(searching, ImageCache.getImage("camera_48.png"), " \nMovie", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(searching);
		RibbonButton personSearch = new RibbonButton(searching, ImageCache.getImage("user_48.png"), " \nPerson", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		
		searchByMovie = new Composite(shell.getShell(),SWT.BORDER);
		searchByMovie.setBackground(shell.getShell().getBackground());
		searchByMovie.setLocation(2, 145);
		searchByMovie.setVisible(false);
		
		searchByPerson = new Composite(shell.getShell(),SWT.BORDER);
		//searchByPerson.setSize((shell.getShell().getSize().x)-5,(shell.getShell().getSize().y)/4);
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
		
		// listeners for the buttons in the search tab
		
		// The Insert Tab
		// Insert Tab
		RibbonGroup inserting = new RibbonGroup(insertTab, "Insert" , toolTip);
		RibbonButton movieInsert = new RibbonButton(inserting, ImageCache.getImage("camera_48.png"), " \nMovie", RibbonButton.STYLE_TWO_LINE_TEXT);
		new RibbonGroupSeparator(inserting);
		RibbonButton personInsert = new RibbonButton(inserting, ImageCache.getImage("user_48.png"), " \nPerson", RibbonButton.STYLE_TWO_LINE_TEXT);
		RibbonGroup importing = new RibbonGroup(insertTab, "Import", toolTip);
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
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				if ((resultsMovieTable!= null) && !(resultsMovieTable.isDisposed()))
					resultsMovieTable.dispose();
				if ((resultsPersonTable!= null) && !(resultsPersonTable.isDisposed()))
					resultsPersonTable.dispose();
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
				if ((entityDetails!= null) && !(entityDetails.isDisposed()))
					entityDetails.dispose();
				if ((resultsMovieTable!= null) && !(resultsMovieTable.isDisposed()))
					resultsMovieTable.dispose();
				if ((resultsPersonTable!= null) && !(resultsPersonTable.isDisposed()))
					resultsPersonTable.dispose();
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
		Image image = ImageCache.getImage("search_48.png");
		// First item
		final Composite composite = new Composite (bar, SWT.NONE);
		//composite.setSize(search.getSize());
		//createMovieForm(composite);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout); 
		Label label = new Label(composite,SWT.NONE);
		label.setText("Movie Name");
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
		label = new Label(composite ,SWT.NONE);
		label.setText("Color-Info");
		final Combo colorCombo = new Combo (composite, SWT.READ_ONLY);
		String[] colorString= new String[colorList.size()+1];
		colorString[0]="";
		for (int i=0; i<colorList.size(); i++){
			colorString[i+1]=colorList.get(i).getName();
		}
		colorCombo.setItems (colorString);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Search for movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		item0.setExpanded(true);
		bar.setSpacing(8);
		//bar.setSize(shell.getShell().getSize());
		//searchByActor.setVisible(checked);
		//composite.setSize(shell.getShell().getSize().x-5, (shell.getShell().getSize().y/4));
		search.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/4);
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
				if (nameText.getText()!= ""){
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_NAME, nameText.getText()));
				}
				if (genresCombo.getText() != ""){
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_GENRE,getID(genresList , genresCombo.getText()) ));
				}
				if (langCombo.getText() != ""){
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_LANGUAGES,getID(langList , langCombo.getText()) ));
				}
				if (colorCombo.getText()!= ""){
					list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_COLOR_INFO,getID(colorList , colorCombo.getText()) ));
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
	}
	public static void searchByPerson(Composite search){
		search.setLocation(2, 145);
		search.setLayout(new FillLayout());
		if ((bar != null)&& !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (search, SWT.V_SCROLL);
		Image image = ImageCache.getImage("search_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		//to add- search by origin country, age-range 
		Label label= new Label(composite,SWT.NONE);
		label.setText("Person Name");
		final Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Age Range	From");
		//label = new Label(composite,SWT.CHECK);
		//label.setText("From");
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
		search.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/4);
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
				if (nameText.getText()!= ""){
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
	public int yesNoMessageBox(String q){	
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
	public static void ShowMovieResult(RibbonTab tab, final MovieEntity movie){
		searchByMovie.setVisible(false);
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
		
		// Groups

		// Movie tab
		RibbonGroup generalInfo = new RibbonGroup(tab, "General Info" , toolTip);
		RibbonButton general = new RibbonButton(generalInfo, ImageCache.getImage("book_48.png"), " \nInformation", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonGroup results = new RibbonGroup(tab, "More Details" , toolTip);
		RibbonButton aka = new RibbonButton(results, ImageCache.getImage("book_48.png"), " \nAKA names", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton connections = new RibbonButton(results, ImageCache.getImage("google_48.png"), " \nMovie Connections", RibbonButton.STYLE_TWO_LINE_TEXT);
		new RibbonGroupSeparator(results);
		RibbonButton countries = new RibbonButton(results, ImageCache.getImage("globe_48.png"), " \nCountries", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton languages = new RibbonButton(results, ImageCache.getImage("furl_48.png"), " \nLanguages", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton goofs = new RibbonButton(results, ImageCache.getImage("smile_grin_48.png"), " \nGoofs", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton quotes = new RibbonButton(results, ImageCache.getImage("speech_bubble_48.png"), " \nQuotes", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton genres = new RibbonButton(results, ImageCache.getImage("pie_chart_48.png"), " \nGenres", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonGroup personsGroup = new RibbonGroup(tab, "Cast" , toolTip);
		RibbonButton persons = new RibbonButton(personsGroup, ImageCache.getImage("users_two_48.png"), " \nPersons", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		ButtonSelectGroup group = new ButtonSelectGroup();
		general.setButtonSelectGroup(group);
		aka.setButtonSelectGroup(group);
		countries.setButtonSelectGroup(group);
		genres.setButtonSelectGroup(group);
		goofs.setButtonSelectGroup(group);
		languages.setButtonSelectGroup(group);
		quotes.setButtonSelectGroup(group);
		persons.setButtonSelectGroup(group);
		if ((entityDetails!= null) && !(entityDetails.isDisposed()))
			entityDetails.dispose();
		entityDetails = new Composite(shell.getShell(),SWT.BORDER);
		entityDetails.setLocation(2, 145);
		entityDetails.setLayout(new FillLayout());
		if ((bar!=null)&& !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (entityDetails, SWT.V_SCROLL);
		Image image = ImageCache.getImage("paper_content_48.png");
		
		// general information
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (2,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label movieName = new Label(composite,SWT.NONE);
		movieName.setText("Movie Name:");
		Text nameText = new Text(composite ,SWT.FILL);
		if (movie.getName() != null)
			nameText.setText(movie.getName());
		Label movieYear = new Label(composite,SWT.NONE);
		movieYear.setText("Movie Year:");
		Text yearText = new Text(composite ,SWT.FILL);
		if (movie.getYear()!= 0)
			yearText.setText(String.valueOf(movie.getYear()));
		Label runningTime = new Label(composite ,SWT.NONE);
		runningTime.setText("Running Time:");
		Text timeText = new Text(composite ,SWT.FILL);
		if (movie.getRunningTime()!=0)
			timeText.setText(String.valueOf(movie.getRunningTime()));
		Label plot = new Label(composite,SWT.NONE);
		plot.setText("Plot: ");
		Text plotText = new Text(composite ,SWT.WRAP);
		if (movie.getPlot()!= null)
			plotText.setText(movie.getPlot());
		
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Save");
		Button close = new Button(composite , SWT.PUSH);
		close.setText("Close Tab");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("General Information About The Movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
		
		//final Composite buttonsComp = new Composite(bar , SWT.FILL);
		//buttonsComp.setVisible(false);

		aka.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_AKAS);
			}
		});
		connections.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				//movieButtonsResults(movie , MovieDataEnum.MOVIE_CONNECTIONS);
				//not working
			}
		});
		countries.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_COUNTRIES);
			}
		});
		languages.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_LANGUAGES);
		}});
		goofs.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_GOOFS);
			}			
		});
		quotes.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_QUOTES);
			}
		});
		genres.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_GENRES);
			}
		});
		persons.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				entityDetails.setVisible(true);
				movieButtonsResults(movie , MovieDataEnum.MOVIE_CAST);
				//not working
			}
		});

		bar.setSpacing(8);
		entityDetails.setSize(shell.getShell().getSize().x-5, (shell.getShell().getSize().y)-150);
		close.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				RibbonTab current = shell.getRibbonTabFolder().getSelectedTab();
				entityDetails.setVisible(false);
				shell.getRibbonTabFolder().selectPrevTab();
				List<RibbonTab> tabList = shell.getRibbonTabFolder().getTabs();
				tabList.remove(current.getIndex());
                shell.getRibbonTabFolder().redraw();;
			}			
		});
	/*	tab.getTabFolder().addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event e){
				if (tab.isSelected())
					tab.dispose();
			}
		});*/
	}
	
	public static void ShowPersonResult(RibbonTab tab,final PersonEntity person){
		searchByPerson.setVisible(false);
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
		// Person Tab
		RibbonGroup results = new RibbonGroup(tab, "More About" , toolTip);
		RibbonButton aka = new RibbonButton(results, ImageCache.getImage("book_48.png"), " \nAKA names", RibbonButton.STYLE_TWO_LINE_TEXT | RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
	//	RibbonButton movies = new RibbonButton(results, ImageCache.getImage("camera_48.png"), " \nMovies", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
	//	new RibbonGroupSeparator(results);
		RibbonButton role = new RibbonButton(results, ImageCache.getImage("camera_48.png"), " \nRole", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton quotes = new RibbonButton(results, ImageCache.getImage("speech_bubble_48.png"), " \nQuotes", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);

		ButtonSelectGroup group = new ButtonSelectGroup();
		aka.setButtonSelectGroup(group);
	//	movies.setButtonSelectGroup(group);
		role.setButtonSelectGroup(group);
		quotes.setButtonSelectGroup(group);

		if ((entityDetails!= null) && !(entityDetails.isDisposed()))
			entityDetails.dispose();
		entityDetails = new Composite(shell.getShell(),SWT.BORDER);
		entityDetails.setLocation(2, 145);
		entityDetails.setLayout(new FillLayout());
		if ((bar!=null)&&!(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (entityDetails, SWT.V_SCROLL);
		Image image = ImageCache.getImage("paper_content_48.png");
		
		// general information
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label personName = new Label(composite,SWT.NONE);
		personName.setText("Person Name:");
		Text nameText = new Text(composite ,SWT.FILL);
		nameText.setText(person.getName());
		Label realName = new Label(composite,SWT.NONE);
		realName.setText("Real Name:");
		Text rnameText = new Text(composite ,SWT.FILL);
		if (person.getPersonRealName()!= null)
			rnameText.setText(person.getPersonRealName());
		Label nicks = new Label(composite ,SWT.NONE);
		nicks.setText("Nick Names:");
		org.eclipse.swt.widgets.List nicksList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		/*if (person.getPersonNickNames()!= null){
			String[] nicksString = person.getPersonNickNames().split("/X\\");
			nicksList.setBounds(50, 50, 75, 75);
			for(int i=0; i<nicksString.length; i++) nicksList.add(nicksString[i]);
		}*/
		System.out.println(person.getPersonNickNames());
		Label date = new Label(composite,SWT.NONE);
		date.setText("Year Of Birth: ");
		Text dateText = new Text(composite ,SWT.FILL);
		if (person.getYearOfBirth()!= 0)
			dateText.setText(String.valueOf(person.getYearOfBirth()));
		Label city = new Label(composite,SWT.NONE);
		city.setText("Born in: ");
		Text cityText = new Text(composite ,SWT.FILL);
		if (person.getCityOfBirth()!= null)
			cityText.setText(person.getCityOfBirth());
		Label country = new Label(composite,SWT.NONE);
		country.setText("Country: ");
		Text countryText = new Text(composite ,SWT.FILL);
		if (person.getCountryOfBirth()!= 0)
			countryText.setText(getName(countriesList , String.valueOf(person.getCountryOfBirth())));
		Label death = new Label(composite,SWT.NONE);
		death.setText("Died in: ");
		Text deathText = new Text(composite ,SWT.FILL);
		if (person.getYearOfDeath()!= 0)
			deathText.setText(String.valueOf(person.getYearOfDeath()));
		Label height = new Label(composite,SWT.NONE);
		height.setText("Height: ");
		Text heightText = new Text(composite ,SWT.FILL);
		if (person.getHeight()!= 0)
			heightText.setText(String.valueOf(person.getHeight()));
		Label trademark = new Label(composite, SWT.NONE);
		trademark.setText("Trademark: ");
		Text tmText = new Text(composite, SWT.MULTI);
		if (person.getTrademark()!= null)	
			tmText.setText(person.getTrademark());
		Label bio = new Label(composite,SWT.NONE);
		bio.setText("Biography: ");
		Text bioText = new Text(composite ,SWT.MULTI|SWT.V_SCROLL);
		if (person.getBiography()!= null)	
			bioText.setText(person.getBiography());
		
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Save");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("General Information About The Person");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
	
		aka.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				personButtonsResults(person , PersonDataEnum.PERSON_AKAS);
			}			
		});
		role.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				personButtonsResults(person , PersonDataEnum.PERSON_ROLES);
			}			
		});
		quotes.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				personButtonsResults(person , PersonDataEnum.PERSON_QUOTES);
			}
		});
		
		bar.setSpacing(8);
		entityDetails.setSize(shell.getShell().getSize().x-5, shell.getShell().getSize().y-150);
		
	}
	static protected void personButtonsResults(PersonEntity person, PersonDataEnum type){
		try {
			pool.execute(DataManager.getPersonData(type, person.getId()));
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static protected void movieButtonsResults(MovieEntity movie, MovieDataEnum type){
		try {
			pool.execute(DataManager.getMovieData(type, movie.getId()));
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertMovie(Composite insert){
		Calendar toDay = Calendar.getInstance();
		final int year = toDay.get(Calendar.YEAR);
		insert.setLocation(2,145);
		//FillLayout layout = new FillLayout();
		insert.setLayout(new FillLayout(1));
		if ((bar!= null) && !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (insert, SWT.V_SCROLL);
		Image image = ImageCache.getImage("add_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		//composite.setSize(search.getSize());
		//createMovieForm(composite);
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
		label = new Label(composite ,SWT.NONE);
		label.setText("Color-Info");
		final Combo colorCombo = new Combo (composite, SWT.READ_ONLY);
		String[] colorString= new String[colorList.size()];
		for (int i=0; i<colorList.size(); i++){
			colorString[i]=colorList.get(i).getName();
		}
		colorCombo.setItems (colorString);
		label = new Label(composite,SWT.NONE);
		label = new Label(composite,SWT.NONE);
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
				Calendar toDay = Calendar.getInstance();
				int today = toDay.get(Calendar.YEAR);
				boolean valid = true ;
				int year = 0;
				int color = 0;
				int time = 0;
				String taglines = null;
				String plot = null;
				String location = null;
				if ((nameText.getText() == "" )||(colorCombo.getText()=="" )||(yearText.getText()== "") )
					okMessageBox("Please insert movie name, movie year and movie color info.");
				else{
					try{
						year =Integer.parseInt(yearText.getText());
						if ((year < 1880) || (year >today+100)){
							okMessageBox("Year is not valid. Must be between 1880 and " +(today+100)+".");
							valid = false;
						}
					}
					catch (NumberFormatException nfe){
						okMessageBox("Year must be a number.");
						valid = false;
					}
					String id = getID(colorList, colorCombo.getText());
					if (id!= null)
						color = Integer.parseInt(id);
					else{
						okMessageBox("Color-info is not valid.");
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
						AbsType movie = new MovieEntity(0, nameText.getText(), year, null, null, color, time, taglines, plot, location);
						try {
							pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE, movie));
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		bar.setSpacing(8);
		insert.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/3-60);
	}
	public void insertPerson(Composite insert){
		
		Calendar toDay = Calendar.getInstance();
		final int year = toDay.get(Calendar.YEAR);
		insert.setLocation(2,145);
		insert.setLayout(new FillLayout(1));
		if ((bar!= null) && !(bar.isDisposed()))
			bar.dispose();
		bar = new ExpandBar (insert, SWT.V_SCROLL);
		Image image = ImageCache.getImage("add_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		//composite.setSize(search.getSize());
		//createMovieForm(composite);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout); 
		Label label = new Label(composite,SWT.NONE);
		label.setText("Person Name:");
		final Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Real Name:");
		final Text rNameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Nick Names:");
		final Text nNameText = new Text(composite ,SWT.MULTI|SWT.V_SCROLL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Year Of Birth:");
		final Text birthText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
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
		label = new Label(composite ,SWT.NONE);
		label.setText("Year Of Death:");
		final Text deathText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite ,SWT.NONE);
		label.setText("Height (cm):");
		final Text heightText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite ,SWT.NONE);
		label.setText("Trade Mark:");
		final Text markText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		label = new Label(composite,SWT.NONE);
		label.setText("Biography:");
		final Text bioText = new Text(composite ,SWT.MULTI|SWT.BORDER| SWT.V_SCROLL);
		
		label = new Label(composite,SWT.NONE);
		label = new Label(composite,SWT.NONE);
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
				Calendar toDay = Calendar.getInstance();
				int today = toDay.get(Calendar.YEAR);
				boolean valid = true ;
				String nick = null;
				String rName = null;
				String city = null;
				int country = 0;
				int height = 0;
				String mark = null;
				String bio = null;
				Date birth = null;
				Date death = null;
				int bYear = 0;
				int dYear = 0;
				
				if ((nameText.getText() == "")||(birthText.getText()=="")||(countryCombo.getText()==""))
					okMessageBox("Please insert person name, year of birth and origin country.");
				else{
					try{
						bYear =Integer.parseInt(birthText.getText());
						if ((year < 1800) || (year >today)){
							okMessageBox("Year is not valid. Must be between 1800 and " +(today)+".");
							valid = false;
						}
					}
					catch (NumberFormatException nfe){
						okMessageBox("Year must be a number.");
						valid = false;
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
					if (heightText.getText() != ""){
						try{
							height =Integer.parseInt(heightText.getText());
							if (height<0){
								okMessageBox("Height is not valid. Must be larger than 0.");
								valid = false;
							}
						}
						catch (NumberFormatException nfe){
							okMessageBox("Height must be a number.");
							valid = false;
						}
					}
						
					if (bioText.getText() != "")
						bio = bioText.getText();
					if (markText.getText() != "")
						mark = markText.getText();
					if (valid){
						AbsType person = new PersonEntity(0,nameText.getText() , rName , nick , birth ,bYear , city , country , death , dYear , height, mark , bio );
						try {
							pool.execute(DataManager.insertPersonData(PersonDataEnum.PERSON, person));
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
	public void createMovieForm(Composite composite){
		Calendar toDay = Calendar.getInstance();
		int year = toDay.get(Calendar.YEAR);
		FormLayout layout = new FormLayout();
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.spacing = 10;
		composite.setLayout(layout);
		
		Label movieName = new Label(composite,SWT.NONE);
		movieName.setText("Movie Name");
		Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		Label movieYear = new Label(composite,SWT.NONE);
		movieYear.setText("Movie Year");
		Label from = new Label(composite, SWT.NONE);
		from.setText("From");
		Spinner yearFrom = new Spinner (composite, SWT.BORDER);
		yearFrom.setMinimum(1900);
		yearFrom.setMaximum(year);
		yearFrom.setSelection(year);
		yearFrom.setPageIncrement(1);
		yearFrom.pack();
		/*Label to= new Label(composite,SWT.NONE);
		to.setText("To");
		Spinner yearTo = new Spinner (composite, SWT.BORDER);
		yearTo.setMinimum(1900);
		yearTo.setMaximum(year);
		yearTo.setSelection(year);
		yearTo.setPageIncrement(1);
		yearTo.pack();*/
		/*Label genre = new Label(composite ,SWT.NONE);
		genre.setText("Movie Genre");
		Combo genreCombo = new Combo (composite, SWT.READ_ONLY);
		//get the genres list from a table
		genreCombo.setItems (new String [] {"Action", "Fiction", "Sheker"});
		Label lang = new Label(composite,SWT.NONE);
		lang.setText("Movie Language");
		Combo langCombo = new Combo(composite ,SWT.READ_ONLY);
		langCombo.setItems(new String [] {"Hebrew", "English", "SHEKER"});
		Label color = new Label(composite ,SWT.NONE);
		color.setText("Color-Info");
		Combo colorCombo = new Combo (composite, SWT.READ_ONLY);
		//get the genres list from a table
		colorCombo.setItems (new String [] {"Black&White", "Colors", "SHEKER"});
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");*/
		
		FormData data= new FormData();
		
		data.top = new FormAttachment(nameText, 0, SWT.CENTER);
		movieName.setLayoutData(data);
		data = new FormData();
		data.left = new FormAttachment(movieName, 5);
		data.right = new FormAttachment(100, 0);
		nameText.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(from , 0 , SWT.CENTER);
		movieYear.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(nameText, 5);
		data.left = new FormAttachment(nameText, 0, SWT.LEFT);
		data.right = new FormAttachment(yearFrom, -5 );
		from.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(nameText, 5);
		data.left = new FormAttachment(from, 0, SWT.LEFT);
		//data.right = new FormAttachment(yearFrom, -5 );
		yearFrom.setLayoutData(data);

		
	/*	
		data = new FormData();
		data.top = new FormAttachment(genreCombo, 0, SWT.CENTER);
		genre.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(nameText, 5);
		data.left = new FormAttachment(nameText, 0, SWT.LEFT);
		genreCombo.setLayoutData(data);*/
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

	static protected void drawSearchMovieTable(final List<DatedEntity> searched, final SearchEntitiesEnum search) {
		display.asyncExec(new Runnable() {
			public void run() {
				//creating the search results table
				final int count = searched.size();
				System.out.println(count);
				if (count > 0){
					final Table table = new Table (resultsMovieTable, SWT.MULTI| SWT.BORDER|SWT.FULL_SELECTION);
					table.setLinesVisible (true);
					table.setHeaderVisible (true);
					GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
					data.heightHint = 200;
					table.setLayoutData(data);
					String[] titles = {" ", "Name", "Year", "ID"};
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
					resultsMovieTable.setSize(shell.getShell().getSize().x, shell.getShell().getSize().y/2);
				}
				else{ // if there were no results
					switch(okMessageBox("No results. Please change your choises and try again.")){
    				case(SWT.OK):{}
					}
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
					table.setLinesVisible (true);
					table.setHeaderVisible (true);
					GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
					data.heightHint = 200;
					table.setLayoutData(data);
					String[] titles = {" ", "Name", "Birth", "ID"};
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
					resultsPersonTable.setSize(shell.getShell().getSize().x, shell.getShell().getSize().y/2);
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
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_GENRES, t));
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
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_LANGUAGES, t));
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
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_COUNTRIES, t));
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
					
					//goofs - 5th item
					composite = new Composite (moreInsert, SWT.NONE);
					image =ImageCache.getImage("smile_grin_48.png");
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
					label.setText("Goof:");
					final Text goofText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
					Button goofButton = new Button (composite, SWT.PUSH);
					goofButton.setText("Add");
					goofButton.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}
						public void widgetSelected(SelectionEvent e) {
							if(goofText.getText() != "" ) {
								AbsType t = new NamedEntity(id, goofText.getText());
								try {
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_GOOFS, t));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item4 = new ExpandItem(moreInsert, SWT.NONE, 3);
					item4.setText("Insert Movie's Goofs");
					item4.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item4.setControl(composite);
					item4.setImage(image);
					item4.setExpanded(false);
					
					//quotes - 6th item
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
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_QUOTES, t));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item5 = new ExpandItem(moreInsert, SWT.NONE, 4);
					item5.setText("Insert Movie's Quotes");
					item5.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item5.setControl(composite);
					item5.setImage(image);
					item5.setExpanded(false);
					
					//aka names - 7th item
					composite = new Composite (moreInsert, SWT.NONE);
					image =  ImageCache.getImage("book_48.png");
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
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
									pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_AKAS, t));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item6 = new ExpandItem(moreInsert, SWT.NONE, 5);
					item6.setText("Insert Movie's AKA Names");
					item6.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item6.setControl(composite);
					item6.setImage(image);
					item6.setExpanded(false);
					//connections
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
							System.out.println(castText.getText());
							if (castText.getText()!= ""){
								list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME, castText.getText()));
							/*	AbsType t = new NamedEntity(id, akaText.getText());*/
								try {
									pool.execute(DataManager.search(SearchEntitiesEnum.PERSONS, list , id));
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
					ExpandItem item7 = new ExpandItem(moreInsert, SWT.NONE, 6);
					item7.setText("Insert Movie's Cast");
					item7.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item7.setControl(composite);
					item7.setImage(image);
					item7.setExpanded(false);
					
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
									pool.execute(DataManager.insertPersonData(PersonDataEnum.PERSON_AKAS, t));
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
									pool.execute(DataManager.insertPersonData(PersonDataEnum.PERSON_QUOTES, t));
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
					//roles- 4th item
					/*composite = new Composite (moreInsert, SWT.NONE);
					layout = new GridLayout (6,false);
					layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
					layout.verticalSpacing = 10;
					composite.setLayout(layout); 
					label = new Label(composite,SWT.NONE);
					label.setText("Production Roles:");
					final Combo roleCombo = new Combo(composite ,SWT.READ_ONLY);
					String[] roleString= new String[rolesList.size()];
					for (int i=0; i<rolesList.size(); i++){
						roleString[i]=rolesList.get(i).getName();
					}
					roleCombo.setItems(roleString);
					Button langButton = new Button (composite, SWT.PUSH);
					langButton.setText("Add");
					ExpandItem item2 = new ExpandItem(moreInsert, SWT.NONE, 2);
					item2.setText("Insert Person's Production Roles");
					item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					item2.setControl(composite);
					item2.setImage(image);
					item2.setExpanded(false);*/
					
					moreInsert.setSpacing(8);
					insertPerson.setSize((shell.getShell().getSize().x)-5, (shell.getShell().getSize().y)/2+100);
				}
			}
		});
	}
	static protected void peopleToInsertTable(final List<DatedEntity> searched, final SearchEntitiesEnum search ,final int movieId) {
		display.asyncExec(new Runnable() {
			public void run() {
				final int count = searched.size();
				//creating the search results table
				if (count > 0){
					castInsertWindow(searched , search , movieId);	
				}
				else{ // if there were no results
					switch(okMessageBox("No such person. Please try again.")){
					case(SWT.OK):{}
					}
				}
			}
		});
	}
	static protected void castInsertWindow(final List<DatedEntity> searched, SearchEntitiesEnum search , final int id){	
		shell.getShell().setEnabled(false);
		//final RibbonShell personResults = new RibbonShell(display);	
		final Shell personResults = new Shell();
	//	personResults.setBackground(shell.getShell().getBackground());
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
		String[] titles = {" ", "Name", "Age", "ID"};
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
		final Combo rolesCombo = new Combo (personResults, SWT.FILL|SWT.READ_ONLY);
		String[] rolesString= new String[rolesList.size()];
		for (int i=0; i<rolesList.size(); i++){
			rolesString[i]=rolesList.get(i).getName();
		}
		rolesCombo.setItems (rolesString);
		Label label = new Label(personResults , SWT.NONE);
		label.setText("Actor's Role:");
		final Text roleText = new Text(personResults , SWT.BORDER);
		label = new Label(personResults , SWT.NONE);
		label.setText("Actor's Rank:");
		final Text rankText = new Text(personResults , SWT.BORDER);
		Button add = new Button(personResults, SWT.PUSH);
		add.setText("Add");
		Button close = new Button(personResults, SWT.PUSH);
		close.setText("Close");
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
<<<<<<< .mine
					if (role != actors){
						for (int i = 0 ; i<t.length; i++){
							secondId = Integer.parseInt(table.getItem(t[i]).getText(3));
							AbsType relation = new CastingRelation(secondId, id, role);
							try {
								pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_CAST,relation ));
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
=======
					for (int i = 0 ; i<t.length; i++){
						secondId = Integer.parseInt(table.getItem(t[i]).getText(3));
						AbsType relation = new CastingRelation(secondId, id, role);
						try {
							pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_CAST,relation ));
						} catch (InterruptedException e1) {
							e1.printStackTrace();
>>>>>>> .r154
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
										pool.execute(DataManager.insertMovieData(MovieDataEnum.MOVIE_CAST,relation ));
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
		//shell.getShell().setEnabled(true);		
	}
	static protected void drawInsertDataSuccess() {
		display.asyncExec(new Runnable() {
			public void run() {
				okMessageBox("Successfuly added to movie");		
			}
		});
	}
}
