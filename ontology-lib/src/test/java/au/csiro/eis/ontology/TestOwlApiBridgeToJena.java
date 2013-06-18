package au.csiro.eis.ontology;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.topbraid.spin.vocabulary.SPIN;

import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.openrdf.sesame.tools.SesameHttpUtils;
import au.csiro.eis.ontology.owlapi.tools.OntologyLoader;
import au.csiro.eis.ontology.spin.tools.SpinModelManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestOwlApiBridgeToJena {
	OntologyLoader loader ;
	String tripleStore = "http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/event-detection/statements";
	String chaffey_base = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/chaffey-domain";
	String chaffey_rules_base = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/chaffey-rules";

	String chaffey_user = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/user/jonathanyu";
	String uwda_base = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/uwda-domain";
	String uwda_rules_base = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/uwda-rules";

	@Before
	public void setUp() {
	}

	@Test
	public void testOwlApiToJenaTransforming() throws ClientProtocolException, IOException, URISyntaxException, OWLOntologyCreationException, OntologyInitException, OWLOntologyStorageException {
		runTest(chaffey_user, chaffey_rules_base, tripleStore);
		
	}
		
	private void runTest(String base, String rules_base, String repo) throws ClientProtocolException, OWLOntologyCreationException, IOException, URISyntaxException, OWLOntologyStorageException {
		loader = new OntologyLoader();

		//Step 1: Load from OWL API
		System.out.println("Loading ontologies using OWL API");
		loadOntologyUsingOwlApi(base, rules_base, repo, loader);

		
		SpinModelManager spinMgr = new SpinModelManager(repo);		
		boolean initInputResult = spinMgr.initInputModel(loader.getRootOntology(), loader.getOntologyMgr(), base);
		boolean initSpinModelResult = spinMgr.initSpinModel(rules_base);
		
		System.out.println("Testing spin inferencing");
		Model outputModel = spinMgr.runInferences();
		OntModel spinModel = spinMgr.getSpinModel();

		System.out.println("Query spinModel #1:");
		queryForMatchingSensorFromEventRule(spinModel);

		System.out.println("Reset spin inferences");
		spinMgr.resetInferences();
		
		System.out.println("Re-running spin inferencing");
		spinMgr.runInferences();

		System.out.println("Query spinModel #2:");
		queryForMatchingSensorFromEventRule(spinModel);
		
		System.out.println("Done!");
	}
	
	
	private void loadOntologyUsingOwlApi(String base, String rules_base,String repo, OntologyLoader loader) throws ClientProtocolException, OWLOntologyCreationException, IOException, URISyntaxException {
		loader.initOntologyMgr();
		loader.overrideImport("http://purl.oclc.org/NET/ssnx/ssn", "http://waterinformatics1-cdc.it.csiro.au/resource/ssn.owl");
		loader.overrideImport("http://qudt.org/vocab/unit", "http://waterinformatics1-cdc.it.csiro.au/resource/unit.rdf");
		loader.overrideImport("http://qudt.org/1.1/vocab/unit", "http://waterinformatics1-cdc.it.csiro.au/resource/unit.rdf");
		loader.overrideImport("http://qudt.org/schema/qudt", "http://waterinformatics1-cdc.it.csiro.au/resource/qudt.ttl");
			
		loader.ignoreImport("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl");
		
		loadOntology(base, repo);

		loader.printImportedOntologies();
	}
	
	
	private void queryForMatchingSensorFromEventRule(Model model) {
		String queryString = "SELECT ?rule ?sensor WHERE { ?rule a <http://waterinformatics1-cdc.it.csiro.au/resource/event-detection.owl#ValueConstraintEventRule> . ?rule <http://waterinformatics1-cdc.it.csiro.au/resource/event-detection.owl#constraintSensorMatch> ?sensor}";
	
		  Query query = QueryFactory.create(queryString) ;
		  QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
		  try {
		    ResultSet results = qexec.execSelect() ;
		    ResultSetFormatter.out(System.out, results, query) ;
		    /*
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("varName") ;       // Get a result variable by name.
		      Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
		      Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
		    }*/
		  } finally { qexec.close() ; }
	}
	
	public  OntModel getOntologyDefAsModel(InputStream input, String base) throws ClientProtocolException, IOException, URISyntaxException {

		//Model model = ModelFactory.createDefaultModel();
		OntModel model = ModelFactory.createOntologyModel();
		model.read(input, base);


		return model;
	}

	
	public  OntModel getSpinRules(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
		String context = "<" + base + ">";

		//String result = doGetSesameStatementsHttpCall2("http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/uwda/statements", context, null);
		String result = SesameHttpUtils.getStatements(repo, context, null);
		
		
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF);

		model.read(new StringReader(result), base);
		boolean isSpinLoaded = model.hasLoadedImport(SPIN.BASE_URI);
		if(isSpinLoaded) {
			System.out.println("Spin is loaded");
		}
		else {
			//load spin
			//model.getDocumentManager()base;
			System.out.println("Loading imports for SPIN");

			model.loadImports();
		}
		return model;
	}
	
	
	public void loadOntology(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException, OWLOntologyCreationException {
		System.out.println("Querying domain triples <"+ base +"> from triple store...");
		String ontologyContent = getOntologyAsString(base, repo);

		System.out.println("Loading domain triples...");
		loader.loadOntologyFromInputString(ontologyContent, "rdfxml");
	}
	
	public void loadOntologyViaFile(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException, OWLOntologyCreationException {
		
		System.out.println("Querying domain triples <"+ base +"> from triple store...");
		File ontologyContent = getOntologyAsFile(base, repo);

		IRI docIri = IRI.create(base);
		
		System.out.println("Loading domain triples...");
		loader.loadOntologyFromFile(ontologyContent, docIri, "rdfxml");
	}
	
	public  String getOntologyAsString(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
		String context= "<" + base +">";

		String result = SesameHttpUtils.getStatements(repo, context, null);

		return result;
	}

	public  File getOntologyAsFile(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
		String context= "<" + base +">";

		File result = SesameHttpUtils.getStatementsAsFile(repo, context, null);

		return result;
	}
}
