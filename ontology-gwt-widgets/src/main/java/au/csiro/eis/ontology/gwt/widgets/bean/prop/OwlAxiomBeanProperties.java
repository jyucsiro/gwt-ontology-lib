package au.csiro.eis.ontology.gwt.widgets.bean.prop;

import java.util.List;


import au.csiro.eis.ontology.beans.OwlAxiomBean;
import au.csiro.eis.ontology.beans.OwlClassBean;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface OwlAxiomBeanProperties extends PropertyAccess<OwlClassBean> {
	  @Path("id")
	  ModelKeyProvider<OwlAxiomBean> key();
	  ValueProvider<OwlAxiomBean, String> label();
	  

}
