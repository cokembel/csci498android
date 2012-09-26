package csci498.cokembel.lunchlist;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;


@SuppressWarnings("deprecation")
public class LunchList extends ListActivity {
	
	Cursor model = null;
	RestaurantAdapter adapter = null;
	RestaurantHelper restaurantHelper = null;
	
	RadioButton sit_down, take_out, delivery;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	String restaurantType = null;
	RadioGroup typesRadioGroup;
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		Intent i = new Intent(LunchList.this, DetailForm.class);
    		startActivity(i);
    		
    	}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTitle("LunchList");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        restaurantHelper = new RestaurantHelper(this);
          
        model = restaurantHelper.getAll();
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
        setListAdapter(adapter);		
    }

	@Override
	public void onDestroy(){
		super.onDestroy();
		restaurantHelper.close();
	}
	
    public class RestaurantAdapter extends CursorAdapter {
	   	
    	RestaurantAdapter(Cursor c) {
    	super(LunchList.this, c);
    	}
    	
    	@Override
    	public void bindView(View row, Context ctxt, Cursor c) {
	    	RestaurantHolder holder = (RestaurantHolder)row.getTag();
	    	holder.populateFrom(c, restaurantHelper);
    	}
    	
    	@Override
    	public View newView(Context ctxt, Cursor c, ViewGroup parent) {
	    	LayoutInflater inflater = getLayoutInflater();
	    	View row=inflater.inflate(R.layout.row, parent, false);
	    	RestaurantHolder holder = new RestaurantHolder(row);
	    	row.setTag(holder);
	    	return(row);
    	}
   }
       
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
    }  
}
