package au.csiro.eis.ontology.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OwlObjectPropertyAxiomBean extends OwlAxiomBean implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String propertyIri;
	OwlIndividualBean domain;
	OwlIndividualBean range;
	public final String type = "ObjectProperty";

	public OwlObjectPropertyAxiomBean() {
		
	}


	public OwlObjectPropertyAxiomBean(String propertyIri,
			OwlIndividualBean domain, OwlIndividualBean range) {
		super();
		this.propertyIri = propertyIri;
		this.domain = domain;
		this.range = range;
		
		this.createLabel();
	}


	private void createLabel() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(type+"(");
		if(this.domain != null) {
			sb.append(this.domain.getName());
		}
		else {
			sb.append("*");
		}
		
		sb.append(", ");
		
		if(this.propertyIri != null) {
			sb.append(this.propertyIri);
		}
		else {
			sb.append("*");
		}
		
		
		sb.append(", ");

		if(this.range != null) {
			sb.append(this.range.getName());
		}
		else {
			sb.append("*");
		}
		sb.append(")");
		
		
		this.setLabel(sb.toString());
		
	}


	public String getPropertyIri() {
		return propertyIri;
	}


	public void setPropertyIri(String propertyIri) {
		this.propertyIri = propertyIri;
	}


	public OwlIndividualBean getDomain() {
		return domain;
	}


	public void setDomain(OwlIndividualBean domain) {
		this.domain = domain;
	}


	public OwlIndividualBean getRange() {
		return range;
	}


	public void setRange(OwlIndividualBean range) {
		this.range = range;
	}


	public String getType() {
		return type;
	}
	
	
}
