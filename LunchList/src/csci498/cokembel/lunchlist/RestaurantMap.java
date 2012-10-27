package csci498.cokembel.lunchlist;

import csci498.cokembel.lunshlist.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RestaurantMap extends MapActivity {
	
	public static final String EXTRA_LATITUDE = "csci498.cokembel.lunchlist.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "csci498.cokembel.lunchlist.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "csci498.cokembel.lunchlist.EXTRA_NAME";
	
	private MapView map = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		double lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
		double lon = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);
		
		map = (MapView)findViewById(R.id.map);
		map.getController().setZoom(17);
		
		GeoPoint status = new GeoPoint((int)(lat*1000000.0),(int)(lon*1000000.0));
		
		map.getController().setCenter(status);
		map.setBuiltInZoomControls(true);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
