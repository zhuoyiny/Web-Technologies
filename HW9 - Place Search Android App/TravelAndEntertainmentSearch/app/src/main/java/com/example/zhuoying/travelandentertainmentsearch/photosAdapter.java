package com.example.zhuoying.travelandentertainmentsearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;



public class photosAdapter extends RecyclerView.Adapter<photosAdapter.ViewHolder> {

    private static final String TAG = "photosAdapter";
    private Context mContext;
//    private String message;
//    private String[] imgDataset;
//    private String[] mDataset;
//    private String[] addressDataset;
//    private String[] place_idDataset;
//    private String message_detail;
    private Bitmap[] bitmap_array;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public ImageView imgView;
//        public TextView mTextView;
//        public TextView addTextView;
//        public LinearLayout parentLayout;
        public ImageView photo_view;
        public ViewHolder(View v) {
            super(v);
//            imgView = v.findViewById(R.id.results_img);
//            mTextView = v.findViewById(R.id.results_name);
//            addTextView = v.findViewById(R.id.results_address);
//            parentLayout = v.findViewById(R.id.parent_layout);
            photo_view = (ImageView) v.findViewById(R.id.imgs);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public photosAdapter(Context mContext, Bitmap[] bitmap_array) {
        this.mContext = mContext;
//        this.message = message;
//        imgDataset = ImgDataset;
//        mDataset = myDataset;
//        addressDataset = AddressDataset;
//        this.place_idDataset = place_idDataset;
        this.bitmap_array = bitmap_array;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public photosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_photos, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "........."+bitmap_array.length);
        holder.photo_view.setImageBitmap(bitmap_array[position]);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        Picasso.get().load(imgDataset[position]).into(holder.imgView);
//        holder.mTextView.setText(mDataset[position]);
//        holder.addTextView.setText(addressDataset[position]);
//        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                Log.d(TAG, "onclick: clicked on: " + addressDataset[position]);
//                Log.d(TAG, "onclick: clicked on: " + position);
//                Toast.makeText(mContext, addressDataset[position], Toast.LENGTH_SHORT).show();
//
//                String id = place_idDataset[position];
//
//                RequestQueue mQueue = Volley.newRequestQueue(mContext);
//                JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET,"http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/information?place_id="+id,null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                Log.d(TAG, response.toString());
//
////                            Intent intent = new Intent(DetailsActivity.this, info.class);
//                                message_detail = response.toString();
////                            intent.putExtra("message", message);
////                            startActivity(intent);
//                                Log.d(TAG, "message_detail: "+message_detail);
//
//
//                                // Start NewActivity.class
//                                Intent myIntent = new Intent(mContext, DetailsActivity.class);
//                                myIntent.putExtra("message",message);
//                                myIntent.putExtra("position",position+"");
//                                myIntent.putExtra("message_detail", message_detail);
//                                mContext.startActivity(myIntent);
//
//
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, error.getMessage(), error);
//                    }
//                }
//                );
//                mQueue.add(mJsonArrayRequest);
//
//
////                // Start NewActivity.class
////                Intent myIntent = new Intent(mContext, DetailsActivity.class);
////                myIntent.putExtra("message",message);
////                myIntent.putExtra("position",position+"");
////                myIntent.putExtra("message_detail", message_detail);
////                mContext.startActivity(myIntent);
//            }
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bitmap_array.length;
    }
}