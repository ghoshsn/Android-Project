/*****************************************************************************
 * Name..........: MediacatalogFileBrowser.java
 * Description...: Activity for file browsing
 *****************************************************************************/

package com.iit.sghosh.mediacatalog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MediacatalogFileBrowser extends ListActivity implements Runnable {

	private enum DISPLAYMODE {
		ABSOLUTE, RELATIVE;
	}

	private final DISPLAYMODE displayMode = DISPLAYMODE.ABSOLUTE;
	private final int TITLE_INDEX = 0;
	private final int RATING_INDEX = 1;
	private final int CATEGORY_INDEX = 2;
	private final int STATUS_INDEX = 3;
	private final int TYPE_INDEX = 4;
	private final int SUBTYPE_INDEX = 5;
	private final int RANK_INDEX = 6;
	private final int ARTIST_INDEX = 1;
	private final int AUTHOR_INDEX = 1;
	private final int YEAR_INDEX = 8;
	private List<String> directoryEntries = new ArrayList<String>();
	private File currentDirectory = new File("/");
	private Context _context;
	ProgressDialog pd;
	private File _selectedFile = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTitle(R.string.title_import_file);
		_context = this.getApplicationContext();
		browseToRoot();
	}

	/**
	 * This function browses to the root-directory of the file-system.
	 */
	private void browseToRoot() {
		browseTo(new File("/"));
	}

	/**
	 * This function browses up one level according to the field:
	 * currentDirectory
	 */
	private void upOneLevel() {
		if (this.currentDirectory.getParent() != null)
			this.browseTo(this.currentDirectory.getParentFile());
	}

	private void browseTo(final File aDirectory) {
		if (aDirectory.isDirectory()) {
			this.currentDirectory = aDirectory;
			fill(aDirectory.listFiles());
		}
	}

	/**
	 * Fills the array to pass off to the ArrayAdapter
	 * 
	 * @param files
	 *            A list of files to display in a ListView
	 */
	private void fill(File[] files) {
		this.directoryEntries.clear();

		// Add the "." and the ".." == 'Up one level'
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}
		this.directoryEntries.add(".");

		if (this.currentDirectory.getParent() != null)
			this.directoryEntries.add("..");

		switch (this.displayMode) {
		case ABSOLUTE:
			for (File file : files) {
				this.directoryEntries.add(file.getPath());
			}
			break;
		case RELATIVE: // On relative Mode, we have to add the current-path to
			// the beginning
			int currentPathStringLenght = this.currentDirectory
					.getAbsolutePath().length();
			for (File file : files) {
				this.directoryEntries.add(file.getAbsolutePath().substring(
						currentPathStringLenght));
			}
			break;
		}

		ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
				R.layout.file_row, this.directoryEntries);

		this.setListAdapter(directoryList);
	}

	/**
	 * 
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int selectionRowID = (int) this.getSelectedItemId();
		String selectedFileString = this.directoryEntries.get(selectionRowID);
		if (selectedFileString.equals(".")) {
			// Refresh
			this.browseTo(this.currentDirectory);
		} else if (selectedFileString.equals("..")) {
			this.upOneLevel();
		} else {
			File clickedFile = null;
			switch (this.displayMode) {
			case RELATIVE:
				clickedFile = new File(this.currentDirectory.getAbsolutePath()
						+ this.directoryEntries.get(selectionRowID));
				break;
			case ABSOLUTE:
				clickedFile = new File(this.directoryEntries
						.get(selectionRowID));
				break;
			}
			if (clickedFile != null)
				if(clickedFile.isDirectory())
				{
					this.browseTo(clickedFile);
				} else {
					startImport(clickedFile);
				}
			
		}
	}//End of onItemClick

	private void startImport(File file) {
		pd = ProgressDialog.show(this, "Importing...",
				"Please wait while we load the video", true, false);
		Thread td = new Thread(this);
		_selectedFile = file;
		td.start();
	}

	/**
	 * This method parses data from the selected CSV file and inserts the data
	 * into the video table.
	 * 
	 * @param filePath The path to the CSV file
	 */
	private void ImportData(File filePath) {
		FileReader fis = null;
		BufferedReader br = null;
		MediacatalogDataBaseSetup dbAdapter = null;
		try {
			dbAdapter = new MediacatalogDataBaseSetup(_context);
			dbAdapter.open();
			
			MediacatalogCsvReader reader = new MediacatalogCsvReader(filePath.toString(), ',');
			reader.setTextQualifier('"');
			while (reader.readRecord()) {
				
				if (reader.getColumnCount() >= 8) {
					try {
						String type = (reader.get(TYPE_INDEX) != null)?reader.get(TYPE_INDEX).trim().toUpperCase():"";
						if(type.equals("VIDEO") || type.equals("GAME")) {
							dbAdapter.createMEDIA(
								reader.get(TITLE_INDEX)
								,reader.get(RATING_INDEX)
								,dbAdapter.fetchCategoryIdByNameType(reader.get(CATEGORY_INDEX), reader.get(TYPE_INDEX))
								,reader.get(STATUS_INDEX)
								,reader.get(TYPE_INDEX)
								,reader.get(SUBTYPE_INDEX)
								,Integer.parseInt(reader.get(RANK_INDEX))
								,null
								,null
								,null
								,null
								,(reader.getColumnCount() > 8)?reader.get(YEAR_INDEX):""
								,""
							);
						} else if(type.equals("AUDIO")) {
							dbAdapter.createMEDIA(
								reader.get(TITLE_INDEX)
								,reader.get(RATING_INDEX)
								,dbAdapter.fetchCategoryIdByNameType(reader.get(CATEGORY_INDEX), reader.get(TYPE_INDEX))
								,reader.get(STATUS_INDEX)
								,reader.get(TYPE_INDEX)
								,reader.get(SUBTYPE_INDEX)
								,Integer.parseInt(reader.get(RANK_INDEX))
								,null
								,null
								,reader.get(ARTIST_INDEX)
								,null
								,(reader.getColumnCount() > 8)?reader.get(YEAR_INDEX):""
								,""
							);
						} else if(type.equals("BOOK")) {
							dbAdapter.createMEDIA(
								reader.get(TITLE_INDEX)
								,reader.get(RATING_INDEX)
								,dbAdapter.fetchCategoryIdByNameType(reader.get(CATEGORY_INDEX), reader.get(TYPE_INDEX))
								,reader.get(STATUS_INDEX)
								,reader.get(TYPE_INDEX)
								,reader.get(SUBTYPE_INDEX)
								,Integer.parseInt(reader.get(RANK_INDEX))
								,null
								,null
								,null
								,reader.get(AUTHOR_INDEX)
								,(reader.getColumnCount() > 8)?reader.get(YEAR_INDEX):""
								,""
							);
						}
					} catch(Exception ex) {
						android.util.Log.v("IMPORT ERROR", ex.getMessage());
					}
				}
			}
		} catch (IOException ioe) {
			// DO NOTHING
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (dbAdapter != null) {
					dbAdapter.close();
				}
			} catch (IOException ex) {
				// DO NOTHING
			}
		}
	}// End if method ImportData(File)

	public void run() {
		ImportData(_selectedFile);
		handler.sendEmptyMessage(0);
	}// End of method private void run()

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
			setResult(RESULT_OK);
			finish();
		}
	};

}// End of class AndroidFileBrowser
