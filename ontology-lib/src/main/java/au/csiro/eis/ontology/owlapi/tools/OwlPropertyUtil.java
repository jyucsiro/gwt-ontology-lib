package au.csiro.eis.ontology.owlapi.tools;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.PropertyAssertionValueShortFormProvider;
import org.semanticweb.owlapi.util.QNameShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import au.csiro.eis.ontology.beans.OwlObjectPropertyBean;
import au.csiro.eis.ontology.beans.OwlRestrictionBean;


public class OwlPropertyUtil {
	SimpleShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	QNameShortFormProvider    shortFormQnameProvider = new QNameShortFormProvider ();
	
	public String renderProperty(OWLObjectProperty op) {
		
		return op.toString();
	}
	

	public OwlObjectPropertyBean renderPropertyAsBean(OWLObjectPropertyExpression op, OWLReasoner reasoner) {
		OwlObjectPropertyBean bean = null;


		bean = new OwlObjectPropertyBean();
				
		Set<OWLClass> classes = reasoner.getObjectPropertyRanges(op, true).getFlattened();
		
		StringBuffer sb = new StringBuffer();
		for(OWLClass c : classes) {
			sb.append(shortFormProvider.getShortForm(c) + " ");
		}

		bean.setFiller(sb.toString());
		bean.setProperty(shortFormProvider.getShortForm(op.asOWLObjectProperty()));
		bean.setType("OWLObjectProperty");

		return bean;
	}

	/*
	ModelData renderRestrictionAsModelData(OWLObjectProperty op, OWLReasoner reasoner) {
		ModelData md = null;

		OwlObjectPropertyBean bean = renderPropertyAsBean(op, reasoner);
		
		return (bean == null) ? null : bean.asModelData();		

	} */
}

