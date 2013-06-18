package au.csiro.eis.ontology.owlapi.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import au.csiro.eis.ontology.exception.OntologyInitException;


public class OntologyLoader {
	OWLOntologyManager ontologyMgr ;
	Set<OWLOntology> ontSet;
	List<IRI> ignoredImports;
	OWLOntology rootOntology;
	int numOntologies = 0;
    
	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws OntologyInitException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, OntologyInitException, IOException {
		// TODO Auto-generated method stub
	    final String PIZZA_ONTOLOGY = "war/pizza.owl"; 
	    final String SSN_XG_ONTOLOGY = "war/models/ssn.owl"; 

		OntologyLoader loader = new OntologyLoader();
		loader.initOntologyMgr();
		//loader.loadOntology(PIZZA_ONTOLOGY);
		loader.loadOntologyFromFile(SSN_XG_ONTOLOGY);
		
		for(String className : loader.getClasses()) {
			System.out.println("Class: " + className);
		}
		
		loader.unloadOntologies();
	}

	public OntologyLoader() {
		ontSet = Collections.synchronizedSet(new HashSet<OWLOntology>());
		ignoredImports = new ArrayList<IRI>();
		rootOntology = null;
	}
	
	public void loadOntologyFromFile(String ontologyFile) throws OWLOntologyCreationException, IOException {
		loadOntologyFromFile(new File(ontologyFile), null, null);
	}
	
	public void loadOntologyFromFile(String ontologyFile, IRI docIri) throws OWLOntologyCreationException, IOException {
		loadOntologyFromFile(new File(ontologyFile), docIri, null);
	}
	
	public void loadOntologyFromFile(File ontologyFile, IRI docIri, String format) throws OWLOntologyCreationException, IOException {
		
		if(docIri == null) {
			OWLOntology ont  = ontologyMgr.loadOntologyFromOntologyDocument(ontologyFile);
		
			ontSet.add(ont);
		
			if(this.numOntologies == 0) {
				this.rootOntology = ont;
			}
		
			this.numOntologies++;
		}
		
		else {
			FileInputStream fis = new FileInputStream (ontologyFile);
			this.loadOntologyFromInputStream(fis, docIri, format);
		}
	}

	public void loadOntologyFromInputString(String ontologyContents, String lang) throws OWLOntologyCreationException {
		// TODO Auto-generated method stub
		//String ontologyFile = PIZZA_ONTOLOGY;
		
		
		OWLOntologyDocumentSource documentSource = new StringDocumentSource(ontologyContents);
		
		 OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
		 config = config.setLoadAnnotationAxioms(false);
		 
		 for(IRI iri : this.ignoredImports) {
			 config.addIgnoredImport(iri);
		 }
		
		OWLOntology ont  = ontologyMgr.loadOntologyFromOntologyDocument(documentSource, config);
		
		ontSet.add(ont);
		
		if(this.numOntologies == 0) {
			this.rootOntology = ont;
		}
		
		this.numOntologies++;
		
	}
	
	public void loadOntologyFromInputStream(InputStream ontologyContents, IRI documentIri, String lang) throws OWLOntologyCreationException, IOException {
		// TODO Auto-generated method stub
		//String ontologyFile = PIZZA_ONTOLOGY;
	
		OWLOntologyDocumentSource documentSource = new StreamDocumentSource(ontologyContents, documentIri);
		
		 OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
		 config = config.setLoadAnnotationAxioms(false);
		 
		 for(IRI iri : this.ignoredImports) {
			 config.addIgnoredImport(iri);
		 }
		
		OWLOntology ont  = ontologyMgr.loadOntologyFromOntologyDocument(documentSource, config);
		
		ontSet.add(ont);
		
		if(this.numOntologies == 0) {
			this.rootOntology = ont;
		}
		
		this.numOntologies++;
		ontologyContents.close();
	}
	
	public void printImportedOntologies() {
		
		
		if(this.rootOntology != null) {
			for(OWLOntology ont : this.rootOntology.getImports()) {
				System.out.println("Imported: " + ont.getOntologyID().toString());
			}
		}
	}
	
	public void initOntologyMgr() {
		this.ontologyMgr = OWLManager.createOWLOntologyManager();
		
	}
	
	public void unloadOntologies() {
		
		for(OWLOntology ont : ontSet) {
			ontologyMgr.removeOntology(ont);
		}
	}
	
	public String[] getClasses() throws OntologyInitException {
		ArrayList<String> classList = new ArrayList<String>();
		
		OWLOntologyWalker walker = new OWLOntologyWalker(ontSet);
		ClassVisitor visitor = new ClassVisitor(walker);
		walker.walkStructure(visitor);

		for(OWLClass c : visitor.getClassList()) {
			classList.add(c.toStringID());
		}

		return (String[]) classList.toArray(new String[0]);
	}
	
	public String[] getClasses(OWLOntology ont) throws OntologyInitException {
		ArrayList<String> classList = new ArrayList<String>();
		
		Set<OWLOntology> ontSet = new HashSet<OWLOntology>();
		ontSet.add(ont);
		
		OWLOntologyWalker walker = new OWLOntologyWalker(ontSet);
		ClassVisitor visitor = new ClassVisitor(walker);
		walker.walkStructure(visitor);

		for(OWLClass c : visitor.getClassList()) {
			classList.add(c.toStringID());
		}

		return (String[]) classList.toArray(new String[0]);
	}

	public void ignoreImport(String uriString) {
		IRI iri = IRI.create(uriString);
		this.ignoredImports.add(iri);
		
	}

	public void overrideImport(String origUri, String overridedUri) {
		IRI iri = IRI.create(origUri);
		IRI iri_mapped = IRI.create(overridedUri);
		
		this.ontologyMgr.addIRIMapper(new SimpleIRIMapper(iri, iri_mapped));

		
	}

	public OWLReasoner getReasoner(OWLOntology ont) {
		if(ont != null) {
			OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
			return reasonerFactory.createReasoner(ont);			
		}	
		 
		return null;
		
	}
	
	

	public OWLOntologyManager getOntologyMgr() {
		return ontologyMgr;
	}

	public void setOntologyMgr(OWLOntologyManager ontologyMgr) {
		this.ontologyMgr = ontologyMgr;
	}

	public Set<OWLOntology> getOntSet() {
		return ontSet;
	}

	public void setOntSet(Set<OWLOntology> ontSet) {
		this.ontSet = ontSet;
	}

	public OWLOntology getRootOntology() {
		return rootOntology;
	}

	public void setRootOntology(OWLOntology rootOntology) {
		this.rootOntology = rootOntology;
	}
	
	public OWLOntology getInferredOntology() throws OWLOntologyCreationException {
		OWLReasoner reasoner = this.getReasoner(getRootOntology());
		
		boolean isConsistent = reasoner.isConsistent();
		
		System.out.println("isConsistent? " + isConsistent);
		if(isConsistent) {		
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			 List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
			 gens.add(new InferredSubClassAxiomGenerator()); 
			 
			 OWLOntology infOnt = this.ontologyMgr.createOntology();
			// Now get the inferred ontology generator to generate some inferred
			// axioms for us (into our fresh ontology). We specify the reasoner that
			// we want to use and the inferred axiom generators that we want to use.
			InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
			iog.fillOntology(this.ontologyMgr, infOnt); 
			return infOnt;

		}
		
		return null;		
	}
	
	
}
