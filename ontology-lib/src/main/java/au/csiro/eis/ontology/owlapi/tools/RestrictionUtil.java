package au.csiro.eis.ontology.owlapi.tools;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.PropertyAssertionValueShortFormProvider;
import org.semanticweb.owlapi.util.QNameShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import au.csiro.eis.ontology.beans.OwlRestrictionBean;
import au.csiro.eis.ontology.constants.RestrictionConstants;

public class RestrictionUtil {
	SimpleShortFormProvider shortFormProvider = new SimpleShortFormProvider();
	QNameShortFormProvider    shortFormQnameProvider = new QNameShortFormProvider ();

	public String renderRestriction(OWLRestriction r) {
		StringBuffer sb = new StringBuffer();
		if(r.isObjectRestriction()) {
			if( r instanceof OWLObjectAllValuesFrom) {
				OWLObjectAllValuesFrom oavf = (OWLObjectAllValuesFrom) r;
				return oavf.toString();
			}
			else if( r instanceof OWLObjectSomeValuesFrom) {
				OWLObjectSomeValuesFrom osvf = (OWLObjectSomeValuesFrom) r;
				return osvf.toString();
			}
			else if( r instanceof OWLObjectSomeValuesFrom) {
				OWLObjectSomeValuesFrom osvf = (OWLObjectSomeValuesFrom) r;
				return osvf.toString();
			}
		}
		else if(r.isDataRestriction()) {
			if( r instanceof OWLDataAllValuesFrom) {
				OWLDataAllValuesFrom avf = (OWLDataAllValuesFrom) r;
				return avf.toString();
			}
			else if( r instanceof OWLDataSomeValuesFrom) {
				OWLDataSomeValuesFrom svf = (OWLDataSomeValuesFrom) r;
				return svf.toString();
			}
		}
		
		return sb.toString();
	}
	

	public OwlRestrictionBean renderRestrictionAsBean(OWLRestriction r, Set<OWLOntology> o, OWLAnnotationProperty rdfslabel) {
		OwlRestrictionBean bean = null;

		if(r.isObjectRestriction()) {
			if( r instanceof OWLObjectAllValuesFrom) {
				OWLObjectAllValuesFrom oavf = (OWLObjectAllValuesFrom) r;
				OWLClassExpression filler = oavf.getFiller();
				OWLObjectPropertyExpression prop =  oavf.getProperty();
				
				//get the label if possible
				String fillerStr;

				if(filler.isAnonymous() == false) { 
					OWLClass fillerClass = filler.asOWLClass();
					fillerStr = class2string(fillerClass, rdfslabel,o);
					
					String fillerIri = filler.asOWLClass().getIRI().toString();
					String propStr = shortFormProvider.getShortForm(prop.asOWLObjectProperty());
					String propIri =prop.asOWLObjectProperty().getIRI().toString();
					String type = RestrictionConstants.OBJ_PROP_ALL_VALUES_FROM;
				
					bean = this.getRestrictionBean(fillerStr, fillerIri, propStr, propIri, type);
				}
			}
		
			else if( r instanceof OWLObjectSomeValuesFrom) {
				OWLObjectSomeValuesFrom osvf = (OWLObjectSomeValuesFrom) r;
				OWLClassExpression filler = osvf.getFiller();
				OWLObjectPropertyExpression prop =  osvf.getProperty();

				//String fillerStr =shortFormProvider.getShortForm(filler.asOWLClass());
				
				//get the label if possible
				String fillerStr;
				
				if(filler.isAnonymous() == false) { 
				
					OWLClass fillerClass = filler.asOWLClass();
					fillerStr = class2string(fillerClass,rdfslabel,o);
					
					String fillerIri = filler.asOWLClass().getIRI().toString();
					String propStr = shortFormProvider.getShortForm(prop.asOWLObjectProperty());
					String propIri =prop.asOWLObjectProperty().getIRI().toString();
					String type = RestrictionConstants.OBJ_PROP_SOME_VALUES_FROM;
					
					bean = this.getRestrictionBean(fillerStr, fillerIri, propStr, propIri, type);
				}
			}
		}
		
		else if(r.isDataRestriction()) {
			if( r instanceof OWLDataAllValuesFrom) {
				OWLDataAllValuesFrom avf = (OWLDataAllValuesFrom) r;
				OWLDataRange range = avf.getFiller();
				OWLDataPropertyExpression prop =  avf.getProperty();
				
				String rangeStr =shortFormProvider.getShortForm(range.asOWLDatatype());
				String rangeIri = range.asOWLDatatype().getIRI().toString();
				String propStr = shortFormProvider.getShortForm(prop.asOWLDataProperty());
				String propIri =prop.asOWLDataProperty().getIRI().toString();
				String type = RestrictionConstants.DATA_PROP_ALL_VALUES_FROM;
				
				bean = this.getRestrictionBean(rangeStr, rangeIri, propStr, propIri, type);				
			}
		
			else if( r instanceof OWLDataSomeValuesFrom) {
				OWLDataSomeValuesFrom svf = (OWLDataSomeValuesFrom) r;
				OWLDataRange range = svf.getFiller();
				OWLDataPropertyExpression prop =  svf.getProperty();
				
				String rangeStr =shortFormProvider.getShortForm(range.asOWLDatatype());
				String rangeIri = range.asOWLDatatype().getIRI().toString();
				String propStr = shortFormProvider.getShortForm(prop.asOWLDataProperty());
				String propIri =prop.asOWLDataProperty().getIRI().toString();
				String type = RestrictionConstants.DATA_PROP_ALL_VALUES_FROM;
				
				bean = this.getRestrictionBean(rangeStr, rangeIri, propStr, propIri, type);			
			}
		}
		
		return bean;
	}
	
	public String class2string(OWLClass c, OWLAnnotationProperty l, Set<OWLOntology> o) {
		String fillerStr = null;
		if(c!= null) {
			String str =  OwlClassUtil.getClassAnnotationLabel(c, l, o);
			
			if(str != null) {
				fillerStr = str;
			}
			else {
				fillerStr = shortFormProvider.getShortForm(c);		
			}
		}
		
		return fillerStr;		
	}
	
	public OwlRestrictionBean getRestrictionBean(String filler, String fillerIri, String prop, String propIri, String type) {
		OwlRestrictionBean bean = new OwlRestrictionBean();
		
		bean.setFillerIri(fillerIri);
		
		bean.setFiller(filler);
		bean.setProperty(prop);
		bean.setPropertyIri(propIri);
		bean.setType(type);
		
		return bean;
		
	}

	/*
	ModelData renderRestrictionAsModelData(OWLRestriction r, Set<OWLOntology> o, OWLAnnotationProperty annProp) {
		ModelData md = null;

		OwlRestrictionBean bean = renderRestrictionAsBean(r, o, annProp);
		
		return (bean == null) ? null : bean.asModelData();		

	}
	*/
}

