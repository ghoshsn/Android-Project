/*****************************************************************************
 * Name..........: MediacatalogMain.java
 *Description...: Main activity for Mediacatalog app
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import com.iit.sghosh.mediacatalog.R;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * Class for main activity for My Media Catalog
 */
public class MediacatalogMain extends ListActivity implements Runnable {

	/****************
	 * CLASS FIELDS *
	 ****************/
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_IMPORT = 3;
	private static final int ACTIVITY_ABOUT = 4;

	private static final int INSERT_ID = Menu.FIRST;
	private MenuItem insertMenuItem;
	private static final int EDIT_ID = Menu.FIRST + 1;
	private MenuItem editMenuItem;
	private static final int DELETE_ID = Menu.FIRST + 2;
	private MenuItem deleteMenuItem;
	private static final int CATEGORY_ID = Menu.FIRST + 3;
	private MenuItem categoryMenuItem;
	private static final int IMPORT_ID = Menu.FIRST + 4;
	private MenuItem importMenuItem;
	private static final int EXPORT_ID = Menu.FIRST + 5;
	private MenuItem exportMenuItem;
	private static final int PREFERENCES_ID = Menu.FIRST + 6;
	private MenuItem preferencesMenuItem;
	private static final int ABOUT_ID = Menu.FIRST + 7;
	private MenuItem aboutMenuItem;
	private static final int CONTEXT_EDIT_ID = Menu.FIRST + 8;
	private static final int CONTEXT_DELETE_ID = Menu.FIRST + 9;
	private MediacatalogDataBaseSetup mDbHelper;
	private String filterExpr = "";
	private long videoId = 0;
	private String _Type;
	private Context _context = null;
	private TextView mCount = null;
	ProgressDialog pd = null;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle(R.string.title_video);
		mDbHelper = new MediacatalogDataBaseSetup(this);
		mDbHelper.open();
		registerForContextMenu(getListView());

		/**
		 * Add onClick event to filter button. Android offered a built-in method
		 * of filtering a list, but I wanted the filter visible all the time so
		 * I added it to the top
		 */
		Button filterButton = (Button) findViewById(R.id.filter_button);
		filterButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText filter = (EditText) findViewById(R.id.filter_text);
				filterExpr = filter.getText().toString();
				fillData();
			}
		});

		/**
		 * In order to allow operations from the keyboard, we needed to set the
		 * video Id when it was selected. When nothing is selected, we need to
		 * clear the selection as well
		 */
		getListView().setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v,
					int possition, long id) {
				videoId = id;
				if (editMenuItem != null) {
					editMenuItem.setEnabled(true);
				}
				if (deleteMenuItem != null) {
					deleteMenuItem.setEnabled(true);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(-1);
				if (editMenuItem != null) {
					editMenuItem.setEnabled(false);
				}
				if (deleteMenuItem != null) {
					deleteMenuItem.setEnabled(false);
				}
			}
		});
		mCount = (TextView) findViewById(R.id.total);
		_context = this;
		fillData();
	}

	/**
	 * This method fetches the data from the database adapter applying a filter
	 * if necessary. Then it binds the data to the ListAdapter using our
	 * extended CursorAdapter called Media Catalog PanelAdapter.
	 */
	private void fillData() {

		// Get type
		Bundle extras = this.getIntent().getExtras();
		_Type = (extras != null) ? (extras
				.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE)) ? extras
				.getString(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE) : "'DVD','Blu-Ray','MP4','AIFF','WAV','AVI','FLASH','QUICKTIME','VHS','DIVX','MKV','OGG'"
				: "'DVD','Blu-Ray','MP4','AIFF','WAV','AVI','FLASH','QUICKTIME','VHS','DIVX','MKV','OGG'";
		// Get all of the rows from the database and create the item list
		Cursor mVideoCursor;
		if (filterExpr.length() <= 0) {
			mVideoCursor = mDbHelper.fetchAllMEDIA(_Type);
		} else {
			mVideoCursor = mDbHelper.fetchAllMEDIAByTitle(filterExpr, _Type);
		}

		startManagingCursor(mVideoCursor);

		// Now create a simple cursor adapter and set it to display

		MediacatalogScreenSetup adapter = new MediacatalogScreenSetup(this,
				mVideoCursor);
		setListAdapter(adapter);

		// Get count
		mCount.setText(String.format("Total: %d",
				mDbHelper.fetchItemCountByType(_Type)));
	}

	/**
	 * Creates the context menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(0, CONTEXT_EDIT_ID, 0, R.string.menu_edit);
		menu.add(0, CONTEXT_DELETE_ID, 0, R.string.menu_remove);

		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			android.util.Log.e("ItemId Retreival", "bad menuInfo", e);
			return;
		}
		videoId = getListAdapter().getItemId(info.position);
		menu.setHeaderTitle(mDbHelper.fetchMEDIATitleByMEDIAId(videoId));
	}// End of method public void onCreateContextMenu(ContextMenu, view,
		// ContextMenuInfo)

	/**
	 * Handles the context menu item selection
	 */
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CONTEXT_EDIT_ID:
			editVideo();
			return true;
		case CONTEXT_DELETE_ID:
			deleteVideo();
			return true;
		}
		return super.onContextItemSelected(item);
	}// End of method public boolean onContextItemSelected(MenuItem)

	/**
	 * Sets up the menus and adds a nice menu icon.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		insertMenuItem = menu.add(0, INSERT_ID, 0, R.string.menu_add);
		insertMenuItem.setIcon(android.R.drawable.ic_menu_add);
		editMenuItem = menu.add(0, EDIT_ID, 0, R.string.menu_edit);
		editMenuItem.setIcon(android.R.drawable.ic_menu_edit);
		deleteMenuItem = menu.add(0, DELETE_ID, 0, R.string.menu_remove);
		deleteMenuItem.setIcon(android.R.drawable.ic_menu_delete);
		categoryMenuItem = menu
				.add(0, CATEGORY_ID, 0, R.string.menu_categories);
		categoryMenuItem.setIcon(android.R.drawable.ic_menu_manage);
		importMenuItem = menu.add(0, IMPORT_ID, 0, R.string.menu_import);
		importMenuItem.setIcon(android.R.drawable.ic_menu_gallery);
		exportMenuItem = menu.add(0, EXPORT_ID, 0, R.string.menu_export);
		exportMenuItem.setIcon(android.R.drawable.ic_menu_share);
		preferencesMenuItem = menu.add(0, PREFERENCES_ID, 0,
				R.string.menu_preferences);
		preferencesMenuItem.setIcon(android.R.drawable.ic_menu_preferences);
		aboutMenuItem = menu.add(0, ABOUT_ID, 0, R.string.menu_about);
		aboutMenuItem.setIcon(android.R.drawable.ic_menu_help);
		return true;
	}

	/**
	 * Menu event handler. We need to launch a new media, edit an existing
	 * media, delete a media, or launch the media import action
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createVideo();
			return true;
		case EDIT_ID:
			editVideo();
			return true;
		case DELETE_ID:
			deleteVideo();
			return true;
		case CATEGORY_ID:
			showCategoriesList();
			return true;
		case IMPORT_ID:
			showImport();
			return true;
		case EXPORT_ID:
			runExport();
			return true;
		case PREFERENCES_ID:
			showPreferences();
			return true;
		case ABOUT_ID:
			showAboutDialog();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * Shows the preferences dialog
	 */
	private void showPreferences() {
		Intent i = new Intent(this, MediacatalogPreferences.class);
		startActivityForResult(i, 0);
	}// End of method private void showPreferences()

	/**
	 * Shows the activity for a category listing
	 */
	private void showCategoriesList() {
		Intent i = new Intent(this, MediacatalogCategories.class);
		startActivityForResult(i, 0);
	}// End of method private void showCategoriesList()

	/**
	 * Shows a dialog with the about text
	 */
	private void showAboutDialog() {
		Intent i = new Intent(this, MediacatalogAbout.class);
		startActivityForResult(i, 0);
	}// End of method private void showAboutDialog()


	/**
	 * Runs the export routine in the background
	 */
	private void runExport() {
		pd = ProgressDialog.show(this, "Exporting...",
				"Please wait while your data is exported", true, false);
		Thread td = new Thread(this);
		td.start();
	}// End of method private void runExport()

	/**
	 * Launches the AndroidFileBrowser activity
	 */
	private void showImport() {
		Intent i = new Intent(this, MediacatalogFileBrowser.class);
		startActivityForResult(i, ACTIVITY_IMPORT);
	}// End of method private void showImport()

	/**
	 * Launches the Edit Video activity with NO parameters
	 */
	private void createVideo() {
		Intent i = new Intent(this, MediacatalogEditMedia.class);
		i.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, _Type);
		startActivityForResult(i, ACTIVITY_CREATE);
	}// End of method private void createVideo()

	/**
	 * Launches the EditMenu activity and passes in the selected  Video Id
	 */
	private void editVideo() {
		Intent i = new Intent(this, MediacatalogEditMedia.class);
		i.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_ID, videoId);
		i.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, _Type);
		startActivityForResult(i, ACTIVITY_EDIT);
	}// End of method private void createVideo()

	

	/**
	 * Deletes the selected video from the database
	 */
	private void deleteVideo() {
		MediacatalogNotificationHelper.showYesNoAlert(this, "Confirm Deletion",
				"Are you sure you want to delete this item?",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDbHelper.deleteMEDIA(videoId);
						fillData();
					}

				}, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				});

	}// End of method private void deletevideo()

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		videoId = id;
		editVideo();
	}

	@Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		// Check that the filter textview doesn't have focus. The delete
		// event for the TextView was being picked up here and deleting
		// videos when you just wanted to delete your filter
		EditText filter = (EditText) findViewById(R.id.filter_text);
		if (!filter.hasFocus()) {
			if (keyCode == KeyEvent.KEYCODE_DEL) {
				deleteVideo();
			}
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				editVideo();
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case ACTIVITY_ABOUT:
			break;
		case ACTIVITY_CREATE:
		case ACTIVITY_EDIT:
		case ACTIVITY_IMPORT:
			fillData();
			break;

		}
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * 
	 * @param context
	 *            The application's environment.
	 * @param action
	 *            The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	@Override
	public void run() {
		try {
			File f = new File("/sdcard/mediacatalog");
			if (!f.exists()) {
				f.mkdir();
			}
			FileWriter fs = new FileWriter("/sdcard/mediacatalog/export.csv",
					false);
			PrintWriter out = new PrintWriter(fs);
			MediacatalogDataBaseSetup adapter = new MediacatalogDataBaseSetup(_context);
			adapter.open();
			Cursor c = adapter.fetchAllMEDIA();

			while (c.moveToNext()) {
				String type = c
						.getString(
								c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))
						.trim().toUpperCase();
				String rating = "";
				if (type.equals("GAME") || type.equals("VIDEO")) {
					rating = c.getString(c
							.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_RATING));
				} else if (type.equals("AUDIO")) {
					rating = c.getString(c
							.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST));
				} else if (type.equals("BOOK")) {
					rating = c.getString(c
							.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR));
				}
				String lineFormat = "%s,%s,%s,%s,%s,%s,%s,%s,%s";
				out.println(String.format(
						lineFormat,
						'"' + c.getString(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE)) + '"',
						'"' + rating + '"',
						'"' + c.getString(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_CATEGORY_NAME)) + '"',
						'"' + c.getString(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS)) + '"',
						'"' + c.getString(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE)) + '"',
						'"' + c.getString(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE)) + '"',
						'"' + c.getInt(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_RANK)) + '"',
						'"' + c.getString(c
								.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR)) + '"'));
			}
			c.close();
			out.close();

			adapter.close();
			handler.sendEmptyMessage(0);
		} catch (Exception ex) {
			handler.sendEmptyMessage(1);
		}

	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			MediacatalogNotificationHelper
					.showAlert(_context, "Done!",
							"Your data was exported to /sdcard/mediacatalog/export.csv");
		}
	};
}  //end of class MediacatalogMain
