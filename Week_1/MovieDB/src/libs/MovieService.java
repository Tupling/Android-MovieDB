/*
 * Project:        MoviesDB
 * 
 * Package: com.daletupling.movies
 * 
 * Author:         Dale Tupling
 * 
 * Date:        December 3rd, 2013
 * 
 */
package libs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;



public class MovieService extends IntentService {
        Context mContext;

        public MovieService() {
                super("MovieService");
                // TODO Auto-generated constructor stub
        }

        String dataResponse = null;
        String finalSearch = null;
        URL finalURL = null;
        int result;
        public static String FILENAME = "APIData";
        
        JSONArray array_results;
        

        @Override
        protected void onHandleIntent(Intent intent) {
                // TODO Auto-generated method stub
                Bundle extras = intent.getExtras();
                if (extras != null){
                        //Set finalSearch string to search_string passed from MainActivity using extras Bundle
                        finalSearch = (String) extras.get("search_string");
                        //API_URL string with user keyword search string appended
                        String dataResponse = "http://api.themoviedb.org/3/search/movie?api_key=f9787f25661403e36a229aa8a0afd09c&query="+finalSearch;
                        
                        //Try catch get data from string passed URL
                        try{
                                //instantiate finalURL from string dataResponse
                                finalURL = new URL (dataResponse);
                                //run url through Data class
                                dataResponse = Data.getResponse(finalURL);
                                //Check for empty response
                                //Log.i("JSON DATA STRING:", dataResponse.toString());
                                
                               FileOutputStream fos = null;
                                try {
                                        fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                                        fos.write(dataResponse.getBytes());
                                        fos.close();
                                        Log.i("DATA PULLED", dataResponse.toString());
                                    	Log.i("FILEWRITE:", "FILE SAVED");
                                } catch (FileNotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }

                                
                                if(dataResponse.length() > 0) result = Activity.RESULT_OK;
                        }catch(MalformedURLException e){
                                Log.e("URL ERROR", "URL MALFORMED");
                                //set finalURL if URL is malformed
                                finalURL = null;
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        //Send data through messenger
                        Messenger messenger = (Messenger) extras.get("messenger");
                        Message message = Message.obtain();
                        message.arg1 = result;
                        message.obj = dataResponse;
                        try{
                                messenger.send(message);
                        }catch(RemoteException e){
                                Log.e(getClass().getName(), "Message failed to send", e);
                        }
                }
        }



}