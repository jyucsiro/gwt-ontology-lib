package au.csiro.eis.ontology.gwt.widgets.bean.prop;



import au.csiro.eis.ontology.beans.OwlIndividualBean;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public class OwlIndividualBeanKeyProvider implements ModelKeyProvider<OwlIndividualBean> {
	public String getKey(OwlIndividualBean item) {
		return item.getName();
	}


}