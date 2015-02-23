/*****************************************************************************
 * Name..........: MediacatalogDatabasesetup.java
 * Description...: Helper class used to create, and use the SQL Lite database
 *                 that the application uses to store media
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Simple media database access helper class. Defines the basic CRUD operations
 * for the Media catalog application, and gives the ability to list all notes as well
 * as retrieve, modify, or remove a specific media.
 */
public class MediacatalogDataBaseSetup {

	/****************
	 * CLASS FIELDS *
	 ****************/
	private static final String TAG = "MediacatalogDataBaseSetup";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yy HH:mm:ss";
	public static final String KEY_VIDEO_FILTER_TYPE = "FilterType";
	public static final String KEY_MEDIA_ID = "_id";
	public static final String KEY_MEDIA_TITLE = "title";
	public static final String KEY_MEDIA_CATEGORY = "category";
	public static final String KEY_MEDIA_CATEGORY_ID = "category_id";
	public static final String KEY_MEDIA_RATING = "rating";
	public static final String KEY_MEDIA_STATUS = "status";
	public static final String KEY_MEDIA_TYPE = "type";
	public static final String KEY_MEDIA_SUBTYPE = "subtype";
	public static final String KEY_MEDIA_RANK = "rank";
	public static final String KEY_MEDIA_SMALL_IMAGE = "image_small";
	public static final String KEY_MEDIA_LARGE_IMAGE = "image_large";
	public static final String KEY_MEDIA_IMAGE_URL = "image_url";
	public static final String KEY_VIDEO_TITLE_SORTABLE = "title_sortable";
	public static final String KEY_MEDIA_AUTHOR = "author";
	public static final String KEY_MEDIA_ARTIST = "artist";
	public static final String KEY_MEDIA_YEAR = "year";
	public static final String KEY_CATEGORY_ID = "_id";
	public static final String KEY_CATEGORY_NAME = "name";
	public static final String KEY_CATEGORY_TYPE = "type";
	private static final String DATABASE_CREATE_MEDIA = "CREATE TABLE MEDIA ("
			+ "_id integer primary key autoincrement "
			+ ", title text not null "
			+ ", category text not null DEFAULT 'Unassigned' "
			+ ", category_id integer null "
			+ ", rating text not null DEFAULT 'PG' "
			+ ", rank integer not null DEFAULT 1 "
			+ ", status text not null " 
			+ ", type text not null "
			+ ", subtype text not null "
			+ ", image_small blob null " 
			+ ", image_large blob null "
			+ ", title_sortable text null "
			+ ", artist text null " 
			+ ", author text null "
			+ ", year text null "
			+ ", image_url text null "
			+ ");";
	private static final String DATABASE_CREATE_CATEGORIES = "CREATE TABLE categories ("
		+ "_id integer primary key autoincrement "
		+ ", name text not null "
		+ ", type text not null "
		+ ");";
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_2_TO_3 = {
		"ALTER TABLE VIDEO ADD COLUMN category TEXT NOT NULL DEFAULT 'Unassigned';"
		,"ALTER TABLE VIDEO ADD COLUMN rank integer NOT NULL DEFAULT 1;"
		,"UPDATE VIDEO SET type='Blu-Ray' WHERE type='BlueRay';"
	};
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_3_TO_4 = {
		"ALTER TABLE VIDEO ADD COLUMN image_small BLOB NULL;"
		,"ALTER TABLE VIDEO ADD COLUMN image_large BLOB NULL;"
	};
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_4_OR_5_TO_6 = {
		"ALTER TABLE VIDEO ADD COLUMN category_id integer NULL;"
		,"ALTER TABLE VIDEO ADD COLUMN subtype text NULL;"
		,"UPDATE VIDEO SET subtype='Blu-ray' WHERE type='Blu-ray';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='Blu-ray';"
		,"UPDATE VIDEO SET subtype='DVD' WHERE type='DVD';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='DVD';"
		,"UPDATE VIDEO SET subtype='MP4' WHERE type='MP4';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='MP4';"
		,"UPDATE VIDEO SET subtype='AIFF' WHERE type='AIFF';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='AIFF';"
		,"UPDATE VIDEO SET subtype='WAV' WHERE type='WAV';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='WAV';"
		,"UPDATE VIDEO SET subtype='AVI' WHERE type='AVI';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='AVI';"
		,"UPDATE VIDEO SET subtype='FLASH' WHERE type='FLASH';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='FLASH';"
		,"UPDATE VIDEO SET subtype='QUICKTIME' WHERE type='QUICKTIME';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='QUICKTIME';"
		,"UPDATE VIDEO SET subtype='VHS' WHERE type='VHS';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='VHS';"
		,"UPDATE VIDEO SET subtype='DIVX' WHERE type='DIVX';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='DIVX';"
		,"UPDATE VIDEO SET subtype='MKV' WHERE type='MKV';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='MKV';"
		,"UPDATE VIDEO SET subtype='OGG' WHERE type='OGG';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='OGG';"
	};
	
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_6_TO_7 = {
		"UPDATE VIDEO SET subtype='Blu-ray' WHERE type='Blu-Ray';"
		,"UPDATE VIDEO SET type='VIDEO' WHERE subtype='Blu-ray';"
	};
	
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_7_TO_8 = {
		"ALTER TABLE VIDEO ADD COLUMN title_sortable text NULL;"
	};
	
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_8_TO_9 = {
		"ALTER TABLE VIDEO ADD COLUMN artist text NULL;"
		,"ALTER TABLE VIDEO ADD COLUMN author text NULL;"
	};
	
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_9_TO_10 = {
		"ALTER TABLE VIDEO ADD COLUMN year text NULL;"
	};
	
	private static final String[] DATABASE_UPGRADE_MEDIA_FROM_10_TO_11 = {
		"ALTER TABLE VIDEO ADD COLUMN image_url text NULL;"
	};
	
	private static final String[] DATABASE_UPGRADE_MEDIA_INSERT_CATEGORIES = {
		"INSERT INTO categories (name, type) VALUES ('Unassigned', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Action', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Adventure', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Animation', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Biography', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Comedy', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Crime', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Documentary', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Drama', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Family', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Fantasy', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Film-Noir', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Game-Show', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('History', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Horror', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Music', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Musical', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Mystery', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('News', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Reality-TV', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Romance', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Sci-Fi', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Short', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Sport', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Talk-Show', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Thriller', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('War', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Western', 'Video');"
		,"INSERT INTO categories (name, type) VALUES ('Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Action Adventure', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Action Compilation', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Action RPG', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Action Simulation', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Adventure', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Board', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Card', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Casino', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Educational Puzzle', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Fighting', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Fighting Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Fighting Adventure', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('First Person Shooter', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Flight Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Hunting', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Music', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Music Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Party', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Pinball', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Platformer', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Puzzle', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Puzzle Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Puzzle Word Games', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('RPG', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Racing', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Racing Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Racing Simulation', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Shooter', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Simulation', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Sports', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Sports Action', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Sports Simulation', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Strategy', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Trivia', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Virtual Pet', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Wrestling', 'Game');"
		,"INSERT INTO categories (name, type) VALUES ('Arts & Photography', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Biographies & Memoirs', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Business & Investing', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Calendars', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Childrens Books', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Comics & Graphic Novels', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Computers & Internet', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Cooking, Food & Wine', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Entertainment', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Gay & Lesbian', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Health, Mind & Body', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('History', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Home & Garden', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Law', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Literature & Fiction', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Medicine', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Mystery & Thrillers', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Nonfiction', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Outdoors & Nature', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Parenting & Families', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Professional & Technical', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Reference', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Religion & Spirituality', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Romance', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Science', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Science Fiction & Fantasy', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Sports', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Teens', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Travel', 'Book');"
		,"INSERT INTO categories (name, type) VALUES ('Alternative Rock', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Blues', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Broadway & Vocalists', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Childrens', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Christian & Gospel', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Classical', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Country', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Dance & Electronica', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Folk', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Hard Rock & Metal', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Hawaiian', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Imports', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Indie Music', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Jazz', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Latin', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Miscellaneous', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('New Age', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Opera & Vocal Music', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Pop', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('R&B & Soul', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Rap & Hip-Hop', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Rock', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Soundtracks', 'Audio');"
		,"INSERT INTO categories (name, type) VALUES ('Word Music', 'Audio');"
	};
	private static final String DATABASE_NAME = "MEDIA";
	private static final String DATABASE_SDCARD_ROOT_PATH = "/sdcard/mediacatalog";
	private static final String DATABASE_MEDIA_TABLE = "MEDIA";
	private static final String DATABASE_CATEGORY_TABLE = "categories";
	private static final int DATABASE_VERSION = 11;
	private final Context mCtx;

	/**
	 * Private nested class used to initialize the database
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public SQLiteDatabase getReadableDatabase() {
			
			File dbFile = new File(DATABASE_SDCARD_ROOT_PATH + "/" + DATABASE_NAME);
			
			boolean fileExisted = dbFile.exists();
			if(!dbFile.canRead()) {
				return super.getReadableDatabase();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
			if(!fileExisted){
				onCreate(db);
				db.setVersion(DATABASE_VERSION);
			} else {
				if(db.getVersion() < DATABASE_VERSION) {
					onUpgrade(db, db.getVersion(), DATABASE_VERSION);
				}
			}
			
			return db;
		}
		
		@Override
		public SQLiteDatabase getWritableDatabase() {
			
			File dbFile = new File(DATABASE_SDCARD_ROOT_PATH + "/" + DATABASE_NAME);
			
			boolean fileExisted = dbFile.exists();
			if(!dbFile.canWrite()) {
				return super.getWritableDatabase();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
			
			if(!fileExisted){
				onCreate(db);
				db.setVersion(DATABASE_VERSION);
			} else {
				if(db.getVersion() < DATABASE_VERSION) {
					onUpgrade(db, db.getVersion(), DATABASE_VERSION);
					db.setVersion(DATABASE_VERSION);
				}
			}
			
			return db;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE_MEDIA);
			db.execSQL(DATABASE_CREATE_CATEGORIES);
			//Seed categories table
			for(int i = 0; i < DATABASE_UPGRADE_MEDIA_INSERT_CATEGORIES.length; i++) {
				db.execSQL(DATABASE_UPGRADE_MEDIA_INSERT_CATEGORIES[i]);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			if(oldVersion <= 2) {
				//Perform alters on VIDEO table
				try {
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_2_TO_3.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_2_TO_3[i]);
					}
					db.setVersion(3);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
				
			}
			if(oldVersion <= 3 ){
				try {
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_3_TO_4.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_3_TO_4[i]);
					}
					db.setVersion(4);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			} 
				
			if(oldVersion <= 5) {
				try {
					//Create new categories table
					db.execSQL(DATABASE_CREATE_CATEGORIES);
					
					//Alter VIDEO and add records to categories
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_4_OR_5_TO_6.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_4_OR_5_TO_6[i]);
					}
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_INSERT_CATEGORIES.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_INSERT_CATEGORIES[i]);
					}
					db.setVersion(6);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			}
			
			if(oldVersion <= 6) {
				try {
					//Alter VIDEO and add records to categories
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_6_TO_7.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_6_TO_7[i]);
					}
					db.setVersion(7);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			}
			
			if(oldVersion <= 7) {
				try {
					//Alter VIDEO and add title_sortable
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_7_TO_8.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_7_TO_8[i]);
					}
					db.setVersion(8);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			}
			if(oldVersion <= 8) {
				try {
					//Alter VIDEO and add title_sortable
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_8_TO_9.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_8_TO_9[i]);
					}
					db.setVersion(9);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			}
			
			if(oldVersion <= 9) {
				try {
					//Alter VIDEO and add title_sortable
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_9_TO_10.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_9_TO_10[i]);
					}
					db.setVersion(10);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			}
			
			if(oldVersion <= 10) {
				try {
					//Alter VIDEO and add title_sortable
					for(int i = 0; i < DATABASE_UPGRADE_MEDIA_FROM_10_TO_11.length; i++) {
						db.execSQL(DATABASE_UPGRADE_MEDIA_FROM_10_TO_11[i]);
					}
					db.setVersion(10);
				} catch(Exception ex) {
					android.util.Log.v("DATABASE_UPGRADE_ERROR", ex.getMessage());
				}
			}
		} 
	}// End of private static class DatabaseHelper

	/*** Constructor - takes the context to allow the database to be 
	 * opened/created @param ctx the Context within which to work  */
	
	public MediacatalogDataBaseSetup(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the Media database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public MediacatalogDataBaseSetup open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Verifies that the database connection is still valid
	 * 
	 */
	private void VerifyDbConnection()
	{
		if(mDb == null || !mDb.isOpen())
		{
			this.open();
		}
	}
	
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Return a Cursor over the list of all VIDEO in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllMEDIA() {

		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		String query = "SELECT "
			+ "M." + KEY_MEDIA_ID
			+ ", M." + KEY_MEDIA_TITLE
			+ ", M." + KEY_MEDIA_RATING
			+ ", C." + KEY_CATEGORY_NAME
			+ ", M." + KEY_MEDIA_STATUS
			+ ", M." + KEY_MEDIA_TYPE
			+ ", M." + KEY_MEDIA_SUBTYPE
			+ ", M." + KEY_MEDIA_RANK
			+ ", M." + KEY_MEDIA_SMALL_IMAGE
			+ ", M." + KEY_MEDIA_ARTIST
			+ ", M." + KEY_MEDIA_AUTHOR
			+ ", M." + KEY_MEDIA_YEAR
			+ ", M." + KEY_MEDIA_IMAGE_URL
			+ ", CASE WHEN substr(upper(M.title), 0, 2) = 'A ' THEN substr(M.title, 3) ELSE CASE WHEN substr(upper(M.title), 0, 4) = 'THE ' THEN substr(M.title, 5) ELSE M.title END END AS sortable"
			+ " FROM " + DATABASE_MEDIA_TABLE + " AS M"
			+ " LEFT JOIN " + DATABASE_CATEGORY_TABLE + " AS C ON (M." + KEY_MEDIA_CATEGORY_ID + " = C." + KEY_CATEGORY_ID + ")"
			+ " ORDER BY sortable";
		return mDb.rawQuery(query, null);
	}
	
	/**
	 * Return a Cursor positioned at the video that matches the given rowId
	 * 
	 * @param rowId id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException if note could not be found/retrieved
	 */
	public Cursor fetchMEDIA(long rowId) throws SQLException {

		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		Cursor mCursor = mDb.query(true, DATABASE_MEDIA_TABLE, new String[] { KEY_MEDIA_ID, KEY_MEDIA_TITLE,
			KEY_MEDIA_RATING, KEY_MEDIA_CATEGORY_ID, KEY_MEDIA_STATUS, KEY_MEDIA_TYPE, KEY_MEDIA_SUBTYPE
			, KEY_MEDIA_RANK , KEY_MEDIA_SMALL_IMAGE, KEY_MEDIA_LARGE_IMAGE
			, KEY_MEDIA_AUTHOR, KEY_MEDIA_ARTIST, KEY_MEDIA_YEAR, KEY_MEDIA_IMAGE_URL}
			, KEY_MEDIA_ID + "=" + rowId, null,	null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}//End of public Cursor method fetchVIDEO(long)

	/**
	 * Retrieves the video title based on the passed VIDEOId
	 * 
	 * @param MEDIAId Uniquely identifies the video
	 * @return Returns the title
	 */
	public String fetchMEDIATitleByMEDIAId(long MEDIAId) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		String title = "";
		try {
			Cursor mCursor = mDb.query(true, DATABASE_MEDIA_TABLE, new String[] { KEY_MEDIA_TITLE }
				, KEY_MEDIA_ID + "=" + MEDIAId, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				title = mCursor.getString(mCursor.getColumnIndex(KEY_MEDIA_TITLE));
			}
		} catch (Exception ex) {
			android.util.Log.v("FETCH_MEDIA_TITLE_ERROR", ex.getMessage());
		}
		return title;
	}//End of method public String fetchVIDEOTitleByVIDEOId(long)
	
	/**
	 * Return a Cursor over the list of all VIDEO in the database
	 * 
	 * @param String The type of VIDEO we want returned
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllMEDIA(String type) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		String sort = "";
		if(type.trim().toUpperCase().equals("VIDEO") ||
				type.trim().toUpperCase().equals("GAME")) {
			sort = "sortable ASC"; 
		} else if (type.trim().toUpperCase().equals("AUDIO")) {
			sort = KEY_MEDIA_ARTIST + " ASC " +
				"," + "sortable ASC";
		} else if (type.trim().toUpperCase().equals("BOOK")) {
			sort = KEY_MEDIA_AUTHOR + " ASC " +
			"," + "sortable ASC";
		}
		String query = "SELECT "
			+ "M." + KEY_MEDIA_ID
			+ ", M." + KEY_MEDIA_TITLE
			+ ", M." + KEY_MEDIA_RATING
			+ ", C." + KEY_CATEGORY_NAME
			+ ", M." + KEY_MEDIA_STATUS
			+ ", M." + KEY_MEDIA_TYPE
			+ ", M." + KEY_MEDIA_SUBTYPE
			+ ", M." + KEY_MEDIA_RANK
			+ ", M." + KEY_MEDIA_SMALL_IMAGE
			+ ", M." + KEY_MEDIA_ARTIST
			+ ", M." + KEY_MEDIA_AUTHOR
			+ ", M." + KEY_MEDIA_IMAGE_URL
			+ ", CASE WHEN substr(upper(M.title), 0, 2) = 'A ' THEN substr(M.title, 3) ELSE CASE WHEN substr(upper(M.title), 0, 4) = 'THE ' THEN substr(M.title, 5) ELSE M.title END END AS sortable"
			+ " FROM " + DATABASE_MEDIA_TABLE + " AS M"
			+ " LEFT JOIN " + DATABASE_CATEGORY_TABLE + " AS C ON (M." + KEY_MEDIA_CATEGORY_ID + " = C." + KEY_CATEGORY_ID + ")"
			+ " WHERE M." + KEY_MEDIA_TYPE + "=? "
			+ " ORDER BY " + sort;
		return mDb.rawQuery(query, new String[] {type});
	}//End of method public Cursor fetchAllVIDEO(String)
	
	/**
	 * Retrieves the count of items in the database by type
	 * 
	 * @param type
	 * @return Count of the items for the specified type
	 */
	public int fetchItemCountByType(String type) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		int count = 0;
		try {
			String query = "SELECT COUNT(*) FROM " + DATABASE_MEDIA_TABLE + " WHERE Type=?";
			Cursor cursor = mDb.rawQuery(query, new String[] {type});
			if(cursor != null) {
				cursor.moveToFirst();
				count = cursor.getInt(0);
			}
		} catch(Exception ex) {
			android.util.Log.v("QUERY ERROR", ex.getMessage());
		}
		return count;
	}//End of method fetchItemCountByType

	/**
	 * This method allows the filtering of the VIDEO by title
	 * 
	 * @param title The title expression we are filtering on
	 * @return A cursor to the results
	 */
	public Cursor fetchAllMEDIAByTitle(String title, String type) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		String sort = "";
		if(type.trim().toUpperCase().equals("VIDEO") ||
				type.trim().toUpperCase().equals("GAME")) {
			sort = KEY_MEDIA_TITLE + " ASC"; 
		} else if (type.trim().toUpperCase().equals("AUDIO")) {
			sort = KEY_MEDIA_ARTIST + " ASC " +
				"," + KEY_MEDIA_TITLE + " ASC";
		} else if (type.trim().toUpperCase().equals("BOOK")) {
			sort = KEY_MEDIA_AUTHOR + " ASC " +
			"," + KEY_MEDIA_TITLE + " ASC";
		}
		String query = "SELECT "
			+ "M." + KEY_MEDIA_ID
			+ ", M." + KEY_MEDIA_TITLE
			+ ", M." + KEY_MEDIA_RATING
			+ ", C." + KEY_CATEGORY_NAME
			+ ", M." + KEY_MEDIA_STATUS
			+ ", M." + KEY_MEDIA_TYPE
			+ ", M." + KEY_MEDIA_SUBTYPE
			+ ", M." + KEY_MEDIA_RANK
			+ ", M." + KEY_MEDIA_SMALL_IMAGE
			+ ", M." + KEY_MEDIA_ARTIST
			+ ", M." + KEY_MEDIA_AUTHOR
			+ ", M." + KEY_MEDIA_IMAGE_URL
			+ ", CASE WHEN substr(upper(M.title), 0, 2) = 'A ' THEN substr(M.title, 3) ELSE CASE WHEN substr(upper(M.title), 0, 4) = 'THE ' THEN substr(M.title, 5) ELSE M.title END END AS sortable"
			+ " FROM " + DATABASE_MEDIA_TABLE + " AS M"
			+ " LEFT JOIN " + DATABASE_CATEGORY_TABLE + " AS C ON (M." + KEY_MEDIA_CATEGORY_ID + " = C." + KEY_CATEGORY_ID + ")"
			+ " WHERE M." + KEY_MEDIA_TYPE + "=? "
			+ " AND M." + KEY_MEDIA_TITLE + " LIKE '%" + title + "%' "
			+ " ORDER BY " + sort;
		return mDb.rawQuery(query, new String[] {type});
		
	}
	
	/**
	 * Create a new video using the properties provided. If the video is
	 * successfully created return the new rowId for that video, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param title value to set video title to
	 * @param rating value to set video rating to
	 * @param status value to set the video status to
	 * @param type value to set the video type to
	 * @return rowId or -1 if failed
	 */
	public long createMEDIA(String title, String rating, Long category_id, String status
			,String type, String subtype, int rank, byte[] largeImage
			, byte[] smallImage, String artist, String author, String year, String imageUrl) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MEDIA_TITLE, title.trim());
		initialValues.put(KEY_MEDIA_STATUS, status);
		initialValues.put(KEY_MEDIA_RATING, rating);
		initialValues.put(KEY_MEDIA_CATEGORY_ID, category_id);
		initialValues.put(KEY_MEDIA_TYPE, type);
		initialValues.put(KEY_MEDIA_SUBTYPE, subtype);
		initialValues.put(KEY_MEDIA_RANK, rank);
		initialValues.put(KEY_MEDIA_LARGE_IMAGE, largeImage);
		initialValues.put(KEY_MEDIA_SMALL_IMAGE, smallImage);
		initialValues.put(KEY_MEDIA_ARTIST, artist);
		initialValues.put(KEY_MEDIA_AUTHOR, author);
		initialValues.put(KEY_MEDIA_YEAR, year);
		initialValues.put(KEY_MEDIA_IMAGE_URL, imageUrl);
		return mDb.insert(DATABASE_MEDIA_TABLE, null, initialValues);
	}

	/**
	 * Delete the video with the given rowId
	 * 
	 * @param rowId
	 *            id of video to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteMEDIA(long rowId) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		return mDb.delete(DATABASE_MEDIA_TABLE, KEY_MEDIA_ID + "=" + rowId, null) > 0;
	}//End of method public boolean deleteVIDEO(long)
	
	/**
	 * Update the video using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId id of note to update
	 * @param title value to set video title to
	 * @param rating value to set video rating to
	 * @param status value to set the video status to
	 * @param type value to set the video type to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateMEDIA(long rowId, String title, String rating, Long category_id
			,String status, String type, String subtype, int rank,
			byte[] largeImage, byte[] smallImage, String artist, String author, String year
			, String imageUrl) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		ContentValues args = new ContentValues();
		args.put(KEY_MEDIA_TITLE, title.trim());
		args.put(KEY_MEDIA_RATING, rating);
		args.put(KEY_MEDIA_CATEGORY_ID, category_id);
		args.put(KEY_MEDIA_STATUS, status);
		args.put(KEY_MEDIA_TYPE, type);
		args.put(KEY_MEDIA_SUBTYPE, subtype);
		args.put(KEY_MEDIA_RANK, rank);
		args.put(KEY_MEDIA_LARGE_IMAGE, largeImage);
		args.put(KEY_MEDIA_SMALL_IMAGE, smallImage);
		args.put(KEY_MEDIA_ARTIST, artist);
		args.put(KEY_MEDIA_AUTHOR, author);
		args.put(KEY_MEDIA_YEAR, year);
		args.put(KEY_MEDIA_IMAGE_URL, imageUrl);
		return mDb.update(DATABASE_MEDIA_TABLE, args, KEY_MEDIA_ID + "=" + rowId, null) > 0;
		
	}//End of public boolean method updateVIDEO(long, String, String, String, String, String)



	//////////////////////////////////////////////////////////////
	//////////////////// CATEGORY METHODS ////////////////////////
	//////////////////////////////////////////////////////////////
	/**
	 * Returns all categories
	 * 
	 * @param type Identifies the type of category
	 * @return A cursor for all the categories belonging to the passed type
	 */
	public Cursor fetchAllCategories() {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		Cursor mCursor = null;
		try {
			mCursor = mDb.query(DATABASE_CATEGORY_TABLE, new String[] { KEY_CATEGORY_ID, KEY_CATEGORY_NAME, KEY_CATEGORY_TYPE }, 
					null, null, null, null, KEY_CATEGORY_TYPE + " ASC, " +  KEY_CATEGORY_NAME + " ASC");
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
		} catch(Exception ex) {
			android.util.Log.v("ERROR", ex.getMessage());
		}
		return mCursor;
	}//End of method public Cursor fetchGategoriesByType(String)
	
	/**
	 * Returns all categories for the passed type
	 * 
	 * @param type Identifies the type of category
	 * @return A cursor for all the categories belonging to the passed type
	 */
	public Cursor fetchCategoriesByType(String type) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		Cursor mCursor = mDb.query(DATABASE_CATEGORY_TABLE, new String[] { KEY_CATEGORY_ID, KEY_CATEGORY_NAME }, 
				KEY_CATEGORY_TYPE + " = '" + type + "'", null, null, null, KEY_CATEGORY_NAME + " ASC");
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}//End of method public Cursor fetchGategoriesByType(String)
	
	/**
	 * Returns the name of the category based on the id
	 * 
	 * @param id Identifies the category
	 * @return String containing the name
	 */
	public String fetchCategoryNameById(Long id) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		String name = "";
		try {
			Cursor mCursor = mDb.query(true, DATABASE_CATEGORY_TABLE, new String[] { KEY_CATEGORY_NAME }
				, KEY_CATEGORY_ID + "=" + id, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				name = mCursor.getString(mCursor.getColumnIndex(KEY_CATEGORY_NAME));
			}
		} catch (Exception ex) {
			android.util.Log.v("FETCH_MEDIA_TITLE_ERROR", ex.getMessage());
		}
		return name;
	}//End of method publicString fetchCategoryById(Long)
	
	/**
	 * Returns the name of the category based on the id
	 * 
	 * @param id Identifies the category
	 * @return String containing the name
	 */
	public Long fetchCategoryIdByNameType(String name, String type) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		Long id = null;
		try {
			Cursor mCursor = mDb.query(true, DATABASE_CATEGORY_TABLE, new String[] { KEY_CATEGORY_ID }
				, KEY_CATEGORY_NAME + " = '" + name + "' "
				+ " AND " + KEY_CATEGORY_TYPE + " = '" + type + "'", null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				id = mCursor.getLong(mCursor.getColumnIndex(KEY_CATEGORY_ID));
			}
		} catch (Exception ex) {
			android.util.Log.v("FETCH_CATEGORY_ID_ERROR", ex.getMessage());
		}
		return id;
	}//End of method publicString fetchCategoryIdByNameType(String, String)
	
	/**
	 * Returns a cursor to the category record identified by the passed id
	 * 
	 * @param id Identifies the category record
	 * @return Cursor positioned to the record identified by the id
	 */
	public Cursor fetchCategoryById(Long id) {
		
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		Cursor mCursor = null;
		try {
			 mCursor = mDb.query(true, DATABASE_CATEGORY_TABLE, new String[] { KEY_CATEGORY_NAME, KEY_CATEGORY_TYPE }
				, KEY_CATEGORY_ID + "=" + id, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
		} catch (Exception ex) {
			android.util.Log.v("FETCH_MEDIA_TITLE_ERROR", ex.getMessage());
		}
		return mCursor;
	}//End of method public Cursor fetchCategoriesById(Long)
	
	/**
	 * Deletes the category
	 * 
	 * @param categoryId
	 */
	public boolean deleteCategory(long categoryId) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		return mDb.delete(DATABASE_CATEGORY_TABLE, KEY_CATEGORY_ID + " = " + categoryId, null) > 0;
	}//End of method public void deleteCategory(long)

	/**
	 * Creates a new category record
	 * @param type The type classification for the category (Video, Game, Book, AUDIO)
	 * @param name The text for the category
	 * @return The ID of the new record
	 */
	public long createCategory(String type, String name) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CATEGORY_TYPE, type);
		initialValues.put(KEY_CATEGORY_NAME, name);
		return mDb.insert(DATABASE_CATEGORY_TABLE, null, initialValues);
	}//End of method public long createCategory(String, String)

	/**
	 * Updates the category record
	 * 
	 * @param rowId Identifies the category record
	 * @param type The type classification for the category (Video, Game, Book, AUDIO)
	 * @param name The text for the category
	 */
	public void updateCategory(Long rowId, String type, String name) {
		//Verify database connection
		VerifyDbConnection();
		
		//Perform query
		ContentValues args = new ContentValues();
		args.put(KEY_CATEGORY_TYPE, type);
		args.put(KEY_CATEGORY_NAME, name);
		mDb.update(DATABASE_CATEGORY_TABLE, args, KEY_CATEGORY_ID + "=" + rowId, null);
	}//End of method public void updateCategory(Long, String, String)
	
}// End of public class MediacatalogDataBaseSetup
