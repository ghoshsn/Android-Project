/*****************************************************************************
 * Name..........: MediacatalogCategoriesScreenSetup.java
 * 
 * Description...: Main activity for the MEDIA CATALOG application
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

import com.iit.sghosh.mediacatalog.R;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MediacatalogCategoriesScreenSetup extends CursorAdapter {

	Cursor _cursor;
	Context _context;

	public MediacatalogCategoriesScreenSetup(Context context, Cursor c) {
		super(context, c);
		_context = context;
		_cursor = c;
	}// End of constructor

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view != null) {
			CellRendererView cell = (CellRendererView) view;
			cell.display(cursor);
		}
	}// End of method public void bindView(View, Context, Cursor)

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		CellRendererView cellRenderView = new CellRendererView();
		return cellRenderView;
	}// End of method public View newView(Context, Cursor, ViewGroup)

	/**
	 * Embedded class for rendering the content of a given list item. 
	*/
	public class CellRendererView extends TableLayout {

		private ImageView _imgContactName;
		private TextView _lblName;
		private TableRow _Row;
		private static final int LEFT_PADDING = 2;
		private static final int TOP_PADDING = 2;
		private static final int RIGHT_PADDING = 2;
		private static final int BOTTOM_PADDING = 2;

		/**
		 * Default constructor.
		 */
		public CellRendererView() {
			super(_context);
			_createUI();
		}// End of construcor

		private void _createUI() {

			// Setup controls behavoirs
			setColumnShrinkable(1, true);
			setColumnStretchable(1, true);

			// set the padding
			setPadding(LEFT_PADDING, TOP_PADDING, RIGHT_PADDING, BOTTOM_PADDING);

			// single row that holds icon/flag & name
			_Row = new TableRow(_context);
			_imgContactName = new ImageView(_context);
			_imgContactName.setPadding(LEFT_PADDING, TOP_PADDING,
					RIGHT_PADDING, BOTTOM_PADDING);
			_lblName = new TextView(_context);
			_lblName.setPadding(LEFT_PADDING + 4, TOP_PADDING, RIGHT_PADDING,
					BOTTOM_PADDING);

			_Row.addView(_imgContactName);
			_Row.addView(_lblName);
			addView(_Row);
		}// End of method private void _createUI()

		public void display(Cursor c) {
			if (c != null) {
				String type = c.getString(c
						.getColumnIndex(MediacatalogDataBaseSetup.KEY_CATEGORY_TYPE));
				if (type.toUpperCase().equals("VIDEO")) {
					_imgContactName.setImageResource(R.drawable.video);
				} else if (type.toUpperCase().equals("GAME")) {
					_imgContactName.setImageResource(R.drawable.controller);
				} else if (type.toUpperCase().equals("BOOK")) {
					_imgContactName.setImageResource(R.drawable.book);
				} else if (type.toUpperCase().equals("AUDIO")) {
					_imgContactName.setImageResource(R.drawable.violin);
				}
				_lblName.setText(c.getString(c
						.getColumnIndex(MediacatalogDataBaseSetup.KEY_CATEGORY_NAME)));
			}
		}// End of method public void display(Cursor)

	}// End of embedded class CellRenderView

}// End of class CategoriesPanalAdapter
