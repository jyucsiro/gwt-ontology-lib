package au.csiro.eis.client.event.ontology;

public class OntologyServiceExceptionEvent extends OntologyServiceEvent {
	Throwable exception;
	
	public OntologyServiceExceptionEvent(Throwable e) {
		this.exception = e;
	}

	public Throwable getException() {
		return exception;
	}

	
}
