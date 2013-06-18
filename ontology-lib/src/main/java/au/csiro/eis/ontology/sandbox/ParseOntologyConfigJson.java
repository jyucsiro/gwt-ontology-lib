package au.csiro.eis.ontology.sandbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.Gson;

import au.csiro.eis.ontology.beans.config.OntologyConfig;
import au.csiro.eis.ontology.beans.config.OntologyConfigMapping;
import au.csiro.eis.utils.FileIOUtils;


public class ParseOntologyConfigJson {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		String path = "src/main/webapp/ontology_config.json";
		File f = new File(path);
		String json = FileIOUtils.readFile(f);
		
		//Type collectionType = new TypeToken<Collection>(){}.getType();
		//Collection<OntologyConfig> ints2 = new Gson().fromJson(json, collectionType);

        OntologyConfig[] arrConfig = new Gson().fromJson(json, OntologyConfig[].class);
        
        for(OntologyConfig config : arrConfig) {        	
        	if(config.getType() != null && config.getType().equals("mapping")) {
        		if(config.getValue() != null) {
        			for(OntologyConfigMapping mapping : config.getValue()) {
        				if(mapping.getPath() != null) {
        					System.out.println(mapping.getPath());
        				}
        			}
        			
        		}
        	}
        	
        	System.out.println(config.toString());
        }
	}
	
	public static void loadOntologies() {
		
	}

}
