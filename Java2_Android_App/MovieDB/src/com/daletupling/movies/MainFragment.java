package com.daletupling.movies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {

	private MovieListener m_listener;

	public interface MovieListener {
		public void findMovies(String string);

		public void movieSearch(String string);

		public void filterMovies();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.linear_layout, container, false);

		// Search Button
		Button search_button = (Button) view.findViewById(
				R.id.search_button);
		search_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText search = (EditText) getActivity().findViewById(R.id.search_input);
				String search_string = search.getText().toString();
				m_listener.findMovies(search_string);

			}// onClick closing bracket

		});// SEARCH BUTTON onClickListener closing bracket

		// Filter button
		Button filter_button = (Button) view.findViewById(
				R.id.filter_button);
		// disable filter button
		filter_button.setVisibility(View.GONE);
		filter_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				m_listener.filterMovies();
			}
		});// FILTER BUTTON on clickListener closing bracket

		// Refresh Button
		Button refresh_button = (Button) view.findViewById(
				R.id.refresh_previous);
		refresh_button.setVisibility(View.GONE);
		refresh_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText search = (EditText) getActivity().findViewById(R.id.search_input);
				//String search_string = search.getText().toString();
				
				Button refresh_button = (Button) getActivity().findViewById(R.id.refresh_previous);
				//Button filter_button = (Button) getActivity().findViewById(R.id.filter_button);
				
				refresh_button.setVisibility(View.GONE);
				//filter_button.setVisibility(View.VISIBLE);
				search.setText("");
				search.setHint(R.string.filter_hint);
				m_listener.movieSearch(search.toString());
			}
		});// refresh button closing bracket
		return view;
	};

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			m_listener = (MovieListener) activity;
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			throw new ClassCastException(activity.toString());

		}
	}
}
