/*****************************************************************************
 * Name..........: MediacatalogLookupData.java
 * Description...: Activity for media look up
 *****************************************************************************/
package com.iit.sghosh.mediacatalog;

public class MediacatlogLookupData {

	/***********CLASS FIELDS******/
	
	private String _title = "";
	private String _rating = "";
	private String _lookupMessage = "";
	private boolean _lookupValid = false;

	/*****ACCESSORS****/
	
	public String getTitle() {
		return _title;
	}// End of public accessor getTitle()

	public String getRating() {
		return _rating;
	}// End of public accessor getRating()


	public String getLookupMessage() {
		return _lookupMessage;
	}// End of public accessor getLookupMessage()

	public boolean isLookupValid() {
		return _lookupValid;
	}// End of public accessor isLookupValid()

	/*******MUTATORS******/
	
	public void setTitle(String title) {
		_title = title;
	}// End of public mutator setTitle(String)

	public void setRating(String rating) {
		_rating = rating;
	}// End of public mutator setRating(String)


	public void setLookupMessage(String lookupMessage) {
		_lookupMessage = lookupMessage;
	}// End of public mutator setLookupMessage(String)

	public void setLookupValidity(boolean valid) {
		_lookupValid = valid;
	}// End of public mutator setLookupValidity(Boolean)

} // end of MediacatlogLookupData class
