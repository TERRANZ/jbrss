package ru.terra.jbrss.activity.components;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ru.terra.jbrss.R;
import ru.terra.jbrss.entity.FeedEntity;

public class FeedListCursorAdapter extends CursorAdapter {

	public FeedListCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView) view.findViewById(R.id.tv_feed_item_name)).setText(cursor.getString(cursor.getColumnIndex(FeedEntity.FEED_NAME)));
		((TextView) view.findViewById(R.id.tv_feed_item_unread))
				.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(FeedEntity.FEED_UNREAD))));
		view.setTag(cursor.getInt(cursor.getColumnIndex(FeedEntity.FEED_EXTERNAL_ID)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.i_feed_item, null);
	}
}
