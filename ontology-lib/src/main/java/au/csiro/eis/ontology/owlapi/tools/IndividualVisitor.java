package au.csiro.eis.ontology.owlapi.tools;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class IndividualVisitor extends OWLOntologyWalkerVisitor<Object> {

	List<OWLNamedIndividual> indivList;
	
	
	
	public List<OWLNamedIndividual> getNamedIndividualList() {
		return indivList;
	}

	public IndividualVisitor(OWLOntologyWalker walker) {
		super(walker);
		// TODO Auto-generated constructor stub
		
		indivList = new ArrayList<OWLNamedIndividual>();
	}

	public Object visit(OWLNamedIndividual c) {
		indivList.add(c);
		
		return null;
	}
	
}
