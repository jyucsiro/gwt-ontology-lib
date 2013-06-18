package au.csiro.eis.ontology.owlapi.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class ClassVisitor extends OWLOntologyWalkerVisitor<Object> {

	List<OWLClass> classList;
	
	
	
	public List<OWLClass> getClassList() {
		return classList;
	}

	public ClassVisitor(OWLOntologyWalker walker) {
		super(walker);
		// TODO Auto-generated constructor stub
		
		classList = Collections.synchronizedList(new ArrayList<OWLClass>());
	}

	public Object visit(OWLClass c) {
		classList.add(c);
		
		return null;
	}
	
}
