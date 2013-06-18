package au.csiro.eis.client.event.ontology;

import au.csiro.eis.ontology.beans.OwlOntologyBean;


public class OntologyServiceGetOntologyEvent extends OntologyServiceEvent {
	OwlOntologyBean ontology;
	
	public OntologyServiceGetOntologyEvent(OwlOntologyBean ontology) {

		this.ontology = ontology;
	}

	public OwlOntologyBean getOntology() {
		return ontology;
	}

	public void setOntology(OwlOntologyBean ontology) {
		this.ontology = ontology;
	}

	
	

}
