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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//Handler Leak suppress for movieHandler

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements
		MainFragment.MovieListener {

	public static Context mContext;
	// Layout elements
	EditText search;
	Button filter_button;
	ListView movie_list;
	TextView previous_text;
	TextView previous_movie;

	SimpleAdapter listA;

	// Connection boolean declare
	Boolean connected = false;

	// Search string variables
	String search_string;

	List<Map<String, String>> movieListMap;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_frag);
		mContext = this;
		movieListMap = new ArrayList<Map<String, String>>();

		previous_text = (TextView) findViewById(R.id.previous_viewed);
		previous_text.setVisibility(View.GONE);

		previous_movie = (TextView) findViewById(R.id.previous_movie);
		previous_movie.setVisibility(View.GONE);

		if (movieListMap != null) {
			listA = new SimpleAdapter(mContext, movieListMap,
					R.layout.list_layout, new String[] { "title", "release" },
					new int[] { R.id.title_text, R.id.release_text });
			// Set ListAdapter to previous created listA
			// movie_list.setAdapter(listA);
		}

		// instantiate EditText, ListView
		search = (EditText) findViewById(R.id.search_input);
		movie_list = (ListView) findViewById(R.id.movie_list);

		movie_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> passedMap = (HashMap<String, String>) movie_list
						.getItemAtPosition(position);

				Intent i = new Intent(getApplicationContext(),
						MovieDetailsActivity.class);

				i.putExtra("selectedMovie", passedMap.get("title"));
				i.putExtra("selectedRelease", passedMap.get("release"));
				i.putExtra("posterPath", passedMap.get("poster"));
				i.putExtra("voteCount", passedMap.get("votes"));
				i.putExtra("voteAvg", passedMap.get("vote_avg"));
				startActivityForResult(i, position);

			}

		});// movie list on click listener closing bracket

		// Check Network Status
		checkConnection();

	}// onCreate closing bracket

	private Handler movieHandler = new Handler() {
		public void handleMessage(Message message) {
			Object dataObj = message.obj;
			if (message.arg1 == RESULT_OK && dataObj != null) {
				String movieResults = (String) dataObj.toString();
				// set enable once data has been found from previous search or
				// new search
				Log.i("DATA OBJECT", movieResults);
				JSONObject jsonObject = null;
				JSONArray movieArray = null;
				try {
					jsonObject = new JSONObject(movieResults);
					movieArray = jsonObject.getJSONArray("results");

				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.i("MOVIE ARRAY STRING", movieArray.toString());
				if (movieArray.toString().equals("[]")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage(
							"Your movie search returned 0 movies. Please enter another keyword and perform new search.")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				} else {
					Button filter_button = (Button) findViewById(R.id.filter_button);
					filter_button.setVisibility(View.VISIBLE);

					Uri startURI = Uri
							.parse(MovieContentProvider.MovieData.CONTENT_URI
									.toString());
					displayMovies(startURI);
				}

			}// message check statement closing bracket
		}// handlerMessage closing bracket
	};// Handler closing bracket

	public void displayMovies(Uri uri) {

		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor == null) {
			Log.e("Cursor Null:", uri.toString());
		} else {
			if (movieListMap != null) {
				movieListMap.clear();
			}
			if (cursor.moveToFirst() == true) {

				for (int i = 0; i < cursor.getCount(); i++) {
					Map<String, String> map = new HashMap<String, String>(2);

					map.put("title", cursor.getString(1));
					map.put("poster", cursor.getString(3));
					map.put("votes", cursor.getString(4));
					map.put("vote_avg", cursor.getString(5));
					if (cursor.getString(2).equals("")) {
						map.put("release", "No Date");
					} else {

						map.put("release", cursor.getString(2).substring(0, 4));
					}

					movieListMap.add(map);
					cursor.moveToNext();
				}

				listA = new SimpleAdapter(mContext, movieListMap,
						R.layout.list_layout,
						new String[] { "title", "release" }, new int[] {
								R.id.title_text, R.id.release_text });

				// Set ListAdapter to previous created listA
				movie_list.setAdapter(listA);

			}
		}
		cursor.close();
	}// displayMovies method closing bracket


	@Override
	public void findMovies(String string) {
		String tempString = string;
		// Check for empty search string display error dialog if it is
		// empty.

		if (!tempString.equals("")) {
			search_string = tempString.replaceAll(" ", "%20");
			// Pass search string to movieSearch method
			movieSearch(search_string);
			// close keyboard when Search is clicked
			closeKeyboard();

			search.setText("");
			search.setHint(R.string.filter_hint);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"You did not enter a valid search keyword. Please enter a movie title or keyword that may be in the movie you are looking for.")
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
		}// if statement closing bracket

	}// movieSearch Closing bracket
	
	//
	//
	//
	// May need to add override
	public void movieSearch(String string) {

		Log.i("SEARCH:", search_string);

		Messenger messenger = new Messenger(movieHandler);
		Intent intent = new Intent(mContext, MovieService.class);
		intent.putExtra("search_string", search_string);
		intent.putExtra("messenger", messenger);
		startService(intent);
	}// movieSearch Closing bracket

	

	//FILTER MOVIE MOVIELISTENER
	@Override
	public void filterMovies() {
		closeKeyboard();
		// TODO Auto-generated method stub
		String filter_Valid = search.getText().toString();

		Pattern stringPattern = Pattern.compile("^[0-9]{1,4}$");
		Matcher matchString = stringPattern.matcher(filter_Valid);
		// Check for empty keyword, if empty display dialog otherwise
		// continue to check if keyword is valid
		if (filter_Valid.equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"Filter keyword is empty. Please enter a keyword and try again.")
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

			// check whether keyword meets criteria set via regex.

		} else if (!matchString.matches()) {
			// Display dialog box for invalid filter string only allows
			// ^[0-9]{1,4}$
			// number 0-9 and only 4 character, all characters must be
			// numbers
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"Filter keyword may only contain numbers 0-9 and only 4 characters. Please try entering a valid keyword.")
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

		} else {
			
			Uri yearUri = Uri.parse(MovieContentProvider.MovieData.CONTENT_URI
					.toString() + "/year/" + search.getText().toString());
			Log.i("YEAR URI", yearUri.toString());
			displayMovies(yearUri);
			Button refresh_button = (Button) findViewById(R.id.refresh_previous);
			Button filter_button = (Button) findViewById(R.id.filter_button);
			refresh_button.setVisibility(View.VISIBLE);
			filter_button.setVisibility(View.GONE);
		}

	}

	//ON ACTIVITY RESULT
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (resultCode == RESULT_OK && intent != null) {
			String previousMovie = intent.getExtras().getString("previous");
			previous_movie.setVisibility(View.VISIBLE);
			previous_text.setVisibility(View.VISIBLE);
			previous_movie.setText(previousMovie);
		}

	}

	//CHECK NETWORK STATUS
	public void checkConnection() {

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
			Button search_button = (Button) findViewById(R.id.search_button);
			search_button.setEnabled(false);

		}// network connection if statement closing bracket
	}



	public void closeKeyboard(){
		// close keyboard when Search is clicked
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
	}

}// MainActivity closing bracket