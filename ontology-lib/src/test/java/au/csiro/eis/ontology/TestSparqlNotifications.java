package au.csiro.eis.ontology;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;

import au.csiro.eis.ontology.resource.CepOntologyManager;
import au.csiro.eis.ontology.resource.jena.JenaModelManager;

public class TestSparqlNotifications {

	@Test
	public void test() {
		JenaModelManager jmm = new JenaModelManager();

		String describeQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
				"PREFIX ed: <http://waterinformatics1-cdc.it.csiro.au/resource/event-detection.owl#> \n" +
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> \n" +
				"PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> \n" +
				"DESCRIBE ?obs ?n \n" +
				"where { \n" +
				"   { \n" +
				"    SELECT ?obs \n" +
				"    where { \n" +
				"      ?obs a ed:DataStreamObservation . \n" +
				"      ?obs ssn:observationResultTime ?obsResultTime . \n" +  
				"      ?obs ed:timestamp ?timestamp . \n" + 
				"      ?obs ssn:observedBy ?sensor . \n" +
				"      ?obs ssn:observedProperty ?obsprop .        \n" +
				"      FILTER(?sensor = <http://waterinformatics1-cdc.it.csiro.au/linked-data/resource/sensor/1>) \n" +
				"      FILTER(?obsprop = <http://waterinformatics1-cdc.it.csiro.au/linked-data/resource/observedProperty/flow>) \n" +   
				"    } \n" +
				"    ORDER BY DESC(xsd:dateTime(?timestamp)) \n" +
				"    LIMIT 50 \n" +   
				"   } \n" +
				"   ?obs ssn:observationResultTime ?obsResultTime . \n" +  
				"   ?obs ssn:observationResult ?sensorOutput .  \n" +
				"   ?sensorOutput ssn:hasValue ?sensorValue .  \n" +
				"   ?sensorValue ed:hasQuantityValue ?obsval .  \n" +
				"   OPTIONAL { ?n a ed:ComplexEventNotification .  ?n ed:triggerObservation ?obs .  } \n" + 
				"} \n";
		
		System.out.println(describeQuery);
		
		String endpoint = "http://waterinformatics1-cdc.it.csiro.au/linked-data/sparql";

		QueryExecution qexec = 	QueryExecutionFactory.sparqlService(endpoint, describeQuery);
		Model resultModel = qexec.execDescribe() ;
		qexec.close() ;

		//place this in the jena model
		if(resultModel == null) {
			fail("could not exec query at endpoint " + endpoint);
		}
		
		resultModel.write(System.out);


	}
}
