package au.csiro.eis.ontology;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.openrdf.sesame.tools.SesameHttpUtils;
import au.csiro.eis.ontology.owlapi.tools.OntologyLoader;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestOwlApiLoadFromTripleStore {
	OntologyLoader loader ;
	String repo = "http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/event-detection/statements";
	String chaffey_base = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/chaffey-domain";
	String uwda_base = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/uwda-domain";

	@Before
	public void setUp() {
		
		

	}

	@Test
	public void test() throws ClientProtocolException, IOException, URISyntaxException, OWLOntologyCreationException, OntologyInitException {

		System.out.println("Initialising OWL API ontologyLoader...");
		loader = new OntologyLoader();
		loader.initOntologyMgr();
		loader.overrideImport("http://purl.oclc.org/NET/ssnx/ssn", "http://waterinformatics1-cdc.it.csiro.au/resource/ssn.owl");
		loader.overrideImport("http://qudt.org/vocab/unit", "http://waterinformatics1-cdc.it.csiro.au/resource/unit.ttl");
		loader.ignoreImport("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl");
		
		
		loadOntology(uwda_base, repo);		

		System.out.println("Printing class list");
		int count = 0;
		for(String className : loader.getClasses()) {
			System.out.println("Class: " + className);
			count ++;

		}
		System.out.println("Num classes: " + count);

		loader.unloadOntologies();

		System.out.println("Done!");

	}
	
	
	
	@Test
	public void testUsingWithPelletReasoner() throws ClientProtocolException, IOException, URISyntaxException, OWLOntologyCreationException, OntologyInitException {
	
		loader = new OntologyLoader();
		loader.initOntologyMgr();
		loader.overrideImport("http://purl.oclc.org/NET/ssnx/ssn", "http://waterinformatics1-cdc.it.csiro.au/resource/ssn.owl");
		loader.overrideImport("http://qudt.org/vocab/unit", "http://waterinformatics1-cdc.it.csiro.au/resource/unit.rdf");
		loader.overrideImport("http://qudt.org/1.1/vocab/unit", "http://waterinformatics1-cdc.it.csiro.au/resource/unit.rdf");
		loader.overrideImport("http://qudt.org/schema/qudt", "http://waterinformatics1-cdc.it.csiro.au/resource/qudt.ttl");
			
		loader.ignoreImport("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl");
		
		loadOntology(chaffey_base, repo);

		loader.printImportedOntologies();
		
		processInferences();
		
		
		System.out.println("Done!");
	}
	
	@Test
	public void testingFileMethod() throws ClientProtocolException, IOException, URISyntaxException, OWLOntologyCreationException, OntologyInitException {
		

		System.out.println("Initialising OWL API ontologyLoader...");
		loader = new OntologyLoader();
		loader.initOntologyMgr();
		loader.overrideImport("http://purl.oclc.org/NET/ssnx/ssn", "http://waterinformatics1-cdc.it.csiro.au/resource/ssn.owl");
		loader.overrideImport("http://qudt.org/vocab/unit", "http://waterinformatics1-cdc.it.csiro.au/resource/unit.ttl");
		loader.ignoreImport("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl");
		
		
		loadOntologyViaFile(chaffey_base, repo);		

		System.out.println("Printing class list");
		int count = 0;
		for(String className : loader.getClasses()) {
			System.out.println("Class: " + className);
			count ++;

		}
		System.out.println("Num classes: " + count);

		loader.unloadOntologies();

		System.out.println("Done!");

	}
	
	public void processInferences() throws OWLOntologyCreationException, OntologyInitException {
		OWLOntology infOnt = this.loader.getInferredOntology();
		
		if(infOnt != null) {
			System.out.println("Printing class list");
			int count = 0;
			for(String className : loader.getClasses(infOnt)) {
				System.out.println("Class: " + className);
				count ++;
	
			}
			
			System.out.println("Num classes: " + count);
	
			loader.unloadOntologies();
		}
	
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
