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

import java.io.BufferedInputStream;
import java.io.FileInputStream;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
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
	public static final Uri CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY+"/items");

		public static final String CONTENT_TYPE = "cnd.android.cursor.dir/vnd.daletupling.movies.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.daletupling.movies.item";

		public static final String TITLE = "original_title";
		public static final String DATE = "release_date";

		public static String[] PROJECTION = { "_Id", TITLE, DATE };

		private MovieData() {
		};

	}

	public static final int ITEMS = 1;
	public static final int ITEMS_ID = 2;

	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(CONTENT_AUTHORITY, "items/", ITEMS);
		uriMatcher.addURI(CONTENT_AUTHORITY, "items/#", ITEMS_ID);
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

		// TODO Auto-generated method stub
		String fileData = "";

		FileInputStream fis = null;
		try {
			fis = mContext.openFileInput(MovieService.FILENAME);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			while ((bytesRead = bis.read(contentBytes)) != -1) {
				fileData = new String(contentBytes, 0, bytesRead);
				fileData = contentBuffer.toString();
			}
			Log.i("FILE LOADED", "FILE HAS LOADED");
		} catch (Exception e) {

		}

		MatrixCursor result = new MatrixCursor(MovieData.PROJECTION);

		String dataString = fileData;
		Log.i("JSON FILE:", fileData.toString());
		JSONObject jsonObject = null;
		JSONArray movieArray = null;

		try {
			jsonObject = new JSONObject(dataString);
			movieArray = jsonObject.getJSONArray("results");

		} catch (JSONException e) {
			e.printStackTrace();
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
				Log.i("Movie Release:", movieArrayObj.getString("release_date"));
				
					result.addRow(new Object[] { i + 1,
							movieArrayObj.get("original_title"),
							movieArrayObj.get("release_date") });
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

		case ITEMS_ID:

		}

		return result;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
