package com.nitish.recyclerviewparallaxzomato;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//    import com.google.android.gms.appindexing.Action;
//    import com.google.android.gms.appindexing.AppIndex;
//    import com.google.android.gms.appindexing.Thing;

public class HomeActivity extends AppCompatActivity {

    ArrayList<Hotel> hotels;//source
    RecyclerView recyclerView; //destination
    MyRecyclerAdapter myRecyclerAdapter; //adapter
    MyTask myTask;  //async task
    int pos;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /**
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /**
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
*/

    private void showPopupMenu(View view)
    {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.overflowmenu, popup.getMenu()); //1st par is ur menu xml
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    //menu items click listener
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.item1: //show restrnt on map
                    //OPEN MAP ACTIVITY, and pass latitude and longitude
                    Hotel hotel = hotels.get(pos);
                    Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                    intent.putExtra("latitude", hotel.getLatitude());
                    intent.putExtra("longitude", hotel.getLongitude());
                    intent.putExtra("restaurant", hotel.getName());
                    startActivity(intent);
                    break;
                case R.id.item2: //search restrnt on google
                    //OPEN GOOGLE SEARCH ACTIVITY
                    break;
            }
            return true;
        }
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView hotelImage, overflowImage;
            public TextView hotelName, hotelAddress, hotelDishes;


            public MyViewHolder(View itemView) {
                super(itemView);

                hotelImage = (ImageView) itemView.findViewById(R.id.imageButton);
                overflowImage = (ImageView) itemView.findViewById(R.id.imageButton2);

                hotelName = (TextView) itemView.findViewById(R.id.textView2);
                hotelAddress = (TextView) itemView.findViewById(R.id.textView3);
                hotelDishes = (TextView) itemView.findViewById(R.id.textView4);

            }
        }

        @Override
        public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = getLayoutInflater().inflate(R.layout.row,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.overflowImage.setTag(position);  //store position
            //get hotel object based on position from arraylist
            Hotel hotel = hotels.get(position);
            //apply data bind onto holder
            holder.hotelName.setText(hotel.getName());
            holder.hotelAddress.setText(hotel.getAddress()+"\n"+hotel.getCity());
            holder.hotelDishes.setText(hotel.getCuisines());
            //setoverflow menu
            holder.overflowImage.setImageResource(R.drawable.download);

            //on clicking overflow image,display popup
            holder.overflowImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get position of the item
                    pos = (int) ((ImageView)v).getTag();
                    Toast.makeText(HomeActivity.this, "positon is .."+pos, Toast.LENGTH_SHORT).show();
                    //call show popup menu
                    showPopupMenu(v);
                }
            });
            //ask glider library to laod hotel thum nail image onto imageview
            Glide.with(HomeActivity.this)
                    .load(hotel.getImageUrl())
                    .into(holder.hotelImage);
        }

        @Override
        public int getItemCount() {
            return hotels.size();
        }

    }

    public class MyTask extends AsyncTask<String, Void, String> {

        URL myUrl;
        HttpURLConnection connection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String line;   //for reading line from bufferedReader
        StringBuilder result;    // for accumulating all lines


        @Override
        protected String doInBackground(String... p1) {

            //myUrl = new URL("http://google.com");
            try {
                myUrl = new URL(p1[0]);
                connection = (HttpURLConnection) myUrl.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("user_key", "386168501c9848adc8999d1d39b740cd");
                connection.connect();
                //open channel for reading
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                result = new StringBuilder();
                line = bufferedReader.readLine();     //read first line
                while (line != null) {
                    //pile up in string builder. (update)
                    result.append(line);
                    line = bufferedReader.readLine();    //reads next line
                }
                return result.toString();
            } catch (MalformedURLException e) {
                Log.d("b33", "improperURL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("b33", "checkInternet" + e.getMessage());
                Log.d("b33", "Casuse.." + e.getCause());
                e.printStackTrace();
            }
            finally {
                if (connection!= null)
                {
                    connection.disconnect();
                    if (inputStream!= null)
                    {
                        try {
                            inputStream.close();
                            if (inputStreamReader!= null)
                            {
                                inputStreamReader.close();
                                if (bufferedReader!= null)
                                {
                                    bufferedReader.close();
                                }
                            }
                        } catch (IOException e) {
                            Log.d("MyLog","Problem is closing connection");
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s == null){
                Toast.makeText(HomeActivity.this, "NETWORK ISSUE PLEASE FIX IT", Toast.LENGTH_SHORT).show();

                return;
            }

            try {
                JSONObject j = new JSONObject(s);
                JSONArray arr = j.getJSONArray("nearby_restaurants");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject temp = arr.getJSONObject(i);
                    JSONObject restr = temp.getJSONObject("restaurant");
                    String name = restr.getString("name");
                    JSONObject loc = restr.getJSONObject("location");
                    String address = loc.getString("address");
                    String locality = loc.getString("locality");
                    String city = loc.getString("city");
                    String latitude = loc.getString("latitude");
                    String longitude = loc.getString("longitude");
                    String cuisines = restr.getString("cuisines");
                    String imageUrl = restr.getString("thumb");

                    //prepare empty hotel  object - pass values to constructor
                    //pass data arrlis setter
                    //  Hotel hotel = new Hotel(name,address,locality,city,latitude,longitude,cuisines,imageUrl);
                    Hotel hotel = new Hotel (name, address, locality, city, latitude, longitude, cuisines, imageUrl);
                    hotels.add(hotel);

                }
                myRecyclerAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(s);
        }
/*
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
 */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //intialize
        hotels = new ArrayList<Hotel>();
        recyclerView = (RecyclerView) findViewById(R.id.recylerView1);

        //improve recylerview performace
        recyclerView.setHasFixedSize(true);

        myTask = new MyTask();
        myRecyclerAdapter = new MyRecyclerAdapter();

        //pass grid layout manager to recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        //pass grid layout manager to recycler view
        recyclerView.setLayoutManager(gridLayoutManager);



        //set adapter to recycler view
        recyclerView.setAdapter(myRecyclerAdapter);
        //start asynctask,paste zomato url
        myTask.execute("https://developers.zomato.com/api/v2.1/geocode?lat=12.8984&lon=77.6179");





        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
  //      client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
}
