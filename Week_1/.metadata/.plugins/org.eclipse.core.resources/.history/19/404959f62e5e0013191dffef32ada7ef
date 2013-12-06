package libs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class Storage {
	
	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String filename, Object content, Boolean external){
		try{
			File file;
			FileOutputStream fileOutputStream;
			ObjectOutputStream objectOutputStream;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fileOutputStream = new FileOutputStream(file);
			} else {
				fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(content);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (IOException e){
			Log.e("WRITE ERROR", filename);
		}		
		return true;
	}


@SuppressWarnings("resource")
public static Object readObjectFile(Context context, String filename, Boolean external){
	Object content = new Object();
	try{
		File file;
		FileInputStream fileInputStream;
		if(external){
			file = new File(context.getExternalFilesDir(null), filename);
			fileInputStream = new FileInputStream(file);
		} else {
			file = new File(filename);
			fileInputStream = context.openFileInput(filename);
		}
		
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		
		try{
			content = (Object) objectInputStream.readObject();
		} catch (ClassNotFoundException e){
			Log.e("READ ERROR", "INVALID JAVA OBJECT FILE");
		}
		objectInputStream.close();
		fileInputStream.close();
	} catch (FileNotFoundException e){
		Log.e("READ ERROR", "FILE NOT FOUND " + filename);
		return null;
	} catch (IOException e){
		Log.e("READ ERROR", "I/O ERROR");
	}
	return content;
}
}
