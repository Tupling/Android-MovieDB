/*
 * Project:        MoviesDB
 * 
 * Package: com.daletupling.movies
 * 
 * Author:         Dale Tupling
 * 
 * Date:        December 5th, 2013
 * 
 */

package libs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class MovieContentProvider extends ContentProvider {
	Context mContext;

	public static final String CONTENT_AUTHORITY = "com.daletupling.movies.provider";

	public static String MOVIEFILE = MovieService.FILENAME;

	public static class MovieData implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ CONTENT_AUTHORITY + "/items");

		public static final String CONTENT_TYPE = "cnd.android.cursor.dir/vnd.daletupling.movies.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.daletupling.movies.item";

		public static final String TITLE = "original_title";
		public static final String DATE = "release_date";
		public static final String POSTER = "poster_path";
		public static final String VOTECOUNT = "vote_count";
		public static final String VOTEAVG = "vote_average";

		public static String[] PROJECTION = { "_Id", TITLE, DATE, POSTER,
				VOTECOUNT, VOTEAVG };

		private MovieData() {
		};

	}

	public static final int ITEMS = 1;
	public static final int ITEMS_ID = 2;
	public static final int ITEMS_RELEASE_FILTER = 3;

	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(CONTENT_AUTHORITY, "items/", ITEMS);
		uriMatcher.addURI(CONTENT_AUTHORITY, "items/#", ITEMS_ID);
		uriMatcher.addURI(CONTENT_AUTHORITY, "items/year/*",
				ITEMS_RELEASE_FILTER);
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			return MovieData.CONTENT_TYPE;

		case ITEMS_ID:
			return MovieData.CONTENT_ITEM_TYPE;
		}

		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		MatrixCursor result = new MatrixCursor(MovieData.PROJECTION);

		String JSONData = Storage.readFile(getContext(), MOVIEFILE);

		Log.i("JSON FILE:", JSONData);

		JSONObject jsonObject = null;
		JSONArray movieArray = null;
		if (JSONData != null) {
			try {
				jsonObject = new JSONObject(JSONData);
				movieArray = jsonObject.getJSONArray("results");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.i("FILEDATA", "FILEDATA IS NULL!");
		}
		if (movieArray == null) {

			return result;
		}

		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			for (int i = 0; i < movieArray.length(); i++) {
				try {
					JSONObject movieArrayObj = movieArray.getJSONObject(i);
					Log.i("Movie Titles:",
							movieArrayObj.getString("original_title"));
					Log.i("Movie Release:",
							movieArrayObj.getString("release_date"));
					Log.i("Movie Poster:",
							movieArrayObj.getString("poster_path"));

					result.addRow(new Object[] { i + 1,
							movieArrayObj.get("original_title"),
							movieArrayObj.get("release_date"),
							movieArrayObj.get("poster_path"),
							movieArrayObj.get("vote_count"),
							movieArrayObj.get("vote_average") });
				} catch (JSONException e) {

					e.printStackTrace();
				}// catch closing bracket
			}// for loop closing bracket
			break;
		// Not actually using a filter for ITEMS_ID added this via instructions
		// from video
		// so i could get an idea of how ID filter works.
		case ITEMS_ID:
			String itemID = uri.getLastPathSegment();
			int index;
			try {
				index = Integer.parseInt(itemID);
			} catch (NumberFormatException e) {
				break;
			}// catch closing bracket
			if (index <= 0 || index > movieArray.length()) {
				break;
			}// if statement closing bracket
			try {
				JSONObject movieArrayObj = movieArray.getJSONObject(index - 1);
				// add new row to result
				result.addRow(new Object[] { index + 1,
						movieArrayObj.get("original_title"),
						movieArrayObj.get("release_date"),
						movieArrayObj.get("poster_path"),
						movieArrayObj.get("vote_count"),
						movieArrayObj.get("vote_average") });
			} catch (JSONException e) {
				e.printStackTrace();
			}// catch closing bracket

			break;
		case ITEMS_RELEASE_FILTER:
			// get the last Segment of uri and place it into string
			String yearFilter = uri.getLastPathSegment();
			// loop through movieArray
			for (int i = 0; i < movieArray.length(); i++) {
				try {
					JSONObject movieArrayObj = movieArray.getJSONObject(i);
					// check for yearFilter string contents to be contained in
					// JSON "release_date"
					if (movieArrayObj.getString("release_date").contains(
							yearFilter)) {
						// add new row to result
						result.addRow(new Object[] { i + 1,
								movieArrayObj.get("original_title"),
								movieArrayObj.get("release_date"),
								movieArrayObj.get("poster_path"),
								movieArrayObj.get("vote_count"),
								movieArrayObj.get("vote_average") });
						Log.e("RESULT:", result.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			// display invalid URI
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"The search you attempted was invalid and return 0 responses.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			Log.e("query", "INVALID URI = " + uri.toString());

		}

		return result;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
