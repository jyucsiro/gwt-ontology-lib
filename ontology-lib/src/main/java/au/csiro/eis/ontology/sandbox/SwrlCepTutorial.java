package au.csiro.eis.ontology.sandbox;

import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator; 
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor; 
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory; 
import com.clarkparsia.pellet.owlapiv3.Reasoner;

import org.semanticweb.owlapi.apibinding.OWLManager; 
import org.semanticweb.owlapi.io.OWLObjectRenderer; 
import org.semanticweb.owlapi.model.*; 
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner; 
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory; 
import org.semanticweb.owlapi.reasoner.SimpleConfiguration; 
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.SWRLVariableExtractor;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary; 
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat; 
import uk.ac.manchester.cs.bhig.util.Tree; 
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree; 
import uk.ac.manchester.cs.owl.owlapi.OWLObjectInverseOfImpl; 
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer; 
 
import java.io.File;
import java.util.*; 
 
/** 
 * Example how to use an OWL ontology with a reasoner. 
 * 
 * @author Martin Kuba makub@ics.muni.cz 
 */ 
public class SwrlCepTutorial { 
 
    private static final String BASE_URL = "http://www.csiro.au/cep/chaffey-data-streams.owl"; 
 
    private static OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
 
    public static void main(String[] args) throws OWLOntologyCreationException { 
 
        //prepare ontology and reasoner 
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("src/main/webapp/ontologies/chaffey-data-streams.ttl");	
        //OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(BASE_URL));
        
        System.out.println("Loading file: " + file.toString());
        
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        
        System.out.println("Loaded ");
        
        
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        // Specify the progress monitor via a configuration.  We could also specify other setup parameters in
        // the configuration, and different reasoners may accept their own defined parameters this way.
        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
        // Create a reasoner that will reason over our ontology and its imports closure.  Pass in the configuration.

        
        //hermit reasoner - doesn't support swrl builtins :(
        //System.out.println("Getting Hermit reasoner");
        //OWLReasonerFactory reasonerFactory= new org.semanticweb.HermiT.Reasoner.ReasonerFactory();
        //OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
        

        
        //pellet reasoner - supports swrl built ins
        System.out.println("Getting Pellet reasoner");
        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance(); 
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);

        
        
        reasoner.precomputeInferences();
        
        boolean consistent = reasoner.isConsistent();
        System.out.println("Consistent: " + consistent);
        System.out.println("\n");

        OWLDataFactory factory = manager.getOWLDataFactory(); 
        PrefixOWLOntologyFormat pm = manager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat(); 
        pm.setDefaultPrefix(BASE_URL + "#"); 

        //output all swrl rules
        
        SwrlRuleVisitor swrlRuleVisitor = new SwrlRuleVisitor(Collections.singleton(ontology));
        
        //SWRLVariableExtractor sve = new SWRLVariableExtractor();
        
        //explain why 
        DefaultExplanationGenerator explanationGenerator = 
                new DefaultExplanationGenerator( 
                        manager, reasonerFactory, ontology, reasoner, new SilentExplanationProgressMonitor()); 

        for (SWRLRule ax : ontology.getAxioms(AxiomType.SWRL_RULE)) {
            ax.accept(swrlRuleVisitor);
            
            
        }
        
        Iterator<SWRLRule> rulesIterator = swrlRuleVisitor.getSwrlRules().iterator();
        while(rulesIterator.hasNext()) {
        	SWRLRule curr = rulesIterator.next();

        	System.out.println(curr);
	         
        }

        
        
        
        
        //check whether the SWRL rule is used 
        checkHighNitrogenEvent(":NitrogenEvent1", pm, reasoner, factory, explanationGenerator, true);
        checkHighNitrogenEvent(":NitrogenEvent2", pm, reasoner, factory, explanationGenerator, true);
        
    } 
 
    public static void checkHighNitrogenEvent(String qname, PrefixManager pm, OWLReasoner reasoner, OWLDataFactory factory, 
    		DefaultExplanationGenerator explanationGenerator, boolean printExplanation) {
    	OWLNamedIndividual ne1 = factory.getOWLNamedIndividual(qname, pm); 
        OWLClass hneClass = factory.getOWLClass(":HighNitrogenEvent", pm); 
        OWLClassAssertionAxiom axiomToExplain = factory.getOWLClassAssertionAxiom(hneClass, ne1);
        
        boolean isEntailed = reasoner.isEntailed(axiomToExplain);
        System.out.println("Is NitrogenEvent1 a HighNitrogenEvent? : " + isEntailed); 
 
 
        if(isEntailed && printExplanation) {
	        Set<OWLAxiom> explanation = explanationGenerator.getExplanation(axiomToExplain); 
	        //DefaultExplanationOrderer deo = new DefaultExplanationOrderer(); 
	        //ExplanationTree explanationTree = deo.getOrderedExplanation(axiomToExplain, explanation); 
	        System.out.println(); 
	        System.out.println("-- explanation why NitrogenEvent1 is in class HighNitrogenEvent --"); 
	        //printIndented(explanationTree, ""); 
        }
    }

    private static void printIndented(Tree<OWLAxiom> node, String indent) { 
        OWLAxiom axiom = node.getUserObject(); 
        System.out.println(indent + renderer.render(axiom)); 
        if (!node.isLeaf()) { 
            for (Tree<OWLAxiom> child : node.getChildren()) { 
                printIndented(child, indent + "    "); 
            } 
        } 
    } 
 
    /** 
     * Helper class for extracting labels, comments and other anotations in preffered languages. 
     * Selects the first literal annotation matching the given languages in the given order. 
     */ 
    public static class LocalizedAnnotationSelector { 
        private final List<String> langs; 
        private final OWLOntology ontology; 
        private final OWLDataFactory factory; 
 
        /** 
         * Constructor. 
         * 
         * @param ontology ontology 
         * @param factory  data factory 
         * @param langs    list of prefered languages; if none is provided the Locale.getDefault() is used 
         */ 
        public LocalizedAnnotationSelector(OWLOntology ontology, OWLDataFactory factory, String... langs) { 
            this.langs = (langs == null) ? Arrays.asList(Locale.getDefault().toString()) : Arrays.asList(langs); 
            this.ontology = ontology; 
            this.factory = factory; 
        } 
 
        /** 
         * Provides the first label in the first matching language. 
         * 
         * @param ind individual 
         * @return label in one of preferred languages or null if not available 
         */ 
        public String getLabel(OWLNamedIndividual ind) { 
            return getAnnotationString(ind, OWLRDFVocabulary.RDFS_LABEL.getIRI()); 
        } 
 
        public String getComment(OWLNamedIndividual ind) { 
            return getAnnotationString(ind, OWLRDFVocabulary.RDFS_COMMENT.getIRI()); 
        } 
 
        public String getAnnotationString(OWLNamedIndividual ind, IRI annotationIRI) { 
            return getLocalizedString(ind.getAnnotations(ontology, factory.getOWLAnnotationProperty(annotationIRI))); 
        } 
 
        private String getLocalizedString(Set<OWLAnnotation> annotations) { 
            List<OWLLiteral> literalLabels = new ArrayList<OWLLiteral>(annotations.size()); 
            for (OWLAnnotation label : annotations) { 
                if (label.getValue() instanceof OWLLiteral) { 
                    literalLabels.add((OWLLiteral) label.getValue()); 
                } 
            } 
            for (String lang : langs) { 
                for (OWLLiteral literal : literalLabels) { 
                    if (literal.hasLang(lang)) return literal.getLiteral(); 
                } 
            } 
            for (OWLLiteral literal : literalLabels) { 
                if (!literal.hasLang()) return literal.getLiteral(); 
            } 
            return null; 
        } 
    }
    
    /**
     * Visits swrl rules
     */
    private static class SwrlRuleVisitor extends SWRLVariableExtractor {

        private boolean processInherited = true;

        private Set<SWRLRule> swrlRules;

        private Set<OWLOntology> onts;

        public SwrlRuleVisitor(Set<OWLOntology> onts) {
            swrlRules = new HashSet<SWRLRule>();
            this.onts = onts;
        }


        public void setProcessInherited(boolean processInherited) {
            this.processInherited = processInherited;
        }

        public void visit(SWRLRule node) {
        	swrlRules.add(node);
        }


        public Set<SWRLRule> getSwrlRules() {
        	return this.swrlRules;
        }
        
        public Set<SWRLVariable> getSwrlVariables() {
        	return this.getSwrlVariables();
        }

    }
} 