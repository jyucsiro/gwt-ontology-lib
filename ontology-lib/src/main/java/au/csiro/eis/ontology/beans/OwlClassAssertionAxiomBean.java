package au.csiro.eis.ontology.beans;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OwlClassAssertionAxiomBean extends OwlAxiomBean implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	OwlClassBean cls;
	OwlIndividualBean indiv;
	public final String type = "ClassAssertion";

	public OwlClassAssertionAxiomBean() {
		
	}
	
	public OwlClassAssertionAxiomBean(OwlClassBean cls, OwlIndividualBean indiv) {
		super();

		this.cls = cls;
		this.indiv = indiv;

		this.createLabel();

	}

	
	private void createLabel() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(type+"(");
		if(this.cls!= null) {
			sb.append(this.cls.getName());
		}
		
		sb.append(", ");
		if(this.indiv != null) {
			sb.append(this.indiv.getName());
		}
		sb.append(")");
		
		
		this.setLabel(sb.toString());
	}
		

	public OwlClassBean getCls() {
		return cls;
	}

	public void setCls(OwlClassBean cls) {
		this.cls = cls;
	}

	public OwlIndividualBean getIndiv() {
		return indiv;
	}

	public void setIndiv(OwlIndividualBean indiv) {
		this.indiv = indiv;
	}
	
	
	
}
