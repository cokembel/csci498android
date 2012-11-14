package csci498.cokembel.lunchlist;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;


@SuppressWarnings("deprecation")
public class LunchList extends FragmentActivity implements LunchFragment.OnRestaurantListener {
	
	public final static String ID_EXTRA = "csci498.cokembel.lunchlist._ID";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTitle("LunchList");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LunchFragment lunch = (LunchFragment)getSupportFragmentManager().findFragmentById(R.id.lunch);
        
        lunch.setOnRestaurantListener(this);
    }

	@Override
	public void onRestaurantSelected(long id) {
		Intent i = new Intent(this, DetailForm.class);
		
		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	};
   
};




