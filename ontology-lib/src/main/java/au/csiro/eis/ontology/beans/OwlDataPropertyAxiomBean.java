package au.csiro.eis.ontology.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OwlDataPropertyAxiomBean extends OwlAxiomBean implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String propertyIri;
	OwlIndividualBean domain;
	OwlLiteralBean range;
	public final String type = "DataProperty";

	public OwlDataPropertyAxiomBean() {
		
	}


	public OwlDataPropertyAxiomBean(String propertyIri,
			OwlIndividualBean domain, OwlLiteralBean range) {
		super();
		this.propertyIri = propertyIri;
		this.domain = domain;
		this.range = range;
		this.createLabel();
	}
	
	private void createLabel() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(type+"(");
		if(this.propertyIri != null) {
			sb.append(this.propertyIri);
		}
		sb.append(", ");
		if(this.domain != null) {
			sb.append(this.domain.getName());
		}
		
		sb.append(", ");
		if(this.range != null) {
			sb.append(this.range.getLiteral());
		}
		sb.append(")");
		
		
		this.setLabel(sb.toString());
		
	}

}