package au.csiro.eis.ontology.resource.owlapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mindswap.pellet.exceptions.InternalReasonerException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlDataPropertyBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.beans.OwlLiteralBean;
import au.csiro.eis.ontology.beans.OwlObjectPropertyBean;
import au.csiro.eis.ontology.beans.OwlRestrictionBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.owlapi.tools.ClassVisitor;
import au.csiro.eis.ontology.owlapi.tools.IndividualVisitor;
import au.csiro.eis.ontology.owlapi.tools.OwlPropertyUtil;
import au.csiro.eis.ontology.owlapi.tools.RestrictionUtil;
import au.csiro.eis.ontology.owlapi.tools.RestrictionVisitor;
import au.csiro.eis.ontology.owlapi.tools.SubclassVisitor;
import au.csiro.eis.ontology.resource.CepOntologyManager;

public class OntologyBeanFactory {
	final int MAX_DEPTH_TRAVERSAL = 3;
	CepOntologyManager cepOntMgr;

	public OntologyBeanFactory(CepOntologyManager cepOntMgr) {
		this.cepOntMgr = cepOntMgr;
	}


	//retrieves a OwlClassBean with iri and annotations
	public OwlClassBean convertToOwlClassBeanSimple(OWLClass cls, int currDepth) {
		if(currDepth > MAX_DEPTH_TRAVERSAL) {
			return null;
		}
		
		if(cls.isOWLNothing() || cls.isOWLThing()) {
    		return null;
    	}
		
    	String shortForm = this.cepOntMgr.getShortFormProvider().getShortForm(cls);

		OwlClassBean newBean = new OwlClassBean(shortForm,cls.toStringID());
		newBean.setLoadIndividuals(false);
		
		//System.out.println("class: " + newBean.getIri());
		
		//System.out.println("processing annotations");
		 // Firstly, get the annotation property for rdfs:label as the name prop

		OWLAnnotationProperty label = this.cepOntMgr.getDataFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		for(OWLOntology ont : this.cepOntMgr.getOntologies()) {
            for (OWLAnnotation annotation : cls.getAnnotations(ont, label)) {
            	if (annotation.getValue() instanceof OWLLiteral) {
            		OWLLiteral val = (OWLLiteral) annotation.getValue();
            		
            		if(val.getLang() == "" || val.getLang() == "en") {
                		newBean.setName(val.getLiteral());
            			newBean.setLabel(val.getLiteral());
            		}
            	}
            }
        }
        
		OWLAnnotationProperty rdfsComment = this.cepOntMgr.getDataFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		for(OWLOntology ont : this.cepOntMgr.getOntologies()) {
            for (OWLAnnotation annotation : cls.getAnnotations(ont, rdfsComment)) {
            	OWLAnnotationValue v = annotation.getValue();
            	
            	if (annotation.getValue() instanceof OWLLiteral) {
            		OWLLiteral val = (OWLLiteral) annotation.getValue();            		
            		if(val.getLang() == "" || val.getLang() == "en") {
            			newBean.setRdfsComment(val.getLiteral());
                		
            		}
            	}
            }
        }
		//get restrictions
        //walk over restrictions
        newBean.setRestrictions(this.walkToGetRestrictions(cls));
        //newBean.setObjectProperties(this.walkToGetObjectProperties(cls));
        
        newBean.initLongLabel();
        
        return newBean;
	}
	
	public OwlClassBean convertToOwlClassBean(OWLClass cls, int currDepth, boolean getIndividuals, boolean loadParents, boolean loadChildren) {
		if(currDepth > MAX_DEPTH_TRAVERSAL) {
			return null;
		}
		
		if(cls.isOWLNothing() || cls.isOWLThing()) {
    		return null;
    	}
		
		OwlClassBean newBean = this.convertToOwlClassBeanSimple(cls, currDepth);
		
		if(loadParents) {
		    //System.out.println("adding parents");
	        // look for other bits of information... superclass
			try {
				NodeSet<OWLClass> parentClses = this.cepOntMgr.getReasoner().getSuperClasses(cls, true);
			
		        if(parentClses != null) {
			        Set<OWLClass> parentSet = parentClses.getFlattened();
			        
			        List<OwlClassBean> parentBeans = new ArrayList<OwlClassBean>();
			        for(OWLClass currParent : parentSet) {
			        	if(currParent.isOWLNothing() || currParent.isOWLThing()) {
			        		continue;
			        	}
			        	
			        	OwlClassBean resultBean = convertToOwlClassBean(currParent, currDepth+1, getIndividuals, loadParents, loadChildren);
			        	if(resultBean != null) {
							parentBeans.add(resultBean);
						}
			        }
			        newBean.setParents(parentBeans);
		        }
			} catch (InternalReasonerException ire) {
				ire.printStackTrace();
			}
		}
		
		if(loadChildren) {
	        //System.out.println("adding children");
	        // look for other bits of information... subclasses
			try{
		        NodeSet<OWLClass> childClses = this.cepOntMgr.getReasoner().getSubClasses(cls, true);
		        
		        if(childClses != null) {
			        Set<OWLClass> childSet = childClses.getFlattened();
			        
			        List<OwlClassBean> childBeans = new ArrayList<OwlClassBean>();
			        for(OWLClass currChild : childSet) {
			        	
			        	if(currChild.isOWLNothing() || currChild.isOWLThing()) {
			        		continue;
			        	}
			        	
			        	OwlClassBean resultBean = convertToOwlClassBean(currChild, currDepth+1, getIndividuals, loadParents, loadChildren);
			        	
						if(resultBean != null) {
							childBeans.add(resultBean);
						}
			        }
			        
			        newBean.setChildren(childBeans);
		        }
			} catch (InternalReasonerException ire) {
				ire.printStackTrace();
			}
        
		}
        
        //System.out.println("adding individuals");
        // look for other bits of information... subclasses

		if(getIndividuals) {
		
	        //check if there are individuals
	        try {
		        NodeSet<OWLNamedIndividual> indivs = this.cepOntMgr.getReasoner().getInstances(cls, true);
		
		        if(indivs != null) {
		        	Set<OWLNamedIndividual> indivSet = indivs.getFlattened();
		
		        	if(indivSet.size() > 0) {
		        		newBean.setHasIndividuals(true);
		        	}
		        	if(getIndividuals) {        
		        		List<OwlIndividualBean> indivBeans = new ArrayList<OwlIndividualBean>();
		        		for(OWLNamedIndividual currIndiv : indivSet) {
		        			OwlIndividualBean resultBean = convertToOwlIndividualBean(currIndiv, newBean, currDepth+1);
		        			if(resultBean != null) {
		        				indivBeans.add(resultBean);
		        			}
		        		}
		
		        		//System.out.println("Number of indivBeans: " + indivBeans.size());
		        		newBean.setIndividuals(indivBeans);
		        	}
		        }
	        } catch (InternalReasonerException ire) {
	        	ire.printStackTrace();
	        }
		}
        //System.out.println("adding related classes");
        
        
        //properties
        //Set<OWLAxiom> ax = this.cepOntMgr.getDefaultOntology().getAxioms(cls);
        //this.walk(cls);
        
        
        return newBean;
	}
	
	
	public List<OwlRestrictionBean> walkToGetRestrictions(OWLClass cls) {
		List<OwlRestrictionBean> restrictionList =new ArrayList<OwlRestrictionBean>();
		RestrictionUtil util = new RestrictionUtil();
		
		RestrictionVisitor restrictionVisitor = new RestrictionVisitor(this.cepOntMgr.getOntologies(), this.cepOntMgr.getReasoner());
        for (OWLSubClassOfAxiom ax : this.cepOntMgr.getDefaultOntology().getSubClassAxiomsForSubClass(cls)) {
        	OWLClassExpression superCls = ax.getSuperClass();
        	superCls.accept(restrictionVisitor);
        }
        
        for(OWLOntology importedOntology :  this.cepOntMgr.getDefaultOntology().getImports()) {
        	for (OWLSubClassOfAxiom ax : importedOntology.getSubClassAxiomsForSubClass(cls)) {
            	OWLClassExpression superCls = ax.getSuperClass();
            	superCls.accept(restrictionVisitor);
            }
		}
               
        for (OWLPropertyExpression prop : restrictionVisitor.getRestrictions().keySet()) {
        	OWLRestriction restr = restrictionVisitor.getRestrictions().get(prop);
        	OWLAnnotationProperty label = this.cepOntMgr.getDataFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        	OwlRestrictionBean rb = util.renderRestrictionAsBean(restr, this.cepOntMgr.getOntologies(), label);
        	
        	try {
        		if (rb == null) {
                	//rb = util.renderRestrictionAsBean(restr, this.cepOntMgr.getDefaultOntology());

        			throw new Exception("rb is null");
        		}
        		
        	} catch (Exception e) {
        		System.out.println("Caught an owl restriction that is null for ");
        	}
        	
        	
        	restrictionList.add(rb);
            
        }
        
        return restrictionList;
	}
	

	
	public List<OwlObjectPropertyBean> walkToGetObjectProperties(OWLClass cls) {
		List<OwlObjectPropertyBean> propList =new ArrayList<OwlObjectPropertyBean>();
		OwlPropertyUtil util = new OwlPropertyUtil();
		
		//OwlObjectPropertyVisitor propVisitor = new OwlObjectPropertyVisitor(this.ontologies, this.cepOntMgr.getReasoner());
		//cls.accept(propVisitor);
		
		Set<OWLObjectProperty> objPropSet = this.cepOntMgr.getDefaultOntology().getObjectPropertiesInSignature(true);
		
		for(OWLObjectProperty p : objPropSet ) {
			for(OWLClass c : this.cepOntMgr.getReasoner().getObjectPropertyDomains(p, true).getFlattened()) {
				
				if(c.equals(cls)) {
					System.out.println("obj prop: " + p + " --- cls: " + c + " ==>MATCHES!!");	
				} 
				else {
					
					if(this.cepOntMgr.getReasoner().getSuperClasses(cls, false).containsEntity(c)) {
						System.out.println("obj prop: " + p + " --- cls: " + c + " ==>MATCHES in superclass!!");
					}
					else {
						System.out.println("obj prop: " + p + " --- cls: " + c);
					}
				}
	        	propList.add(util.renderPropertyAsBean(p, this.cepOntMgr.getReasoner()));
			}
		}
        
		/*
		if(propVisitor.getProperties() != null) {
			
	        for (OWLObjectPropertyExpression prop : propVisitor.getProperties()) {
	        	propList.add(util.renderPropertyAsBean(prop, reasoner));
	            
	        }
		}*/
		
        return propList;
	}

	
	public OwlIndividualBean convertToOwlIndividualBean(OWLNamedIndividual indiv, int currDepth, boolean skipPropMappings) {
		return this.convertToOwlIndividualBean(indiv, null, currDepth, skipPropMappings, true);
	}
	
	/**
	 * InputclsBean can be null
	 * 
	 * @param indiv
	 * @param inputClsBean
	 * @param currDepth
	 * @param skipPropMappings
	 * @return
	 */
	public OwlIndividualBean convertToOwlIndividualBean(OWLNamedIndividual indiv, OwlClassBean inputClsBean, int currDepth, boolean skipPropMappings, boolean resolveParents) {
		
		if(currDepth > MAX_DEPTH_TRAVERSAL || indiv == null) {
			System.out.println("MAX_DEPTH_TRAVERSAL exceeded or indiv is null");
			return null;
		}
		String shortForm = this.cepOntMgr.getShortFormProvider().getShortForm(indiv);

		OwlClassBean clsBean = null;
		OwlIndividualBean newBean = new OwlIndividualBean(shortForm,indiv.toStringID());
		
		//only process if the input owl class for the indivi is unknown
		if(inputClsBean == null && resolveParents == true) {
			Set<OWLClassExpression> clsTypeExprSet = indiv.getTypes(this.cepOntMgr.getOntologies());
			
			if(clsTypeExprSet == null || clsTypeExprSet.iterator() == null) {
				System.out.println("clsTypeExprSet is null");
				return null;
			}
			
			Iterator<OWLClassExpression> citer = clsTypeExprSet.iterator();
			
			if(citer.hasNext()) {
				OWLClassExpression cexp = citer.next();			
				if(cexp == null) {
					OWLClass cls = cexp.asOWLClass();
			    	//clsBean = this.convertToOwlClassBean(cls, 0, false);
					clsBean = this.convertToOwlClassBeanSimple(cls, 0);
			    	newBean.setType(clsBean);					
				}
			}
			else {
				System.out.println("citer.hasNext() is null");
				
				//return null;
			}
		}
		else {
	    	newBean.setType(inputClsBean);				
		}
		
		//System.out.println("indiv: " + newBean.getIri());
		

		if(!skipPropMappings) {
			//get the asserted ont indiv props
			Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectProperties = new HashMap<OWLObjectPropertyExpression, Set<OWLIndividual>>();
			
			for(OWLOntology o : this.cepOntMgr.getOntologies()) {
				objectProperties.putAll(indiv.getObjectPropertyValues(o));
			}
			
			Map<OwlObjectPropertyBean, Set<OwlIndividualBean>> objectPropertiesBeanMap = new HashMap<OwlObjectPropertyBean, Set<OwlIndividualBean>>();
			
			for(OWLObjectPropertyExpression expr : objectProperties.keySet()) {
				OwlObjectPropertyBean opBean = convertToOwlObjectPropertyBean(expr);
				Set<OwlIndividualBean> setIndiv = new HashSet<OwlIndividualBean>();
				
				for(OWLIndividual i:objectProperties.get(expr) ) {
					if(i.isNamed()) {
						OWLNamedIndividual ni = i.asOWLNamedIndividual();
						boolean doSkipPropMapping = true;
						setIndiv.add(this.convertToOwlIndividualBean(ni, null, 0, doSkipPropMapping, true));
					}
				}
				
				objectPropertiesBeanMap.put(opBean, setIndiv);
				
			}
	
			newBean.setObjectProperties(objectPropertiesBeanMap);
		}
		
		Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataProperties = new HashMap<OWLDataPropertyExpression, Set<OWLLiteral>>();
		for(OWLOntology o : this.cepOntMgr.getOntologies()) {
			dataProperties.putAll(indiv.getDataPropertyValues(o));
		}
		
		Map<OwlDataPropertyBean, Set<OwlLiteralBean>> dataPropertiesBeanMap = new HashMap<OwlDataPropertyBean, Set<OwlLiteralBean>>();
		
		for(OWLDataPropertyExpression expr : dataProperties.keySet()) {
			OwlDataPropertyBean dpBean = convertToOwlDataPropertyBean(expr);
			Set<OwlLiteralBean> setLiterals = new HashSet<OwlLiteralBean>();
			
			for(OWLLiteral lit:dataProperties.get(expr) ) {
				OwlLiteralBean litBean = convertToOwlLiteralBean(lit);
				
				setLiterals.add(litBean);
			}
			
			dataPropertiesBeanMap.put(dpBean, setLiterals);			
		}
	
		newBean.setDataProperties(dataPropertiesBeanMap);
		
		
		//System.out.println("processing annotations");

		 // Firstly, get the annotation property for rdfs:label as the name prop
        OWLAnnotationProperty label = this.cepOntMgr.getDataFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        
        for(OWLOntology ont : this.cepOntMgr.getOntologies()) {
            for (OWLAnnotation annotation : indiv.getAnnotations(ont, label)) {
            	if (annotation.getValue() instanceof OWLLiteral) {
            		OWLLiteral val = (OWLLiteral) annotation.getValue();
            		
            		newBean.setName(val.getLiteral());
            		newBean.setLabel(val.getLiteral());
            	}
            }
        }
        return newBean;
	}
	
	public OwlLiteralBean convertToOwlLiteralBean(OWLLiteral lit) {
		OwlLiteralBean newBean = new OwlLiteralBean();
		
		newBean.setDatatypeIri(lit.getDatatype().getIRI().toString());
		newBean.setLang(lit.getLang());
		newBean.setLiteral(lit.getLiteral());
		
		return newBean;
	}

	public OwlDataPropertyBean convertToOwlDataPropertyBean(
			OWLDataPropertyExpression expr) {
		
		OwlDataPropertyBean bean = new OwlDataPropertyBean();
		
		//TODO: fill out the bean
		OWLDataProperty dataProp = expr.asOWLDataProperty();
		
		
		bean.setPropertyIri(dataProp.getIRI().toString());
		bean.setProperty(this.cepOntMgr.getShortFormProvider().getShortForm(dataProp));
		
		
		return bean;
	}

	public OwlObjectPropertyBean convertToOwlObjectPropertyBeanOfIndividual(OWLObjectPropertyExpression expr, OWLNamedIndividual indiv) {
		OwlObjectPropertyBean bean = convertToOwlObjectPropertyBean(expr);
		

		//ADD filler as the instance
		bean.setFiller(this.cepOntMgr.getShortFormProvider().getShortForm(indiv));
		bean.setFillerIri(indiv.getIRI().toString());
		
		
		return bean;
	}
	
	public OwlObjectPropertyBean convertToOwlObjectPropertyBean(OWLObjectPropertyExpression expr) {
		OwlObjectPropertyBean bean = new OwlObjectPropertyBean();
		
		//TODO: fill out the bean
		OWLObjectProperty objProp = expr.asOWLObjectProperty();
		
		
		bean.setPropertyIri(objProp.getIRI().toString());
		bean.setProperty(this.cepOntMgr.getShortFormProvider().getShortForm(objProp));
		
		
		return bean;
	}
	
	public OwlIndividualBean convertToOwlIndividualBean(OWLNamedIndividual indiv, OwlClassBean cls, int currDepth) {
		
		if(currDepth > MAX_DEPTH_TRAVERSAL || indiv == null) {
			return null;
		}
		
		/*
		String shortForm = this.cepOntMgr.getShortFormProvider().getShortForm(indiv);

    	OwlIndividualBean newBean = new OwlIndividualBean(shortForm,indiv.toStringID());
    	newBean.setType(cls);
		//System.out.println("indiv: " + newBean.getIri());
		
		
		//System.out.println("processing annotations");

		 // Firstly, get the annotation property for rdfs:label as the name prop
        OWLAnnotationProperty label = this.cepOntMgr.getDataFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        
        for(OWLOntology ont : this.cepOntMgr.getOntologies()) {
            for (OWLAnnotation annotation : indiv.getAnnotations(ont, label)) {
            	if (annotation.getValue() instanceof OWLLiteral) {
            		OWLLiteral val = (OWLLiteral) annotation.getValue();
            		
            		newBean.setName(val.getLiteral());            		
            	}
            }
        }*/
		
		OwlIndividualBean newBean ;
		
		newBean = this.convertToOwlIndividualBean(indiv, null, currDepth+1, false, true);
		
        return newBean;
	}
	
	public void walk(final OWLClass cls) {
		//OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(this.cepOntMgr.getDefaultOntology()));
		OWLOntologyWalker walker = new OWLOntologyWalker(this.cepOntMgr.getOntologies());

        // Now ask our walker to walk over the ontology.  We specify a visitor who gets visited
        // by the various objects as the walker encounters them.

        // We need to create out visitor.  This can be any ordinary visitor, but we will
        // extend the OWLOntologyWalkerVisitor because it provides a convenience method to
        // get the current axiom being visited as we go.
        // Create an instance and override the visit(OWLObjectSomeValuesFrom) method, because
        // we are interested in some values from restrictions.
		
		
        OWLOntologyWalkerVisitor<Object> visitor = new OWLOntologyWalkerVisitor<Object>(walker) {

            public Object visit(OWLObjectSomeValuesFrom desc) {
            	OWLAxiom ax = getCurrentAxiom();
            	
            	Set<OWLClass> axClses = ax.getClassesInSignature();
            	for(OWLClass c : axClses) {
            		if(c.equals(cls)) {
                        // Print out the restriction
                        System.out.println("OWLObjectSomeValuesFrom: " + desc);
                        // Print out the axiom where the restriction is used
                        System.out.println("         " + getCurrentAxiom());
                        System.out.println();            			
            		}
            	}
                // We don't need to return anything here.
                return null;
            }
            
            public Object visit(OWLObjectProperty desc) {
            	OWLAxiom ax = getCurrentAxiom();
            	
            	
            	Set<OWLClass> axClses = ax.getClassesInSignature();
            	for(OWLClass c : axClses) {
            		if(c.equals(cls)) {
                        // Print out the restriction
                        System.out.println("OWLObjectProperty: " + desc);
                        // Print out the axiom where the restriction is used
                        System.out.println("         " + getCurrentAxiom());
                        System.out.println();            			
            		}
            	}
                // We don't need to return anything here.
                return null;
            }
        };

        //walk over restrictions
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(this.cepOntMgr.getOntologies(), this.cepOntMgr.getReasoner());
        for (OWLSubClassOfAxiom ax : this.cepOntMgr.getDefaultOntology().getSubClassAxiomsForSubClass(cls)) {
        	OWLClassExpression superCls = ax.getSuperClass();
        	superCls.accept(restrictionVisitor);
        }
        
        for(OWLOntology importedOntology :  this.cepOntMgr.getDefaultOntology().getImports()) {
        	for (OWLSubClassOfAxiom ax : importedOntology.getSubClassAxiomsForSubClass(cls)) {
            	OWLClassExpression superCls = ax.getSuperClass();
            	superCls.accept(restrictionVisitor);
            }
		}
        
        /*
        System.out.println("Restricted properties for " + cls + ": " + restrictionVisitor.getRestrictedProperties().size());
        for (OWLObjectPropertyExpression prop : restrictionVisitor.getRestrictedProperties()) {
            System.out.println("    " + prop);
            for(OWLClassExpression ce : prop.getNestedClassExpressions()) {
                System.out.println("target:         " + ce);
            	
            }
            Set<OWLAxiom> axioms = cls.getReferencingAxioms(this.defaultOntology, true);
            for(OWLAxiom ax: axioms) {
            	if(ax.getObjectPropertiesInSignature().contains(prop)) {
            		
            		
                    System.out.println("ax:         " + ax);
                    
            		
            	}
            	
            }
        }
        
        
	*/

       
        System.out.println("Restricted properties for " + cls + ": " + restrictionVisitor.getRestrictions().size());
        for (OWLPropertyExpression prop : restrictionVisitor.getRestrictions().keySet()) {
        	OWLRestriction restr = restrictionVisitor.getRestrictions().get(prop);
            System.out.println("    " + restr);
            
            
        }

		
	
        // Now ask the walker to walk over the ontology structure using our visitor instance.
        //walker.walkStructure(visitor);
        
        /*ClassAxiomVisitor cav = new ClassAxiomVisitor(walker, this.ontologies);
        //walker.walkStructure(cav);
        
       
        for(OWLAxiom ax : cav.getClassAxioms()) {
        	 System.out.println("Ax: " + ax);
             System.out.println();        
        }*/
        
	}


	public List<String> doOntologyClassWalking(OWLOntology ont) {
		ArrayList<String> classList = new ArrayList<String>();

		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ont));
        ClassVisitor visitor = new ClassVisitor(walker);
        walker.walkStructure(visitor);
        
        for(OWLClass c : visitor.getClassList()) {
        	classList.add(c.toStringID());
        	//classList.add(c.getIRI().toString());
        }
        
        return classList;
	}
	
	public Map<String, OwlClassBean> doOntologyClassWalkingForBean(OWLOntology ont, boolean loadIndividuals) {
		Map<String, OwlClassBean> classMap = new HashMap<String, OwlClassBean>();

		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ont));
        ClassVisitor visitor = new ClassVisitor(walker);
        walker.walkStructure(visitor);
        
        for(OWLClass c : visitor.getClassList()) {
        	boolean loadParents = false;
        	boolean loadChildren = false;
        	classMap.put(c.toStringID(),this.convertToOwlClassBean(c, 0, loadIndividuals, loadParents, loadChildren));
        }
        
        return classMap;
	}
	
	public Map<String, OwlIndividualBean> doOntologyIndividualWalkingForBean(OWLOntology ont) {
		Map<String, OwlIndividualBean> indivMap = new HashMap<String, OwlIndividualBean>();

		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ont));
        IndividualVisitor visitor = new IndividualVisitor(walker);
		
		//OWLIndividualVisitor visitor = new OWLIndividualVisitor(visitor);
        walker.walkStructure(visitor);
        
        for(OWLNamedIndividual indiv : visitor.getNamedIndividualList()) {
        	OwlIndividualBean indivBean = this.convertToOwlIndividualBean(indiv, null, 0, false, true);
        	
        	if(indivBean != null) {
        		indivMap.put(indivBean.getIri(), indivBean);
        	}
        }
        
        return indivMap;
	}
	
	public List<String> doOntologySubClassWalking(String iri, OWLOntology ont) {
		ArrayList<String> subclassList = new ArrayList<String>();

		IRI superCls = IRI.create(iri);
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ont));
		SubclassVisitor visitor = new SubclassVisitor(walker, superCls);
        walker.walkStructure(visitor);
        
        for(OWLClass c : visitor.getSubclassList()) {
        	subclassList.add(c.toStringID());
        }
        
        return subclassList;
	}
	
	
	public List<OWLClass> doOntologySubClassWalkingGetOwlClass(String iri, OWLOntology ont) {
		ArrayList<OWLClass> subclassList = new ArrayList<OWLClass>();

		IRI superCls = IRI.create(iri);
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ont));
		SubclassVisitor visitor = new SubclassVisitor(walker, superCls);
        walker.walkStructure(visitor);
        
        for(OWLClass c : visitor.getSubclassList()) {
        	subclassList.add(c);
        }
        
        return subclassList;
	}

	

	public List<OwlIndividualBean> getOntIndividualsForClass(String iri_string, boolean isDirect)
			throws OntologyInitException {
		
		if(iri_string == null)
			return null;
		
		Set<OWLNamedIndividual> setOfIndiv = new TreeSet<OWLNamedIndividual>();

        OWLClass cls = this.cepOntMgr.getDataFactory().getOWLClass(IRI.create(iri_string));
        NodeSet<OWLNamedIndividual> individuals = this.cepOntMgr.getReasoner().getInstances(cls, isDirect);
        
		for(Node<OWLNamedIndividual> currIndiv  : individuals.getNodes()) {
			
			Set<OWLNamedIndividual> entities = currIndiv.getEntitiesMinusTop();
			for(OWLNamedIndividual ent : entities) {
				if(ent.isOWLNamedIndividual()) {
					setOfIndiv.add(ent);
				}
			}
		}
		
		//populate beans to return
		ArrayList<OwlIndividualBean> beans = new ArrayList<OwlIndividualBean>();

        OwlClassBean clsBean = this.convertToOwlClassBean(cls, 0, false, false, false);

		for(OWLNamedIndividual c : setOfIndiv) {
			//System.out.println("individual: " + c.getIRI());
			OwlIndividualBean resultBean = this.convertToOwlIndividualBean(c, clsBean, 0, false, true);
			
			if(resultBean != null) {
				beans.add(resultBean);
			}
			else {
				System.out.println("resultBean: is null :(");				
			}
		}
		
		return beans;		
		
	}
	
	public OwlIndividualBean getOntIndividual(String indiv_iri_string, boolean skipPropMappings, boolean resolveParents)
			throws OntologyInitException {
		
		if(indiv_iri_string == null)
			return null;
		

		OWLNamedIndividual indiv = this.cepOntMgr.getDataFactory().getOWLNamedIndividual(IRI.create(indiv_iri_string));
		OwlIndividualBean resultBean = this.convertToOwlIndividualBean(indiv, null, 0, skipPropMappings, resolveParents);

		
		return resultBean ;		
		
	}
	
	
}
