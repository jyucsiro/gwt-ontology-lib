package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class OwlOntologyBean implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String iri;
	Map<String,OwlClassBean> classIndex;
	List<OwlClassBean> rootClasses;
	Map<String,String> prefixes;
	//Map<String,OwlIndividualBean> individuals;
	
	public OwlOntologyBean() {
		
	}

	public OwlOntologyBean(String name, String iri) {
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
	
	
	
	
	public Map<String, OwlClassBean> getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(Map<String, OwlClassBean> classIndex) {
		this.classIndex = classIndex;
	}

	public List<OwlClassBean> getRootClasses() {
		return rootClasses;
	}

	public void setRootClasses(List<OwlClassBean> rootClasses) {
		this.rootClasses = rootClasses;
	}

	public Map<String, String> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(Map<String, String> prefixes) {
		this.prefixes = prefixes;
	}
	
	
	

	/*
	public BeanModel getBeanModel(){
		BeanModelFactory factory = BeanModelLookup.get().getFactory(OwlOntologyBean.class);

		BeanModel convertedBean = factory.createModel(this);
		return convertedBean;
		
	}
	*/

	/*
	public Map<String, OwlIndividualBean> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(Map<String, OwlIndividualBean> individuals) {
		this.individuals = individuals;
	}
	*/
	
	
	
}
