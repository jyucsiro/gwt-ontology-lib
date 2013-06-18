package au.csiro.eis.ontology;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.topbraid.spin.inference.SPINExplanations;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.progress.SimpleProgressMonitor;
import org.topbraid.spin.statistics.SPINStatistics;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.vocabulary.SPIN;

import au.csiro.eis.ontology.openrdf.sesame.tools.SesameHttpUtils;
import au.csiro.eis.ontology.spin.tools.SpinUtils;

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
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class TestSpinForChaffey {
	String domainURI = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/chaffey-domain";
	String rulesURI = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/chaffey-rules";
	String tripleStore = "http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/event-detection/statements";
	
	
	
	@Test
	public void test2() throws ClientProtocolException, IOException, URISyntaxException {
		Model inputModel = getOntologyDefAsModel();

		//OntModel spinModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		OntModel spinModel = getSpinRules();
		
		Model outputModel = SpinUtils.runSpin(inputModel, spinModel);
		//get inferred statements
				StmtIterator itr = outputModel.listStatements();
				System.out.println("Iterating over statements");
				while(itr.hasNext()){
					Statement s = itr.next();
					System.out.println(s.toString());
				}
	}

	
	public  OntModel getOntologyDefAsOntModel() throws ClientProtocolException, IOException, URISyntaxException {
		String base = domainURI;
		String context= "<" + base +">";

		//String result = doGetSesameStatementsHttpCall2("http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/uwda/statements", context, null);
		String result = SesameHttpUtils.getStatements(tripleStore, context, null);

		System.out.println("Creating ontology model ... " + base );

		OntModel model = ModelFactory.createOntologyModel();
		model.read(new StringReader(result), base);


		return model;
	}

	public  Model getOntologyDefAsModel() throws ClientProtocolException, IOException, URISyntaxException {
		String base = domainURI;
		String context= "<" + base +">";

		//String result = doGetSesameStatementsHttpCall2("http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/uwda/statements", context, null);
		String result = SesameHttpUtils.getStatements(tripleStore, context, null);

		System.out.println("Creating ontology model ... " + base );

		Model model = ModelFactory.createDefaultModel();
		model.read(new StringReader(result), base);


		return model;
	}

	
	public  OntModel getSpinRules() throws ClientProtocolException, IOException, URISyntaxException {
		String base = rulesURI;
		String context = "<" + base + ">";

		//String result = doGetSesameStatementsHttpCall2("http://waterinformatics1-cdc.it.csiro.au/openrdf-sesame/repositories/uwda/statements", context, null);
		String result = SesameHttpUtils.getStatements(tripleStore, context, null);
		
		
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


}
