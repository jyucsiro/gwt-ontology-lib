package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryService;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.rpc.shared.ConfigBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class GwtTestOntologySvc extends GWTTestCase {
	OntologyQueryServiceAsync service;
	public static final String CHAFFEY_DAM_DOMAIN = "Chaffey dam";
	public static final String CHAFFEY_DAM_CONFIG = "chaffey_domain_ontology_config.json";
	public static final String CHAFFEY_DAM_USER = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/user/jonathanyu";

	public static final String SEWER_RISING_MAINS_DOMAIN = "Sewer rising mains";
	public static final String SEWER_RISING_MAINS_CONFIG = "uwda_domain_ontology_config.json";
	public static final String SEWER_RISING_MAINS_USER = "http://waterinformatics1-cdc.it.csiro.au/resource/event-detection/user/jonathanyu-uwda";
	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "au.csiro.eis.ontology.gwt.rpc.ontologyServiceJUnit";
	}


	/**
	 * This test will send a request to the server using the greetServer method in
	 * GreetingService and verify the response.
	 */
	public void testOntologyService() {
		// Create the service that we will test.
		service = GWT.create(OntologyQueryService.class);
		ServiceDefTarget target = (ServiceDefTarget) service;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "app/ontologyQuery");

		// Since RPC calls are asynchronous, we will need to wait for a response
		// after this test method returns. This line tells the test runner to wait
		// up to 10 seconds before timing out.
		delayTestFinish(100000);



		// Send a request to the server.

		initialiseSvc();


	}

	public void initialiseSvc() {
		//initialise service
		ConfigBean config = new ConfigBean(CHAFFEY_DAM_USER, CHAFFEY_DAM_CONFIG);

		service.initialiseSvc(config, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(Boolean result) {
				if(result == true) {
					try {
						sendRequestToSvc();
					} catch (OntologyInitException e) {
						// TODO Auto-generated catch block
						fail("Request failure: " + e.getMessage());
					}
				}
			}
		});
	}

	public void sendRequestToSvc() throws OntologyInitException {
		service.getOntologyBean(new AsyncCallback<OwlOntologyBean>() {
			public void onFailure(Throwable caught) {
				// The request resulted in an unexpected error.
				fail("Request failure: " + caught.getMessage());
			}

			public void onSuccess(OwlOntologyBean result) {
				// Verify that the response is correct.
				assertTrue(result != null);

				// Now that we have received a response, we need to tell the test runner
				// that the test is complete. You must call finishTest() after an
				// asynchronous test finishes successfully, or the test will time out.
				finishTest();				
			}
		});
	}

}
