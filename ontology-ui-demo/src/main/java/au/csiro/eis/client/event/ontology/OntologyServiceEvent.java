package au.csiro.eis.client.event.ontology;

import com.google.gwt.event.shared.GwtEvent;

public class OntologyServiceEvent extends GwtEvent<OntologyServiceEventHandler> {
	private static GwtEvent.Type<OntologyServiceEventHandler> TYPE = new GwtEvent.Type<OntologyServiceEventHandler>();


	
	public OntologyServiceEvent() {

	}

	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OntologyServiceEventHandler> getAssociatedType() {
		return getType();		  
	}

	public static GwtEvent.Type<OntologyServiceEventHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OntologyServiceEventHandler handler) {
		/* Call the specific handler method to inform listeners that the event is happening */
		handler.onOntologyServiceEvent(this);
	}


	



}
