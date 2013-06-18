package au.csiro.eis.ontology.spin.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.topbraid.spin.vocabulary.SPIN;

import au.csiro.eis.ontology.openrdf.sesame.tools.SesameHttpUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class SpinModelManager {
	OntModel spinModel;
	OntModel inputModel;
	Model outputModel;
	
	String baseUriString;
	String rulesUriString;
	String tripleStore;
	
	boolean inferencingPerformed = false;
	
	
	
	public SpinModelManager(String tripleStore) {
		spinModel = null;
		inputModel = null;
		this.baseUriString = null;
		this.tripleStore = tripleStore;
		this.rulesUriString = null;
		
	}
	
	public boolean initInputModel(OWLOntology ontology, OWLOntologyManager ontMgr, String base) {

		this.baseUriString = base;
				
		boolean result = false;
		//Step 1: Convert to Jena model
		System.out.println("Converting owlapi ontology to Jena OntModel");
		try {
			this.inputModel = getJenaModelFromOwlApiOntology(this.baseUriString, ontology, ontMgr);
			
			if(this.inputModel != null) {

				System.out.println("Loading imports");
				this.inputModel.loadImports();
				
				result = true;
			}
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}

		return result;
	}
	
	public boolean initSpinModel(String rulesUri) throws OWLOntologyStorageException, IOException, URISyntaxException {
		this.rulesUriString = rulesUri;

		if(this.tripleStore == null || this.inputModel == null || this.rulesUriString == null) {
			return false;
		}
		//Get SPIN inference model
		System.out.println("Running spin inferencing");
		this.spinModel = this.createSpinOntModel(inputModel, this.rulesUriString, this.tripleStore);


		if(this.spinModel == null) {
			return false;
		}
		
		return true;
	}
	
	public void resetSpinModel() {
		this.spinModel.removeAll();
		this.spinModel = null;
		this.inputModel = null;
	}
	
	public Model runInferences() {
		if(inputModel == null || spinModel == null)
			return null;
		
		this.outputModel = SpinUtils.runSpin(inputModel, spinModel);
		return this.outputModel;
	}
	
	public void resetInferences() {
		spinModel.removeAll();
		this.outputModel = null;
	}
	
	private OntModel createSpinOntModel(Model model, String rules_base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
		//Model inputModel = getOntologyDefAsModel();
		Model inputModel  = model;
		//OntModel spinModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		OntModel spinModel = getSpinRules(rules_base, repo);

		if(spinModel == null) {
			return null;
		}
		
		Model outputModel = SpinUtils.runSpin(inputModel, spinModel);
		
		if(outputModel == null) {
			return null;
		}
		
		//get inferred statements
		StmtIterator itr = outputModel.listStatements();
		System.out.println("Iterating over statements");
		while(itr.hasNext()){
			Statement s = itr.next();
			System.out.println(s.toString());
		}
		return spinModel;

	}
	
	private  OntModel getSpinRules(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
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
	
	
	
	private OntModel convertOwlApiOntologyToJenaOntModel(String base, OWLOntology ontology, OWLOntologyManager ontMgr) throws OWLOntologyStorageException, IOException, URISyntaxException {
		System.out.println("Converting owlapi ontology to Jena OntModel");
		OntModel inputModel = getJenaModelFromOwlApiOntology(base, ontology, ontMgr);

		System.out.println("Loading imports");
		inputModel.loadImports();
		
		return inputModel;
	}
	
	private OntModel getJenaModelFromOwlApiOntology(String base, OWLOntology owlOntology, OWLOntologyManager ontMgr) throws OWLOntologyStorageException, IOException, URISyntaxException {
	
		File outFile = File.createTempFile("testOwlApi", ".owl");
		FileOutputStream fos = new FileOutputStream(outFile);
		ontMgr.saveOntology(owlOntology, fos);
		
		//read from file
		FileInputStream fis = new FileInputStream(outFile);
		OntModel model = getOntologyDefAsModel(fis, base);
		
		fis.close();
		outFile.delete();
		
		return model;
	}
	
	private  OntModel getOntologyDefAsModel(InputStream input, String base) throws ClientProtocolException, IOException, URISyntaxException {

		//Model model = ModelFactory.createDefaultModel();
		OntModel model = ModelFactory.createOntologyModel();
		model.read(input, base);


		return model;
	}
	

	private  String getOntologyAsString(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
		String context= "<" + base +">";

		String result = SesameHttpUtils.getStatements(repo, context, null);

		return result;
	}

	private  File getOntologyAsFile(String base, String repo) throws ClientProtocolException, IOException, URISyntaxException {
		String context= "<" + base +">";

		File result = SesameHttpUtils.getStatementsAsFile(repo, context, null);

		return result;
	}

	public OntModel getSpinModel() {
		return spinModel;
	}


	public OntModel getInputModel() {
		return inputModel;
	}

	public String getBaseUriString() {
		return baseUriString;
	}

	public String getRulesUriString() {
		return rulesUriString;
	}

	public String getTripleStore() {
		return tripleStore;
	}

	public void setTripleStore(String tripleStore) {
		this.tripleStore = tripleStore;
	}

	public Model getOutputModel() {
		return outputModel;
	}

	public boolean hasInferencingPerformed() {
		return inferencingPerformed;
	}

	public void setInferencingPerformed(boolean inferencingPerformed) {
		this.inferencingPerformed = inferencingPerformed;
	}
	
	
}
