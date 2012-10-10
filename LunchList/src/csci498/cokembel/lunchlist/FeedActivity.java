package csci498.cokembel.lunchlist;

import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSFeed;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedActivity extends Activity {
	
	private static class FeedTask extends AsyncTask<String, Void, RSSFeed> {
		private RSSReader reader = new RSSReader();
		private Exception e = null;
		private FeedActivity activity = null;
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		void attach(FeedActivity activity) {
			this.activity = activity;
		}
	
		void detach() {
			this.activity = null;
		}
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result = null;
			
			try {
				result = reader.load(urls[0]);
			}
			catch (Exception e) {
				this.e = e;
			}
			
			return result;
		}
	
		@Override
		public void onPostExecute(RSSFeed feed) {
			if (e == null) {
				activity.setFeed(feed);
			} else {
				Log.e("LunchList", "Exception parsing feed",e);
				activity.goBlooey(e);
			}
		}
		
	};
	
	private class FeedAdapter extends BaseAdapter {
		RSSFeed feed = null;
		
		FeedAdapter(RSSFeed feed) {
			super();
			this.feed = feed;
		}
		
		public int getCount() {
			return feed.getItems().size();
		}
		
		public Object getItem(int position) {
			return position;
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				
				row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
		
		
			RSSItem item = (RSSItem)getItem(position);
		
			((TextView)row).setText(item.getTitle());
		
			return row;
		}
	}
	
}
