package au.csiro.eis.ontology.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SparqlSelectResultBean implements Serializable , IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<String> entryList ;
	
	public SparqlSelectResultBean() {
		entryList = new ArrayList<String>();
	}

	public List<String> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<String> entryList) {
		this.entryList = entryList;
	}


	
	
	
}
