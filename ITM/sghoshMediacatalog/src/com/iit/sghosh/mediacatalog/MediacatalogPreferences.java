/*****************************************************************************
 * Name..........: MediacatalogPreferences.java
 * Description...: Manages application preferences
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import com.iit.sghosh.mediacatalog.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class MediacatalogPreferences extends Activity {

	private CheckBox mShowBook = null;
	private CheckBox mShowAudio = null;
	private CheckBox mShowGame = null;
	private CheckBox mShowVideo = null;
	private Spinner mDefaultTab = null;
	String _book = "";
	String _audio = "";
	String _game = "";
	String _video = "";
	String _defaultTab = "";
	public static final String KEY_SHOW_BOOK_TAB = "KEY_SHOW_BOOK_TAB";
	public static final String KEY_SHOW_AUDIO_TAB = "KEY_SHOW_Audio_TAB";
	public static final String KEY_SHOW_GAME_TAB = "KEY_SHOW_GAME_TAB";
	public static final String KEY_SHOW_VIDEO_TAB = "KEY_SHOW_VIDEO_TAB";
	public static final String KEY_DEFAULT_TAB = "KEY_DEFAULT_TAB";
	public static final String PREFS_NAME = "MediacatalogPrefsFile";
	private static final int MENU_SAVE_ID = Menu.FIRST;
	private MenuItem saveMenu = null;
	private static final int MENU_CANCEL_ID = Menu.FIRST + 1;
	private MenuItem cancelMenu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup activity
		setContentView(R.layout.preferences);
		setTitle(R.string.title_preferences);

		// Setup CheckBox widgets
		mShowBook = (CheckBox) findViewById(R.id.show_book);
		mShowAudio = (CheckBox) findViewById(R.id.show_audio);
		mShowGame = (CheckBox) findViewById(R.id.show_game);
		mShowVideo = (CheckBox) findViewById(R.id.show_video);


		// Setup default tab Spinner
		mDefaultTab = (Spinner) findViewById(R.id.default_tab);
		ArrayAdapter<CharSequence> defaultTabAdapter = ArrayAdapter
				.createFromResource(this, R.array.tabs,
						android.R.layout.simple_spinner_item);
		defaultTabAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDefaultTab.setAdapter(defaultTabAdapter);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			_book = (extras.containsKey(KEY_SHOW_BOOK_TAB)) ? extras
					.getString(KEY_SHOW_BOOK_TAB) : "";
			_audio = (extras.containsKey(KEY_SHOW_AUDIO_TAB)) ? extras
					.getString(KEY_SHOW_AUDIO_TAB) : "";
			_game = (extras.containsKey(KEY_SHOW_GAME_TAB)) ? extras
					.getString(KEY_SHOW_GAME_TAB) : "";
			_video = (extras.containsKey(KEY_SHOW_VIDEO_TAB)) ? extras
					.getString(KEY_SHOW_VIDEO_TAB) : "";
			_defaultTab = (extras.containsKey(KEY_DEFAULT_TAB)) ? extras
					.getString(KEY_DEFAULT_TAB) : "";
		}

		// Populate the activity with data from preferences
		fillData();

	}// End of method protected void onCreate(Bundle)

	/**
	 * Populates the widgets with their values from the preferences system.
	 */
	private void fillData() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		// Book
		if (_book.equals("")) {
			_book = (settings.getBoolean(KEY_SHOW_BOOK_TAB, true)) ? "TRUE"
					: "FALSE";
		}
		mShowBook
				.setChecked((_book.toUpperCase().equals("TRUE") ? true : false));
		// AUDIO
		if (_audio.equals("")) {
			_audio = (settings.getBoolean(KEY_SHOW_AUDIO_TAB, true)) ? "TRUE"
					: "FALSE";
		}
		mShowAudio.setChecked((_audio.toUpperCase().equals("TRUE") ? true
				: false));
		// Game
		if (_game.equals("")) {
			_game = (settings.getBoolean(KEY_SHOW_GAME_TAB, true)) ? "TRUE"
					: "FALSE";
		}
		mShowGame
				.setChecked((_game.toUpperCase().equals("TRUE") ? true : false));
		// Video
		if (_video.equals("")) {
			_video = (settings.getBoolean(KEY_SHOW_VIDEO_TAB, true)) ? "TRUE"
					: "FALSE";
		}
		mShowVideo.setChecked((_video.toUpperCase().equals("TRUE") ? true
				: false));

		if (_defaultTab.equals("")) {
			_defaultTab = settings.getString(KEY_DEFAULT_TAB, "BOOK")
					.toUpperCase();
		}
		int defaultIndex = 0;
		for (int i = 0; i < mDefaultTab.getCount(); i++) {
			if (mDefaultTab.getItemAtPosition(i).toString().toUpperCase()
					.equals(_defaultTab)) {
				defaultIndex = i;
				break;
			}
		}
		mDefaultTab.setSelection(defaultIndex);

	}// End of method private void fillData()

	@Override
	protected void onPause() {
		super.onPause();

		Intent editor = getIntent();

		_book = (mShowBook.isChecked()) ? "TRUE" : "FALSE";
		_audio = (mShowAudio.isChecked()) ? "TRUE" : "FALSE";
		_game = (mShowGame.isChecked()) ? "TRUE" : "FALSE";
		_video = (mShowVideo.isChecked()) ? "TRUE" : "FALSE";
		_defaultTab = mDefaultTab.getSelectedItem().toString().toUpperCase();
		editor.putExtra(KEY_SHOW_BOOK_TAB, _book);
		editor.putExtra(KEY_SHOW_AUDIO_TAB, _audio);
		editor.putExtra(KEY_SHOW_GAME_TAB, _game);
		editor.putExtra(KEY_SHOW_VIDEO_TAB, _video);
		editor.putExtra(KEY_DEFAULT_TAB, _defaultTab);

	}// End of method protected void onPause()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		saveMenu = menu.add(0, MENU_SAVE_ID, 0, R.string.menu_save);
		saveMenu.setIcon(android.R.drawable.ic_menu_save);
		cancelMenu = menu.add(0, MENU_CANCEL_ID, 0, R.string.menu_cancel);
		cancelMenu.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}// End of method public boolean onCreateOptionsMenu(Menu)

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE_ID:
			SavePreferences();
			return true;
		case MENU_CANCEL_ID:
			CancelAction();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}// End of method public boolean onMenuItemSelected(int, MenuItem)

	/**
	 * Finished the activity without saving changes
	 */
	private void CancelAction() {
		setResult(RESULT_CANCELED);
		finish();
	}// End of method private void CancelAction()

	/**
	 * Saves the preferences then exits the activity
	 */
	private void SavePreferences() {
		if (validate()) {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(KEY_SHOW_BOOK_TAB, mShowBook.isChecked());
			editor.putBoolean(KEY_SHOW_AUDIO_TAB, mShowAudio.isChecked());
			editor.putBoolean(KEY_SHOW_GAME_TAB, mShowGame.isChecked());
			editor.putBoolean(KEY_SHOW_VIDEO_TAB, mShowVideo.isChecked());
			editor.putString(KEY_DEFAULT_TAB, mDefaultTab.getSelectedItem()
					.toString());
			editor.commit();
			MediacatalogNotificationHelper
					.showOkAlert(
							this,
							"INFORMATION",
							"Your changes will not take effect until you close and re-open the application",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setResult(RESULT_OK);
									finish();
								}

							});
		}
	}// End of method private void SavePreferences

	/** Validates that the user does not disable a tab and set it to be the
	 * default
	 * @return *******/
	
	private boolean validate() {
		boolean valid = true;
		_book = (mShowBook.isChecked()) ? "TRUE" : "FALSE";
		_audio = (mShowAudio.isChecked()) ? "TRUE" : "FALSE";
		_game = (mShowGame.isChecked()) ? "TRUE" : "FALSE";
		_video = (mShowVideo.isChecked()) ? "TRUE" : "FALSE";
		_defaultTab = mDefaultTab.getSelectedItem().toString().toUpperCase();
		if (_defaultTab.equals("BOOK") && _book.equals("FALSE")) {
			valid = false;
			MediacatalogNotificationHelper
					.showOkAlert(
							this,
							"WRONG SELECTION !!!",
							"Please do not set the default tab to a hidden tab",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							});
		} else if (_defaultTab.equals("AUDIO") && _audio.equals("FALSE")) {
			valid = false;
			MediacatalogNotificationHelper
					.showOkAlert(
							this,
							"WRONG SELECTION !!!",
							"Please do not set the default tab to a hidden tab",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							});
		} else if (_defaultTab.equals("GAME") && _game.equals("FALSE")) {
			valid = false;
			MediacatalogNotificationHelper
					.showOkAlert(
							this,
							"WRONG SELECTION !!!",
							"Please do not set the default tab to a hidden tab",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							});
		} else if (_defaultTab.equals("VIDEO") && _video.equals("FALSE")) {
			valid = false;
			MediacatalogNotificationHelper
					.showOkAlert(
							this,
							"WRONG SELECTION !!!",
							"Please do not set the default tab to a hidden tab",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							});

		}
		return valid;
	}
}// End of class MediacatalogPreferences
