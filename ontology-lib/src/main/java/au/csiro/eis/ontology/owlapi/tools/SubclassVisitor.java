package au.csiro.eis.ontology.owlapi.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class SubclassVisitor extends OWLOntologyWalkerVisitor<Object> {

	List<OWLClass> subclassList;
	
	IRI superCls = null;

	public List<OWLClass> getSubclassList() {
		return subclassList;
	}

	public SubclassVisitor(OWLOntologyWalker walker, IRI superCls) {
		super(walker);
		// TODO Auto-generated constructor stub
		
		subclassList = Collections.synchronizedList(new ArrayList<OWLClass>());
		this.superCls = superCls;
	}

	public Object visit(OWLClass c) {

		if(c.getIRI().equals(this.superCls)) {
			Set<OWLClassExpression> expressions = c.getSubClasses(this.getCurrentOntology());
			for(OWLClassExpression classExp : expressions) {
			
				if(!classExp.isAnonymous()) {
					subclassList.add(classExp.asOWLClass());
				}
			}
		}
		
		return null;
	}
	
}
