package au.csiro.eis.client.event.ontology;

import au.csiro.eis.client.event.UiEvent;


public class OntologyUIEvent extends UiEvent {
	String eventType;
	
	 public class Predefined {
		public final static String UPDATE = "update";
		public final static String INIT = "INIT";
		public final static String DISPLAY = "DISPLAY";


	}
	
	
	public OntologyUIEvent(String eventType) {
		this.eventType = eventType;
	}


	public String getEventType() {
		return eventType;
	}


	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	
	
}
