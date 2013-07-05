package au.csiro.eis.ontology.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RdfResourceBean extends AbstractRdfResourceBean implements Serializable , IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String name;
	String localName;
	String label;
	String iri;
	
	String rdfsComment;

	public RdfResourceBean() {
		name = null;
		localName = null;
		label = null;
		iri = null;
		
		rdfsComment = null;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getRdfsComment() {
		return rdfsComment;
	}

	public void setRdfsComment(String rdfsComment) {
		this.rdfsComment = rdfsComment;
	}

	

}
