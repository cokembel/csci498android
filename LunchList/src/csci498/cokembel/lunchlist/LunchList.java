package csci498.cokembel.lunchlist;

import java.util.ArrayList;
import java.util.List;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.app.TabActivity;


public class LunchList extends TabActivity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	RadioButton sit_down, take_out, delivery;
	RadioGroup types;
	public static RestaurantType currentRestaurantType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTitle("LunchList");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeButtonGroup();
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
        		
    }
     
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			EditText name = (EditText) findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());

			switch (types.getCheckedRadioButtonId()){
				case 1:
					r.setType("take_out");
					break;
				case 2:
					r.setType("sit_down");
					break;
				case 3:
					r.setType("delivery");
					break;
			}	
			adapter.add(r);
		}
	};
	
	public enum RestaurantType {
			SIT_DOWN,
			TAKE_OUT,
			DELIVERY;
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
    	public int getItemViewType(int position){
    		if(currentRestaurantType == RestaurantType.SIT_DOWN) {
    			return 1;
    		}else if(currentRestaurantType == RestaurantType.TAKE_OUT) {
    			return 2;
    		}
    		return 3;
    	}
    	
    	@Override
    	public int getViewTypeCount(){
			return 3;
    		
    	}
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
   
    
    private void initializeButtonGroup() {
        TableRow radioRow = (TableRow) findViewById(R.id.radioRow);
     
        types = new RadioGroup(this);
        radioRow.addView(types);
        
        take_out = new RadioButton(this);
        take_out.setText("Take-Out");
        take_out.setId(1);
        sit_down = new RadioButton(this);
        sit_down.setText("Sit_Down");
        sit_down.setId(2);
        delivery = new RadioButton(this);
        delivery.setText("Delivery");
        delivery.setId(3);
        
        types.addView(take_out);
        types.addView(sit_down);
        types.addView(delivery);

    }
}
