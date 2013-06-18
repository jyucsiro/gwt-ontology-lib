package au.csiro.eis.ontology.gwt.widgets;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;

public class ListViewFactory {

	public static <M> ListView<M,String> createListView(ListStore<M> listStore, ValueProvider<M,String> prov) {

	    ListView<M,String> view = new ListView<M,String>(listStore,prov);  
	    
	    return view;
	}
	
	public static <M> ListView<M,M> createListView(ListStore<M> store) {
		ListView<M, M> view = new ListView<M, M>(store, new IdentityValueProvider<M>() {
			 
		      @Override
		      public void setValue(M object, M value) {
		 
		      }
		    });
		
		
		
		return view;

	}
	
	public static <M> ListView<M,String> createListView(ListStore<M> listStore, String[] propList, ValueProvider<M,String> prov) {

	    ListView<M,String> view = new ListView<M,String>(listStore, prov);  
	    view.setBorders(true);  
	    view.setStore(listStore);  

	    String templateStr= "";
	    int counter = 0;
	    for(String p:propList) {
	    	if(counter > 0) {
	    		templateStr = templateStr + ",";	
	    	}
	    	templateStr = templateStr + "{" + p +  "}";
	    	counter++;
	    }

	    System.out.println("prop template ("+ counter + "): " + templateStr);
	    
	    return view;
	}
}
