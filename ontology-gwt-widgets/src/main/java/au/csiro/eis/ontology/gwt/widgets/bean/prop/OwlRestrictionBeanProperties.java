package au.csiro.eis.ontology.gwt.widgets.bean.prop;


import au.csiro.eis.ontology.beans.OwlRestrictionBean;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface OwlRestrictionBeanProperties  extends PropertyAccess<OwlRestrictionBean> {
    @Path("property")
    ModelKeyProvider<OwlRestrictionBean> key();
    ValueProvider<OwlRestrictionBean, String> property();
    ValueProvider<OwlRestrictionBean, String> propertyIri();
    ValueProvider<OwlRestrictionBean, String> filler();
    ValueProvider<OwlRestrictionBean, String> fillerIri();
    ValueProvider<OwlRestrictionBean, String> type();
    ValueProvider<OwlRestrictionBean, String> minCard();
    ValueProvider<OwlRestrictionBean, String> maxCard();


  }