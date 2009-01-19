package view;

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
		
		//Text text = new Text(shell.getShell(), SWT.BORDER);
		//closing the program.
		shell.getShell().addListener(SWT.Close, new Listener(){
			public void handleEvent(Event e){    			
    			switch(yesNoCancelMessageBox("Are you sure you want to exit?")){
    				case(SWT.YES):{shell.getShell().dispose();}
    				case(SWT.NO):{e.doit = false;}
    			}
			}
		});
		/*
		GridLayout searchLayout = new GridLayout();
		searchLayout.makeColumnsEqualWidth = true;
		FormData searchLData = new FormData();
		searchLData.width = 541;
		searchLData.height = 343;
		searchLData.left =  new FormAttachment(0, 1000, 0);
		searchLData.top =  new FormAttachment(0, 1000, 48);
		final Composite searches = new Composite(shell.getShell(),SWT.BORDER);
		searches.setLayout(searchLayout);
		searches.setLayoutData(searchLData);
		//searches.computeSize(300, 300);*/
		
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
		
		
		//shell.setLayout(new FillLayout());
		//Composite inner = new Composite(shell, SWT.None);
		//inner.setLayout(new FillLayout(SWT.VERTICAL)); 
		//inner.setBackground(ColorCache.getInstance().getColor(182, 206, 238));		
		
		// Tab folder
		//final RibbonTabFolder ftf = new RibbonTabFolder(inner, SWT.NONE);
		RibbonTabFolder tabs = shell.getRibbonTabFolder();
		
		tabs.setHelpImage(ImageCache.getImage("questionmark.gif"));
		tabs.getHelpButton().setToolTip(new RibbonTooltip("Title", "Get Help Using Whatever This Is"));
		//ftf.setDrawEmptyTabs(false);
		// Tabs
		RibbonTab searchTab = new RibbonTab(tabs, "Search");
		RibbonTab insertTab = new RibbonTab(tabs, "Insert");	
		new RibbonTab(tabs, "Empty");
		
		// Tooltip
		RibbonTooltip toolTip = new RibbonTooltip("Some Action Title", "This is content text that\nsplits over\nmore than one\nline\n\\b\\c255000000and \\xhas \\bdifferent \\c000000200look \\xand \\bfeel.", ImageCache.getImage("tooltip.jpg"), ImageCache.getImage("questionmark.gif"), "Press F1 for more help"); 
	
		// Group

		// properties group
		RibbonGroup searchProperties = new RibbonGroup(searchTab, "Search By" , toolTip);
		RibbonButtonGroup searchCheckboxes = new RibbonButtonGroup(searchProperties);
		RibbonCheckbox movie = new RibbonCheckbox(searchCheckboxes, "Movie", SWT.NONE);
		RibbonCheckbox actor = new RibbonCheckbox(searchCheckboxes, "Actor", SWT.NONE);
		RibbonCheckbox genre = new RibbonCheckbox(searchCheckboxes, "Genre", SWT.NONE);
		movie.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}

			public void widgetSelected(SelectionEvent e) {
				final RibbonCheckbox rcb = (RibbonCheckbox) e.data;
				if (rcb.isChecked()){ 
					System.err.println("checked");
					searchByMovie(shell.getShell());
				}
				else {
					System.err.println("unchecked");
									
				}
			}			
		});
		
		actor.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}

			public void widgetSelected(SelectionEvent e) {
				final RibbonCheckbox rcb = (RibbonCheckbox) e.data;
				if (rcb.isChecked()){ 
					System.err.println("checked");
					searchByActor(shell.getShell());
				}
				else {
					System.err.println("unchecked");
					//searchByActor();				
				}
			}			
		});
		//RibbonToolbar toolbar = new RibbonToolbar(searchProperties, RibbonToolbar.STYLE_BORDERED, 2);
		//RibbonToolbarGrouping rtg = new RibbonToolbarGrouping(toolbar, 1);		
		//RibbonToolbarGrouping rtg2 = new RibbonToolbarGrouping(toolbar, 1);
		//RibbonToolbarGrouping rtg3 = new RibbonToolbarGrouping(toolbar, 1);
		
		//RibbonToolbarGrouping rtg4 = new RibbonToolbarGrouping(toolbar, 2);
		
		//RibbonButton rbTb1 = new RibbonButton(rtg, ImageCache.getImage("books_16.gif"), null, RibbonButton.STYLE_ARROW_DOWN_SPLIT | RibbonButton.STYLE_TOGGLE);
		//RibbonButton rbTb2 = new RibbonButton(rtg2, ImageCache.getImage("gear_ok_16.gif"), null, SWT.NONE);
		//RibbonButton rbTb3 = new RibbonButton(rtg2, ImageCache.getImage("gantt_16.gif"), null, RibbonButton.STYLE_ARROW_DOWN);
		//RibbonButton rbTb4 = new RibbonButton(rtg3, ImageCache.getImage("gantt_16.gif"), null, RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		
		//RibbonButton rbTb5 = new RibbonButton(rtg4, ImageCache.getImage("enabled_small.gif"), null, RibbonButton.STYLE_NO_DEPRESS);
		//RibbonButton rbTb6 = new RibbonButton(rtg4, ImageCache.getImage("selection_recycle_16.gif"), null, RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		
		//rbTb4.setEnabled(false);
		// end toolbar group
		RibbonGroup searching = new RibbonGroup(searchTab, "Different Searches", toolTip);
		
		
		// Button
		RibbonButton movieSearch = new RibbonButton(searching, ImageCache.getImage("olb_picture.gif"), "Movie\nSearch", RibbonButton.STYLE_TWO_LINE_TEXT | RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonButton ActorSearch = new RibbonButton(searching, ImageCache.getImage("olb_picture.gif"), "Actor\nSearch", RibbonButton.STYLE_TWO_LINE_TEXT | RibbonButton.STYLE_ARROW_DOWN);//RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonButton rb2 = new RibbonButton(searching, ImageCache.getImage("olb_picture.gif"), "I'm split\ntoggle", RibbonButton.STYLE_ARROW_DOWN_SPLIT | RibbonButton.STYLE_TOGGLE | RibbonButton.STYLE_TWO_LINE_TEXT);
		rb2.setBottomOrRightToolTip(toolTip);
		MenuItem test = new MenuItem(rb2.getMenu(), SWT.POP_UP);
		test.setText("Testing a menu");

		rb2.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}

			public void widgetSelected(SelectionEvent e) {
				final RibbonButton rb = (RibbonButton) e.data;
				if (rb.isTopSelected()) 
					System.err.println("Top clicked");
				else {
					System.err.println("Bottom clicked");
					rb.showMenu();					
				}
			}			
		});
		// The Insert Tab
		RibbonGroup insertGroup = new RibbonGroup(insertTab, "          Insert          ", toolTip);
		GridLayout gridL = new GridLayout(2, false);
		gridL.marginHeight = 5;
		gridL.marginLeft = 1;
		gridL.marginRight = 1;
		gridL.verticalSpacing = 1;
		gridL.horizontalSpacing = 2;
		gridL.marginBottom = 5;
		insertGroup.setLayout(gridL);
		Label nameLabel = new Label(insertGroup, SWT.NONE);
		nameLabel.setText("Name");
		nameLabel.setBackground(ColorCache.getInstance().getColor(222, 232, 246));
		final Text nameText = new Text(insertGroup, SWT.LEFT | SWT.BORDER);
		Label phoneLabel = new Label(insertGroup, SWT.NONE );
		phoneLabel.setText("Phone");
		phoneLabel.setBackground(ColorCache.getInstance().getColor(222, 232, 246));
		final Text phoneText = new Text(insertGroup, SWT.LEFT | SWT.BORDER);
		Label emailLable = new Label(insertGroup,SWT.NONE);
    	emailLable.setText("Email");
    	emailLable.setBackground(ColorCache.getInstance().getColor(222, 232, 246));
    	final Text emailText = new Text(insertGroup, SWT.LEFT | SWT.BORDER);

	

		ActorSearch.setToolTip(toolTip);
		//TODO: Check when a dialog opens as a result of clicking this to see if this button does not redraw for some reason or think it's still selected
		//new RibbonButton(searching, ImageCache.getImage("olb_picture.gif"), "I am longer and do not depress", RibbonButton.STYLE_NO_DEPRESS);

		RibbonGroup ftg2 = new RibbonGroup(insertTab, "Group 1");
		RibbonButton rb1 = new RibbonButton(ftg2, ImageCache.getImage("olb_picture2.gif"), "Button 1", SWT.NONE);
		//RibbonButton rb2 = new RibbonButton(ftg2, ImageCache.getImage("olb_picture3.gif"), "Button 2", SWT.NONE);

		RibbonGroup ftg3 = new RibbonGroup(insertTab, "Group 2");
		RibbonButton rb3 = new RibbonButton(ftg3, ImageCache.getImage("olb_picture4.gif"), "Button 3", SWT.NONE);
		RibbonButton rb4 = new RibbonButton(ftg3, ImageCache.getImage("olb_picture6.gif"), "Button 4", SWT.NONE);
		rb4.setToolTip(toolTip);

		ButtonSelectGroup group = new ButtonSelectGroup();
		
		// native controls example
		RibbonGroup ftg4 = new RibbonGroup(insertTab, "Native");
		GridLayout gl = new GridLayout(1, false);
		gl.marginHeight = 7;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.verticalSpacing = 1;
		gl.horizontalSpacing = 0;
		gl.marginBottom = 7;
		ftg4.setLayout(gl);
		Combo foo = new Combo(ftg4, SWT.READ_ONLY);
		foo.setText("Testing");
		foo.add("Testing 2");
		foo.add("Testing 3");
		foo.add("Testing 4");
		Button b = new Button(ftg4, SWT.PUSH);
		b.setText("Test");
				
		// create sub button containing 3 buttons inside it
		new RibbonGroupSeparator(searching);
		
		RibbonButtonGroup sub = new RibbonButtonGroup(searching);
		RibbonButton sub1 = new RibbonButton(sub, ImageCache.getImage("enabled_small.gif"), ImageCache.getImage("disabled_small.gif"), "Disabled", SWT.NONE);
		sub1.setEnabled(false);
		new RibbonCheckbox(sub, "I'm mixed in", SWT.NONE);

		// make arrow down
		RibbonButton rb5 = new RibbonButton(sub, ImageCache.getImage("olb_small2.gif"), "I am toggle split", RibbonButton.STYLE_TOGGLE | RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonButton rb6 = new RibbonButton(sub, ImageCache.getImage("olb_small3.gif"), "I am a quite long button", SWT.NONE);
		RibbonButton rb7 = new RibbonButton(sub, ImageCache.getImage("olb_small3.gif"), "I split normal", RibbonButton.STYLE_ARROW_DOWN_SPLIT);
		RibbonButton rb8 = new RibbonButton(sub, ImageCache.getImage("olb_small3.gif"), "I am arrowed", RibbonButton.STYLE_ARROW_DOWN);

		MenuItem test2 = new MenuItem(rb8.getMenu(), SWT.POP_UP);
		test2.setText("Testing an arrow down menu");

		rb8.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				final RibbonButton rb = (RibbonButton) e.data;
				rb.showMenu();					
			}
			
		});
		
		rb1.setButtonSelectGroup(group);
		rb2.setButtonSelectGroup(group);
		rb3.setButtonSelectGroup(group);
		rb4.setButtonSelectGroup(group);
		rb5.setButtonSelectGroup(group);
		rb6.setButtonSelectGroup(group);
					
	}
	public static void searchByMovie(Composite searches){
		Composite searchByMovie = new Composite(searches,SWT.BORDER);
		searchByMovie.setLayout(new FillLayout());
		ExpandBar bar = new ExpandBar (searchByMovie, SWT.V_SCROLL);
		Image image = display.getSystemImage(SWT.ICON_QUESTION);
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout (3,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label firstName = new Label (composite, SWT.NONE);
		firstName.setText("Movie Name");
		Text text = new Text(composite ,SWT.SEARCH);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Search by movie name");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		// Second item
		composite = new Composite (bar, SWT.NONE);
		layout = new GridLayout (3, false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);	
		Label lastName = new Label(composite, SWT.NONE);
		lastName.setText("Year");
		Text text1 = new Text(composite ,SWT.SEARCH);
		button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item1 = new ExpandItem (bar, SWT.NONE, 1);
		item1.setText("Search by year");
		item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item1.setControl(composite);
		item1.setImage(image);
		
		// Third item
		composite = new Composite (bar, SWT.NONE);
		layout = new GridLayout (3, false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label date = new Label(composite, SWT.NONE);
		date.setText("Director");
		Text text2 = new Text(composite ,SWT.SEARCH);
		button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item2 = new ExpandItem (bar, SWT.NONE, 2);
		item2.setText("Search by director name");
		item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item2.setControl(composite);
		item2.setImage(image);
		
		item0.setExpanded(true);
		item1.setExpanded(true);
		item2.setExpanded(true);
		bar.setSpacing(8);
		//searchByActor.setVisible(checked);
		searchByMovie.setSize(400, 350);
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
	public static void searchByActor(Composite searches){
		Composite searchByActor = new Composite(searches,SWT.BORDER);
		searchByActor.setLayout(new FillLayout());
		ExpandBar bar = new ExpandBar (searchByActor, SWT.V_SCROLL);
		Image image = display.getSystemImage(SWT.ICON_QUESTION);
		// First item
		Composite composite = new Composite (bar, SWT.NONE);
		GridLayout layout = new GridLayout (3,false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label firstName = new Label (composite, SWT.NONE);
		firstName.setText("First Name");
		Text text = new Text(composite ,SWT.SEARCH);
		Button button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		item0.setText("Search by first name");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		
		// Second item
		composite = new Composite (bar, SWT.NONE);
		layout = new GridLayout (3, false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);	
		Label lastName = new Label(composite, SWT.NONE);
		lastName.setText("Last Name");
		Text text1 = new Text(composite ,SWT.SEARCH);
		button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item1 = new ExpandItem (bar, SWT.NONE, 1);
		item1.setText("Search by last name");
		item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item1.setControl(composite);
		item1.setImage(image);
		
		// Third item
		composite = new Composite (bar, SWT.NONE);
		layout = new GridLayout (3, false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label date = new Label(composite, SWT.NONE);
		date.setText("Date of Birth");
		Text text2 = new Text(composite ,SWT.SEARCH);
		button = new Button (composite, SWT.PUSH);
		button.setText("Search");
		ExpandItem item2 = new ExpandItem (bar, SWT.NONE, 2);
		item2.setText("Search by date of birth");
		item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item2.setControl(composite);
		item2.setImage(image);
		
		item0.setExpanded(true);
		item1.setExpanded(true);
		item2.setExpanded(true);
		bar.setSpacing(8);
		//searchByActor.setVisible(checked);
		searchByActor.setSize(400, 350);

	}
}
