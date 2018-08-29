package com.example.zhuoying.travelandentertainmentsearch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DisplayJsonActivity extends AppCompatActivity {

    private static final String TAG = "DisplayJsonActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLinearLayoutManager;
//    private MailAdapter mMailAdapter;
//    private List mEmailData;
    private String message;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_json);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra("message");
//        String[] ResultArray = intent.getStringArrayExtra("message");

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(message);
        }catch(JSONException e){
            e.printStackTrace();
        }

        // Capture the layout's TextView and set the string as its text



        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutManager = new LinearLayoutManager(DisplayJsonActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

//        EmailData mEmail = new EmailData("Sam", "Weekend adventure",
//                "Let's go fishing with John and others. We will do some barbecue and have soo much fun", "10:42am");
//        mEmailData.add(mEmail);

//        mMailAdapter = new MailAdapter(this, mEmailData);
//        mRecyclerView.setAdapter(mMailAdapter);

//        String[] mmEmailData;
//        mmEmailData = new String[5];
//        mmEmailData[0] = "Cheeseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
//        mmEmailData[1] = "Pepperonieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
//        mmEmailData[2] = "Black Oleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
//        mmEmailData[3] = "Black Olivdaeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
//        mmEmailData[4] = "Black Olivesdsaeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";

        String[] img_array = new String[0];
        String[] names_array = new String[0];
        String[] address_array = new String[0];
        String[] placeid_array = new String[0];
        try {
            img_array = getStringArray(2,jsonArray);
            names_array = getStringArray(3,jsonArray);
            address_array = getStringArray(4,jsonArray);
            placeid_array = getStringArray(5,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for(int i=0; i<jsonArray.length(); i++){
            Log.d(TAG,jsonArray.length()+"");
        }

        if(names_array.length != 0) {

            mAdapter = new MyAdapter(this, message, img_array, names_array, address_array, placeid_array);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            TextView textView = findViewById(R.id.jsonShow);
            textView.setText("No result.");
        }

    }

    public String[] getStringArray(int m, JSONArray jsonArray) throws JSONException {
        String[] stringsArray = new String[]{};

        if (jsonArray.getJSONArray(m) != null) {
            stringsArray = new String[jsonArray.getJSONArray(m).length()];
            for (int i = 0; i < jsonArray.getJSONArray(m).length(); i++) {
                stringsArray[i] = jsonArray.getJSONArray(m).getString(i);
            }
            return stringsArray;
        } else {
            return null;
        }

    }
}
