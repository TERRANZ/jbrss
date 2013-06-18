package ru.terra.jbrss.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import ru.terra.jbrss.R;
import ru.terra.jbrss.activity.components.CursorsCache;
import ru.terra.jbrss.activity.components.FeedPostsCursorAdapter;
import ru.terra.jbrss.activity.components.FeedPostsCursorAdapter.FeedsPostViewHolder;
import ru.terra.jbrss.constants.Constants;
import ru.terra.jbrss.entity.FeedPostEntity;
import ru.terra.jbrss.network.JBRssRest;
import ru.terra.jbrss.service.MarkReadService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.inject.Inject;

public class FeedPostsListActivity extends RoboActivity {

	@InjectView(R.id.lv_posts)
	private ListView lvPosts;
	private Cursor cursorFull;
	private Cursor cursorOnlyUnread;
	private Integer feed;
	private boolean isFromDate;

	@Inject
	private CursorsCache cursorsCache;

	@Inject
	JBRssRest jbRssRest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_feed_list);
		final Boolean isFromNotification = getIntent().getBooleanExtra("from_notify", false);
		if (!isFromNotification) {
			feed = getIntent().getIntExtra("id", -1);
			if (feed.equals(Constants.ALL_FEED_POSTS_ITEM_ID)) {
				cursorFull = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, null, null, FeedPostEntity.POST_DATE + " DESC ");
				cursorOnlyUnread = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, FeedPostEntity.POST_ISREAD + " = ?",
						new String[] { "false" }, FeedPostEntity.POST_DATE + " DESC ");
				lvPosts.setAdapter(new FeedPostsCursorAdapter(this, cursorFull));
                cursorsCache.setPostCursor(cursorFull);
				lvPosts.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						startActivity(new Intent(FeedPostsListActivity.this, FeedPostsViewAcitivity.class).putExtra("pos", position).putExtra(
								"feed_id", ((FeedsPostViewHolder) view.getTag()).feedId));
					}
				});
			} else {
				cursorFull = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, FeedPostEntity.POST_FEED_ID + " = ?",
						new String[] { feed.toString() }, FeedPostEntity.POST_DATE + " DESC ");
				cursorOnlyUnread = getContentResolver().query(FeedPostEntity.CONTENT_URI, null,
						FeedPostEntity.POST_FEED_ID + " = ? AND " + FeedPostEntity.POST_ISREAD + " = ?", new String[] { feed.toString(), "false" },
						FeedPostEntity.POST_DATE + " DESC ");
				lvPosts.setAdapter(new FeedPostsCursorAdapter(this, cursorFull));
				lvPosts.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Integer postId = ((FeedsPostViewHolder) view.getTag()).id;
						startActivity(new Intent(FeedPostsListActivity.this, FeedPostsViewAcitivity.class).putExtra("pos", position).putExtra(
								"feed_id", feed));
					}
				});
				cursorsCache.setPostCursor(cursorFull);
			}
		} else {
			String fromDate = getIntent().getStringExtra("from_date");
			isFromDate = true;
			Cursor cursorFromDate = getContentResolver().query(FeedPostEntity.CONTENT_URI, null, FeedPostEntity.POST_DATE + " >= ?",
					new String[] { fromDate != null ? fromDate : "0" }, FeedPostEntity.POST_DATE + " DESC ");
			lvPosts.setAdapter(new FeedPostsCursorAdapter(this, cursorFromDate));
			lvPosts.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Integer postId = ((FeedsPostViewHolder) view.getTag()).id;
					Integer feedId = ((FeedsPostViewHolder) view.getTag()).feedId;
					startActivity(new Intent(FeedPostsListActivity.this, FeedPostsViewAcitivity.class).putExtra("pos", position));
				}
			});
			cursorsCache.setPostCursor(cursorFromDate);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!isFromDate) {
			getMenuInflater().inflate(R.menu.m_posts, menu);
			menu.findItem(R.id.mi_posts_show_read).setChecked(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mi_posts_show_read: {
			if (item.isChecked()) {
				lvPosts.setAdapter(null);
				lvPosts.setAdapter(new FeedPostsCursorAdapter(this, cursorOnlyUnread));
				item.setChecked(false);
				item.setTitle("Все");
				cursorsCache.setPostCursor(cursorOnlyUnread);
			} else {
				lvPosts.setAdapter(null);
				lvPosts.setAdapter(new FeedPostsCursorAdapter(this, cursorFull));
				item.setChecked(true);
				item.setTitle(R.string.unread);
				cursorsCache.setPostCursor(cursorFull);
			}
			return true;
		}
		case R.id.mi_posts_all_read: {
			ContentValues cv = new ContentValues();
			cv.put(FeedPostEntity.POST_ISREAD, "true");
			getContentResolver().update(FeedPostEntity.CONTENT_URI, cv, FeedPostEntity.POST_FEED_ID + " = ?", new String[] { feed.toString() });
			startService(new Intent(this, MarkReadService.class).putExtra("id", feed).putExtra("operation", Constants.OP_MARK_READ_FEED));
		}
			return true;
		}
		return true;
	}
}
