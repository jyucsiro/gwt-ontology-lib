package au.csiro.eis.ontology.beans;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.csiro.eis.ontology.constants.SwrlVocabularyMapping;

import com.google.gwt.user.client.rpc.IsSerializable;



public class RuleAtomSwrlBuiltInBean extends RuleAtomBean 
				implements Comparable<RuleAtomSwrlBuiltInBean>, Serializable, IsSerializable {
	/**
	 * propA( ?x, ?y )
	 */
	private static final long serialVersionUID = 1L;
	
	SwrlVocabularyMapping swrlBuiltIn;
	RuleVarBean var1;
	RuleVarBean var2;
	
	
	public RuleAtomSwrlBuiltInBean() {
		this.swrlBuiltIn = null;
		this.var1 = null;
		this.var2 = null;
	}

	public RuleAtomSwrlBuiltInBean(SwrlVocabularyMapping swrlBuiltIn, RuleVarBean var1, RuleVarBean var2) {
	//public RuleAtomSwrlBuiltInBean(String swrlBuiltIn, RuleVarBean var1, RuleVarBean var2) {
		this.swrlBuiltIn = swrlBuiltIn;
		this.var1 = var1;
		this.var2 = var2;
		super.setRenderedLabel(this.toString());

	}

	

	public RuleVarBean getVar1() {
		return var1;
	}

	public void setVar1(RuleVarBean var1) {
		this.var1 = var1;
	}

	public RuleVarBean getVar2() {
		return var2;
	}

	public void setVar2(RuleVarBean var2) {
		this.var2 = var2;
	}

	public void updateRenderedLabel() {
		super.setRenderedLabel(this.toString());
	}


	public RuleTextVarBean getRuleVarBean(String text) {
		RuleTextVarBean textBean = new RuleTextVarBean();
		textBean.setValue(text);
		
		return textBean;		
	}
	


	public String toString() {
		return this.swrlBuiltIn.getSwrlPrefixedName() + "(" + this.var1.toString() + "," + this.var2.toString() + ")"; 
	}

/*
	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.swrlBuiltIn.getSwrlPrefixedName());
		m.set("var1", this.getVar1());
		m.set("var2", this.getVar2());
		
		return m;
	}
*/
	
	public int compareTo(RuleAtomSwrlBuiltInBean o) {
		return this.swrlBuiltIn.getSwrlPrefixedName().compareTo(o.swrlBuiltIn.getSwrlPrefixedName());
	}

	public SwrlVocabularyMapping getSwrlBuiltIn() {
		return swrlBuiltIn;
	}

	public void setSwrlBuiltIn(SwrlVocabularyMapping swrlBuiltIn) {
		this.swrlBuiltIn = swrlBuiltIn;
	}

	
	
}
