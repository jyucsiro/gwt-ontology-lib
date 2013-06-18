package au.csiro.eis.ontology.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SparqlSelectResultSetBean implements Serializable , IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<SparqlSelectResultBean> resultSet ; 
	List<String> varNames;
	
	public SparqlSelectResultSetBean() {
		resultSet = new ArrayList<SparqlSelectResultBean>();
	}

	
	public List<SparqlSelectResultBean> getResultSet() {
		return resultSet;
	}


	public void setResultSet(List<SparqlSelectResultBean> resultSet) {
		this.resultSet = resultSet;
	}


	
	public void addEntry(SparqlSelectResultBean entry) {
		this.resultSet.add(entry);
	}


	public List<String> getVarNames() {
		return varNames;
	}


	public void setVarNames(List<String> varNames) {
		this.varNames = varNames;
	}
	
	
	
}
