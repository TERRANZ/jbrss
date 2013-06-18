package ru.terra.jbrss.activity.components;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
	ImageView imageView;

	public DownloadImageAsyncTask(ImageView imageView) {
		this.imageView = imageView;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap image = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			image = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return image;
	}

	protected void onPostExecute(Bitmap result) {
		imageView.setImageBitmap(result);
	}
}