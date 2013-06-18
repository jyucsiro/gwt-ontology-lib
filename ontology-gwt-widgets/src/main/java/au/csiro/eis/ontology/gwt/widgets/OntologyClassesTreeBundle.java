package au.csiro.eis.ontology.gwt.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface OntologyClassesTreeBundle extends ClientBundle {
	  public static final OntologyClassesTreeBundle INSTANCE =  GWT.create(OntologyClassesTreeBundle.class);

	  @Source("concept-icon-light.png")
	ImageResource conceptLeaf();
	@Source("concept-icon-dark.png")
	ImageResource concept();

}