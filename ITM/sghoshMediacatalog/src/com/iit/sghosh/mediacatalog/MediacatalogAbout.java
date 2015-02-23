/*****************************************************************************
 * Name..........: mediacatalogAbout.java
 * Description...: About page for mediacatalog App
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import android.app.Activity;
import android.os.Bundle;


public class MediacatalogAbout extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setTitle(R.string.title_about);

	}// End of method public void MediacatalogAbout

}// End of class About
