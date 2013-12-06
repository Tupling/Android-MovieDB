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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContentProvider extends ContentProvider {

	public static final String CONTENT_AUTHORITY = "com.daletupling.movies.libs.MovieContenProvider";
	
	public static class MovieData implements BaseColumns{
		public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY +"/movies");
		
		public static final String CONTENT_TYPE= "vnd.android.curser.dir/vnd.daletupling.movies.movies";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.curser.movies/vnd.daletupling.movies.movies";
		
		public static final String TITLE = "title";
		public static final String DATE = "release_date";

		public static String[] PROJECTION = {"Id", TITLE, DATE};
		
	private MovieData (){};
	
	}
	
	public static final int MOVIES = 1;
	public static final int MOVIES_ID = 2;
	
	
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		uriMatcher.addURI(CONTENT_AUTHORITY, "movies/", MOVIES);
		uriMatcher.addURI(CONTENT_AUTHORITY, "movies/#", MOVIES_ID);
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(uriMatcher.match(uri)){
		case MOVIES:
			return MovieData.CONTENT_TYPE;
			
		case MOVIES_ID:
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
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		
		
	
		
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}