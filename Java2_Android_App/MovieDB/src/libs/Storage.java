/*
 * Project:        MoviesDB
 * 
 * Package: com.daletupling.movies
 * 
 * Author:         Dale Tupling
 * 
 * Date:        December 5th, 2013
 * 
 */

package libs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;

import android.util.Log;

public class Storage {

	public static String readFile(Context context, String filename) {

		String content = "";
		try {

			FileInputStream fin;
			fin = context.openFileInput(filename);

			BufferedInputStream bin = new BufferedInputStream(fin);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();

			while ((bytesRead = bin.read(contentBytes)) != -1) {
				content = new String(contentBytes, 0, bytesRead);
				contentBuffer.append(content);
			}
			content = contentBuffer.toString();
			fin.close();
		} catch (FileNotFoundException e) {
			Log.e("READ ERROR", "FILE NOT FOUND " + filename);
		} catch (IOException e) {
			Log.e("READ ERROR", "I/O ERROR");
		}
		return content;
	}

	public static Boolean storeStringFile(Context context, String filename,
			String content, Boolean external) {
		try {
			FileOutputStream fos;
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			Log.i("FILE SAVED", filename + " SAVED");
			fos.close();
		} catch (IOException e) {
			Log.e("WRITE ERROR", filename);
		}
		return true;
	}

}
