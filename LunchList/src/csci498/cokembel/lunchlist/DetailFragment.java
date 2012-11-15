package csci498.cokembel.lunchlist;

import csci498.cokembel.lunshlist.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

public class DetailFragment extends Fragment {

    private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private EditText feed = null;
	private RadioGroup types = null;
	private RestaurantHelper helper = null;
	private String restaurantId = null;
	private TextView location = null;
	private LocationManager locMgr = null;
	
	private double latitude = 0.0d;
	private double longitude = 0.0d;
	
	private static final String ARG_REST_ID = "csci498.cokembel.lunchlist.ARG_REST_ID";
	
	LocationListener onLocationChange = new LocationListener() {
	
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			
			location.setText(String.valueOf(fix.getLatitude()) + ", " + String.valueOf(fix.getLongitude()));
			locMgr.removeUpdates(onLocationChange);
			
			Toast.makeText(getActivity(), "Location saved", Toast.LENGTH_LONG).show();
		}
		
		public void onProviderDisabled(String provider) {
			// not used
		}
		
		public void onProviderEnabled(String provider) {
			// not used
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// not used
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.detail_form, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		locMgr = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		initializingWidgets();
		
		Bundle args = getArguments();
		
		if (args != null) {
			loadRestaurant(args.getString(ARG_REST_ID));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		helper = new RestaurantHelper(getActivity());
		restaurantId = getActivity().getIntent().getStringExtra(LunchList.ID_EXTRA);
		
		if (restaurantId != null) {
			load();
		}
	}

	@Override
	public void onPause() {
		save();
		helper.close();
		locMgr.removeUpdates(onLocationChange);
		
		super.onPause();
	}

	private void initializingWidgets() {
		name = (EditText) getView().findViewById(R.id.name);
		address = (EditText) getView().findViewById(R.id.addr);
		notes = (EditText) getView().findViewById(R.id.notes);
		types = (RadioGroup) getView().findViewById(R.id.types);
		feed = (EditText) getView().findViewById(R.id.feed);
		location = (TextView) getView().findViewById(R.id.location);
	}

	private void load() {
		Cursor c = helper.getById(restaurantId);

		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));

		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}
		
		latitude = helper.getLatitude(c);
		longitude = helper.getLongitude(c);
		
		location.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));

		c.close();

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.details_option, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent i = new Intent(getActivity(), FeedActivity.class);
				
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(i);
			} else {
				Toast
					.makeText(getActivity(), "Sorry, the Internet is not available", Toast.LENGTH_LONG)
					.show();
			}
			return true;
		} else if (item.getItemId() == R.id.location){
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
			return true;
		} else if (item.getItemId() == R.id.map) {
			Intent i = new Intent(getActivity(), RestaurantMap.class);
			
			i.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
			i.putExtra(RestaurantMap.EXTRA_LONGITUDE, longitude);
			i.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
			
			startActivity(i);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		return info != null;
	}

	private void save() {
		if (name.getText().toString().length() > 0) {
			String type = null;
	
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = "sit_down";
				break;
			case R.id.take_out:
				type = "take_out";
				break;
			case R.id.delivery:
				type = "delivery";
				break;
			}
			
			if (restaurantId == null) {
				helper.insert(name.getText().toString(), address.getText()
						.toString(), type, notes.getText().toString(), feed.getText().toString());
			} else {
				helper.update(restaurantId, name.getText().toString(), address
						.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
			}
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
			menu.findItem(R.id.map).setEnabled(false);
		}
	}

	public void loadRestaurant(String restaurantId) {
		this.restaurantId = restaurantId;
		
		if (restaurantId != null) {
			load();
		}
	}
	
	public static DetailFragment newInstance(long id) {
		DetailFragment result = new DetailFragment();
		Bundle args = new Bundle();
		
		args.putString(ARG_REST_ID, String.valueOf(id));
		result.setArguments(args);
		
		return result;
	}
};