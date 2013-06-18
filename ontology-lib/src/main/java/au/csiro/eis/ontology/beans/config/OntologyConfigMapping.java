package au.csiro.eis.ontology.beans.config;

public class OntologyConfigMapping {
	String prefix;
	String IRI;
	String path;
	
	
	public OntologyConfigMapping() {
		prefix= null;
		this.IRI = null;
		this.path = null;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getIRI() {
		return IRI;
	}
	public void setIRI(String iRI) {
		IRI = iRI;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int sentinel = 0;
		
		if(this.getPrefix() != null) {
			sb.append("prefix: " + this.getPrefix() + "; ");
			sentinel++;
		}
		if(this.getIRI() != null) {
			sb.append("IRI: " + this.getIRI() + "; ");
			sentinel++;
		}
		if(this.getPath() != null) {
			sb.append("path: " + this.getPath() + "; ");
			sentinel++;
		}
		return sb.toString();
	}
}
