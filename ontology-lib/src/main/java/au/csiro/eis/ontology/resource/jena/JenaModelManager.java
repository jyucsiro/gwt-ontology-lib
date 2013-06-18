package au.csiro.eis.ontology.resource.jena;

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
import au.csiro.eis.ontology.spin.tools.SpinUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class JenaModelManager {
	OntModel inputModel;
	Model outputModel;
	
	String baseUriString;
	
	
	public JenaModelManager() {
		inputModel = null;
		this.baseUriString = null;
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

	public OntModel getInputModel() {
		// TODO Auto-generated method stub
		return this.inputModel;
	}
}
