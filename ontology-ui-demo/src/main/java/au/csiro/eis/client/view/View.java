package au.csiro.eis.client.view;

import java.util.List;

import au.csiro.eis.client.event.UiEventHandler;
import au.csiro.eis.client.presenter.Presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;


public interface View<T> extends UiEventHandler, IsWidget {

	public Presenter<T> getPresenter();
	public void setPresenter(Presenter<T> presenter);
	
	//three states: init (initialise), display (after init, display in a container), update(update status quo)

	public void init(HasWidgets container);
	public void display(HasWidgets container);
	public void update();
	public void reload();

}
