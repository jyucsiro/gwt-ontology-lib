package au.csiro.eis.ontology.gwt.widgets.bean.prop;


import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface OwlIndividualBeanProperties  extends PropertyAccess<OwlIndividualBean> {
    @Path("name")
    ModelKeyProvider<OwlIndividualBean> key();
    
    @Path("name")
    LabelProvider<OwlIndividualBean> nameLabel();
    
    ValueProvider<OwlIndividualBean, String> name();
    ValueProvider<OwlIndividualBean, String> localName();
    ValueProvider<OwlIndividualBean, String> label();

    ValueProvider<OwlIndividualBean, OwlClassBean> type();

    
  }