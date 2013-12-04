
/*
 * Project:        MoviesDB
 * 
 * Package: com.daletupling.libs
 * 
 * Author:         Dale Tupling
 * 
 * Date:        December 2nd, 2013
 * 
 */

package libs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daletupling.movies.MainActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Data {
	
	static Context context;
	
	public static ArrayList<String> movies = new ArrayList<String>();
	public static ArrayList<String> genre = new ArrayList<String>();
	
	
	//tag string
	static String TAG = "SERVICES - APIDATA";
	
	//static variable to check data
	static JSONObject moviesObject;
	
	//get response from URL
	public static String getResponse(URL url){
		//instantiate data_string string
		String data_string = "";
		//try to retrieve data in string format
		try{
			URLConnection urlConn = url.openConnection();
			BufferedInputStream bufferedInput = new BufferedInputStream(urlConn.getInputStream());
			byte[] contextByte = new byte [1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			while((bytesRead = bufferedInput.read(contextByte)) != -1){
				data_string = new String(contextByte, 0, bytesRead);
				responseBuffer.append(data_string);
			}
			
			data_string = responseBuffer.toString();
			Log.i(TAG, data_string);
		} catch (IOException IOExc){
			data_string = "Something happened and we were unable to retreive the data.";
			Log.e(TAG, "IOEXCEPTION");
			IOExc.printStackTrace();
		}
		return data_string;
	}//getResponse closing bracket
	
	
	//Get API data drill down data to get desired movie information
	public static class getData extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			String response_string = "";
			try{
				URL url = new URL(MainActivity.finalURL);
				Log.i("URL", url.toString());
				response_string = getResponse(url);
			} catch (MalformedURLException URLExc){
				response_string ="Malformed URL";
				Log.e(TAG, "ERROR:", URLExc);
			}//catch closing bracket
			
			return response_string;
		}//doInbackGround closing bracket
		
		@Override
		protected void onPostExecute(String result){
			Log.i("RESPONSE", result);
			
		}
		
	}//getData closing bracket

}//Data class closing bracket
