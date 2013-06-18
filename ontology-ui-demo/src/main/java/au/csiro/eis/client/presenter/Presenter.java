package au.csiro.eis.client.presenter;

import au.csiro.eis.client.view.View;

import com.google.gwt.user.client.ui.HasWidgets;


public interface Presenter<T> {

	public void setView(View<T> view);
	public View<T> getView();
	
	//three states: init (initialise), display (after init, display in a container), update(update status quo)
	
	public void viewUpdateComplete();
	public void viewInitComplete();
	public void viewDisplayComplete();

	public Object getModel();
	
	public void load();
	public void display(HasWidgets container);

	public void setActive(boolean isActive);
	public boolean isActive();
	public void destroy();
	
}
