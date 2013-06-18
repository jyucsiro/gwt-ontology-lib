package au.csiro.eis.ontology.gwt.widgets.xtemplates;


import au.csiro.eis.ontology.beans.OwlIndividualBean;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface OwlIndividualRenderer extends XTemplates {
    @XTemplate("{data.name}")
    public SafeHtml render(OwlIndividualBean data);
 
  }
