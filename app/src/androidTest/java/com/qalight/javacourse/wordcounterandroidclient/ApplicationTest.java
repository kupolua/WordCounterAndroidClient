package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Application;
import android.test.ApplicationTestCase;

public class ApplicationTest extends ApplicationTestCase<Application> {

	private Application mApplication;

    public ApplicationTest() {
        super(Application.class);
    }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createApplication();

	} // end of setUp() method definition

}