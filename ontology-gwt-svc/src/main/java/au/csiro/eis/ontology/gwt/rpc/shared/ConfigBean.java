package au.csiro.eis.ontology.gwt.rpc.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConfigBean implements Serializable, IsSerializable{
	String userIri = null;
	String configFileName;
	
	public ConfigBean () {
		
	}
	
	public ConfigBean (String userIri, String configFileName) {
		this.configFileName = configFileName;
		this.userIri = userIri;
	}
	
	
	public String getUserIri() {
		return userIri;
	}
	public void setUserIri(String userIri) {
		this.userIri = userIri;
	}
	public String getConfigFileName() {
		return configFileName;
	}
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}
	
	
}
