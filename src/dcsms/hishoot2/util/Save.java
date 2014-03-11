package dcsms.hishoot2.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class Save {
	private Context con;

	public Save(Context c) {
		con = c;
	}

	public File SaveBitmap(Bitmap bm) {
		String sdcard = Environment.getExternalStorageDirectory()
				+ "/HiShoot/";
		String namafile = String.valueOf("HiShoot-"
				+ System.currentTimeMillis())
				+ ".png";
		OutputStream outStream = null;
		File f = new File(sdcard);
		if(!f.exists()){
			f.mkdirs();
		}
		File file = new File(sdcard, namafile);

		try {

			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			//new ToastAlert(con, "Save", true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//new ToastAlert(con, e.toString(), true);

		} catch (IOException e) {
			e.printStackTrace();
			//new ToastAlert(con, e.toString(), true);

		}
		return file;

	}

}
