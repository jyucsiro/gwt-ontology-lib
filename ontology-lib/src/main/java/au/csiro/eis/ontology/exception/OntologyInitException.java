package au.csiro.eis.ontology.exception;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OntologyInitException extends OntologyException implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String ontologyId;
	String message;
	
	public OntologyInitException() {
		
	}

	
	public OntologyInitException(String ontologyId) {
		this.ontologyId = ontologyId;
	}
	
	public OntologyInitException(String ontologyId, String message) {
		this.ontologyId = ontologyId;
		this.message = message;
		
	}

	public String getOntologyId() {
		return this.ontologyId;
	}


	public String getMessage() {
		return this.ontologyId + " : " + message;
	}


	
	
}
