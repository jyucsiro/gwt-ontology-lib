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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ontologyQuery")
public interface OntologyQueryService extends RemoteService {

	boolean initialiseSvc(ConfigBean config) throws OntologyInitException;
	boolean reloadOntologies() throws OntologyInitException;

	List<String> getClasses() throws OntologyInitException;

	List<String> getSubClasses(String iri_string) throws OntologyInitException;

	List<OwlClassBean> getOntSubClasses(String iri_string) throws OntologyInitException;

	List<OwlClassBean> getOntSubClasses(String iri_string, boolean isDirect,	boolean loadIndiv, boolean loadParents, 
			boolean loadChildren) throws OntologyInitException;


	OwlOntologyBean getOntologyBean() throws OntologyInitException;

	//List<ModelData> getOntSubClassesModelData(String iri_string) throws OntologyInitException;
	//List<ModelData> getOntSubClassesModelData(String iri_string, boolean isDirect) throws OntologyInitException;

	List<OwlIndividualBean> getOntIndividuals(List<String> indivIriList, boolean skipPropMappings, boolean resolveParents)
			throws OntologyInitException;
	
	List<OwlIndividualBean> getOntIndividualsForClass(String class_iri_string)
			throws OntologyInitException;
	List<OwlIndividualBean> getOntIndividualsForClass(String class_iri_string, boolean isDirect)
			throws OntologyInitException;

	
	boolean saveModel();


	
	//List<ModelData> getOntClassesModelData(List<String> iriList)		throws OntologyInitException;

	List<OwlClassBean> getOntClasses(List<String> iriList)
			throws OntologyInitException;

	List<SwrlBuiltInBean> getSwrlBuiltInFunctions() throws OntologyInitException;
	//List<ModelData> getSwrlBuiltInFunctionsModelData();

	OwlOntologyBean getOntologyBean(String ontology_uri)
			throws OntologyInitException;

	boolean isSubclassOf(String iri, String parentIri, boolean isDirect) throws OntologyInitException;
	OwlClassBean getOntClass(String iri_string) throws OntologyInitException;
	OwlClassBean getOntClass(String iri_string, boolean loadIndividuals, boolean loadParents, boolean loadChildren) throws OntologyInitException;


	List<OwlClassBean> getOntClasses(List<String> iriList, boolean loadIndividuals, boolean loadParents, boolean loadChildren)

			throws OntologyInitException;

	
	boolean switchDomain(ConfigBean config) throws OntologyInitException;

	SparqlSelectResultSetBean performSparqlQueryOnSpinModel(String sparql);
	SparqlSelectResultSetBean performSparqlSelectQuery(String sparql);
	
	OwlIndividualBean getOntIndividual(String iri_string, boolean skipPropMappings, boolean resolveParents) throws OntologyInitException;
	
	RdfResourceBean getRdfResource(String iri_string) throws OntologyInitException;
	
	int addOwlIndividual(OwlIndividualBean vsensorIndiv) throws OntologyInitException;
	
	int deleteOwlIndividual(OwlIndividualBean vsensorIndiv) throws OntologyInitException;


}