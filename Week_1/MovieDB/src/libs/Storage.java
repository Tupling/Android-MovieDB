package libs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

import android.util.Log;

public class Storage {


	public Boolean writeFile(Context context, String filename, String content){
		
		Boolean result = false;
		
		
		FileOutputStream fos = null;;
		
		try{
			fos = context.openFileOutput(MovieService.FILENAME, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
			Log.i("FILEWRITE:", "FILE SAVED");
		}catch (Exception e){
			Log.e("FILEWRITE ERROR", e.toString());
		}
		return result;
		
	}
	

	
	public String readFile(Context context, String filename){
		
		String fileData = "";
		
		FileInputStream fis = null;
		try{
			fis = context.openFileInput(MovieService.FILENAME);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			while((bytesRead = bis.read(contentBytes)) != -1){
				fileData = new String(contentBytes, 0, bytesRead);
				fileData = contentBuffer.toString();
			}
		}catch (Exception e){
		
		}finally{
			try{
			fis.close();
		}catch (IOException e){
			Log.e("FILE CLOSE ERROR", e.toString());
		}
		}
		return fileData;
	}
}
