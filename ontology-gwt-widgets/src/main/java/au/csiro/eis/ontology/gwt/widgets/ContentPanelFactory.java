package au.csiro.eis.ontology.gwt.widgets;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public final class ContentPanelFactory {
	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source w contentMenu
	 * @wbp.factory.parameter.source w_1 contentDispPanel
	 */
	public static VerticalPanel createHomeContentPanel() {
		SimplePanel contentDispPanel = new SimplePanel();
		contentDispPanel.setSize("100%", "500px");
		
		SimplePanel contentMenu = new SimplePanel();
		contentMenu.setSize("100%", "50px");

		List<String> menuItems = new ArrayList<String>();
		menuItems.add("Home");
		menuItems.add("Button1");
		menuItems.add("Button2");
		Panel contentMenuBar = ContentPanelFactory.createContentPanelMenuButtons(menuItems);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSize("500px", "100%");
		verticalPanel.add(contentMenuBar);
		verticalPanel.add(contentDispPanel);
		
		return verticalPanel;
	}
	
	public static MenuBar createContentPanelMenuBar(List<String> menuItems) {
		MenuBar contentMenuBar = new MenuBar(false);
		Command sayHello = new Command() {
			   public void execute() {
			     Window.alert("Hello");
			   }
			 };
		
		for(String item : menuItems) {
			contentMenuBar.addItem(item, sayHello);
		}
		
		contentMenuBar.setSize("100%", "50px");
		
		
		 // Create a Horizontal Panel
	    HorizontalPanel hPanel = new HorizontalPanel();

	    // Leave some room between the widgets
	    hPanel.setSpacing(5); 

	    // Add some content to the panel
	    for (int i = 1; i < 5; i++) {
	      hPanel.add(new Button("Button " + i));
	    }
		
		return contentMenuBar;
	}
	
	public static HorizontalPanel createContentPanelMenuButtons(List<String> menuItems) {
		 // Create a Horizontal Panel
	    HorizontalPanel hPanel = new HorizontalPanel();

	    // Leave some room between the widgets
	    hPanel.setSpacing(5); 
	    
	    // Add some content to the panel
	    for(String item : menuItems) {
	    	Button currButton = new Button(item);
	    	currButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Button clicked! " );
				}
			});
	    	
	    	hPanel.add(currButton);
	    }
		
		return hPanel;
	}
	
	public static ContentPanel  createPlainContentPanel(String title) {
		ContentPanel cp = new ContentPanel();
		cp.setHeadingText(title);
		//cp.setAutoHeight(true);
		//cp.setScrollMode(Scroll.AUTOY);  

		//cp.setPosition(10, 10);
		cp.setCollapsible(true);
		//cp.setFrame(true);
		cp.setBodyStyle("backgroundColor: white;");
		
		return cp;
	}
	
	public static ContentPanel  createFitLayoutContentPanel(String title) {
		ContentPanel cp = new ContentPanel();
		cp.setHeadingText(title);
		//cp.setLayout(new FitLayout());
		//cp.setSize(400, 300);
		cp.setHeight("100%");
		//cp.setAutoHeight(true);
		//cp.setAutoWidth(true);
	    //cp.setScrollMode(Scroll.AUTOY);  

		cp.setPosition(10, 10);
		cp.setCollapsible(true);
		//cp.setFrame(true);
		cp.setBodyStyle("backgroundColor: white;");
		
		return cp;
	}
	
	public static ContentPanel  createRowLayoutContentPanel(String title) {
		ContentPanel cp = new ContentPanel();
		//cp.setHeading(title);
		//cp.setLayout(new VerticalLayoutContainer());
		//cp.setLayout(new VerticalLayoutContainer());  
		//cp.setSize(400, 300);
		cp.setHeight("100%");
		//cp.setAutoHeight(true);
		//cp.setAutoWidth(true);
	    //cp.setScrollMode(Scroll.AUTOY);  

		cp.setPosition(10, 10);
		cp.setCollapsible(true);
		//cp.setFrame(true);
		cp.setBodyStyle("backgroundColor: white;");
		
		return cp;
	}

	public static ContentPanel  createPlainUndecoratedContentPanel() {
		ContentPanel cp = new ContentPanel();
		cp.setHeaderVisible(false);
		//cp.setSize(400, 300);
		cp.setHeight("100%");
		//cp.setAutoHeight(true);
		//cp.setAutoWidth(true);
	    //cp.setScrollMode(Scroll.AUTOY);  

		cp.setPosition(10, 10);
		cp.setCollapsible(false);

		//cp.setFrame(false);
		cp.setBodyStyle("backgroundColor: white;");
		
		return cp;
	}

}