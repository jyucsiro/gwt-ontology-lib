package au.csiro.eis.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UiEvent extends GwtEvent<UiEventHandler>{
	private static GwtEvent.Type<UiEventHandler> TYPE = new 
			GwtEvent.Type<UiEventHandler>();

	@Override
	protected void dispatch(UiEventHandler handler) {
		handler.onUiEvent(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UiEventHandler> getAssociatedType() {
		return getType();
	}


	public static GwtEvent.Type<UiEventHandler> getType() {
		return TYPE;
	}


}
