package ru.terra.twochsaver.twoch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author terranz
 */
public class DownloaderThread implements Runnable {
	private String url;
	private String folder = "2ch-images";

	public DownloaderThread(String url, String threadId) {
		this.url = url;
		folder += "/" + threadId + "/";
		File f = new File(folder);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	@Override
	public void run() {
		System.out.println("Starting to download image " + url);
		//TODO: куда скачивать, надо выяснить только
//		try {
//			URL google = new URL(url);
//			ReadableByteChannel rbc = Channels.newChannel(google.openStream());
//			FileOutputStream fos = new FileOutputStream(folder + url.substring(url.lastIndexOf("/")));
//			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
//			fos.close();
//		} catch (IOException ex) {
//			Logger.getLogger(DownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
//		}
	}
}
