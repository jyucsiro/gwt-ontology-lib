package au.csiro.eis.ontology.gwt.rpc.client.ontologyService;

import java.util.List;


import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.beans.OwlObjectPropertyBean;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.beans.RdfResourceBean;
import au.csiro.eis.ontology.beans.SparqlSelectResultSetBean;
import au.csiro.eis.ontology.beans.SwrlBuiltInBean;
import au.csiro.eis.ontology.beans.WqCepRule;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.shared.ConfigBean;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OntologyQueryServiceAsync {

	void initialiseSvc(ConfigBean config, AsyncCallback<Boolean> callback);
	void reloadOntologies(AsyncCallback<Boolean> callback);
	void getClasses(AsyncCallback<List<String>> callback);
	void getSubClasses(String iri_string, AsyncCallback<List<String>> asyncCallback);

	void getOntSubClasses(String iri_string, boolean isDirect, boolean loadIndiv, boolean loadParents, 
			boolean loadChildren, AsyncCallback<List<OwlClassBean>> callback);
	void getOntSubClasses(String iri_string, AsyncCallback<List<OwlClassBean>> callback);

	
	void getOntIndividuals(List<String> indivIriList, boolean skipPropMappings, boolean resolveParents, AsyncCallback<List<OwlIndividualBean>> callback);
	
	void getOntIndividualsForClass(String class_iri_string, boolean isDirect, AsyncCallback<List<OwlIndividualBean>> callback);
	void getOntIndividualsForClass(String class_iri_string, AsyncCallback<List<OwlIndividualBean>> callback);


	void  getOntologyBean(AsyncCallback<OwlOntologyBean> callback) throws OntologyInitException;
	//void getOntSubClassesModelData(String iri_string, AsyncCallback<List<OwlClassBean>> callback);
	//void getOntSubClassesModelData(String iri_string, boolean isDirect, AsyncCallback<List<OwlClassBean>> callback);
	
	

	void saveModel(AsyncCallback<Boolean> callback);
	
	//void getOntClassesModelData(List<String> iriList, AsyncCallback<List<OwlClassBean>> callback)		throws OntologyInitException;

	void getOntClasses(List<String> iriList, AsyncCallback<List<OwlClassBean>> callback)
			throws OntologyInitException;
	void getOntClasses(List<String> iriList, boolean loadIndividuals, boolean loadParents, boolean loadChildren, AsyncCallback<List<OwlClassBean>> callback)
			throws OntologyInitException;

	void getOntClass(String iri_string, AsyncCallback<OwlClassBean> callback  );
	void getOntClass(String iri_string, boolean loadIndividuals, boolean loadParents, boolean loadChildren, AsyncCallback<OwlClassBean> callback  );


	void getSwrlBuiltInFunctions(AsyncCallback<List<SwrlBuiltInBean>> callback  );

	//void getSwrlBuiltInFunctionsModelData(AsyncCallback<List<SwrlBuiltInBean>> callback  );
	void getOntologyBean(String ontology_uri, AsyncCallback<OwlOntologyBean> callback)
			throws OntologyInitException;

	void isSubclassOf(String iri, String parentIri, boolean isDirect, AsyncCallback<Boolean> callback);

	
	void switchDomain(ConfigBean config, AsyncCallback<Boolean> callback);

	void performSparqlQueryOnSpinModel(String sparql, AsyncCallback<SparqlSelectResultSetBean> callback);
	void performSparqlSelectQuery(String sparql, AsyncCallback<SparqlSelectResultSetBean> callback);
	
	
	void getOntIndividual(String iri_string, boolean skipPropMappings, boolean resolveParents, AsyncCallback<OwlIndividualBean> callback) ;
	
	void getRdfResource(String iri_string, AsyncCallback<RdfResourceBean> callback);
}
