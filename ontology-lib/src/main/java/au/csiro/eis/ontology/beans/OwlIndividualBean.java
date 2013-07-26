package au.csiro.eis.ontology.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;


public class OwlIndividualBean  extends RdfResourceBean implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OwlClassBean type;
	
	Map<OwlObjectPropertyBean,Set<OwlIndividualBean>> objectProperties;
	Map<OwlDataPropertyBean, Set<OwlLiteralBean>> dataProperties;
	
	Map<String, Set<AbstractRdfResourceBean>> propIdx; //index based on the string repr of the prop uri 

	
	public OwlIndividualBean() {
		super();
		propIdx = new HashMap<String,Set<AbstractRdfResourceBean>>();
	}
	
	public OwlIndividualBean(String name, String iri) {
		super();
		this.name = name;
		this.localName = name;
		this.iri = iri;
		type = null;
		objectProperties = null;
		dataProperties = null;
		propIdx = new HashMap<String,Set<AbstractRdfResourceBean>>();

	}
	
	public OwlClassBean getType() {
		return type;
	}

	public void setType(OwlClassBean type) {
		this.type = type;
	}

	
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<OwlObjectPropertyBean, Set<OwlIndividualBean>> getObjectProperties() {
		return objectProperties;
	}
	

	public void setObjectProperties(
			Map<OwlObjectPropertyBean, Set<OwlIndividualBean>> objectProperties) {
		this.objectProperties = objectProperties; 
		
		this.updateObjPropIndex();
	}


	private void updateObjPropIndex() {
		for(OwlObjectPropertyBean key : this.objectProperties.keySet()) {
			Set<OwlIndividualBean> objectPropSet = this.objectProperties.get(key);
			if(this.propIdx.get(key.getPropertyIri()) == null) {
				//convert all to right kind of set
				Set<AbstractRdfResourceBean> set = new HashSet<AbstractRdfResourceBean>();
				set.addAll(objectPropSet);
				this.propIdx.put(key.getPropertyIri(), set);
			}
			else { //something already exists
				Set<AbstractRdfResourceBean> set = this.propIdx.get(key.getPropertyIri());
				for(OwlIndividualBean indiv : objectPropSet) {
					
					if(!set.contains(indiv)) {
						set.add(indiv);
					}
				}
			}
		}
		
	}
	
	private void updateDataPropIndex() {
		
		for(OwlDataPropertyBean key : this.dataProperties.keySet()) {
			Set<OwlLiteralBean> propFillerSet = this.dataProperties.get(key);
			
			if(this.propIdx.get(key.getPropertyIri()) == null) {
				//convert all to right kind of set
				Set<AbstractRdfResourceBean> set = new HashSet<AbstractRdfResourceBean>();
				set.addAll(propFillerSet);
				this.propIdx.put(key.getPropertyIri(), set);
			}
			else {
				Set<AbstractRdfResourceBean> set = this.propIdx.get(key.getPropertyIri());

				for(OwlLiteralBean l : propFillerSet) {
					if(!set.contains(l)) {
						set.add(l);
					}
				}
			}
			
		}		
	}
	
	public Set<AbstractRdfResourceBean> findPropertyFiller(String propIri) {
		return this.propIdx.get(propIri);
	}

	public void setDataProperties(
			Map<OwlDataPropertyBean, Set<OwlLiteralBean>> dataPropertiesBeanMap) {

		this.dataProperties = dataPropertiesBeanMap;
		updateDataPropIndex();
	}

	
	
	public Map<OwlDataPropertyBean, Set<OwlLiteralBean>> getDataProperties() {
		return dataProperties;
	}

	/*
	public List<ModelData> getObjectPropertiesAsList() {
		List<ModelData> propertyExprList = new ArrayList<ModelData>();

		if(objectProperties != null) {


			for(OwlObjectPropertyBean currObjProp : objectProperties.keySet()) {
				Set<OwlIndividualBean> relatedIndivBeans =  objectProperties.get(currObjProp) ;
				
				for(OwlIndividualBean indivBean :relatedIndivBeans ) {
					ModelData newModelData = currObjProp.asModelData();
					newModelData.set("filler", indivBean.getName() );
					newModelData.set("fillerIri", indivBean.getIri());
					
					propertyExprList.add(newModelData );
				}
			}
			
		}
		
		
		return propertyExprList;
		
	}

	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.getName());  
		m.set("id", this.getIri());  

		if(objectProperties != null) {

			List<ModelData> propertyExprList = getObjectPropertiesAsList();
			
			m.set("objectProperties", propertyExprList);  

		}

		if(dataProperties != null) {

			List<ModelData> propertyExprList = getDataPropertiesAsList();
			
			m.set("dataProperties", propertyExprList);  

		}

		return m;
	}

	private List<ModelData> getDataPropertiesAsList() {
		// TODO Auto-generated method stub
		List<ModelData> propertyExprList = new ArrayList<ModelData>();

		if(dataProperties != null) {
			for(OwlDataPropertyBean currObjProp : dataProperties.keySet()) {
				Set<OwlLiteralBean> literalBeans =  dataProperties.get(currObjProp) ;
				
				for(OwlLiteralBean literalBean :literalBeans ) {
					ModelData newModelData = currObjProp.asModelData();
					newModelData.set("datatypeIri", literalBean.getDatatypeIri());
					newModelData.set("literal", literalBean.getLiteral());
					newModelData.set("lang", literalBean.getLang());
					
					
					propertyExprList.add(newModelData );
				}
			}
			
		}
		
		return propertyExprList;
	}
	*/
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Name: " + this.name);
		sb.append("\n");
		sb.append("URI: " + this.iri);
		sb.append("\n");
		if(this.type != null) {
			sb.append("Type: " + this.type.name);
			sb.append("\n");
		}
		
		sb.append("ObjectProperties: " );
		sb.append("\n");
		
		for(OwlObjectPropertyBean key : this.objectProperties.keySet()) {
			Set<OwlIndividualBean> objectPropSet = this.objectProperties.get(key);
			
			for(OwlIndividualBean indiv : objectPropSet) {
				sb.append("- " + key.property + ": " + indiv.localName);
				sb.append("\n");
			}
			
		}
		
		sb.append("Datatype Properties: " );
		sb.append("\n");
		for(OwlDataPropertyBean key : this.dataProperties.keySet()) {
			Set<OwlLiteralBean> propFillerSet = this.dataProperties.get(key);
			
			for(OwlLiteralBean l : propFillerSet) {
				sb.append("- " + key.property + ": " + l.getLiteral());
				sb.append("\n");
			}
			
		}
		
		return sb.toString();
		
	}
	
}
