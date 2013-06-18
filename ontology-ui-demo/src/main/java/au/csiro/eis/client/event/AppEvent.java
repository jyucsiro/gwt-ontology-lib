package au.csiro.eis.client.event;


import com.google.gwt.event.shared.GwtEvent;

public class AppEvent extends GwtEvent<AppEventHandler> {
	private static GwtEvent.Type<AppEventHandler> TYPE = new GwtEvent.Type<AppEventHandler>();

	private String eventType;
	
	private Object data;
	
	
	public AppEvent(String eventType, Object data) {
		this.eventType = eventType;
		this.data = data;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AppEventHandler> getAssociatedType() {
		return getType();		  
	}

	public static GwtEvent.Type<AppEventHandler> getType() {
		return TYPE;
	}


	@Override
	protected void dispatch(AppEventHandler handler) {
		/* Call the specific handler method to inform listeners that the event is happening */
		handler.onAppEvent(this);
	}


	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	

}
