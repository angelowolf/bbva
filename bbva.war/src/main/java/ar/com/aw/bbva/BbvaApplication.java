package ar.com.aw.bbva;

import ar.com.aw.bbva.rest.AbstractBbvaApplication;

public class BbvaApplication extends AbstractBbvaApplication  {

	public BbvaApplication() {
		super();
		packages("ar.com.aw.bbva.resource");
	}

}
