/*****************************************************************************
 * Name..........: MediacatalogNotificationHelper.java
 * Description...: Notification helper
 *****************************************************************************/

package com.iit.sghosh.mediacatalog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class MediacatalogNotificationHelper {

	/**
	 * This method displays an alert to the user in dialog format
	 * 
	 * @param context
	 *            The context to create the alert in
	 * @param title
	 *            The title of the alert
	 * @param body
	 *            The text of the alert
	 */
	public static void showAlert(Context context, String title, String body) {
		Builder b = new AlertDialog.Builder(context);
		b.setTitle(title);
		b.setMessage(body);
		b.show();
	}// End of method private static void showAlert(Context, String, String)

	/**
	 * Shows a dialog with Yes/No buttons
	 * 
	 * @param context
	 * @param title
	 * @param body
	 * @param yes
	 * @param no
	 */
	public static void showYesNoAlert(Context context, String title,
			String body, OnClickListener yes, OnClickListener no) {
		Builder b = new AlertDialog.Builder(context);
		b.setTitle(title);
		b.setMessage(body);
		b.setPositiveButton("Yes", yes);
		b.setNegativeButton("No", no);
		b.show();
	}// End of method public static void showYesNoAlert(Context, String, String)

	/**
	 * Shows a dialog with an Okay button
	 * 
	 * @param context
	 * @param title
	 * @param body
	 * @param ok
	 */
	public static void showOkAlert(Context context, String title, String body,
			OnClickListener ok) {
		Builder b = new AlertDialog.Builder(context);
		b.setTitle(title);
		b.setMessage(body);
		b.setPositiveButton("Okay", ok);
		b.show();
	}// End of method public static void showOkAlert
}
