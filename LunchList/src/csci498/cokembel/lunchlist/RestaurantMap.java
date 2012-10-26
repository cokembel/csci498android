package csci498.cokembel.lunchlist;

import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
