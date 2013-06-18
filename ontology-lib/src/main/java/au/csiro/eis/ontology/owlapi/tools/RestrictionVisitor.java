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
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
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
public class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {
    private boolean processInherited = true;
    private Set<OWLClass> processedClasses;
    private Set<OWLPropertyExpression> restrictedProperties;
    private Map<OWLPropertyExpression,OWLRestriction> restrictions;
    private OWLReasoner reasoner;
    private Set<OWLOntology> onts;

    public RestrictionVisitor(Set<OWLOntology> onts, OWLReasoner reasoner) {
        restrictedProperties = new HashSet<OWLPropertyExpression>();
        processedClasses = new HashSet<OWLClass>();
        this.onts = onts;
        restrictions = new HashMap<OWLPropertyExpression,OWLRestriction>();
        this.reasoner = reasoner;
    }

    public void setProcessInherited(boolean processInherited) {
        this.processInherited = processInherited;
    }

    public Set<OWLPropertyExpression> getRestrictedProperties() {
        return restrictedProperties;
    }

    public Map<OWLPropertyExpression,OWLRestriction> getRestrictions() {
        return restrictions;
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
        restrictedProperties.clear();
    }

    public void visit(OWLObjectAllValuesFrom desc) {
        // This method gets called when a class expression is an
        // existential (someValuesFrom) restriction and it asks us to visit it
        
    	if(restrictions.containsKey(desc.getProperty())) {
    		//check with reasoner which is the leaf restriction
    		
    		//r is what is in the index at the moment
    		OWLRestriction r = restrictions.get(desc.getProperty());
    		
    		if(r instanceof OWLObjectAllValuesFrom) {
    			OWLObjectAllValuesFrom prevRestriction = (OWLObjectAllValuesFrom) r;
    			OWLClassExpression prevRestriction_ce = prevRestriction.getFiller();
    			OWLClassExpression curr_ce = desc.getFiller();

    			NodeSet<OWLClass> prevRestrictionSubclasses = reasoner.getSubClasses(prevRestriction_ce, false);
    			NodeSet<OWLClass> prevRestrictionSuperclasses = reasoner.getSuperClasses(prevRestriction_ce, false);
    			
    			if(prevRestrictionSubclasses == null) {
    				return;
    			}
    			
    			boolean doesPrevRestrictionSubClassContainCurrClass = prevRestrictionSubclasses.containsEntity(curr_ce.asOWLClass());
    			boolean doesPrevRestrictionSuperClassContainCurrClass = prevRestrictionSuperclasses.containsEntity(curr_ce.asOWLClass());
    			
    			if(doesPrevRestrictionSubClassContainCurrClass) {
    				//if prev restriction has a class that is a subclass of the curr restriction;
    				//means that we should use the curr restriction
    				restrictions.remove(desc.getProperty());
    				restrictions.put(desc.getProperty(), desc);
    			}
    			else if(doesPrevRestrictionSuperClassContainCurrClass) {
    				//if prev restriction has a class that is a subclass of the curr restriction;
    				//means that we should keep things as they are
    			}
    		}
    	} else {
			restrictions.put(desc.getProperty(), desc);
        }
    	
    	restrictedProperties.add(desc.getProperty());
        //System.out.println("OWLObjectAllValuesFrom filler: " + desc.getFiller());
    }

    
    public void visit(OWLObjectSomeValuesFrom desc) {
    	if(restrictions.containsKey(desc.getProperty())) {
    		//check with reasoner which is the leaf restriction
    		
    		//r is what is in the index at the moment
    		OWLRestriction r = restrictions.get(desc.getProperty());
    		
    		if(r instanceof OWLObjectSomeValuesFrom) {
    			OWLObjectSomeValuesFrom prevRestriction = (OWLObjectSomeValuesFrom) r;
    			OWLClassExpression prevRestriction_ce = prevRestriction.getFiller();
    			OWLClassExpression curr_ce = desc.getFiller();

    			NodeSet<OWLClass> prevRestrictionSubclasses = reasoner.getSubClasses(prevRestriction_ce, false);
    			NodeSet<OWLClass> prevRestrictionSuperclasses = reasoner.getSuperClasses(prevRestriction_ce, false);
    			
    			if(prevRestrictionSubclasses == null) {
    				return;
    			}
    			
    			boolean doesPrevRestrictionSubClassContainCurrClass = prevRestrictionSubclasses.containsEntity(curr_ce.asOWLClass());
    			boolean doesPrevRestrictionSuperClassContainCurrClass = prevRestrictionSuperclasses.containsEntity(curr_ce.asOWLClass());
    			
    			if(doesPrevRestrictionSubClassContainCurrClass) {
    				//if prev restriction has a class that is a subclass of the curr restriction;
    				//means that we should use the curr restriction
    				restrictions.remove(desc.getProperty());
    				restrictions.put(desc.getProperty(), desc);
    			}
    			else if(doesPrevRestrictionSuperClassContainCurrClass) {
    				//if prev restriction has a class that is a subclass of the curr restriction;
    				//means that we should keep things as they are
    			}
    		}
    	} else {
			restrictions.put(desc.getProperty(), desc);
        }
    	
    	restrictedProperties.add(desc.getProperty());
        //System.out.println("OWLObjectAllValuesFrom filler: " + desc.getFiller());

    }
    
    public void visit(OWLDataAllValuesFrom desc) {
        
    	if(restrictions.containsKey(desc.getProperty())) {
    		//check with reasoner which is the leaf restriction
    		OWLRestriction r = restrictions.get(desc.getProperty());
    		if(r instanceof OWLDataAllValuesFrom) {
    			OWLDataAllValuesFrom prevRestriction = (OWLDataAllValuesFrom) r;
    			OWLDataRange prevRestriction_range = prevRestriction.getFiller();
    			OWLDataRange curr_range = desc.getFiller();
    			
    			if(prevRestriction_range != null && curr_range != null &&
    					prevRestriction_range.getDataRangeType().equals(curr_range.getDataRangeType())) {
    				restrictions.remove(desc.getProperty());
    				restrictions.put(desc.getProperty(), desc);
    			}
    			
    		}
    	} else {
			restrictions.put(desc.getProperty(), desc);
        }
    	
    	restrictedProperties.add(desc.getProperty());
        //System.out.println("OWLObjectAllValuesFrom filler: " + desc.getFiller());
    }
    
    public void visit(OWLDataSomeValuesFrom desc) {
    	if(restrictions.containsKey(desc.getProperty())) {
    		//check with reasoner which is the leaf restriction
    		OWLRestriction r = restrictions.get(desc.getProperty());
    		if(r instanceof OWLDataSomeValuesFrom) {
    			OWLDataSomeValuesFrom prevRestriction = (OWLDataSomeValuesFrom) r;
    			OWLDataRange prevRestriction_range = prevRestriction.getFiller();
    			OWLDataRange curr_range = desc.getFiller();
    			
    			if(prevRestriction_range != null && curr_range != null &&
    					prevRestriction_range.getDataRangeType().equals(curr_range.getDataRangeType())) {
    				restrictions.remove(desc.getProperty());
    				restrictions.put(desc.getProperty(), desc);
    			}
    			
    		}
    	} else {
			restrictions.put(desc.getProperty(), desc);
        }
    	
    	restrictedProperties.add(desc.getProperty());
        //System.out.println("OWLObjectAllValuesFrom filler: " + desc.getFiller());
    }
    

}


