package csci498.cokembel.lunchlist;

import java.util.ArrayList;
import java.util.List;

import csci498.cokembel.lunshlist.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableRow;


public class LunchList extends Activity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	ArrayAdapter<Restaurant> adapter = null;
	RadioButton sit_down, take_out, delivery;
	RadioGroup types;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeButtonGroup();
        Button save = (Button) findViewById(R.id.save);
      
        save.setOnClickListener(onSave);  
        
       ListView restaurantList = (ListView)findViewById(R.id.restaurants);
        
        adapter = new ArrayAdapter<Restaurant>(this,
        		android.R.layout.simple_list_item_1,
        		model);
        restaurantList.setAdapter(adapter);
        		
    }
    
    private void initializeButtonGroup(){
    
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
