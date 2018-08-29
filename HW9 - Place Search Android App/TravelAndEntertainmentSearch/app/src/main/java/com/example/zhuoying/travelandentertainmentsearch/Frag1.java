package com.example.zhuoying.travelandentertainmentsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.sql.BatchUpdateException;
import java.util.ArrayList;

import android.util.Log;

public class Frag1 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Frag1";

    private Button btnTEST;
    private Button clear_btn;
    private TextView mTextView;
    private Spinner mySpinner;
    private String lat_js;
    private String lon_js;
    private String address;
    private String keyword1;
    private int radius1;
    private AutoCompleteTextView mSearchText;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71,136));
    private EditText keyword;
    private EditText radius;
    private String category;
    private int radio_index;

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1_layout, container, false);
        btnTEST = (Button) view.findViewById(R.id.search);
        clear_btn = (Button) view.findViewById(R.id.button3);
        mySpinner=(Spinner) view.findViewById(R.id.spinner);
        mTextView = (TextView) view.findViewById(R.id.keyword);

        keyword = (EditText) view.findViewById(R.id.editText2);
        radius = (EditText) view.findViewById(R.id.editText3);
        category = mySpinner.getSelectedItem().toString();

        final TextInputLayout inputLayout = view.findViewById(R.id.inputLayout);


        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        int radioButton_id = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButton_id);
        radio_index = radioGroup.indexOfChild(radioButton);
        Log.d("position:",radio_index+"");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this.getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, LAT_LNG_BOUNDS, null);

        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

            btnTEST.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (keyword.getText() == null || keyword.getText().toString().trim().length() == 0) {
                        Toast.makeText(getContext(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getActivity(), "search button clicked", Toast.LENGTH_SHORT).show();

                        if (radio_index == 0) {
                            lat_js = "34.0266";
                            lon_js = "-118.2831";
                            address = "";

                            keyword1 = keyword.getText().toString();
                            radius1 = Integer.parseInt(radius.getText().toString());


                            Log.d(TAG, keyword1);
                            Log.d(TAG, radius.getText().toString());
                            Log.d(TAG, category);

                            ProgressDialog progressDialog = new ProgressDialog(Frag1.this.getActivity());
                            progressDialog.setMessage("Fetching next page");
                            progressDialog.show();

                            RequestQueue mQueue = Volley.newRequestQueue(getContext());
                            JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/app.js?keyword=" + keyword1 + "&category=" + category + "&radius=" + radius1 + "&lat_js=" + lat_js + "&lon_js=" + lon_js + "&address=", null,
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
//                            mTextView.setText(response.toString());
                                            Intent intent = new Intent(Frag1.this.getActivity(), DisplayJsonActivity.class);
                                            Log.d(TAG, response.toString());
                                            String message = response.toString();
                                            intent.putExtra("message", message);
                                            startActivity(intent);

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, error.getMessage(), error);
                                }
                            }
                            );
                            mQueue.add(mJsonArrayRequest);


                        } else if (radio_index == 1) {
                            lat_js = "";
                            lon_js = "";
                            address = "";
                        }

                    }
                }

            });


        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0) {
                    inputLayout.setError("Please enter mandatory field");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()!=0) {
                    inputLayout.setError(null);
                }


            }
        });


        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clear button clicked", Toast.LENGTH_SHORT).show();
                keyword1 = "";
                radius1 = 0;
            }

        });

        return view;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "Place query did not complete successfully: "+ places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            Log.d(TAG, "Entered address: "+ place.getAddress());
//            Log.d(TAG, "Place details: "+ place.getName());
//            Log.d(TAG, "Place details: "+ place.getLatLng());
//            mPlace = place;
//            latitude_ori = place.getLatLng().latitude;
//            longitude_ori = place.getLatLng().longitude;
            address= place.getAddress().toString();




            btnTEST.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    checkValid1(v);
//                    if (keyword.getText() == null || keyword1.trim().length() == 0) {
//                        Toast.makeText(getContext(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
//                    } else {
                        Toast.makeText(getActivity(), "search button clicked", Toast.LENGTH_SHORT).show();

                        final String keyword1 = keyword.getText().toString();
                        final int radius1 = Integer.parseInt(radius.getText().toString());

                        Log.d(TAG, keyword1);
                        Log.d(TAG, radius.getText().toString());
                        Log.d(TAG, category);

                        ProgressDialog progressDialog = new ProgressDialog(Frag1.this.getActivity());
                        progressDialog.setMessage("Fetching next page");
                        progressDialog.show();

                        RequestQueue mQueue = Volley.newRequestQueue(getContext());
                        JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/app.js?keyword=" + keyword1 + "&category=" + category + "&radius=" + radius1 + "&lat_js=&lon_js=&address=" + address, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
//                            mTextView.setText(response.toString());
                                        Intent intent = new Intent(Frag1.this.getActivity(), DisplayJsonActivity.class);
                                        Log.d(TAG, response.toString());
                                        String message = response.toString();
                                        intent.putExtra("message", message);
                                        startActivity(intent);

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.getMessage(), error);
                            }
                        }
                        );
                        mQueue.add(mJsonArrayRequest);

//                    }
                }

            });

        }
    };

//    public boolean checkValid1(View view) {
//        EditText keywordText = (EditText) view.findViewById(R.id.inputLayout);
//        String keywordTextStr = keywordText.getText().toString();
//        String keywordTextTrim = keywordTextStr.trim();
//        if (keywordTextStr == null || keywordTextStr.length() == 0 || keywordTextStr.equals("") ||
//                keywordTextTrim == null || keywordTextTrim.length() == 0 || keywordTextTrim.equals("")) {
//            keywordText.setError("Please enter mandatory field");
//            return false;
//        }
//        return true;
//    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
