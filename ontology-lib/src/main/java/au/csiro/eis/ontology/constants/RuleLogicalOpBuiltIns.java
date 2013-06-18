package au.csiro.eis.ontology.constants;

import java.util.ArrayList;
import java.util.List;



public class RuleLogicalOpBuiltIns {
	
	public static SwrlVocabularyMapping GREATER_THAN 
			= new SwrlVocabularyMapping("Greater than", ">", "swrlb:greaterThan");

	public static SwrlVocabularyMapping LESS_THAN 
		= new SwrlVocabularyMapping("Less than", "<", "swrlb:lessThan");

	public static SwrlVocabularyMapping GREATER_THAN_OR_EQUAL 
		= new SwrlVocabularyMapping("Greater than or equal", ">=", "swrlb:greaterThanOrEqual");

	public static SwrlVocabularyMapping LESS_THAN_OR_EQUAL 
		= new SwrlVocabularyMapping("Less than or equal", "<=", "swrlb:lessThanOrEqual");

	public static SwrlVocabularyMapping NOT_EQUAL 
		= new SwrlVocabularyMapping("Not equal", "!=", "swrlb:notEqual");
	
	public static SwrlVocabularyMapping EQUAL_TO 
		= new SwrlVocabularyMapping("equal to", "==", "swrlb:equal");
	
	public static List<SwrlVocabularyMapping> mappings() {
		ArrayList<SwrlVocabularyMapping> mappings = new ArrayList<SwrlVocabularyMapping>();
		mappings.add(GREATER_THAN);
		mappings.add(LESS_THAN);
		mappings.add(GREATER_THAN_OR_EQUAL);
		mappings.add(LESS_THAN_OR_EQUAL);
		mappings.add(NOT_EQUAL);
		mappings.add(EQUAL_TO);
		
		return mappings;		
	}
	
}
