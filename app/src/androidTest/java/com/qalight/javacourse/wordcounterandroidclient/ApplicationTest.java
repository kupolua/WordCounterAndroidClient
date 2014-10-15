package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
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