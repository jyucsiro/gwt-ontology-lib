package au.csiro.eis.ontology.beans.config;

import java.util.List;
import java.util.Map;

public class OntologyConfig {
	String name;
	String type;
	List<OntologyConfigMapping> value;
	String iri;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	public List<OntologyConfigMapping> getValue() {
		return value;
	}
	public void setValue(List<OntologyConfigMapping> value) {
		this.value = value;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		sb.append("," );
		sb.append(this.getType());
		sb.append("," );
		
		for(OntologyConfigMapping kvp: this.getValue()) {
			sb.append("[");
			
			if(kvp != null) {
			
					sb.append("{");
					sb.append(kvp.toString());
					sb.append("}");
				
			}
			sb.append("]");
			
		}
		return sb.toString();
	}
	public String getIri() {
		return iri;
	}
	public void setIri(String iri) {
		this.iri = iri;
	}
	
}
