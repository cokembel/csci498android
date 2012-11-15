package csci498.cokembel.lunchlist;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;


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
		if (findViewById(R.id.details)== null) {
			Intent i = new Intent(this, DetailForm.class);
			
			i.putExtra(ID_EXTRA, String.valueOf(id));
			startActivity(i);
		} else {
			// do something
		}
		
	}
   
};




