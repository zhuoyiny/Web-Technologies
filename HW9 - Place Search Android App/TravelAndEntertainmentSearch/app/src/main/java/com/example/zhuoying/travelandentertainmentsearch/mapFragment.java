package com.example.zhuoying.travelandentertainmentsearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SafeBrowsingResponse;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mapFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Map";
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71,136));

    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private View map;
    private String lati_string;
    private String longi_string;
    private String name;
    private double latitude_ori;
    private double longitude_ori;
    private float lati;
    private float longi;
    private Spinner options;
    private GoogleMap mgoogleMap;
    private Place mPlace;
    private String name_ori;
    private String SetMode;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public mapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mapFragment newInstance(String param1, String param2) {
        mapFragment fragment = new mapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(map == null) {
            map = inflater.inflate(R.layout.fragment_map, container, false);

            options =(Spinner) map.findViewById(R.id.modespinner);
//            final String mode = options.getSelectedItem().toString();

            mGoogleApiClient = new GoogleApiClient
                    .Builder(this.getContext())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), this)
                    .build();


            mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, LAT_LNG_BOUNDS, null);

            lati_string = getArguments().getString("lati");
            longi_string = getArguments().getString("longi");
            name = getArguments().getString("name");
            Log.d(TAG, lati_string);
            Log.d(TAG, longi_string);
            lati = Float.parseFloat(lati_string);
            longi = Float.parseFloat(longi_string);
            final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    // Add a marker in Sydney, Australia,
                    // and move the map's camera to the same location.
                    mgoogleMap = googleMap;
//                    LatLng sydney = new LatLng(-33.852, 151.211);
                    LatLng sydney = new LatLng(lati, longi);
                    mgoogleMap.addMarker(new MarkerOptions().position(sydney)
                            .title(name));
                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14.0f));
                }
            });

            mSearchText = (AutoCompleteTextView) map.findViewById(R.id.real_from);
            mSearchText.setAdapter(mPlaceAutocompleteAdapter);
            mSearchText.setOnItemClickListener(mAutocompleteClickListener);


            final int iCurrentSelection = options.getSelectedItemPosition();
            options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                String mode = options.getSelectedItem().toString();
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i == 0){
                        SetMode = "driving";
                    }else if(i == 1){
                        SetMode = "bicycling";
                    }else if(i == 2) {
                        SetMode = "transit";
                    }else if(i == 3){
                        SetMode = "walking";
                    }
                    Log.d(TAG, SetMode);
                    modeChanged(SetMode);

                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

        }
        return map;
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
            Log.d(TAG, "Place details: "+ place.getAddress());
            Log.d(TAG, "Place details: "+ place.getName());
            Log.d(TAG, "Place details: "+ place.getLatLng());
            mPlace = place;
            latitude_ori = place.getLatLng().latitude;
            longitude_ori = place.getLatLng().longitude;
            name_ori= place.getName().toString();



            String serverKey = "AIzaSyBw8JT7SmJFxq7tidVR7u91Oa1A9uypc6U";
//              LatLng origin = new LatLng(34.0456591, -118.2561879);
            LatLng origin = new LatLng(latitude_ori, longitude_ori);
            LatLng destination = new LatLng(lati, longi);
//              mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination,12.0f));
            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            Log.d(TAG, "show map successfully");
                            if(direction.isOK()) {
                                // Do something
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.BLUE);

                                mgoogleMap.clear();
                                mgoogleMap.addPolyline(polylineOptions);
                                mgoogleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lati, longi))
                                        .title(name));
                                mgoogleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude_ori, longitude_ori))
                                        .title(name_ori));
                                LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
                                LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
                                LatLngBounds bounds = new LatLngBounds(southwest, northeast);
                                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                            Log.d(TAG, "show map failed.");
                        }
                    });

            places.release();

        }
    };


    private void modeChanged(String SetMode){


        String serverKey = "AIzaSyBw8JT7SmJFxq7tidVR7u91Oa1A9uypc6U";
//              LatLng origin = new LatLng(34.0456591, -118.2561879);
        LatLng origin = new LatLng(latitude_ori, longitude_ori);
        LatLng destination = new LatLng(lati, longi);
//              mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination,12.0f));
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(SetMode)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Log.d(TAG, "show map successfully");
                        if(direction.isOK()) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.BLUE);
                            mgoogleMap.clear();
                            mgoogleMap.addPolyline(polylineOptions);
                            mgoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lati, longi))
                                    .title(name));
                            mgoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude_ori, longitude_ori))
                                    .title(name_ori));
                            LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
                            LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
                            LatLngBounds bounds = new LatLngBounds(southwest, northeast);
                            mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                        Log.d(TAG, "show map failed.");
                    }
                });

//        places.release();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
