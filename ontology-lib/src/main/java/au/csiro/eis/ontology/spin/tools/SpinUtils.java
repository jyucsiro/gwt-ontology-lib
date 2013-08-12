package au.csiro.eis.ontology.spin.tools;

import java.util.ArrayList;
import java.util.List;

import org.topbraid.spin.inference.SPINExplanations;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.progress.SimpleProgressMonitor;
import org.topbraid.spin.statistics.SPINStatistics;
import org.topbraid.spin.system.SPINModuleRegistry;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class SpinUtils {
	public static boolean isInit = false;

	public static Model runSpin(Model inputModel, OntModel spinModel) {
		if(inputModel == null || spinModel == null) {
			return null;
		}
		
		if(isInit == false) {
			SPINModuleRegistry.get().init();

			//register locally defined functions
			SPINModuleRegistry.get().registerAll(spinModel, null);
			isInit = true;

		}

		spinModel.addSubModel(inputModel);
		//create output model to contain inferred statements

		Model outputModel = ModelFactory.createDefaultModel();

		//add output model to SPIN model
		spinModel.addSubModel(outputModel);

		SPINExplanations explanations = null;
		//SPINExplanations explanations = new SPINExplanations();
		List<SPINStatistics> statistics = new ArrayList<SPINStatistics>();
		boolean singlePass = true;

		SimpleProgressMonitor monitor = new SimpleProgressMonitor("TestSpin");
		//SimpleProgressMonitor monitor = null;
		
		
		//run SPIN inferences
		SPINInferences.run(spinModel, outputModel, explanations, statistics, false, monitor);
		//get number of inferred statements
		//System.out.println("Inferred triples: " + outputModel.size());

		return outputModel;
		
	}
	
}
