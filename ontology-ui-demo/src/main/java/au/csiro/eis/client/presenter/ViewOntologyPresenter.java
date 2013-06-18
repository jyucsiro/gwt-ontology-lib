package au.csiro.eis.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import au.csiro.eis.client.event.UiEvent;
import au.csiro.eis.client.event.ontology.OntologyServiceEvent;
import au.csiro.eis.client.event.ontology.OntologyServiceEventHandler;
import au.csiro.eis.client.event.ontology.OntologyServiceExceptionEvent;
import au.csiro.eis.client.event.ontology.OntologyServiceGetOntologyEvent;
import au.csiro.eis.client.event.ontology.OntologyUIEvent;
import au.csiro.eis.client.view.View;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;


public class ViewOntologyPresenter implements OntologyPresenter, OntologyServiceEventHandler {
	View<OwlOntologyBean> view;
	HandlerManager eventBus;
	OntologyQueryServiceAsync svc;
	OwlOntologyBean bean;
	HasWidgets container;
	private boolean hasInit;
	boolean isActive;
	
	public ViewOntologyPresenter(OntologyQueryServiceAsync svc, HandlerManager eventBus, 
										View<OwlOntologyBean> view, HasWidgets container) {
		this.svc = svc;
		this.eventBus = eventBus;
		this.container = container;
		this.view = view;
		
		//hook up the view to the UI events 
		eventBus.addHandler(UiEvent.getType(), this.view);
		
		eventBus.addHandler(OntologyServiceEvent.getType(), this);
		
	}
	
	public void setView(View<OwlOntologyBean> view) {
		this.view = view;
	}

	public View<OwlOntologyBean> getView() {
		return this.view;		
	}

	public void setOntologyBean(OwlOntologyBean bean) {
		this.bean = bean;		
	}

	public OwlOntologyBean getOntologyBean() {
		return this.bean;
	}

	public void loadOntologyBean() {
		AsyncCallback<OwlOntologyBean> callback = new AsyncCallback<OwlOntologyBean>() {

			public void onFailure(Throwable caught) {
				System.out.println("getOntologyBean - caught exception");

	            eventBus.fireEvent(new OntologyServiceExceptionEvent(caught));				
			}

			public void onSuccess(OwlOntologyBean result) {
				System.out.println("getOntologyBean - success: firing OntologyServiceGetOntologyEvent");

				eventBus.fireEvent(new OntologyServiceGetOntologyEvent(result));				

			}			
		};

		
		try {
			System.out.println("Calling ontology service with callback");

			svc.getOntologyBean(callback);
		} catch (OntologyInitException e) {
            eventBus.fireEvent(new OntologyServiceExceptionEvent(e));
			e.printStackTrace();
		}
				
	}

	public void onOntologyServiceEvent(OntologyServiceEvent event) {
		System.out.println("in ViewOntologyPresenter: onOntologyServiceEvent");

		if(event instanceof OntologyServiceExceptionEvent) {
			System.out.println("in ViewOntologyPresenter: OntologyServiceExceptionEvent");

		}
		else if(event instanceof OntologyServiceGetOntologyEvent) {
			System.out.println("in ViewOntologyPresenter: OntologyServiceGetOntologyEvent");

			OntologyServiceGetOntologyEvent ontEvent = (OntologyServiceGetOntologyEvent) event;
			//this.bean = ontEvent.getOntology();
			System.out.println("in ViewOntologyPresenter: setting ontologyBean");

			this.setOntologyBean(ontEvent.getOntology());
			//update view
			System.out.println("in ViewOntologyPresenter: view.update()");
			this.view.update();
		}
			
		
	}

	public void viewUpdateComplete() {
		eventBus.fireEvent(new OntologyUIEvent(OntologyUIEvent.Predefined.UPDATE));
	
	}

	public void viewInitComplete() {
		eventBus.fireEvent(new OntologyUIEvent(OntologyUIEvent.Predefined.INIT));
	}
	
	public void viewDisplayComplete() {
		eventBus.fireEvent(new OntologyUIEvent(OntologyUIEvent.Predefined.DISPLAY));
		
	}


	public Object getModel() {
		return bean;
	}

	public void load() {
		System.out.println("ViewOntologyPresenter loading...");
		//load the view with the list
		
		if(!hasInit) {
			System.out.println("loading loadOntologyBean()...");
			this.loadOntologyBean();

			//ensure view has the presenter set
			if(this.view.getPresenter() == null) {
				this.view.setPresenter(this);
				
			}
			System.out.println("initialising view in container");

			this.view.init(this.getContainer());
			this.hasInit = true;
			
		}
		else {
			this.view.update();
		}

		this.setActive(true);
	}

	public void display(HasWidgets container) {
		this.setContainer(container);
		this.view.display(container);
		this.setActive(true);
	}

	public HasWidgets getContainer() {
		return container;
	}

	public void setContainer(HasWidgets container) {
		this.container = container;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return isActive;
	}

	public void destroy() {
		this.view = null;
		
		
	}


	
	
	
	
}
