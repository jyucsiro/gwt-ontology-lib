package au.csiro.eis.ontology.constants;

import java.io.Serializable;

public class SwrlVocabularyMapping implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String symbol;
	//SWRLBuiltInsVocabulary vocabTerm;
	String swrlIri;
	String swrlPrefixedName;
	
	public SwrlVocabularyMapping(String name, String symbol, String swrlPname) {
		this.name = name;
		this.symbol = symbol;
		
		//this.swrlIri = swrlIri;
		this.swrlIri = null;
		this.swrlPrefixedName = swrlPname;
	}
	
	public SwrlVocabularyMapping() {
		this.name = null;
		this.symbol = null;
		
		//this.swrlIri = swrlIri;
		this.swrlIri = null;
		this.swrlPrefixedName = null;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getSwrlIri() {
		return swrlIri;
	}


	public void setSwrlIri(String swrlIri) {
		this.swrlIri = swrlIri;
	}


	public String getSwrlPrefixedName() {
		return swrlPrefixedName;
	}


	public void setSwrlPrefixedName(String swrlPrefixedName) {
		this.swrlPrefixedName = swrlPrefixedName;
	}
	
	
	
	
}
