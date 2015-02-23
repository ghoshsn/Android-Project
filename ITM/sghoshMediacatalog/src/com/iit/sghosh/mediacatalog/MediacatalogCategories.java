/*****************************************************************************
 * Name..........: mediacatalogCategories.java
 * Description...: Main activity for the mediacatalog application
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import com.iit.sghosh.mediacatalog.R;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MediacatalogCategories extends ListActivity {

	/****************
	 * CLASS FIELDS *
	 ****************/
	MediacatalogDataBaseSetup mDbHelper;
	long categoryId = 0;
	String filterExpr = "";
	private static final int INSERT_ID = Menu.FIRST;
	private MenuItem insertMenuItem;
	private static final int EDIT_ID = Menu.FIRST + 1;
	private MenuItem editMenuItem;
	private static final int DELETE_ID = Menu.FIRST + 2;
	private MenuItem deleteMenuItem;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	/**Called on the first creation of the activity **/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);
		setTitle(R.string.title_categories);
		mDbHelper = new MediacatalogDataBaseSetup(this);
		mDbHelper.open();

		
		/** To allow keyboard operations, the videoId is set when it was selected. 
		 The selection is cleared when nothing is selected **/
		
		getListView().setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v,
					int possition, long id) {
				v.setSelected(true);
				categoryId = id;
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
	}// End of method public void onCreate(Bundle)



	/**Menu Setup**/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		insertMenuItem = menu.add(0, INSERT_ID, 0, R.string.menu_add);
		insertMenuItem.setIcon(android.R.drawable.ic_menu_add);
		editMenuItem = menu.add(0, EDIT_ID, 0, R.string.menu_edit);
		editMenuItem.setIcon(android.R.drawable.ic_menu_edit);
		deleteMenuItem = menu.add(0, DELETE_ID, 0, R.string.menu_remove);
		deleteMenuItem.setIcon(android.R.drawable.ic_menu_delete);
		return true;
	}// End of method public boolean onCreateOptionsMenu(Menu)

	/**Menu event handler for launching a new media, editing an existing
	 media, deleting a media, or launching the media import action*/
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createCategory();
			return true;
		case EDIT_ID:
			editCategory();
			return true;
		case DELETE_ID:
			deleteCategory();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}// End of method public boolean onMenuItemSelected(in, MenuItem)

	/**Deletes the category and confirms choice first*/
	
	private void deleteCategory() {
		MediacatalogNotificationHelper.showYesNoAlert(this, "Confirm Deletion",
				"Are you sure you want to delete this item?",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDbHelper.deleteCategory(categoryId);
					}

				}, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				});
	}// End of method private void deleteCategory()

	/**
	 * Launches the EditCategory activity passing the id
	 */
	private void editCategory() {
		Intent i = new Intent(this, MediacatalogEditCategory.class);
		i.putExtra(MediacatalogDataBaseSetup.KEY_CATEGORY_ID, categoryId);
		startActivityForResult(i, ACTIVITY_CREATE);
	}// End of method private void editCategory()

	/*** Launched the EditCategory activity*/
	
	private void createCategory() {
		Intent i = new Intent(this, MediacatalogEditCategory.class);
		startActivityForResult(i, ACTIVITY_EDIT);
	}// End of method private void createCategory()

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

	}// End of method protected void onActivityResult(int, int)

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		categoryId = id;
		editCategory();
	}

}// End of class MediacatalogCategories
