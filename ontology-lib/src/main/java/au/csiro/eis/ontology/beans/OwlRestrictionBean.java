package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class OwlRestrictionBean implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String property;
	String propertyIri;
	String filler;
	String fillerIri;
	String type;

	String minCard;
	String maxCard;

	
	
	public OwlRestrictionBean() {
		
	}

	
	public String getPropertyIri() {
		return propertyIri;
	}


	public void setPropertyIri(String propertyIri) {
		this.propertyIri = propertyIri;
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



	
	
	public String getFillerIri() {
		return fillerIri;
	}



	public void setFillerIri(String filler_iri) {
		this.fillerIri = filler_iri;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();

		if(type.equals("ObjectAllValuesFrom")) {
			sb.append("'" + property + "' only '" + filler  + "'");
		}
		else if(type.equals("ObjectSomeValuesFrom")) {
			sb.append("'" + property + "' some '" + filler  + "'");
		}
		else if(type.equals("DataAllValuesFrom")) {
			sb.append("'" + property + "' only of datatype '" + filler  + "'");


		}else if(type.equals("DataAllValuesFrom")) {
			sb.append("'" + property + "' some of datatype '" + filler  + "'");

		}
		else {
			sb.append(type + "(" + property + ", " + filler + ")");
		}

		return sb.toString();
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
