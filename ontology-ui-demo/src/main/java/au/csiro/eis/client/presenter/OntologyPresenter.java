package au.csiro.eis.client.presenter;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlOntologyBean;


public interface OntologyPresenter extends Presenter<OwlOntologyBean>{
	
	void setOntologyBean(OwlOntologyBean ontology);
	OwlOntologyBean getOntologyBean();
	void loadOntologyBean();
	
}
