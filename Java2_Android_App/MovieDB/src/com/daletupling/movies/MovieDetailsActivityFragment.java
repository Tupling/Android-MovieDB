package com.daletupling.movies;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

@SuppressLint("NewApi")
public class MovieDetailsActivityFragment extends Fragment {

	TextView movie_title;
	TextView movie_release;
	static ImageView poster;
	TextView vote_count;
	TextView vote_avg;
	static Bitmap bmp;
	Button return_button;
	ActionBar actionBar;

	private DetailsListener d_listener;

	public interface DetailsListener {
		public void returnPrevious();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			d_listener = (DetailsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.details_layout, container, false);

		// Layout elements
		movie_title = (TextView) view.findViewById(R.id.movie_title);
		movie_release = (TextView) view.findViewById(R.id.movie_release);
		vote_count = (TextView) view.findViewById(R.id.vote_count);
		vote_avg = (TextView) view.findViewById(R.id.vote_avg);
		poster = (ImageView) view.findViewById(R.id.poster);
		return_button = (Button) view.findViewById(R.id.return_button);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return_button.setVisibility(View.GONE);
		}

		return_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				d_listener.returnPrevious();

			}

		});

		return view;
	}

	public void movieResults(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			// String declarations for passed items
			String movieTitle = bundle.getString("selectedMovie");
			String movieRelease = bundle.getString("selectedRelease");
			String posterPath = bundle.getString("posterPath");
			String voteCount = bundle.getString("voteCount");
			String voteAvg = bundle.getString("voteAvg");

			Log.i("POSTER", posterPath);
			Log.i("MOVIETITLE", movieTitle);

			// Set layout elements
			movie_title.setText(movieTitle);
			movie_release.setText(movieRelease);
			// setTitle(movieTitle);
			vote_count.setText("Votes: " + voteCount);
			vote_avg.setText("Vote Average: " + voteAvg);

			String baseURL = "https://d3gtl9l2a4fn1j.cloudfront.net/t/p/w185"
					+ posterPath;

			GetImage getPoster = new GetImage();
			// Call to execute GetImage with baseURL String
			if (!posterPath.toString().equals("null")) {
				getPoster.execute(new String[] { baseURL });

			} else {
				poster.setImageResource(R.drawable.poster_unavailable);
			}

		}

	}

	// Class class method to get image from string url
	public static class GetImage extends AsyncTask<String, Void, Bitmap> {

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

}
