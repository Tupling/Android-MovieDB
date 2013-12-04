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

import libs.Data;
import libs.Data.getData;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



public class MainActivity extends Activity {
	public static Context mContext;
	//Layout elements
	EditText search;
	Button search_button;
	ListView movie_list;
	
	//Connection boolean declare
	Boolean connected = false;
	
	
	//Search string variables
	String search_string;
	public static String finalSearch;
	
	//API Url Variables
	public static String initialURL = "";
	public static String finalURL;
	
	//ListAdapter
	public static ArrayAdapter<String>listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);
        mContext = this;
        
        //instantiate EditText, ListView
        search = (EditText) findViewById(R.id.search_input);
        movie_list = (ListView) findViewById(R.id.movie_list);
        //ListView Adapter Instantiate
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Data.movies);
        //set ListView Adapter
        movie_list.setAdapter(listAdapter);
       
        
        //Instantiate button
        search_button = (Button) findViewById(R.id.search_button);
        //Set button onClick functionality
        search_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Clear adapter to prepare for new data
				listAdapter.clear();
				
				//Get search string from EditText and set as search_string
				search_string = search.getText().toString();
				
				if(search_string != null){
					//replace search_string spaces, and set as finalSearch
					finalSearch = search_string.replaceAll(" ",  "%20");
					//Call getData method from Data class
					getData data = new getData();
					finalURL = (initialURL+finalSearch);
					//execute getData using finalURL
					data.execute(finalURL);
					
				}//if statement closing bracket
				
			}//onClick closing bracket
		});//onClickListener closing bracket
    }//onCreate closing bracket


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
