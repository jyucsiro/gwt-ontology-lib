package au.csiro.eis.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UiEventHandler extends EventHandler {
	
	public void onUiEvent(UiEvent event);

}
