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
	public static void main(String args []) {
		display = new Display();
		SampleRibbonClass app = new SampleRibbonClass();
		app.createShell();
		
		Utils.centerDialogOnScreen(shell.getShell());

		app.shell.open();
		
		while (!app.shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	private void createShell() {
		shell = new RibbonShell(display);
		shell.setButtonImage(ImageCache.getImage("selection_recycle_24.png"));
		//Shell shell = new Shell(display);
		
		shell.setText("DB Project, TAU 2009");
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		//shell.setSize(new Point(550,monitor_bounds.height/3));
		shell.setSize(550, 550);
		
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

		// Seach tab
		RibbonGroup searching = new RibbonGroup(searchTab, "Search For" , toolTip);
		RibbonButton movieSearch = new RibbonButton(searching, ImageCache.getImage("camera_48.png"), " \nMovie", RibbonButton.STYLE_TWO_LINE_TEXT | RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		new RibbonGroupSeparator(searching);
		RibbonButton personSearch = new RibbonButton(searching, ImageCache.getImage("user_48.png"), " \nPerson", RibbonButton.STYLE_TWO_LINE_TEXT |RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		
		final Composite searchByMovie = new Composite(shell.getShell(),SWT.BORDER);
		searchByMovie.setVisible(false);
		final Composite searchByPerson = new Composite(shell.getShell(),SWT.BORDER);
		searchByPerson.setVisible(false);
		
		// listeners for the buttons in the search tab
		movieSearch.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
			public void widgetSelected(SelectionEvent e){
					searchByMovie.setVisible(true);
					if (searchByPerson.isVisible())
						searchByPerson.setVisible(false);;
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
					searchByMovie.setVisible(false);;
				searchByPerson(searchByPerson);
			}			
		});
		
	
		// The Insert Tab
		// Insert Tab
		RibbonGroup inserting = new RibbonGroup(insertTab, "Insert" , toolTip);
		RibbonButton movieInsert = new RibbonButton(inserting, ImageCache.getImage("camera_48.png"), " \nMovie", SWT.NONE);
		new RibbonGroupSeparator(inserting);
		RibbonButton personInsert = new RibbonButton(inserting, ImageCache.getImage("user_48.png"), " \nPerson", SWT.NONE);
		RibbonGroup importing = new RibbonGroup(insertTab, "Import", toolTip);
		RibbonButton importButton = new RibbonButton(importing , ImageCache.getImage("star_48.png"), " \nImport", SWT.NONE);



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
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		ExpandBar bar = new ExpandBar (search, SWT.V_SCROLL);
		Image image = ImageCache.getImage("search_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (2,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Button nameCheck = new Button(composite,SWT.CHECK);
		nameCheck.setText("Movie Name");
		Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL);
		Button YearCheck = new Button(composite,SWT.CHECK);
		YearCheck.setText("Movie Year");
		Text YearText = new Text(composite ,SWT.SINGLE|SWT.FILL);
		Button genreCheck = new Button(composite ,SWT.CHECK);
		genreCheck.setText("Movie Genre");
		Combo combo = new Combo (composite, SWT.READ_ONLY);
		combo.setItems (new String [] {"Action", "Fiction", "Sheker"});
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
		search.setSize(548, monitor_bounds.height/4);
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
	public static void searchByPerson(Composite search){
		search.setLocation(0, 145);
		search.setLayout(new FillLayout());
		Rectangle monitor_bounds = shell.getShell().getMonitor().getBounds();
		ExpandBar bar = new ExpandBar (search, SWT.V_SCROLL);
		Image image = ImageCache.getImage("search_48.png");
		// First item
		Composite composite = new Composite (bar, SWT.FILL);
		GridLayout layout = new GridLayout (2,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 5;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Button nameCheck = new Button(composite,SWT.CHECK);
		nameCheck.setText("Person Name");
		Text nameText = new Text(composite ,SWT.SINGLE|SWT.FILL);
		Button YearCheck = new Button(composite,SWT.CHECK);
		YearCheck.setText("Person Age");
		Text YearText = new Text(composite ,SWT.SINGLE|SWT.FILL);
		Button genreCheck = new Button(composite ,SWT.CHECK);
		genreCheck.setText("Person Job");
		Combo combo = new Combo (composite, SWT.READ_ONLY);
		combo.setItems (new String [] {"Action", "Fiction", "Sheker"});
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
		search.setSize(548, monitor_bounds.height/4);

	}
}
