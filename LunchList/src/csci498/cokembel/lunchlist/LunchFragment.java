package csci498.cokembel.lunchlist;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;


public class LunchFragment extends ListFragment {
	
	public final static String ID_EXTRA = "csci498.cokembel.lunchlist._ID";
	
	private Cursor model = null;
	private RestaurantAdapter adapter = null;
	private RestaurantHelper restaurantHelper = null;
	
	private RadioButton sit_down, take_out, delivery;
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private String restaurantType = null;
	private RadioGroup typesRadioGroup;
	private SharedPreferences prefs;
	
	private OnRestaurantListener listener;

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		if (listener != null) {
			listener.onRestaurantSelected(id);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setHasOptionsMenu(true);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
        restaurantHelper = new RestaurantHelper(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initList();	
        
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

	@Override
	public void onPause() {
		restaurantHelper.close();
		
		super.onPause();
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.options, menu);
    }
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	   if(item.getItemId() == R.id.add) {
		   startActivity(new Intent(getActivity(), DetailForm.class));
		   return true;
	   } else if (item.getItemId() == R.id.prefs) {
		   startActivity(new Intent(getActivity(), EditPreferences.class));
		   return true;
	   }
	   return super.onOptionsItemSelected(item);
    }
   
    private void initList() {
	   if (model != null) {
		   model.close();
	   }
	   
	   model = restaurantHelper.getAll(prefs.getString("sort_order", "name"));
	   adapter = new RestaurantAdapter(model);
	   setListAdapter(adapter);
    }
    
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
 	   public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
 		   if (key.equals("sort_order")) {
 			   initList();
 		   }
 	   }
 	   
     };
       
    public static class RestaurantHolder {
    	
    	private TextView name = null;
    	private TextView address  =null;
    	private ImageView icon = null;
    	
    	RestaurantHolder(View row) {
    		name = (TextView)row.findViewById(R.id.title);
    		address =(TextView)row.findViewById(R.id.address);
    		icon = (ImageView)row.findViewById(R.id.icon);
    	}
    	
		void populateFrom(Cursor c, RestaurantHelper helper) {
    		name.setText(helper.getName(c));
    		address.setText(helper.getAddress(c));
    		
    		if(helper.getType(c).equals("sit_down")) {
    			icon.setImageResource(R.drawable.ball_red);
    			name.setTextColor(Color.RED);
			}else if(helper.getType(c).equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(Color.YELLOW);
			}else {
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(Color.GREEN);
			}
		}
		
     };  
   
    public class RestaurantAdapter extends CursorAdapter {
	   	
	   	RestaurantAdapter(Cursor c) {
	   		super(getActivity(), c);
	   	}
	   	
	   	@Override
	   	public void bindView(View row, Context ctxt, Cursor c) {
		    	RestaurantHolder holder = (RestaurantHolder)row.getTag();
		    	holder.populateFrom(c, restaurantHelper);
	   	}
	   	
	   	@Override
	   	public View newView(Context ctxt, Cursor c, ViewGroup parent) {
		    	LayoutInflater inflater = getActivity().getLayoutInflater();
		    	View row=inflater.inflate(R.layout.row, parent, false);
		    	RestaurantHolder holder = new RestaurantHolder(row);
		    	
		    	row.setTag(holder);
		    	
		    	return(row);
	   	}
	   	
	};
   
	public interface OnRestaurantListener {
		void onRestaurantSelected(long id);
	}
	
	public void setOnRestaurantListener(OnRestaurantListener listener) {
		this.listener = listener;
	}
};




