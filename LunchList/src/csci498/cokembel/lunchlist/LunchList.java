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


public class LunchList extends TabActivity {
	
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	Restaurant current =  null;
	public static RestaurantType currentRestaurantType;
	
	RadioButton sit_down, take_out, delivery;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup typesRadioGroup;
	
	int progress;
	
	public enum RestaurantType {
		SIT_DOWN,
		TAKE_OUT,
		DELIVERY;
	}
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    		current=model.get(position);
    		name.setText(current.getName());
    		address.setText(current.getAddress());
    		notes.setText(current.getNotes());
    		if (current.getType().equals("sit_down")) {
    			typesRadioGroup.check(R.id.sit_down);
    		}else if (current.getType().equals("take_out")) {
    			typesRadioGroup.check(R.id.take_out);
    		}else {
    			typesRadioGroup.check(R.id.delivery);
    		}
    		getTabHost().setCurrentTab(1);
    	}
	};
	
	private Runnable longTask = new Runnable() {
		public void run() {
			for(int i=0; i<20; i++) {
				doSomeLongWork(500);
			}
			
			runOnUiThread(new Runnable() {
				public void run() {
					setProgressBarVisibility(false);
				}
			});
		}
	};
	
	 private View.OnClickListener onSave = new View.OnClickListener() {
			
		public void onClick(View v) {
			current = new Restaurant();
			EditText name = (EditText) findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString());
			current.setNotes(notes.getText().toString());

			switch (typesRadioGroup.getCheckedRadioButtonId()) {
				case R.id.take_out:
					current.setType("take_out");
					break;
				case R.id.sit_down:
					current.setType("sit_down");
					break;
				case R.id.delivery:
					current.setType("delivery");
					break;
			}	
			adapter.add(current);
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTitle("LunchList");
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
      
        
        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.addr);
        typesRadioGroup = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
         
        Button save = (Button) findViewById(R.id.save);
      
        save.setOnClickListener(onSave);
        
        ListView restaurantList = (ListView)findViewById(R.id.restaurants);
        adapter = new RestaurantAdapter();
        
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
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.toast) {
			String message = "No restaurant selected";
			
			if (current != null) {
				message = current.getNotes();
			}
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return true;
			
		}else if (item.getItemId() == R.id.run) {
			setProgressBarVisibility(true);
			progress = 0;
			new Thread(longTask).start();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void doSomeLongWork(final int incr) {
		runOnUiThread(new Runnable() {
			public void run() {
				progress+=incr;
				setProgress(progress);
			}
		});
		
		SystemClock.sleep(250);
	}
	
    public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
	   	
    	RestaurantAdapter() {
    		super(LunchList.this,android.R.layout.simple_list_item_1,
    		model);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		View row = convertView;
    		RestaurantHolder holder = null;
    		
    		if(row == null) {
    			LayoutInflater inflater = getLayoutInflater();
    			
    			row = inflater.inflate(R.layout.row, null);
    			holder = new RestaurantHolder(row);
    			row.setTag(holder);
    		}else {
    			holder = (RestaurantHolder)row.getTag();
    		}
    		
    		holder.populateFrom(model.get(position));
    		
    		return(row);
    	}
    	
    	@Override
    	public int getItemViewType(int position) {
    		if(currentRestaurantType == RestaurantType.SIT_DOWN) {
    			return 1;
    		}else if(currentRestaurantType == RestaurantType.TAKE_OUT) {
    			return 2;
    		}
    		return 3;
    	}
    	
    	@Override
    	public int getViewTypeCount() {
			return 3;
    	}
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.options, menu);
       return super.onCreateOptionsMenu(menu);
   }
	    
    static class RestaurantHolder {
    	
    	private TextView name = null;
    	private TextView address  =null;
    	private ImageView icon = null;
    	
    	RestaurantHolder(View row) {
    		
    		name = (TextView)row.findViewById(R.id.title);
    		address =(TextView)row.findViewById(R.id.address);
    		icon = (ImageView)row.findViewById(R.id.icon);
    	}
    	
		void populateFrom(Restaurant r) {
    		name.setText(r.getName());
    		address.setText(r.getAddress());
    		
    		if (r.getType().equals("sit_down")) {
    			icon.setImageResource(R.drawable.ball_red);
    			name.setTextColor(Color.RED);
    			currentRestaurantType = RestaurantType.SIT_DOWN;
			}else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(Color.YELLOW);
				currentRestaurantType = RestaurantType.TAKE_OUT;
			}else {
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(Color.GREEN);
				currentRestaurantType = RestaurantType.DELIVERY;
			}
		}
    }  
}
