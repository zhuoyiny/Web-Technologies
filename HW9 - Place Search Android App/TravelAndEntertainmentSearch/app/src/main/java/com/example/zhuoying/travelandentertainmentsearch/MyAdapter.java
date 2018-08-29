package com.example.zhuoying.travelandentertainmentsearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final String TAG = "MyAdapter";
    private Context mContext;
    private String message;
    private String[] imgDataset;
    private String[] mDataset;
    private String[] addressDataset;
    private String[] place_idDataset;
    private String message_detail;
    private String address;
    private String[] arr;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imgView;
        public TextView mTextView;
        public TextView addTextView;
        public LinearLayout parentLayout;
        public ViewHolder(View v) {
            super(v);
            imgView = v.findViewById(R.id.results_img);
            mTextView = v.findViewById(R.id.results_name);
            addTextView = v.findViewById(R.id.results_address);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context mContext, String message, String[] ImgDataset, String[] myDataset, String[] AddressDataset, String[] place_idDataset) {
        this.mContext = mContext;
        this.message = message;
        imgDataset = ImgDataset;
        mDataset = myDataset;
        addressDataset = AddressDataset;
        this.place_idDataset = place_idDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Picasso.get().load(imgDataset[position]).into(holder.imgView);
//        holder.imgView.setImageBitmap(imgDataset[position]);
        holder.mTextView.setText(mDataset[position]);
        holder.addTextView.setText(addressDataset[position]);
        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.d(TAG, "onclick: clicked on: " + addressDataset[position]);
                Log.d(TAG, "onclick: clicked on: " + position);
                Toast.makeText(mContext, addressDataset[position], Toast.LENGTH_SHORT).show();

                String id = place_idDataset[position];

                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET,"http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/information?place_id="+id,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());

//                            Intent intent = new Intent(DetailsActivity.this, info.class);
                                message_detail = response.toString();
//                            intent.putExtra("message", message);
//                            startActivity(intent);
                                Log.d(TAG, "message_detail: "+message_detail);


                                // Start NewActivity.class
//                                Intent myIntent = new Intent(mContext, DetailsActivity.class);
//                                myIntent.putExtra("message",message);
//                                myIntent.putExtra("position",position+"");
//                                myIntent.putExtra("message_detail", message_detail);
//                                mContext.startActivity(myIntent);


                                JSONArray json_details_arr;
                                String addr = null;
                                String name;
                                try {
                                    json_details_arr = new JSONArray(message_detail);
                                    Log.d(TAG, "message_detail: "+json_details_arr);

                                    addr = json_details_arr.getString(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                name = mDataset[position];
                                arr = addr.split(",");
                                if(arr.length ==4){
                                    address = arr[arr.length-4].trim();
                                }
                                String city = arr[arr.length-3].trim();
                                String state = arr[arr.length-2].trim().substring(0,2);
                                String country = arr[arr.length-1].trim();

                                Log.d(TAG, "address: "+address);
                                Log.d(TAG, "city: "+city);
                                Log.d(TAG, "state: "+state);
                                Log.d(TAG, "country: "+country);

                                Log.d(TAG, "yelp_review here: ");
                                RequestQueue mmQueue = Volley.newRequestQueue(mContext);
                                StringRequest mmJsonObjectRequest = new StringRequest(Request.Method.GET,"http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/yelp?name="+name+"&address="+address+"&city="+city+"&state="+state+"&country="+country,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.d(TAG, response);

                                                String id = response;
                                                Log.d(TAG, "yelp_id: "+id);

                                                Intent myIntent = new Intent(mContext, DetailsActivity.class);
                                                myIntent.putExtra("message",message);
                                                myIntent.putExtra("position",position+"");
                                                myIntent.putExtra("message_detail", message_detail);
//                                                  mContext.startActivity(myIntent);
                                                myIntent.putExtra("yelp_id",id);
                                                mContext.startActivity(myIntent);



//                                                RequestQueue Queue = Volley.newRequestQueue(mContext);
//                                                JsonObjectRequest JsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET,"http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/yelpreviews?id="+id,null,
//                                                        new Response.Listener<JSONObject>() {
//
//                                                            @Override
//                                                            public void onResponse(JSONObject response) {
//                                                                Log.d(TAG, response.toString());
//
//                                                                String yelp_review = response.toString();
//                                                                Log.d(TAG, "yelp_review: "+yelp_review);
//
//
//                                                                Intent myIntent = new Intent(mContext, DetailsActivity.class);
//                                                                myIntent.putExtra("message",message);
//                                                                myIntent.putExtra("position",position+"");
//                                                                myIntent.putExtra("message_detail", message_detail);
////                                                  mContext.startActivity(myIntent);
//                                                                myIntent.putExtra("yelp_review",yelp_review);
//                                                                mContext.startActivity(myIntent);
//
//                                                            }
//                                                        }, new Response.ErrorListener() {
//                                                    @Override
//                                                    public void onErrorResponse(VolleyError error) {
//                                                        Log.e(TAG, error.getMessage(), error);
//                                                    }
//                                                }
//                                                );
//                                                Queue.add(JsonObjectRequest1);
//

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG, error.getMessage(), error);
                                    }
                                }
                                );
                                mmQueue.add(mmJsonObjectRequest);





                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                    }
                }
                );
                mQueue.add(mJsonArrayRequest);


//                // Start NewActivity.class
//                Intent myIntent = new Intent(mContext, DetailsActivity.class);
//                myIntent.putExtra("message",message);
//                myIntent.putExtra("position",position+"");
//                myIntent.putExtra("message_detail", message_detail);
//                mContext.startActivity(myIntent);










            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}