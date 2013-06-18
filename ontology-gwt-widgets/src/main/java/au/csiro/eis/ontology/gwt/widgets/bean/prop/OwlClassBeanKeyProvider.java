package au.csiro.eis.ontology.gwt.widgets.bean.prop;



import au.csiro.eis.ontology.beans.OwlClassBean;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public class OwlClassBeanKeyProvider implements ModelKeyProvider<OwlClassBean> {
	
	public String getKey(OwlClassBean item) {
		return item.getName();
	}


}