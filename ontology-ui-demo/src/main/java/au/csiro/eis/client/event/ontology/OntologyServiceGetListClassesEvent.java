package au.csiro.eis.client.event.ontology;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;


public class OntologyServiceGetListClassesEvent extends OntologyServiceEvent {
	List<OwlClassBean> classes;
	
	public OntologyServiceGetListClassesEvent(List<OwlClassBean> classes) {

		this.classes = classes;
	}

	public List<OwlClassBean> getClasses() {
		return classes;
	}

	public void setClasses(List<OwlClassBean> classes) {
		this.classes = classes;
	}
	
	
	

}
