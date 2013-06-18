package au.csiro.eis.ontology.gwt.widgets.xtemplates;


import au.csiro.eis.ontology.beans.OwlClassBean;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

public interface OwlClassRenderer extends XTemplates {
    @XTemplate("<p>Name: {data.name}</p>")
    public SafeHtml render(OwlClassBean data);
 
    @XTemplate(source = "owlclasstemplate.html")
    public SafeHtml renderTemplate(OwlClassBean data);
 
  }