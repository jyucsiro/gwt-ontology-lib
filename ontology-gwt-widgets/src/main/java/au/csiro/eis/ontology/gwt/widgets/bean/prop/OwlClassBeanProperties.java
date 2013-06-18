package au.csiro.eis.ontology.gwt.widgets.bean.prop;

import java.util.List;


import au.csiro.eis.ontology.beans.OwlClassBean;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface OwlClassBeanProperties extends PropertyAccess<OwlClassBean> {
	  @Path("iri")
	  ModelKeyProvider<OwlClassBean> key();
	  ValueProvider<OwlClassBean, String> name();
	  
	  ValueProvider<OwlClassBean, String> iri();
	  ValueProvider<OwlClassBean, String> rdfsComment();
	  ValueProvider<OwlClassBean, String> label();
	  ValueProvider<OwlClassBean, String> longLabel();
	  ValueProvider<OwlClassBean, String> localName();
	  //ValueProvider<OwlClassBean, String> parents();
	  //ValueProvider<OwlClassBean, String> children();
	  ValueProvider<OwlClassBean, Boolean> hasChildren();
	  ValueProvider<OwlClassBean, Boolean> hasParents();
	  ValueProvider<OwlClassBean, List<OwlClassBean>> children();
	  ValueProvider<OwlClassBean, List<OwlClassBean>> parents();

	  ValueProvider<OwlClassBean, Boolean> hasIndividuals();
	  ValueProvider<OwlClassBean, Boolean> loadIndividuals();
	  @Path("name")
	LabelProvider<OwlClassBean> nameLabel();

}
