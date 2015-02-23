/*****************************************************************************
 * Name..........: MediacatalogEditMedia.java
 * Description...: Activity for editing media
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;


/**
 * Activity for viewing/editing a media
 */
public class MediacatalogEditMedia extends Activity {

	/****************
	 * CLASS FIELDS *
	 ****************/
	
	private EditText mTitleText = null;
	private String _Title = new String();
	private Spinner mCategory = null;
	private Long _Category = null;
	private Spinner mRatingText = null;
	private TableRow mRatingRow = null;
	private String _Rating = new String();
	private Spinner mStatusText = null;
	private String _Status = new String();
	private Spinner mTypeText = null;
	private String _SubType = new String();
	private String _Type = new String();
	private Long mRowId = null;
	private EditText mAuthor = null;
	private TableRow mAuthorRow = null;
	private String _Author = new String();
	private EditText mArtist = null;
	private TableRow mArtistRow = null;
	private String _Artist = new String();
	private ImageView mLargeImage = null;
	private EditText mYear = null;
	private String _Year = new String();
	private String _ImageUrl = new String();
	private byte[] _SmallImage = null;
	private byte[] _LargeImage = null;
	private Button mImage1 = null;
	private Button mImage2 = null;
	private Button mImage3 = null;
	private Button mImage4 = null;
	private Button mImage5 = null;
	private MediacatalogDataBaseSetup mDbHelper = null;
	private static final int SAVE_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int CANCEL_ID = Menu.FIRST + 2;
	private static final String KEY_ADDING_NEW = "ADDING_ITEM_KEY";
	private int selectedRank = 0;
	boolean initialLoad = true;
	ProgressDialog pd = null;
	private Context _context = null;
	private int _resultIndex = 0;

	/**
	 * Populates the form fields
	 */
	private void populateFields() {
		
		try {
		if (mDbHelper == null) {
			mDbHelper = new MediacatalogDataBaseSetup(this);
			mDbHelper.open();
		}
		
		Cursor cur_media = null;
		Bundle extras = getIntent().getExtras();
		
		if(mRowId != null ) {
			cur_media = mDbHelper.fetchMEDIA(mRowId);
			startManagingCursor(cur_media);
		}

		//Image
		if(_ImageUrl == null) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL)) {
					_ImageUrl = cur_media.getString(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL));
				} else {
					_ImageUrl = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL);
				}
			} else {
				_ImageUrl = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL):null
							:null;
			}
		}
		if(_LargeImage == null) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE)) {
					_LargeImage = cur_media.getBlob(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE));
				} else {
					_LargeImage = extras.getByteArray(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE);
				}
			} else {
				_LargeImage = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE))?extras.getByteArray(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE):null
							:null;
			}
		}
		//Image
		if(_SmallImage == null) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE)) {
					_SmallImage = cur_media.getBlob(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE));
				} else {
					_SmallImage = extras.getByteArray(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE);
				}
			} else {
				_SmallImage = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE))?extras.getByteArray(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE):null
							:null;
			}
		}
		if(_ImageUrl.length() > 0 ) {
			mLargeImage.setImageURI(Uri.parse(_ImageUrl));
		} else if(_LargeImage != null) {
			mLargeImage.setImageBitmap(BitmapFactory.decodeByteArray(_LargeImage, 0, _LargeImage.length));
		} else if(_SmallImage != null) {
			mLargeImage.setImageBitmap(BitmapFactory.decodeByteArray(_SmallImage, 0, _SmallImage.length));
		}
		//Title
		if(_Title.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE)) {
					_Title = cur_media.getString(cur_media.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE));
				} else {
					_Title = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE);
				}
			} else {
				_Title = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE):""
							:"";
			}
		}
		mTitleText.setText(_Title);
		//Artist
		if(_Artist.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST)) {
					_Artist = cur_media.getString(cur_media.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST));
				} else {
					_Artist = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST);
				}
			} else {
				_Artist = (extras != null)
					?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST):""
					:"";
			}
		}
		mArtist.setText(_Artist);
		//Author
		if(_Author.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR)) {
					_Author = cur_media.getString(cur_media.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR));
				} else {
					_Author = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR);
				}
			} else {
				_Author = (extras != null)
					?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR):""
					:"";
			}
		}
		mAuthor.setText(_Author);
		//Year
		if(_Year.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR)) {
					_Year = cur_media.getString(cur_media.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR));
				} else {
					_Year = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR);
				}
			} else {
				_Year = (extras != null)
					?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR):""
					:"";
			}
		}
		mYear.setText(_Year);

		
		//Rating
		if(_Rating.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_RATING)) {
					_Rating = cur_media.getString(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_RATING));
				} else {
					_Rating = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_RATING);
				}
			} else {
				_Rating = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_RATING))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_RATING):""
							:"";
			}
		}
		int ratingIndex = 0;
		for (int i = 0; i < mRatingText.getCount(); i++) {
			if (_Rating.equals(mRatingText.getItemAtPosition(i)
					.toString())) {
				ratingIndex = i;
				break;
			}
		}
		mRatingText.setSelection(ratingIndex);
		
		//Category
		if(_Category == null) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY)) {
					_Category = cur_media.getLong(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY_ID));
				} else {
					_Category = extras.getLong(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY_ID);
				}
			} else {
				_Category = (extras != null)
								?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY_ID))?extras.getLong(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY_ID):null
								:null;
			}
		}
		int categoryIndex = 0;
		for(int i = 0; i < mCategory.getCount(); i++) {
			if(_Category == mCategory.getItemIdAtPosition(i)) {
				categoryIndex = i;
				break;
			}
		}
		mCategory.setSelection(categoryIndex);
		
		//Status
		if(_Status.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS)) {
					_Status = cur_media.getString(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS));
				} else {
					_Status = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS);
					
				}
			} else {
				_Status = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS):""
							:"";
			}
		}
		int statusIndex = 0;
		for (int i = 0; i < mStatusText.getCount(); i++) {
			if (_Status.equals(mStatusText.getItemAtPosition(i)
					.toString())) {
				statusIndex = i;
				break;
			}
		}
		mStatusText.setSelection(statusIndex);
		//Type
		if(_SubType.equals("")) {
			if(mRowId != null) {
				if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE)) {
					_SubType = cur_media.getString(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE));
				} else {
					_SubType = extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE);
				}
			} else {
				_SubType = (extras != null)
							?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE):""
							:"";
			}
		}
		int typeIndex = 0;
		for (int i = 0; i < mTypeText.getCount(); i++) {
			if (_SubType.equals(mTypeText.getItemAtPosition(i).toString())) {
				typeIndex = i;
				break;
			}
		}
		mTypeText.setSelection(typeIndex);
		//Rank
		if(mRowId != null) {
			if(!extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_RANK)) {
				selectedRank = cur_media.getInt(cur_media.getColumnIndexOrThrow(MediacatalogDataBaseSetup.KEY_MEDIA_RANK));
			} else {
				selectedRank = extras.getInt(MediacatalogDataBaseSetup.KEY_MEDIA_RANK);
			}
		} else {
			selectedRank = (extras != null)
								?(extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_RANK))?extras.getInt(MediacatalogDataBaseSetup.KEY_MEDIA_RANK):1
								:1;
		}
		setRating(selectedRank, R.drawable.star_large);
		} catch(Exception ex) {
			android.util.Log.v("ERROR", (ex.getMessage() == null)?"error":ex.getMessage());
		}
		
		
	}//End of method private void populateFields

	private void setRating(int rank, int image) {
		
		mImage1.setBackgroundResource(R.drawable.disabled_star_large);
		mImage2.setBackgroundResource(R.drawable.disabled_star_large);
		mImage3.setBackgroundResource(R.drawable.disabled_star_large);
		mImage4.setBackgroundResource(R.drawable.disabled_star_large);
		mImage5.setBackgroundResource(R.drawable.disabled_star_large);
		if(rank > 0) {
			mImage1.setBackgroundResource(image);
		}
		if(rank > 1) {
			mImage2.setBackgroundResource(image);		
		}
		if(rank > 2) {
			mImage3.setBackgroundResource(image);
		}
		if(rank > 3) {
			mImage4.setBackgroundResource(image);
		}
		if(rank > 4) {
			mImage5.setBackgroundResource(image);
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case DELETE_ID:
				DeleteMedia();
				return true;
			case SAVE_ID:
				SaveMedia();
				return true;
			case CANCEL_ID:
				CancelAction();
				return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	


	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
	}//End of method protected void onActivityResult(int, int)
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0, SAVE_ID, 0, R.string.menu_save);
		item.setIcon(android.R.drawable.ic_menu_save);
		MenuItem item2 = menu.add(0, DELETE_ID, 0, R.string.menu_remove);
		item2.setIcon(android.R.drawable.ic_menu_delete);
		item2.setEnabled(mRowId != null);
		MenuItem item3 = menu.add(0, CANCEL_ID, 0, R.string.menu_cancel);
		item3.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		Intent editor = getIntent();
		
		_Title = mTitleText.getText().toString();
		_Rating = mRatingText.getSelectedItem().toString();
		_Category = mCategory.getSelectedItemId();
		_Status = mStatusText.getSelectedItem().toString();
		_SubType = mTypeText.getSelectedItem().toString();
		_Artist = mArtist.getText().toString();
		_Author = mAuthor.getText().toString();
		_Year = mYear.getText().toString();
		
		if(mRowId != null) {
			editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_ID, mRowId);
			editor.putExtra(KEY_ADDING_NEW, false);
		} else {
			editor.putExtra(KEY_ADDING_NEW, true);
		}
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE, _Title);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_RATING, _Rating);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY, _Category);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS, _Status);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE, _Type);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE, _SubType);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_RANK, selectedRank);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE, _LargeImage);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE, _SmallImage);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST, _Artist);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR, _Author);
		editor.putExtra(MediacatalogDataBaseSetup.KEY_MEDIA_YEAR, _Year);
	}//End of onPause

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDbHelper = new MediacatalogDataBaseSetup(this);
		mDbHelper.open();
		
		setContentView(R.layout.media_edit);
		setTitle(R.string.title_edit_video);
		
		mTitleText = (EditText) findViewById(R.id.title);
		mRatingText = (Spinner) findViewById(R.id.rating);
		mRatingRow = (TableRow) findViewById(R.id.rating_row);
		mStatusText = (Spinner) findViewById(R.id.status);
		mTypeText = (Spinner) findViewById(R.id.type);
		mCategory = (Spinner) findViewById(R.id.category);
		mLargeImage = (ImageView) findViewById(R.id.large_image);
		mAuthor = (EditText) findViewById(R.id.author);
		mAuthorRow = (TableRow) findViewById(R.id.author_row);
		mArtist = (EditText) findViewById(R.id.artist);
		mArtistRow = (TableRow) findViewById(R.id.artist_row);
		mYear = (EditText) findViewById(R.id.year);

		
		/**
		 * Setup rating images
		 */
		mImage1 = (Button) findViewById(R.id.rank1);
		mImage1.setFocusable(true);
		mImage1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedRank = 1;
				setRating(1, R.drawable.star_large);
			}
		});
		mImage1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					setRating(1, R.drawable.star_selected_large);
				}
			}
		});
		mImage2 = (Button) findViewById(R.id.rank2);
		mImage2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedRank = 2;
				setRating(2, R.drawable.star_large);
			}
		});
		mImage2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					setRating(2, R.drawable.star_selected_large);
				}
			}
		});
		mImage3 = (Button) findViewById(R.id.rank3);
		mImage3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedRank = 3;
				setRating(3, R.drawable.star_large);
			}
		});
		mImage3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					setRating(3, R.drawable.star_selected_large);
				}
			}
		});
		mImage4 = (Button) findViewById(R.id.rank4);
		mImage4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedRank = 4;
				setRating(4, R.drawable.star_large);
			}
		});
		mImage4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					setRating(4, R.drawable.star_selected_large);
				}
			}
		});
		mImage5 = (Button) findViewById(R.id.rank5);
		mImage5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedRank = 5;
				setRating(5, R.drawable.star_large);
			}
		});
		mImage5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					setRating(5, R.drawable.star_selected_large);
				}
			}
		});
		/**
		 *  Setup spinners
		 */
		//Rating
		ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter
				.createFromResource(this, R.array.ratings,
						android.R.layout.simple_spinner_item);
		ratingAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mRatingText.setAdapter(ratingAdapter);
		//Status
		ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, 
				R.array.statuses, android.R.layout.simple_spinner_item);
		statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStatusText.setAdapter(statusAdapter);
		mRowId = savedInstanceState != null ? savedInstanceState
				.getLong(MediacatalogDataBaseSetup.KEY_MEDIA_ID) : null;
		Bundle extras = getIntent().getExtras();
		
		Boolean performLookup = false;
		if(extras != null) {
			mRowId = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_ID))?extras.getLong(MediacatalogDataBaseSetup.KEY_MEDIA_ID):null;
			_Title = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE):"";
			_Rating = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_RATING))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_RATING):"";
			_Category = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY_ID))?extras.getLong(MediacatalogDataBaseSetup.KEY_MEDIA_CATEGORY_ID):null;
			_Status = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_STATUS):"";
			_Type = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE):"";
			_SubType = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE):"";
			performLookup = (extras.containsKey("PerformLookup"))?extras.getBoolean("PerformLookup"):false;
			_LargeImage = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE))?extras.getByteArray(MediacatalogDataBaseSetup.KEY_MEDIA_LARGE_IMAGE):null;
			_SmallImage = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE))?extras.getByteArray(MediacatalogDataBaseSetup.KEY_MEDIA_SMALL_IMAGE):null;
			_ImageUrl = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_IMAGE_URL):"";
			_Artist = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST):"";
			_Author = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR))?extras.getString(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR):"";
			selectedRank = (extras.containsKey(MediacatalogDataBaseSetup.KEY_MEDIA_RANK))?extras.getInt(MediacatalogDataBaseSetup.KEY_MEDIA_RANK):0;
		}
		
		//Set the type list
		int typeResource = 0;
		TableLayout parent = (TableLayout)mArtistRow.getParent();
		if(_Type.toUpperCase().equals("VIDEO")) {
			typeResource = R.array.types_video;
			parent.removeView(mArtistRow);
			parent.removeView(mAuthorRow);
		} else if (_Type.toUpperCase().equals("GAME")) {
			typeResource = R.array.types_game;
			parent.removeView(mArtistRow);
			parent.removeView(mAuthorRow);
		} else if (_Type.toUpperCase().equals("BOOK")) {
			typeResource = R.array.types_book;
			parent.removeView(mRatingRow);
			parent.removeView(mArtistRow);
		} else if (_Type.toUpperCase().equals("AUDIO")) {
			typeResource = R.array.types_audio;
			parent.removeView(mRatingRow);
			parent.removeView(mAuthorRow);
		}
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this
			, typeResource, android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTypeText.setAdapter(typeAdapter);
		//Category Spinner
		Cursor categoryCursor = mDbHelper.fetchCategoriesByType(_Type);
		SimpleCursorAdapter categoryAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item
				, categoryCursor, new String[] {MediacatalogDataBaseSetup.KEY_CATEGORY_NAME}
				, new int[] {android.R.id.text1});
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCategory.setAdapter(categoryAdapter);
		
		_context = this;
		populateFields();

	}
	
	private void SaveMedia() {
		String title = mTitleText.getText().toString();
		String rating = mRatingText.getSelectedItem().toString();
		Long category = mCategory.getSelectedItemId();
		String status = mStatusText.getSelectedItem().toString();
		String subtype = mTypeText.getSelectedItem().toString();
		String artist = mArtist.getText().toString();
		String author = mAuthor.getText().toString();
		String year = mYear.getText().toString();
		
		if (mRowId == null) {
			long id = mDbHelper
					.createMEDIA(title, rating, category, status, _Type, subtype, selectedRank, _LargeImage, _SmallImage, artist, author, year, _ImageUrl);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateMEDIA(mRowId, title, rating, category, status, _Type, subtype, selectedRank, _LargeImage, _SmallImage, artist, author, year, _ImageUrl);
		}
		setResult(RESULT_OK);
		finish();
	}
	
	private void DeleteMedia() {
		MediacatalogNotificationHelper.showYesNoAlert(this, "Confirm Deletion"
			, "Are you sure you want to delete this item?"
			, new OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mDbHelper.deleteMEDIA(mRowId)) {
						setResult(RESULT_OK);
					} else {
						setResult(RESULT_CANCELED);
					}
					finish();
				}
			
			}, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Do nothing
				}
			}
		);
	}
	
	private void CancelAction() {
		setResult(RESULT_CANCELED);
		finish();
	}
		
}//End of class MediacatalogEditMedia
