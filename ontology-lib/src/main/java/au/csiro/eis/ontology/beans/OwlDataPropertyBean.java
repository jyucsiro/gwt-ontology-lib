package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class OwlDataPropertyBean implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String property;
	String propertyIri;
	String filler;
	String fillerIri;
	String type;

	String source;
	String sourceIri;
	
	String minCard;
	String maxCard;

	
	
	public String getFillerIri() {
		return fillerIri;
	}



	public void setFillerIri(String fillerIri) {
		this.fillerIri = fillerIri;
	}



	public String getPropertyIri() {
		return propertyIri;
	}



	public void setPropertyIri(String propertyIri) {
		this.propertyIri = propertyIri;
	}



	public String getSourceIri() {
		return sourceIri;
	}



	public void setSourceIri(String sourceIri) {
		this.sourceIri = sourceIri;
	}



	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}



	public OwlDataPropertyBean() {
		
	}

	
	
	public String getProperty() {
		return property;
	}



	public void setProperty(String property) {
		this.property = property;
	}



	public String getFiller() {
		return filler;
	}



	public void setFiller(String filler) {
		this.filler = filler;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getMinCard() {
		return minCard;
	}



	public void setMinCard(String minCard) {
		this.minCard = minCard;
	}



	public String getMaxCard() {
		return maxCard;
	}



	public void setMaxCard(String maxCard) {
		this.maxCard = maxCard;
	}


	/*

	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("prop", this.getProperty());
		m.set("propIri", this.getPropertyIri());
		m.set("restrictionType", this.getType());
		
		m.set("filler", this.getFiller());
		m.set("fillerIri", this.getFillerIri());
		m.set("minCard", this.getMinCard());
		m.set("maxCard", this.getMaxCard());
		

		return m;
	}
	
	*/
}
