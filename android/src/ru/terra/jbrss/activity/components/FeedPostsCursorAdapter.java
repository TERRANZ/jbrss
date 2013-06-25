package ru.terra.jbrss.activity.components;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ru.terra.jbrss.R;
import ru.terra.jbrss.entity.FeedPostEntity;

public class FeedPostsCursorAdapter extends CursorAdapter {

	public class FeedsPostViewHolder {
		public TextView tvName;
		public Integer id;
		public Integer feedId;
	}

	private LayoutInflater layoutInflater;

	public FeedPostsCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View ret = layoutInflater.inflate(R.layout.i_feed_post_item, null);
		FeedsPostViewHolder vh = new FeedsPostViewHolder();
		vh.tvName = (TextView) ret.findViewById(R.id.tv_feed_post_name);
		ret.setTag(vh);
		return ret;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		FeedsPostViewHolder vh = (FeedsPostViewHolder) view.getTag();
		vh.id = cursor.getInt(cursor.getColumnIndex(FeedPostEntity.POST_EXTERNAL_ID));
		vh.feedId = cursor.getInt(cursor.getColumnIndex(FeedPostEntity.POST_FEED_ID));
		vh.tvName.setText(cursor.getString(cursor.getColumnIndex(FeedPostEntity.POST_TITLE)));
		String read = getCursor().getString(getCursor().getColumnIndex(FeedPostEntity.POST_ISREAD));
		if ("false".equals(read))
			vh.tvName.setTypeface(null, Typeface.BOLD);
		else
			vh.tvName.setTypeface(null, Typeface.ITALIC);
	}

}
