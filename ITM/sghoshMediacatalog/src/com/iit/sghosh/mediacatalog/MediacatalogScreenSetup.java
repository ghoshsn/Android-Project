/*****************************************************************************
 * Name..........: MediacatalogScreenSetup.java
 * Description...: Activity for setting up the Media catalog screen
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import com.iit.sghosh.mediacatalog.R;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MediacatalogScreenSetup extends CursorAdapter {

	Cursor _cursor;
	int _selectedIndex = 0;

	public MediacatalogScreenSetup(Context context, Cursor c) {
		super(context, c);
		_cursor = c;
	}

	public void setSelected(int index) {

		if (index == -1) {
			// unselected
		} else {
			// selected index...
		}

		_selectedIndex = index;

		// notify the model that the data has changed, need to update the view
		notifyDataSetChanged();

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view != null) {
			CellRendererView cell = (CellRendererView) view;
			cell.display(cursor);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		CellRendererView cellRenderView = new CellRendererView(context);
		return cellRenderView;
	}

	public class CellRendererView extends TableLayout {

		private TextView _lblTitle;
		private ImageView _lblIcon;
		private TableRow _row;
		private TableRow _row2;
		private TableRow _row3;
		private TableLayout _tableLayout;
		private TextView _lblCategory;
		private ImageView _imgRating1;
		private ImageView _imgRating2;
		private ImageView _imgRating3;
		private ImageView _imgRating4;
		private ImageView _imgRating5;
		private static final int LEFT_PADDING = 2;
		private static final int TOP_PADDING = 2;
		private static final int RIGHT_PADDING = 2;
		private static final int BOTTOM_PADDING = 2;

		public CellRendererView(Context context) {

			super(context);
			_createUI();
		}

		private void _createUI() {

			// Setup controls behavoirs
			setColumnShrinkable(1, true);
			setColumnStretchable(1, true);
			setPadding(LEFT_PADDING, TOP_PADDING, RIGHT_PADDING, BOTTOM_PADDING);

			// Add Image
			LinearLayout main = new LinearLayout(getContext());
			_lblIcon = new ImageView(getContext());
			_lblIcon.setPadding(LEFT_PADDING, TOP_PADDING, RIGHT_PADDING,
					BOTTOM_PADDING);
			main.addView(_lblIcon);

			// Create Table
			_tableLayout = new TableLayout(getContext());

			// Author and Artist Row
			_row3 = new TableRow(getContext());
			_tableLayout.addView(_row3);

			// Title Row
			_row = new TableRow(getContext());
			_lblTitle = new TextView(getContext());
			_lblTitle.setPadding(LEFT_PADDING, TOP_PADDING, RIGHT_PADDING,
					BOTTOM_PADDING);
			_lblCategory = new TextView(getContext());
			_lblCategory.setPadding(LEFT_PADDING + 4, TOP_PADDING,
					RIGHT_PADDING, BOTTOM_PADDING);
			_row.addView(_lblTitle);
			_tableLayout.addView(_row);
			_row2 = new TableRow(getContext());
			LinearLayout ll = new LinearLayout(getContext());
			ll.setPadding(LEFT_PADDING + 4, TOP_PADDING, RIGHT_PADDING,
					BOTTOM_PADDING);

			_imgRating1 = new ImageView(getContext());
			_imgRating1.setImageResource(R.drawable.disabled_star);
			ll.addView(_imgRating1);
			_imgRating2 = new ImageView(getContext());
			_imgRating2.setImageResource(R.drawable.disabled_star);
			ll.addView(_imgRating2);
			_imgRating3 = new ImageView(getContext());
			_imgRating3.setImageResource(R.drawable.disabled_star);
			ll.addView(_imgRating3);
			_imgRating4 = new ImageView(getContext());
			_imgRating4.setImageResource(R.drawable.disabled_star);
			ll.addView(_imgRating4);
			_imgRating5 = new ImageView(getContext());
			_imgRating5.setImageResource(R.drawable.disabled_star);
			ll.addView(_imgRating5);
			ll.addView(_lblCategory);
			_row2.addView(ll);
			_tableLayout.addView(_row2);
			main.addView(_tableLayout);
			addView(main);
		}

		public void display(Cursor c) {
			if (c != null) {

				String title = c.getString(c
						.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TITLE));
				String subtype = c.getString(c
						.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE));
				String type = c.getString(
						c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))
						.toUpperCase();
				if (type.equals("AUDIO")) {
					String artist = c.getString(c
							.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_ARTIST));
				} else if (type.equals("BOOK")) {
					String author = c.getString(c
							.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_AUTHOR));
				}

				int rank = c.getInt(c
						.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_RANK));
				_lblTitle.setText(title + " - " + subtype);
				String category = c.getString(c
						.getColumnIndex(MediacatalogDataBaseSetup.KEY_CATEGORY_NAME));
				_lblCategory.setText(category);
				if (rank > 0) {
					_imgRating1.setImageResource(R.drawable.star);
				} else {
					_imgRating1.setImageResource(R.drawable.disabled_star);
				}
				if (rank > 1) {
					_imgRating2.setImageResource(R.drawable.star);
				} else {
					_imgRating2.setImageResource(R.drawable.disabled_star);
				}
				if (rank > 2) {
					_imgRating3.setImageResource(R.drawable.star);
				} else {
					_imgRating3.setImageResource(R.drawable.disabled_star);
				}
				if (rank > 3) {
					_imgRating4.setImageResource(R.drawable.star);
				} else {
					_imgRating4.setImageResource(R.drawable.disabled_star);
				}
				if (rank > 4) {
					_imgRating5.setImageResource(R.drawable.star);
				} else {
					_imgRating5.setImageResource(R.drawable.disabled_star);
				}
				{
					if (c.getString(
							c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))
							.toUpperCase().equals("VIDEO")) {
						if (c.getString(
								c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
								.toUpperCase().equals("DVD")) {
							_lblIcon.setImageResource(R.drawable.dvd_large);
						} else if (c
								.getString(
										c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
								.toUpperCase().equals("MP4")) {
							_lblIcon.setImageResource(R.drawable.mp4);
						} 
					else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
							.toUpperCase().equals("AIFF")) {
						_lblIcon.setImageResource(R.drawable.aiff);
					}
					else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
							.toUpperCase().equals("WAV")) {
						_lblIcon.setImageResource(R.drawable.wav);
					}
					else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
							.toUpperCase().equals("AVI")) {
						_lblIcon.setImageResource(R.drawable.avi);
					}
					else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
							.toUpperCase().equals("FLASH")) {
						_lblIcon.setImageResource(R.drawable.flash);
					}
					else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
							.toUpperCase().equals("QUICKTIME")) {
						_lblIcon.setImageResource(R.drawable.quicktime);
					}
						else if (c
								.getString(
										c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
								.toUpperCase().equals("VHS")) {
							_lblIcon.setImageResource(R.drawable.vhs);
					}
						else if (c
								.getString(
										c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
								.toUpperCase().equals("DIVX")) {
							_lblIcon.setImageResource(R.drawable.divx);
					}
						else if (c
								.getString(
										c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
								.toUpperCase().equals("MKV")) {
							_lblIcon.setImageResource(R.drawable.mkv);
					}
						else if (c
								.getString(
										c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
								.toUpperCase().equals("OGG")) {
							_lblIcon.setImageResource(R.drawable.ogg);
					}
					 else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_SUBTYPE))
							.toUpperCase().equals("BLU-RAY")) {
						_lblIcon.setImageResource(R.drawable.bluray_large);
					}
						
						else {
							_lblIcon.setImageResource(R.drawable.video);
						}
					} else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))
							.toUpperCase().equals("GAME")) {
						_lblIcon.setImageResource(R.drawable.controller);
					} else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))
							.toUpperCase().equals("BOOK")) {
						_lblIcon.setImageResource(R.drawable.book);
					} else if (c
							.getString(
									c.getColumnIndex(MediacatalogDataBaseSetup.KEY_MEDIA_TYPE))
							.toUpperCase().equals("AUDIO")) {
						_lblIcon.setImageResource(R.drawable.violin);
					} else {
						_lblIcon.setImageResource(R.drawable.bluray_large);
					}
				} 
			}
		}
	}

} //end of MediacatalogScreenSetup class
