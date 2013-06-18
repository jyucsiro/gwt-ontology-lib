package au.csiro.eis.ontology.owlapi.tools;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnnotationValueVisitor;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OwlClassUtil {

	/**
	 * Gets the class' rdfs:label if any
	 * @param c
	 * @param ont
	 * @return
	 */
	public static String getClassAnnotationLabel(OWLClass c, OWLAnnotationProperty label, Set<OWLOntology> onts) {
		String returnedStr = null;
		
		if(c == null) {
			return null;
		}
		
		
        for(OWLOntology ont : onts) {
            for (OWLAnnotation annotation : c.getAnnotations(ont, label)) {
            	if (annotation.getValue() instanceof OWLLiteral) {
            		OWLLiteral val = (OWLLiteral) annotation.getValue();
            		
            		returnedStr = val.getLiteral();
            		
            		//System.out.println("Literal is: " + returnedStr);
            	}
            }
        }
		
		/*
		for(OWLOntology currOnt : onts) {
			Set<OWLAnnotation> setAnnotations = c.getAnnotations(currOnt);
			for(OWLAnnotation an : setAnnotations) {
				if(an.getProperty().isLabel()) {
					OWLAnnotationValue anVal = an.getValue();
					if(anVal == null) {
						continue;
					}
					OwlLiteralVisitor literalVisitor = new OwlLiteralVisitor();
					
					anVal.accept(literalVisitor);
					returnedStr = literalVisitor.getValue();
					
					if(literalVisitor.getValue() == null) {
						returnedStr = anVal.toString();
					}
				}
			}
		}
		 */
		return returnedStr;
	}

	
	
	/**
	 * Gets the class' rdfs:comment if any
	 * @param c
	 * @param ont
	 * @return
	 */
	public static String getClassCommentLabel(OWLClass c, OWLOntology ont) {
		String returnedStr = null;
		
		if(c == null) {
			return null;
		}
		
		Set<OWLAnnotation> setAnnotations = c.getAnnotations(ont);
		for(OWLAnnotation an : setAnnotations) {
			if(an.getProperty().isComment()) {
				OWLAnnotationValue anVal = an.getValue();
				if(anVal == null) {
					continue;
				}
				OwlLiteralVisitor literalVisitor = new OwlLiteralVisitor();
				
				anVal.accept(literalVisitor);
				returnedStr = literalVisitor.getValue();
				
				if(literalVisitor.getValue() == null) {
					returnedStr = anVal.toString();
				}
		}
		}
		
		return returnedStr;
	}
	
}
