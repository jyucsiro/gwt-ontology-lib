package au.csiro.eis.client.view;

import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

public interface OntologyView extends View<OwlOntologyBean> {
	public void setOntologyService(OntologyQueryServiceAsync ontologyService);
	
	
	
}
