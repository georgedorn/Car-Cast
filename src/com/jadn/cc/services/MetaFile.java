package com.jadn.cc.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.util.Log;

/**
 * Meta information about a podcast. From rss metadata (hopefully someday from id3tags as well.)
 */
public class MetaFile {

	File file;
	Properties properties = new Properties();

	MetaFile(File file) {
		this.file = file;

		File metaFile = getMetaPropertiesFile();
		if (metaFile.exists()) {
			try {
				properties.load(new FileInputStream(metaFile));
				// Log.i("metafile", properties.toString());
			} catch (Exception e) {
				Log.e("Meta", "Can't load properties");
			}
		} else {
			properties.setProperty("title", file.getName());
			properties.setProperty("feedName", "unknown feed");
			properties.setProperty("currentPos", "0");
		}
	}

	public MetaFile(MetaNet metaNet, File castFile) {
		file = castFile;
		properties = metaNet.properties;
	}

	public void delete() {
		file.delete();
		getMetaPropertiesFile().delete();
	}

	public int getCurrentPos() {
		if (properties.getProperty("currentPos") == null)
			return 0;
		return Integer.parseInt(properties.getProperty("currentPos"));
	}

	public int getDuration() {
		if (properties.get("duration") == null)
			return -1;
		return Integer.parseInt(properties.getProperty("duration"));
	}

	public String getFeedName() {
		if (properties.get("feedName") == null)
			return "unknown";
		return properties.get("feedName").toString();
	}

	private File getMetaPropertiesFile() {
		// check for metadata
		String name = file.getName();
		int lastDot = name.lastIndexOf('.');
		if (lastDot != -1) {
			name = name.substring(0, lastDot);
		}
		name += ".meta";
		return new File(file.getParent(), name);
	}

	public String getTitle() {
		if (properties.get("title") == null) {
			String title = file.getName();
			int lastDot = title.lastIndexOf('.');
			return title.substring(0, lastDot);
		}
		return properties.get("title").toString();
	}

	public String getUrl() {
		return properties.getProperty("url");
	}

	public void save() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(getMetaPropertiesFile());
			properties.save(fos, "");
			fos.close();
		} catch (IOException e) {
			Log.e("", "", e);
		}

	}

	public void setCurrentPos(int i) {
		properties.setProperty("currentPos", Integer.toString(i));
	}

	public void setDuration(int duration) {
		properties.setProperty("duration", Integer.toString(duration));
	}

}
