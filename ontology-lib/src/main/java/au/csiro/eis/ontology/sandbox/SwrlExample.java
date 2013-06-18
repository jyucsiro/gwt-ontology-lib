package au.csiro.eis.ontology.sandbox;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 15-Jan-2007<br><br>
 */
public class SwrlExample {

    public static void main(String[] args) {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            // Let's create an ontology and name it "http://www.co-ode.org/ontologies/testont.owl"
            // We need to set up a mapping which points to a concrete file where the ontology will
            // be stored. (It's good practice to do this even if we don't intend to save the ontology).
            IRI ontologyIRI = IRI.create("http://www.co-ode.org/ontologies/testont.owl");
            // Create a document IRI which can be resolved to point to where our ontology will be saved.
            File f = new File("SWRLTest.owl");
            IRI documentIRI = IRI.create(f.toURI());
            
            // Set up a mapping, which maps the ontology to the document IRI
            SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
            manager.addIRIMapper(mapper);

            // Now create the ontology - we use the ontology IRI (not the physical IRI)
            OWLOntology ontology = manager.createOntology(ontologyIRI);
            OWLDataFactory factory = manager.getOWLDataFactory();
            // Get hold of references to class A and class B.  Note that the ontology does not
            // contain class A or classB, we simply get references to objects from a data factory that represent
            // class A and class B
            OWLClass clsA = factory.getOWLClass(IRI.create(ontologyIRI + "#A"));
            OWLClass clsB = factory.getOWLClass(IRI.create(ontologyIRI + "#B"));
            SWRLVariable var = factory.getSWRLVariable(IRI.create(ontologyIRI + "#x"));
            SWRLRule rule = factory.getSWRLRule(
                    Collections.singleton(
                            factory.getSWRLClassAtom(clsA, var)
                    ),
                    Collections.singleton(
                            factory.getSWRLClassAtom(clsB, var)
                    ));
            manager.applyChange(new AddAxiom(ontology, rule));

            OWLObjectProperty prop = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#propA"));
            OWLObjectProperty propB = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#propB"));
            SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(prop, var, var);
            SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(propB, var, var);
            Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
            antecedent.add(propAtom);
            antecedent.add(propAtom2);
            SWRLRule rule2 = factory.getSWRLRule(antecedent,
                    Collections.singleton(propAtom));

            manager.applyChange(new AddAxiom(ontology, rule2));

            // Now save the ontology.  The ontology will be saved to the location where
            // we loaded it from, in the default ontology format
            manager.saveOntology(ontology);
            

        }
        catch (OWLException e) {
            e.printStackTrace();
        }
    }
}