package csci498.cokembel.lunchlist;

import java.util.ArrayList;
import java.util.List;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;

import java.util.concurrent.atomic.AtomicBoolean;


public class LunchList extends TabActivity {
	
	Cursor model = null;
	RestaurantAdapter adapter = null;
	Restaurant current =  null;
	RestaurantHelper restaurantHelper = null;
	
	RadioButton sit_down, take_out, delivery;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	String restaurantType = null;
	RadioGroup typesRadioGroup;
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		model.moveToPosition(position);
    		name.setText(restaurantHelper.getName(model));
    		address.setText(restaurantHelper.getAddress(model));
    		notes.setText(restaurantHelper.getNotes(model));
    		
    		if (restaurantHelper.getType(model).equals("sit_down")) {
    			typesRadioGroup.check(R.id.sit_down);
    		}
    		else if (restaurantHelper.getType(model).equals("take_out")) {
    			typesRadioGroup.check(R.id.take_out);
    		}
    		else {
    			typesRadioGroup.check(R.id.delivery);
    		}
    
    		getTabHost().setCurrentTab(1);
    	}
	};
	
	 private View.OnClickListener onSave = new View.OnClickListener() {
			
		public void onClick(View v) {
			
			restaurantHelper.insert(name.getText().toString(),address.getText().toString(), restaurantType,notes.getText().toString());
			model.requery();
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTitle("LunchList");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
        
        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.addr);
        typesRadioGroup = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
         
        Button save = (Button) findViewById(R.id.save);
      
        save.setOnClickListener(onSave);
        
        ListView restaurantList = (ListView)findViewById(R.id.restaurants);
        
        
        model = restaurantHelper.getAll();
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
        restaurantList.setAdapter(adapter);
       
        restaurantHelper = new RestaurantHelper(this);
        
        restaurantList.setAdapter(adapter);
        
        TabHost.TabSpec spec=getTabHost().newTabSpec("tag1");
        
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));

        getTabHost().addTab(spec);
        
        spec=getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);
        
        restaurantList.setOnItemClickListener(onListClick); 		
    }
	
	@Override
	public void onPause() {
	}
	
	@Override
	public void onResume() {
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
	    	LayoutInflater inflater=getLayoutInflater();
	    	View row=inflater.inflate(R.layout.row, parent, false);
	    	RestaurantHolder holder=new RestaurantHolder(row);
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
