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


public class LunchList extends Activity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	RadioButton sit_down, take_out, delivery;
	RadioGroup types;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeButtonGroup();
        Button save = (Button) findViewById(R.id.save);
      
        save.setOnClickListener(onSave);
        
        Spinner restaurantList = (Spinner)findViewById(R.id.restaurants);
        adapter = new RestaurantAdapter();
        
        restaurantList.setAdapter(adapter);
        		
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
			}else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(Color.YELLOW);
			}else {
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(Color.GREEN);
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
        
        // Extra radio buttons for extra credit for tutorial 3 ( not used in later tutorials )
        // same with scrollView that was previously used
        /*
        RadioButton extra1 = new RadioButton(this);
        RadioButton extra2 = new RadioButton(this);
        RadioButton extra3 = new RadioButton(this);
        RadioButton extra4 = new RadioButton(this);
        RadioButton extra5 = new RadioButton(this);
        RadioButton extra6 = new RadioButton(this);
        RadioButton extra7 = new RadioButton(this);
        RadioButton extra8 = new RadioButton(this);
        */
        types.addView(take_out);
        types.addView(sit_down);
        types.addView(delivery);/*
        
        types.addView(extra1);
        types.addView(extra2);
        types.addView(extra3);
        types.addView(extra4);
        types.addView(extra5);
        types.addView(extra6);
        types.addView(extra7);
        types.addView(extra8);*/  
    }
}
