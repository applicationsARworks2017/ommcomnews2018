package com.lipl.ommcom.twitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

public class Utils {

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static String getPath() {
		String path = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		} else if ((new File("/mnt/emmc")).exists()) {
			path = "/mnt/emmc";
		} else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return path;
	}

	public static String getImagePath(Intent data, Activity activity) {
		String path = null;
		try {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(activity.getContentResolver()
						.openInputStream(data.getData()), new Rect(), options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;

				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;

				Bitmap bm = BitmapFactory.decodeStream(activity
						.getContentResolver().openInputStream(data.getData()),
						new Rect(), options);
				String fileName = String.valueOf(System.currentTimeMillis())
						+ ".jpg";
				OutputStream fOut = activity.openFileOutput(fileName,
						Context.MODE_PRIVATE);
				bm.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
				fOut.flush();
				fOut.close();
				path = activity.getFileStreamPath(fileName).getAbsolutePath();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			System.gc();
			Toast.makeText(activity,
					"Image was large Please Try another Image",
					Toast.LENGTH_LONG).show();
		}
		return path;
	}

}
