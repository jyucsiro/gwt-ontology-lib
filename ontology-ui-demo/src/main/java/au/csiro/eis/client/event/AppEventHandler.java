package au.csiro.eis.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AppEventHandler extends EventHandler {
	public void onAppEvent(AppEvent event);

}
