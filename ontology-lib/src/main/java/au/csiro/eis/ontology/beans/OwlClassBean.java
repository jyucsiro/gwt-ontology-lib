package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class OwlClassBean extends RdfResourceBean implements Serializable , IsSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String longLabel;
	List<OwlClassBean> parents;
	List<OwlClassBean> children;
	List<OwlIndividualBean> individuals;
	
	Map<String, String> dataTypeProperties;
	List<OwlObjectPropertyBean> objectTypeRelations;
	List<OwlRestrictionBean> restrictions;
	List<OwlRestrictionBean> relatedRestrictions;
	

	boolean hasIndividuals;
	boolean loadIndividuals = false;
	
	public OwlClassBean() {
	}

	public OwlClassBean(String name, String iri) {
		this.setName(name);
		this.setLocalName(name);
		this.setIri(iri)  ;
		loadIndividuals = false;
	}




	public List<OwlClassBean> getParents() {
		return parents;
	}

	public void setParents(List<OwlClassBean> parents) {
		this.parents = parents;
	}

	public List<OwlClassBean> getChildren() {
		return children;
	}
	
	



	public void setChildren(List<OwlClassBean> children) {
		this.children = children;
	}

	public Map<String, String> getDataTypeProperties() {
		return dataTypeProperties;
	}

	public void setDataTypeProperties(Map<String, String> dataTypeProperties) {
		this.dataTypeProperties = dataTypeProperties;
	}

	public List<OwlObjectPropertyBean> getObjectTypeRelations() {
		return objectTypeRelations;
	}

	public void setObjectTypeRelations(List<OwlObjectPropertyBean> objectTypeRelations) {
		this.objectTypeRelations = objectTypeRelations;
	}
	
	
	public String getParentsAsString() {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		
		for(OwlClassBean currParent : this.parents) {
			i++;
			String label;
			String name = currParent.getName();
			if(name == null || name.length() == 0) {
				label = currParent.getIri();
			}
			else {
				label = name;
			}
			
			sb.append(label);
			
			if(++i <= this.parents.size()) {
				sb.append(",");
			}
		}
		
		return sb.toString();
	}
	
	public String getChildrenAsString() {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(OwlClassBean currChild : this.children) {
			String label;
			String name = currChild.getName();
			if(name == null || name.length() == 0) {
				label = currChild.getIri();
				
			}
			else {
				label = name;
			}
			
			sb.append(label);
			if(++i <= this.children.size()) {
				sb.append(",");
			}
		}
		
		return sb.toString();
	}
	
	public List<OwlIndividualBean> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<OwlIndividualBean> individuals) {
		this.individuals = individuals;
	}
	
	public boolean hasChildren() {
		boolean hasChildren = false;
		if(this.getChildren() != null && this.getChildren().size() > 0) {
			hasChildren = true;
		}
		
		return hasChildren;
	}
	
	public boolean hasParents() {
		boolean hasParents = false;
		if(this.getParents() != null && this.getParents().size() > 0) {
			hasParents = true;
		}
		
		return hasParents;
	}

	/*
	public boolean hasIndividuals() {
		boolean hasIndividuals = false;
		if(this.getIndividuals() != null && this.getIndividuals().size() > 0) {
			hasIndividuals = true;
		}
		return hasIndividuals;
	}
	*/
	
	public boolean hasRestrictions() {
		boolean hasRestrictions = false;
		if(this.getRestrictions() != null && this.getRestrictions().size() > 0) {
			hasRestrictions = true;
		}
		return hasRestrictions;
	}
	
	public boolean hasRelatedRestrictions() {
		boolean hasRelatedRestrictions = false;
		if(this.getRelatedRestrictions() != null && this.getRelatedRestrictions().size() > 0) {
			hasRelatedRestrictions = true;
		}
		return hasRelatedRestrictions;
	}
	
	public boolean hasObjectProperties() {
		boolean hasObjectProperties = false;
		if(this.getObjectTypeRelations() != null && this.getObjectTypeRelations().size() > 0) {
			hasObjectProperties = true;
		}
		return hasObjectProperties;
	}
	
	public  List<OwlRestrictionBean> getRestrictions() {
		return this.restrictions;
	}
	
	public  List<OwlRestrictionBean> getRelatedRestrictions() {
		return this.relatedRestrictions;
	}

/*
	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.getName());
		
		m.set("id", this.getIri());  

		m.set("comment", this.getRdfsComment());  
		
		
		m.set("hasChildren", this.hasChildren() );
		m.set("hasParents", hasParents() );
		

		
		m.set("hasIndividuals", hasIndividuals());
		

		m.set("isLoadIndividuals", this.isLoadIndividuals());
		
		if(hasChildren()) {
			m.set("children",this.getChildrenModelData());
		}
		if(hasIndividuals() && this.loadIndividuals) {
			m.set("individuals", this.getIndividualsModelData());
		}
		
		m.set("hasRestrictions", hasRestrictions());

		if(hasRestrictions()) {
			List<ModelData> restrictionModelData = new ArrayList<ModelData>();
			
			for(OwlRestrictionBean bean : this.restrictions) {
				ModelData md = bean.asModelData();
				restrictionModelData.add(md);
			}
			
			m.set("restrictions",restrictionModelData );
		}
		
		
		m.set("hasRelatedRestrictions", hasRelatedRestrictions());

		if(hasRelatedRestrictions()) {
			List<ModelData> relRestrictionModelData = new ArrayList<ModelData>();
			
			for(OwlRestrictionBean bean : this.restrictions) {
				ModelData md = bean.asModelData();
				relRestrictionModelData.add(md);
			}
			
			m.set("relatedRestrictions",relRestrictionModelData );
		}

		
		m.set("hasObjectProperties", hasObjectProperties());

		if(hasObjectProperties()) {
			List<ModelData> objectPropModelData = new ArrayList<ModelData>();
			
			for(OwlObjectPropertyBean bean : this.getObjectTypeRelations()) {
				ModelData md = bean.asModelData();
				objectPropModelData.add(md);
			}
			
			m.set("objectProperties",objectPropModelData );
		}
		
		
		 
		if(hasParents) {
			m.set("parents",this.getParentsModelData());
		}
		if(hasIndividuals) {
			m.set("individuals", this.getIndividualsModelData());
		}


		
		

		return m;
	}

*/

	public void setRestrictions(List<OwlRestrictionBean> restrictions) {

		this.restrictions = restrictions;
	}
	

	public void setRelatedRestrictions(List<OwlRestrictionBean> relatedRestrictions) {

		this.relatedRestrictions = relatedRestrictions;
	}

	public void setObjectProperties(
			List<OwlObjectPropertyBean> walkToGetObjectProperties) {
		// TODO Auto-generated method stub
		this.setObjectTypeRelations(walkToGetObjectProperties);
		
	}

	public void setLongLabel(String ll) {
		this.longLabel = ll;
	}
	
	public void initLongLabel() {
		if(this.localName != null && this.label != null) {
			this.longLabel = this.localName + " - " + this.label; 
		}
		else {
			this.longLabel = "";
		}
	}
	
	public String getLongLabel() {
		return longLabel;
		
	}

	
	public boolean hasIndividuals() {
		return hasIndividuals;
	}

	public void setHasIndividuals(boolean hasIndividuals) {
		this.hasIndividuals = hasIndividuals;
	}

	public boolean isLoadIndividuals() {
		return loadIndividuals;
	}

	public void setLoadIndividuals(boolean loadIndividuals) {
		this.loadIndividuals = loadIndividuals;
	}
	

	
	
}
