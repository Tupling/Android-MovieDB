package com.daletupling.movies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class DetailsFragment extends Fragment {

	private DetailsListener d_listener;

	public interface DetailsListener {
		
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

		ScrollView view = (ScrollView) inflater.inflate(
				R.layout.details_layout, container, false);


		return view;
	}

}
