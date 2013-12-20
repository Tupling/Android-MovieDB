package com.daletupling.movies;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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
	ImageView poster;
	TextView vote_count;
	TextView vote_avg;
	Bitmap bmp;
	Bitmap image;
	Button return_button;
	ActionBar actionBar;
	
	private DetailsListener d_listener;

	public interface DetailsListener {
		public void moviePoster(String posterString);
		public void returnPrevious();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			d_listener = (DetailsListener) activity;
		}catch (ClassCastException e){
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
		

		
		return_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				d_listener.returnPrevious();
				
			}
			
		});

		return view;
	}
	

	public void movieResults() {
		Intent intent = getActivity().getIntent();
		
		// String declarations for passed items
		String movieTitle = intent.getStringExtra("selectedMovie");
		String movieRelease = intent.getStringExtra("selectedRelease");
		String posterPath = intent.getStringExtra("posterPath");
		String voteCount = intent.getStringExtra("voteCount");
		String voteAvg = intent.getStringExtra("voteAvg");

		Log.i("POSTER", posterPath);
		Log.i("MOVIETITLE", movieTitle);

		// Set layout elements
		movie_title.setText(movieTitle);
		movie_release.setText(movieRelease);
		//setTitle(movieTitle);
		vote_count.setText("Votes: " + voteCount);
		vote_avg.setText("Vote Average: " + voteAvg);

		d_listener.moviePoster(posterPath);



	}

}
