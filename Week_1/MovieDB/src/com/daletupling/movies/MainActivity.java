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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daletupling.libs.WebData;


import libs.Data;
import libs.MovieService;
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
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


//Handler Leak suppress for movieHandler
@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	public static Context mContext;
	//Layout elements
	EditText search;
	Button search_button;
	Button filter_button;
	ListView movie_list;
	
	//Connection boolean declare
	Boolean connected = false;
	
	
	//Search string variables
	String search_string;

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
				
				String tempString = search.getText().toString();
				//Check for empty search string display error dialog if it is empty.
				search_string = tempString.replaceAll(" ", "%20");
				if(search_string != null){
	
					//Pass search string to movieSearch method
					movieSearch(search_string);
					
					
				}//if statement closing bracket

			}//onClick closing bracket
			
		});//onClickListener closing bracket
       
        
        //Filter button
        filter_button = (Button) findViewById(R.id.filter_button);
        //disable filter button
        filter_button.setEnabled(false);
        filter_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});//filter button on clickListener closing bracket
        
        
        
        
        //Run network status method from WebData class in Network jar file
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
    }//onCreate closing bracket

    private Handler movieHandler = new Handler(){
    	public void handleMessage(Message message){
    		Object dataObj = message.obj;
    		if(message.arg1 == RESULT_OK && dataObj != null){
    			String movieResults = (String) message.obj.toString();
    			//set enable once data has been found from previous search or new search
    			filter_button.setEnabled(true);
    	
    			try{
    				JSONObject jsonObject = new JSONObject(movieResults);
    				JSONArray movieArray = jsonObject.getJSONArray("results");
    				if(movieArray != null){
    					Log.i("JSON DATA:", movieArray.toString());
    				}//if movieArray Null statement closing bracket
    			}catch(JSONException e){
    				Log.e("DATA EXCEPTION", "JSON DATA EXCEPTION");
    				
    			}//catch closing bracket	
    			}//message check statement closing bracket
    		}//handlerMessage closing bracket
    	};//Handler closing bracket

    
    
    //Movie search method
    private void movieSearch(String search_string){

		Log.i("SEARCH:", search_string);
		
		Messenger messenger = new Messenger(movieHandler);
		Intent intent = new Intent(mContext, MovieService.class);
		intent.putExtra("search_string", search_string);
		intent.putExtra("messenger", messenger);
		startService(intent);
    }//movieSearch Closing bracket
}