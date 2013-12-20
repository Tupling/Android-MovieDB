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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;
import android.os.Bundle;

@SuppressLint("NewApi")
public class MovieDetailsActivity extends Activity implements
		MovieDetailsActivityFragment.DetailsListener {

	Context mContext;
	static MovieDetailsActivityFragment frag;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			
			return;
		}
		setContentView(R.layout.details_frag);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			Intent i = new Intent(getApplicationContext(),
					MovieDetailsActivity.class);
			frag = (MovieDetailsActivityFragment) getFragmentManager()
					.findFragmentById(R.id.details_fragment);
			i.putExtras(bundle);
			frag.movieResults(i);
			String movieTitle = bundle.getString("selectedMovie");
			setTitle(movieTitle);

		}

	}// onCreate closing bracket

	@Override
	public void returnPrevious() {
		finish();
		
	}

	

}// MovieDetailsClass closing bracket

