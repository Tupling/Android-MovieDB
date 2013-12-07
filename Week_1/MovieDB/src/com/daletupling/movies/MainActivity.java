/*
 * Project:        MoviesDB
 * 
 * Package: com.daletupling.movies
 * 
 * Author:         Dale Tupling
 * 
 * Date:        December 2nd, 2013
 * 
 */
package com.daletupling.movies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daletupling.libs.WebData;

import libs.MovieContentProvider;
import libs.MovieService;

import android.net.Uri;
//import libs.Data.getData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//Handler Leak suppress for movieHandler
@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	public static Context mContext;
	// Layout elements
	EditText search;
	Button search_button;
	Button filter_button;
	ListView movie_list;

	SimpleAdapter listA;

	// Connection boolean declare
	Boolean connected = false;

	// Search string variables
	String search_string;

	List<Map<String, String>> movieListMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linear_layout);
		mContext = this;

		// instantiate EditText, ListView
		search = (EditText) findViewById(R.id.search_input);
		movie_list = (ListView) findViewById(R.id.movie_list);

		// Instantiate button
		search_button = (Button) findViewById(R.id.search_button);
		// Set button onClick functionality
		search_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String tempString = search.getText().toString();
				// Check for empty search string display error dialog if it is
				// empty.
				search_string = tempString.replaceAll(" ", "%20");
				if (search_string != null) {

					// Pass search string to movieSearch method
					movieSearch(search_string);
					// close keyboard when Search is clicked
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(((View) v).getWindowToken(), 0);

					search.setText("");
					search.setHint("Filter movies or new search...");
				}// if statement closing bracket

			}// onClick closing bracket

		});// onClickListener closing bracket

		// Filter button
		filter_button = (Button) findViewById(R.id.filter_button);
		// disable filter button
		filter_button.setEnabled(false);
		filter_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri yearUri = Uri
						.parse(MovieContentProvider.MovieData.CONTENT_URI
								.toString()+"/year/"+search.getText().toString());
				Log.i("YEAR URI", yearUri.toString());
					displayMovies(yearUri);

			}
		});// filter button on clickListener closing bracket

		// Run network status method from WebData class in Network jar file
		// Check Network Status
		connected = WebData.getStatus(mContext);
		if (connected) {
			Log.i("NETWORK", WebData.getType(mContext));

		} else {
			// Display dialog box for no connection
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"No Internet Connection Detected. Check your connection and try again.")
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

		}// network connection if statement closing bracket
	}// onCreate closing bracket

	private Handler movieHandler = new Handler() {
		public void handleMessage(Message message) {
			Object dataObj = message.obj;
			if (message.arg1 == RESULT_OK && dataObj != null) {
				// String movieResults = (String) message.obj.toString();
				// set enable once data has been found from previous search or
				// new search
				filter_button.setEnabled(true);

				Uri startURI = Uri
						.parse(MovieContentProvider.MovieData.CONTENT_URI
								.toString());
					displayMovies(startURI);
			}// message check statement closing bracket
		}// handlerMessage closing bracket
	};// Handler closing bracket

	// Movie search method
	private void movieSearch(String search_string) {

		Log.i("SEARCH:", search_string);

		Messenger messenger = new Messenger(movieHandler);
		Intent intent = new Intent(mContext, MovieService.class);
		intent.putExtra("search_string", search_string);
		intent.putExtra("messenger", messenger);
		startService(intent);
	}// movieSearch Closing bracket
	
	public void displayMovies(Uri uri){
		Cursor cursor = getContentResolver().query(uri, null,
				null, null, null);
		if (cursor == null) {
			Log.e("Cursor Null:", uri.toString());
		} else {
			if (movieListMap != null) {
				movieListMap.clear();
			}
			if (cursor.moveToFirst() == true) {
				movieListMap = new ArrayList<Map<String, String>>();
				for (int i = 0; i < cursor.getCount(); i++) {
					Map<String, String> map = new HashMap<String, String>(
							2);

					map.put("title", cursor.getString(1));
					if(cursor.getString(2).equals("")){
						map.put("release", "No Date");
					}else{
					
					map.put("release", cursor.getString(2).substring(0,4));
					}
				
					movieListMap.add(map);
					cursor.moveToNext();
				}

				listA = new SimpleAdapter(mContext, movieListMap,
						R.layout.list_layout, new String[] { "title",
								"release" }, new int[] {
								R.id.title_text, R.id.release_text });
				movie_list.setAdapter(listA);
			}
		}
		cursor.close();
	}

}