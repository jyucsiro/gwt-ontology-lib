package au.csiro.eis.ontology.owlapi.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

/**
 * Visits existential restrictions and collects the properties which are restricted
 */
public class OwlObjectPropertyVisitor extends OWLClassExpressionVisitorAdapter {

    private boolean processInherited = true;

    private Set<OWLClass> processedClasses;

    private Set<OWLObjectPropertyExpression> properties;
    
    private OWLReasoner reasoner;

    private Set<OWLOntology> onts;
    
    

    public Set<OWLObjectPropertyExpression> getProperties() {
		return properties;
	}


	public void setProperties(Set<OWLObjectPropertyExpression> properties) {
		this.properties = properties;
	}


	public OwlObjectPropertyVisitor(Set<OWLOntology> onts, OWLReasoner reasoner) {
        processedClasses = new HashSet<OWLClass>();
        this.onts = onts;
        this.reasoner = reasoner;

    }


    public void setProcessInherited(boolean processInherited) {
        this.processInherited = processInherited;
    }


    public void visit(OWLClass desc) {
        if (processInherited && !processedClasses.contains(desc)) {
            // If we are processing inherited restrictions then
            // we recursively visit named supers.  Note that we
            // need to keep track of the classes that we have processed
            // so that we don't get caught out by cycles in the taxonomy
            processedClasses.add(desc);
            for (OWLOntology ont : onts) {
                for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(desc)) {
                    ax.getSuperClass().accept(this);
                }
            }
        }
    }


    public void reset() {
        processedClasses.clear();
    }

    public void visit(OWLObjectPropertyExpression prop) {
    	System.out.println("objprop: " + prop);
    	this.properties.add(prop);
    }
    
    public void visit(OWLObjectProperty prop) {
    	System.out.println("objprop: " + prop);
    	this.properties.add(prop);
    }

}


