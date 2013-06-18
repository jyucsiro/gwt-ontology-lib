package au.csiro.eis.ontology.gwt.widgets.xtemplates;


import au.csiro.eis.ontology.beans.OwlRestrictionBean;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;

public interface OwlRestrictionRenderer extends XTemplates {
    @XTemplate("{data.property} : {data.filler}")
    public SafeHtml render(OwlRestrictionBean data);
    
    @XTemplate(
    		"{data.type} {data.property} {data.filler}" +
    		"-" +
    		"<tpl if='type == \"ObjectAllValuesFrom\"'>Hello</tpl>"
    		
    		)
    public SafeHtml render2(OwlRestrictionBean data);
 
    @XTemplate(source = "owlrestrictiontemplate.html")
    public SafeHtml renderTemplate(OwlRestrictionBean data);
    
  }
