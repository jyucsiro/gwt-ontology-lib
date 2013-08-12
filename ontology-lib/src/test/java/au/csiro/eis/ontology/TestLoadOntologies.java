package au.csiro.eis.ontology;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import au.csiro.eis.ontology.beans.config.OntologyConfig;
import au.csiro.eis.ontology.beans.config.OntologyConfigMapping;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.resource.CepOntologyManager;
import au.csiro.eis.ontology.resource.owlapi.OntologyBeanFactory;
import au.csiro.eis.ontology.resource.owlapi.OwlApiOntologyMediator;
import au.csiro.eis.utils.FileIOUtils;

import com.google.gson.Gson;

public class TestLoadOntologies {

	boolean isConsoleOutputSilent = false;
	
	String defaultPath  = "src/test/resources";
	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testLoadChaffeyOntologies() throws OntologyInitException {
		//String ontConfigPath = defaultPath + "/chaffey_domain_ontology_config.json";
		String ontConfigPath = defaultPath + "/uwda_domain_ontology_config.json";

		File f = new File("");
		System.out.println("Base dir: " + f.getAbsolutePath());
		f = new File(ontConfigPath);
		System.out.println("Base dir: " + f.getAbsolutePath());
		System.out.println("exists: " + f.exists());
		
		OwlApiOntologyMediator ontMediator = new OwlApiOntologyMediator();
		
		CepOntologyManager ontologyManager = new CepOntologyManager(getOntologyConfigs(ontConfigPath),isConsoleOutputSilent);
		
		
		OntologyBeanFactory ontBeanFactory;
		ontBeanFactory = new OntologyBeanFactory(ontologyManager);
		
		ontMediator.init(ontologyManager, ontBeanFactory);

		boolean isInitialised = ontologyManager.initialise();		

		if(!isInitialised) {
			fail("Failed to initialise ontology manager");
		}
		
	}
	
	/*
	@Test
	public void testLoadUwdaOntologies() throws OntologyInitException {
		String ontConfigPath = defaultPath + "/sewer_rising_mains_ontology_config.json";

		OwlApiOntologyMediator ontMediator = new OwlApiOntologyMediator();
		
		CepOntologyManager ontologyManager = new CepOntologyManager(getOntologyConfigs(ontConfigPath),isConsoleOutputSilent);
		
		
		OntologyBeanFactory ontBeanFactory;
		ontBeanFactory = new OntologyBeanFactory(ontologyManager);
		
		ontMediator.init(ontologyManager, ontBeanFactory);

		boolean isInitialised = ontologyManager.initialise();		

		if(!isInitialised) {
			fail("Failed to initialise ontology manager");
		}
		
	}
	*/
	public OntologyConfig[] getOntologyConfigs(String ontologyConfigJsonPath) throws OntologyInitException {
		String ontPath = ontologyConfigJsonPath;
		
		File f = new File(ontPath);
		String json;
		try {
			//json = FileUtils.readFile(f);
			json = FileIOUtils.readFile(f);
			//Type collectionType = new TypeToken<Collection>(){}.getType();
			//Collection<OntologyConfig> ints2 = new Gson().fromJson(json, collectionType);

	        OntologyConfig[] arrConfig = new Gson().fromJson(json, OntologyConfig[].class);
	        System.out.println("Performing path resolution");
	        for(OntologyConfig config : arrConfig) {        	
	        	if(config.getType() != null && (config.getType().equals("mapping") || config.getType().equals("default")) ) {
	        		if(config.getValue() != null) {
	        			for(OntologyConfigMapping mapping : config.getValue()) {
	        				if(mapping.getPath() != null) {
	        					System.out.println(mapping.getPath());
	        					//resolve paths
	        					String resolvedPath = getRealPath(mapping.getPath());
	        					System.out.println("Resolves to: " + resolvedPath);

	        					mapping.setPath(resolvedPath);
	        				}
	        			}
	        			
	        		}
	        	}
				
	        }
	        
	        System.out.println("After Resolved paths");
	        for(OntologyConfig config : arrConfig) {        	
	        	System.out.println(config.toString());
	        }
	        return arrConfig;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OntologyInitException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OntologyInitException();
		}
		

	}
	
	
	private String getRealPath(String path) {
		//add the prefix
		String returned = defaultPath + "/" + path;
		
		return returned ;
	}


}
