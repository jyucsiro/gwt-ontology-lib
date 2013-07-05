package au.csiro.eis.ontology.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OwlAxiomBean extends AbstractRdfResourceBean implements Serializable, IsSerializable {
	
	String label;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OwlAxiomBean() {
		
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
