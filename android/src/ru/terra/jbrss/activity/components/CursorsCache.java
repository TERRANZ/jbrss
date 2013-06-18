package ru.terra.jbrss.activity.components;

import android.database.Cursor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CursorsCache {

	@Inject
	public CursorsCache() {
	}

	private Cursor postCursor;

	public Cursor getPostCursor() {
		return postCursor;
	}

	public void setPostCursor(Cursor postCursor) {
		this.postCursor = postCursor;
	}

}
