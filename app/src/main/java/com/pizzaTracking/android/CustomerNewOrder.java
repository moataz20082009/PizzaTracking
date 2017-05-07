package com.pizzaTracking.android;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseDBConnection;
import com.pizzaTracking.android.Common.FirebaseItemsList;
import com.pizzaTracking.android.Common.FirebaseRootNodes;
import com.pizzaTracking.android.Restaurant.FoodItem;

import java.io.FilterReader;
import java.util.ArrayList;
import java.util.List;

public class CustomerNewOrder extends AppCompatActivity {
    ListView itemsListView;
    FirebaseDBConnection firebaseDBConnection;
    ArrayList<FoodItem> items;
    //StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_order);
        firebaseDBConnection = FirebaseDBConnection.Create();
        FirebaseItemsList<FoodItem> firebaseItemsList = new FirebaseItemsList<FoodItem>(FirebaseRootNodes.FOOOD_ITEM_ROOT,FoodItem.class);
        firebaseItemsList.GetAll(firebaseDBConnection.reference, new DataRetrieved() {
            @Override
            public void OnDataRetrieved(Object data) {
                if( data != null)
                    items = (ArrayList<FoodItem>)data;
                else
                    items = new ArrayList<FoodItem>();
                itemsListView.setAdapter(new itemsAdapter(CustomerNewOrder.this,R.layout.menu_item_with_selection,items));
            }
        });
    }
    private class itemsAdapter extends ArrayAdapter<FoodItem>{

        private Context _context;
        private int _resourceid;
        private List<FoodItem> _items;
        public itemsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FoodItem> objects) {
            super(context, resource, objects);
            _context = context;
            _resourceid = resource;
            _items = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(_resourceid, parent, false);
            TextView nameView = (TextView)rowView.findViewById(R.id.ItemTitleTextView);
            nameView.setText(_items.get(position).Name);
            TextView restaurantView = (TextView)rowView.findViewById(R.id.ItemRestaurantName);
            restaurantView.setText(_items.get(position).RestaurantName);
            //ImageView img = (ImageView)rowView.findViewById(R.id.ItemImageView);

            //TextView phoneView = (TextView)rowView.findViewById(R.id.phoneTextView);
            //phoneView.setText(contacts.get(position).phone);
            return rowView;
        }

        @Override
        public int getCount() {
            return _items.size();
        }
    }
}
