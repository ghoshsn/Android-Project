/*****************************************************************************
 * Name..........: MediacatalogMedia.java
 * Description...: Activity for Media
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import java.util.Locale;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.view.Menu;
import android.view.MenuItem;




public class MediacatalogMedia extends TabActivity {

	private Intent videoIntent = null;
	private Intent audioIntent = null;
	private Intent gameIntent = null;
	private Intent bookIntent = null;
	private static final String KEY_VIDEO = "Video";
	private static final String KEY_GAME = "Game";
	private static final String KEY_BOOK = "Book";
	private static final String KEY_AUDIO = "Audio";

	ProgressDialog pd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(
				MediacatalogPreferences.PREFS_NAME, 0);
		TabHost host = getTabHost();
		Resources res = getResources(); 
		host.clearAllTabs();
		host.clearFocus();

		createTabs();
	}

	protected void createTabs() {

		SharedPreferences settings = getSharedPreferences(
				MediacatalogPreferences.PREFS_NAME, 0);
		int index = 0;
		int bookIndex = 0;
		int audioIndex = 0;
		int gameIndex = 0;
		int videoIndex = 0;
		String defaultTab = "";
		TabHost host = getTabHost();
		Resources res = getResources(); // Resource object to get Drawables


		if (settings.getBoolean(MediacatalogPreferences.KEY_SHOW_BOOK_TAB, true)) {
			bookIntent = new Intent(this, MediacatalogMain.class);
			bookIntent.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, KEY_BOOK);
			host.addTab(host.newTabSpec("one").setIndicator("Books",res.getDrawable(R.drawable.book))
					.setContent(bookIntent));
			bookIndex = index;
			index++;
		}
		if (settings.getBoolean(MediacatalogPreferences.KEY_SHOW_AUDIO_TAB, true)) {
			audioIntent = new Intent(this, MediacatalogMain.class);
			audioIntent.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, KEY_AUDIO);
			host.addTab(host.newTabSpec("two").setIndicator("Audio",res.getDrawable(R.drawable.violin))
					.setContent(audioIntent));
			audioIndex = index;
			index++;
		}
		if (settings.getBoolean(MediacatalogPreferences.KEY_SHOW_GAME_TAB, true)) {
			gameIntent = new Intent(this, MediacatalogMain.class);
			gameIntent.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, KEY_GAME);
			host.addTab(host.newTabSpec("three").setIndicator("Game",res.getDrawable(R.drawable.controller))
					.setContent(gameIntent));
			gameIndex = index;
			index++;
		}
		if (settings.getBoolean(MediacatalogPreferences.KEY_SHOW_VIDEO_TAB, true)) {
			videoIntent = new Intent(this, MediacatalogMain.class);
			videoIntent.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, KEY_VIDEO);
			host.addTab(host.newTabSpec("four").setIndicator("Video",res.getDrawable(R.drawable.video))
					.setContent(videoIntent));
			videoIndex = index;
			index++;
		}
		defaultTab = settings.getString(MediacatalogPreferences.KEY_DEFAULT_TAB, "")
				.toUpperCase();
		if (defaultTab.equals("BOOK")) {
			index = bookIndex;
		} else if (defaultTab.equals("AUDIO")) {
			index = audioIndex;
		} else if (defaultTab.equals("GAME")) {
			index = gameIndex;
		} else if (defaultTab.equals("VIDEO")) {
			index = videoIndex;
		} 
	else {
			index = 0;
		}

		host.setCurrentTab(index);
		setDefaultTab(index);

	}

}   // end of class MediacatalogMedia
