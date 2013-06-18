package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class SwrlBuiltInBean implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String iri;
	
	
	public SwrlBuiltInBean() {
		
	}

	public SwrlBuiltInBean(String name, String iri) {
		this.setName(name); 
		this.setIri(iri)  ;
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

	/*
	public BeanModel getBeanModel(){
		BeanModelFactory factory = BeanModelLookup.get().getFactory(SwrlBuiltInBean.class);

		BeanModel convertedBean = factory.createModel(this);
		return convertedBean;
		
	}


	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.getName());  
		m.set("id", this.getIri());  

		
		return m;
	}
*/
	
}
