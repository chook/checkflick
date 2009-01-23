package view;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import com.hexapixel.widgets.generic.ColorCache;
import com.hexapixel.widgets.generic.ImageCache;
import com.hexapixel.widgets.generic.Utils;
import com.hexapixel.widgets.ribbon.*;

public class SampleRibbonClass {
	static RibbonShell shell;
	static Display display;
	static Composite searchByMovie;
	static Composite searchByPerson;
	static Composite resultsMovieTable;
	static Composite resultsPersonTable;
	public static void main(String args []) {
		display = new Display();
		SampleRibbonClass app = new SampleRibbonClass();
		app.createShell();
		
		Utils.centerDialogOnScreen(shell.getShell());
		app.shell.open();
		
		while (!app.shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
	}
	private void createShell() {
		shell = new RibbonShell(display);
		shell.setButtonImage(ImageCache.getImage("selection_recycle_24.png"));
		//Shell shell = new Shell(display);
		
		shell.setText("DB Project, TAU 2009");
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		shell.setSize(new Point(monitor_bounds.width,monitor_bounds.height));
		//shell.setMaximized(true);
		//shell.setSize(570, 550);
		
		//closing the program.
		shell.getShell().addListener(SWT.Close, new Listener(){
			public void handleEvent(Event e){    			
    			switch(yesNoCancelMessageBox("Are you sure you want to exit?")){
    				case(SWT.YES):{shell.getShell().dispose();}
    				case(SWT.NO):{e.doit = false;}
    			}
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
		
		// Tab folder
		RibbonTabFolder tabs = shell.getRibbonTabFolder();
		
		tabs.setHelpImage(ImageCache.getImage("questionmark.gif"));
		tabs.getHelpButton().setToolTip(new RibbonTooltip("Title", "Get Help Using Whatever This Is"));
		
		// Tabs
		RibbonTab searchTab = new RibbonTab(tabs, "Search");
		RibbonTab insertTab = new RibbonTab(tabs, "Insert");	
		RibbonTab triviaTab = new RibbonTab(tabs, "Trivia");
		
		// Tooltip
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
	
		// Groups

		// Search tab
		RibbonGroup searching = new RibbonGroup(searchTab, "Search For" , toolTip);
		RibbonButton movieSearch = new RibbonButton(searching, ImageCache.getImage("camera_48.png"), " \nMovie", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(searching);
		RibbonButton personSearch = new RibbonButton(searching, ImageCache.getImage("user_48.png"), " \nPerson", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		
		searchByMovie = new Composite(shell.getShell(),SWT.BORDER|SWT.NO_BACKGROUND);
		searchByMovie.setVisible(false);
		searchByPerson = new Composite(shell.getShell(),SWT.BORDER|SWT.NO_BACKGROUND);
		searchByPerson.setVisible(false);
		resultsMovieTable = new Composite(shell.getShell(),SWT.NONE|SWT.NO_BACKGROUND);
		resultsMovieTable.setVisible(false);
		resultsPersonTable = new Composite(shell.getShell(),SWT.NONE|SWT.NO_BACKGROUND);
		resultsPersonTable.setVisible(false);
		
		// listeners for the buttons in the search tab
		movieSearch.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
			public void widgetSelected(SelectionEvent e){
					searchByMovie.setVisible(true);
					if (searchByPerson.isVisible())
						searchByPerson.setVisible(false);
					searchByMovie(searchByMovie);
				}
			}			
		);
		
		personSearch.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				searchByPerson.setVisible(true);
				if (searchByMovie.isVisible())
					searchByMovie.setVisible(false);
				searchByPerson(searchByPerson);
			}			
		});
		
	
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
					
	}
	
	public static void searchByMovie(Composite search){
		search.setLocation(0, 145);
		search.setLayout(new FillLayout());
		final Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		ExpandBar bar = new ExpandBar (search, SWT.V_SCROLL);
		Image image = ImageCache.getImage("search_48.png");
		// First item
		final Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (4,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout); 
		Label nameCheck = new Label(composite,SWT.NONE);
		nameCheck.setText("Movie Name");
		Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		Label yearCheck = new Label(composite,SWT.NONE);
		yearCheck.setText("Movie Year");
		Text yearText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		Label genreCheck = new Label(composite ,SWT.NONE);
		genreCheck.setText("Movie Genre");
		Combo combo = new Combo (composite, SWT.READ_ONLY);
		//get the genres list from a table
		combo.setItems (new String [] {"Action", "Fiction", "Sheker"});
		Label langCheck = new Label(composite,SWT.NONE);
		langCheck.setText("Movie Language");
		Combo langText = new Combo(composite ,SWT.READ_ONLY);
		langText.setItems(new String [] {"Hebrew", "English", "SHEKER"});
		Label colorCheck = new Label(composite ,SWT.NONE);
		colorCheck.setText("Color-Info");
		Combo colorCombo = new Combo (composite, SWT.READ_ONLY);
		//get the genres list from a table
		colorCombo.setItems (new String [] {"Black&White", "Colors", "SHEKER"});
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Search for movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		item0.setExpanded(true);
		bar.setSpacing(8);
		//searchByActor.setVisible(checked);
		search.setSize(monitor_bounds.width-5, monitor_bounds.height/4);
		//listener for the search button
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				/*RibbonTabFolder tabs = shell.getRibbonTabFolder();
				RibbonTab movieTab = new RibbonTab(tabs, "Movie");
				movieTab.setSelected(true);
				ShowMovieResult(movieTab);
				tabs.selectTab(movieTab);
				*/
				resultsPersonTable.setVisible(false);
				resultsMovieTable.setVisible(true);
				resultsMovieTable.setLayout(new GridLayout());
				Table table = new Table (resultsMovieTable, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
				table.setLinesVisible (true);
				table.setHeaderVisible (true);
				GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
				data.heightHint = 200;
				table.setLayoutData(data);
				String[] titles = {" ", "Name", "Year", "Resource", "In Folder", "Location"};
				for (int i=0; i<titles.length; i++) {
					TableColumn column = new TableColumn (table, SWT.NONE);
					column.setText (titles [i]);
				}	
				int count = 128;
				for (int i=1; i<=count; i++) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText (0, ""+i+"");
					item.setText (1, "y");
					item.setText (2, "!");
					item.setText (3, "this stuff behaves the way I expect");
					item.setText (4, "almost everywhere");
					item.setText (5, "some.folder");
				}
				for (int i=0; i<titles.length; i++) {
					table.getColumn (i).pack ();
				}	
				resultsMovieTable.setLocation(0,  145+ monitor_bounds.height/4);
				resultsMovieTable.setSize(monitor_bounds.width, monitor_bounds.height/2);
			}			
		});
	}
	public static void searchByPerson(Composite search){
		search.setLocation(0, 145);
		search.setLayout(new FillLayout());
		final Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		ExpandBar bar = new ExpandBar (search, SWT.V_SCROLL);
		Image image = ImageCache.getImage("search_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (2,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		//to add- search by origin country, age-range 
		Button nameCheck = new Button(composite,SWT.CHECK);
		nameCheck.setText("Person Name");
		Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		Button YearCheck = new Button(composite,SWT.CHECK);
		YearCheck.setText("Person Age");
		Text YearText = new Text(composite ,SWT.SINGLE|SWT.FILL|SWT.BORDER);
		Button genreCheck = new Button(composite ,SWT.CHECK);
		genreCheck.setText("Production Role");
		Combo combo = new Combo (composite, SWT.READ_ONLY);
		combo.setItems (new String [] {"Actor/Actress", "Writer", "Producer" , "Director"});
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
		search.setSize(monitor_bounds.width-5, monitor_bounds.height/4);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				/*RibbonTabFolder tabs = shell.getRibbonTabFolder();
				RibbonTab personTab = new RibbonTab(tabs, "Person");
				personTab.setSelected(true);
				ShowPersonResult(personTab);
				tabs.selectTab(personTab);*/
				resultsPersonTable.setVisible(true);
				resultsMovieTable.setVisible(false);
				resultsPersonTable.setLayout(new GridLayout());
				Table table = new Table (resultsPersonTable, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
				table.setLinesVisible (true);
				table.setHeaderVisible (true);
				GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
				data.heightHint = 200;
				table.setLayoutData(data);
				String[] titles = {" ", "P", "#", "Description", "Resource", "In Folder", "Location"};
				for (int i=0; i<titles.length; i++) {
					TableColumn column = new TableColumn (table, SWT.NONE);
					column.setText (titles [i]);
				}	
				int count = 128;
				for (int i=0; i<count; i++) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText (0, "x");
					item.setText (1, "y");
					item.setText (2, "!");
					item.setText (3, "this stuff behaves the way I expect");
					item.setText (4, "almost everywhere");
					item.setText (5, "some.folder");
					item.setText (6, "line " + i + " in nowhere");
				}
				for (int i=0; i<titles.length; i++) {
					table.getColumn (i).pack ();
				}	
				resultsPersonTable.setLocation(0,  145+ monitor_bounds.height/4);
				resultsPersonTable.setSize(monitor_bounds.width, monitor_bounds.height/2);
				
			}			
		});

	}
	//message box that is opened whenever Yes/No/Cancel question is asked
	public int yesNoCancelMessageBox(String q){	
		shell.getShell().setEnabled(false);
		MessageBox mb = new MessageBox(shell.getShell(), SWT.YES | SWT.NO); 
		mb.setMessage(q);
		int answer = mb.open();	
		shell.getShell().setEnabled(true);
		return answer;		
	}
	public static void ShowMovieResult(RibbonTab tab){
		searchByMovie.setVisible(false);
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
		
		// Groups

		// Seach tab
		RibbonGroup results = new RibbonGroup(tab, "More About" , toolTip);
		RibbonButton aka = new RibbonButton(results, ImageCache.getImage("book_48.png"), " \nAKA names", RibbonButton.STYLE_TWO_LINE_TEXT);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
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
		new RibbonGroupSeparator(results);
		RibbonButton locations = new RibbonButton(results, ImageCache.getImage("image_48.png"), " \nLocations", RibbonButton.STYLE_TWO_LINE_TEXT );//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		
		ButtonSelectGroup group = new ButtonSelectGroup();
		aka.setButtonSelectGroup(group);
		countries.setButtonSelectGroup(group);
		genres.setButtonSelectGroup(group);
		goofs.setButtonSelectGroup(group);
		languages.setButtonSelectGroup(group);
		quotes.setButtonSelectGroup(group);
		locations.setButtonSelectGroup(group);

		final Composite movieDetails = new Composite(shell.getShell(),SWT.BORDER);
		movieDetails.setVisible(true);
		movieDetails.setLocation(0, 145);
		movieDetails.setLayout(new FillLayout());
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		final ExpandBar bar = new ExpandBar (movieDetails, SWT.V_SCROLL);
		Image image = ImageCache.getImage("paper_content_48.png");
		
		// general information
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (6,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label movieName = new Label(composite,SWT.NONE);
		movieName.setText("Movie Name:");
		Text nameText = new Text(composite ,SWT.FILL);
		nameText.setText("Pulp Fiction");
		Label movieYear = new Label(composite,SWT.NONE);
		movieYear.setText("Movie Year:");
		Text yearText = new Text(composite ,SWT.FILL);
		yearText.setText("2009");
		Label runningTime = new Label(composite ,SWT.NONE);
		runningTime.setText("Running Time:");
		Text timeText = new Text(composite ,SWT.FILL);
		timeText.setText("180 min");
		Label plot = new Label(composite,SWT.NONE);
		plot.setText("Plot: ");
		Text plotText = new Text(composite ,SWT.FILL);
		plotText.setText("bla bla bla bla");
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Save");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("General Information About The Movie");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
		
		//final Composite buttonsComp = new Composite (bar, SWT.FILL);
		//buttonsComp.setVisible(false);

		aka.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				//buttonsComp.setVisible(true);
				Composite buttonsComp = new Composite(bar , SWT.FILL);
				Image image = ImageCache.getImage("book_48.png");
				GridLayout layout = new GridLayout (6,false);
				layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
				layout.verticalSpacing = 10;
				buttonsComp.setLayout(layout);
				//result table goes here
				Text text = new Text(buttonsComp ,SWT.None);
				text.setText( "result table goes here");
				ExpandItem item1 = new ExpandItem(bar, SWT.NONE, 1);
				item1.setText("AKA Name For The Movie");
				item1.setHeight(buttonsComp.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				item1.setControl(buttonsComp);
				item1.setImage(image);
				item1.setExpanded(true);
			}			
		});
		countries.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				//buttonsComp.setVisible(false);
				Composite buttonsComp = new Composite(bar , SWT.FILL);
				Image image = ImageCache.getImage("globe_48.png");
				GridLayout layout = new GridLayout (6,false);
				layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
				layout.verticalSpacing = 10;
				buttonsComp.setLayout(layout);
				//result table goes here
				Text text = new Text(buttonsComp ,SWT.None);
				text.setText( "result table goes here");
				ExpandItem item1 = new ExpandItem(bar, SWT.NONE, 1);
				item1.setText("Movie's Countries");
				item1.setHeight(buttonsComp.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				item1.setControl(buttonsComp);
				item1.setImage(image);
				item1.setExpanded(true);
			}			
		});
		bar.setSpacing(8);
		movieDetails.setSize(monitor_bounds.width-5, monitor_bounds.height/3);
		
	}
	
	public static void ShowPersonResult(RibbonTab tab){
		searchByPerson.setVisible(false);
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 

		// Person Tab
		RibbonGroup results = new RibbonGroup(tab, "More About" , toolTip);
		RibbonButton aka = new RibbonButton(results, ImageCache.getImage("book_48.png"), " \nAKA names", RibbonButton.STYLE_TWO_LINE_TEXT | RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton movies = new RibbonButton(results, ImageCache.getImage("camera_48.png"), " \nMovies", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton role = new RibbonButton(results, ImageCache.getImage("spanner_48.png"), " \nRole", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(results);
		RibbonButton quotes = new RibbonButton(results, ImageCache.getImage("speech_bubble_48.png"), " \nQuotes", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);

		ButtonSelectGroup group = new ButtonSelectGroup();
		aka.setButtonSelectGroup(group);
		movies.setButtonSelectGroup(group);
		role.setButtonSelectGroup(group);
		quotes.setButtonSelectGroup(group);

		final Composite movieDetails = new Composite(shell.getShell(),SWT.BORDER);
		movieDetails.setVisible(true);
		movieDetails.setLocation(0, 145);
		movieDetails.setLayout(new FillLayout());
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		final ExpandBar bar = new ExpandBar (movieDetails, SWT.V_SCROLL);
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
		nameText.setText("Brad Pit");
		Label realName = new Label(composite,SWT.NONE);
		realName.setText("Real Name:");
		Text rnameText = new Text(composite ,SWT.FILL);
		rnameText.setText("Overrated");
		Label nicks = new Label(composite ,SWT.NONE);
		nicks.setText("Nick Names:");
		Text nicksText = new Text(composite ,SWT.FILL);
		nicksText.setText("Angelina's husband");
		Label date = new Label(composite,SWT.NONE);
		date.setText("Date Of Birth: ");
		Text dateText = new Text(composite ,SWT.FILL);
		dateText.setText("11.3.85");
		Label city = new Label(composite,SWT.NONE);
		city.setText("Born in: ");
		Text cityText = new Text(composite ,SWT.FILL);
		cityText.setText("Rishon");
		Label country = new Label(composite,SWT.NONE);
		country.setText("Country: ");
		Text countryText = new Text(composite ,SWT.FILL);
		countryText.setText("Israel");
		Label death = new Label(composite,SWT.NONE);
		death.setText("Died in: ");
		Text deathText = new Text(composite ,SWT.FILL);
		deathText.setText("it's alive...ALIVE!");
		Label height = new Label(composite,SWT.NONE);
		height.setText("Height: ");
		Text heightText = new Text(composite ,SWT.FILL);
		heightText.setText("1.88");
		Label bio = new Label(composite,SWT.NONE);
		bio.setText("Biography: ");
		Text bioText = new Text(composite ,SWT.FILL);
		bioText.setText("bla bla bla bla bla");
		
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Save");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("General Information About The Person");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(true);
		
		//final Composite buttonsComp = new Composite (bar, SWT.FILL);
		//buttonsComp.setVisible(false);

		aka.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				//buttonsComp.setVisible(true);
				Composite buttonsComp = new Composite(bar , SWT.FILL);
				Image image = ImageCache.getImage("book_48.png");
				GridLayout layout = new GridLayout (6,false);
				layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
				layout.verticalSpacing = 10;
				buttonsComp.setLayout(layout);
				//result table goes here
				Text text = new Text(buttonsComp ,SWT.None);
				text.setText( "result table goes here");
				ExpandItem item1 = new ExpandItem(bar, SWT.NONE, 1);
				item1.setText("AKA Names For The Person");
				item1.setHeight(buttonsComp.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				item1.setControl(buttonsComp);
				item1.setImage(image);
				item1.setExpanded(true);
			}			
		});
		movies.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				//buttonsComp.setVisible(false);
				Composite buttonsComp = new Composite(bar , SWT.FILL);
				Image image = ImageCache.getImage("camera_48.png");
				GridLayout layout = new GridLayout (6,false);
				layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
				layout.verticalSpacing = 10;
				buttonsComp.setLayout(layout);
				//result table goes here
				Text text = new Text(buttonsComp ,SWT.None);
				text.setText( "result table goes here");
				ExpandItem item1 = new ExpandItem(bar, SWT.NONE, 1);
				item1.setText("Movies The Person Participated In");
				item1.setHeight(buttonsComp.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				item1.setControl(buttonsComp);
				item1.setImage(image);
				item1.setExpanded(true);
			}			
		});
		bar.setSpacing(8);
		movieDetails.setSize(monitor_bounds.width-5, monitor_bounds.height/3);
		
	}
}
