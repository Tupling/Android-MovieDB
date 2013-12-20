/*
 * Project:        MoviesDB
 * 
 * Package: com.daletupling.movies
 * 
 * Author:         Dale Tupling
 * 
 * Date:        December 10th, 2013
 * 
 */

package com.daletupling.movies;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MovieDetailsActivity extends Activity implements
		DetailsFragment.DetailsListener {

	Context mContext;
	TextView movie_title;
	TextView movie_release;
	ImageView poster;
	TextView vote_count;
	TextView vote_avg;
	Bitmap bmp;
	Bitmap image;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_frag);
		// Setup Action Bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Layout elements
		movie_title = (TextView) findViewById(R.id.movie_title);
		movie_release = (TextView) findViewById(R.id.movie_release);
		vote_count = (TextView) findViewById(R.id.vote_count);
		vote_avg = (TextView) findViewById(R.id.vote_avg);
		poster = (ImageView) findViewById(R.id.poster);

		// String declarations for passed items
		Intent i = getIntent();
		String movieTitle = i.getStringExtra("selectedMovie");
		String movieRelease = i.getStringExtra("selectedRelease");
		String posterPath = i.getStringExtra("posterPath");
		String voteCount = i.getStringExtra("voteCount");
		String voteAvg = i.getStringExtra("voteAvg");

		Log.i("POSTER", posterPath);
		Log.i("MOVIETITLE", movieTitle);

		// Set layout elements
		movie_title.setText(movieTitle);
		movie_release.setText(movieRelease);
		setTitle(movieTitle);
		vote_count.setText("Votes: " + voteCount);
		vote_avg.setText("Vote Average: " + voteAvg);

		String baseURL = "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/w342"
				+ posterPath;

		// Creating subclass object
		GetImage getPoster = new GetImage();
		// Call to execute GetImage with baseURL String
		if (!posterPath.toString().equals("null")) {
			getPoster.execute(new String[] { baseURL });
			poster.setImageBitmap(bmp);
		} else {
			poster.setImageResource(R.drawable.poster_unavailable);
		}

	}// onCreate closing bracket

	// Class class method to get image from string url
	public class GetImage extends AsyncTask<String, Void, Bitmap> {

		@Override
		// Call downloadPost method in background
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			bmp = null;
			for (String url : urls) {

				bmp = downloadPoster(url);
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// sets imageView when comeplete AsyncTask
			poster.setImageBitmap(result);
		}

		// Download image through inputstream by getting httpConnection
		private Bitmap downloadPoster(String url) {
			Bitmap bm = null;
			InputStream is = null;
			BitmapFactory.Options bm_o = new BitmapFactory.Options();
			bm_o.inSampleSize = 1;

			try {
				is = getHttpConnection(url);
				bm = BitmapFactory.decodeStream(is, null, bm_o);
				is.close();

			} catch (IOException io_e) {
				io_e.printStackTrace();
			}
			return bm;
		}

		// Get httpConnection
		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream is = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpC = (HttpURLConnection) connection;
				httpC.setRequestMethod("GET");
				httpC.connect();

				if (httpC.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is = httpC.getInputStream();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return is;
		}
	}// AsyncTask closing bracket

	// Pass back string to MainActivity
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent backIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		String previous = movie_title.getText().toString();
		backIntent.putExtra("previous", previous);

		setResult(RESULT_OK, backIntent);
		finish();
		return true;
	}



}// MovieDetailsClass closing bracket
