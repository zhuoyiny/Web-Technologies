package com.example.zhuoying.travelandentertainmentsearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link infoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link infoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class infoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final String TAG = "Info";
    private String message;
    private String name;
    private RatingBar ratingbar;
    private TextView address;
    private TextView phone;
    private TextView price;
    private TextView rating;
    private TextView google_page;
    private TextView website;
    private LinearLayout ll;
    private View info;
//    private Toolbar toolbar_place_details;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public infoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static infoFragment newInstance(String param1, String param2) {
        infoFragment fragment = new infoFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(info == null) {
            info = inflater.inflate(R.layout.fragment_info, container, false);
            ratingbar = (RatingBar) info.findViewById(R.id.ratingBar);
            address = (TextView) info.findViewById(R.id.real_address);
            phone = (TextView) info.findViewById(R.id.real_phone);
            price = (TextView) info.findViewById(R.id.real_price);
//            rating = (TextView) info.findViewById(R.id.real_rating);
            google_page = (TextView) info.findViewById(R.id.real_google_page);
            website = (TextView) info.findViewById(R.id.real_website);
            ll = (LinearLayout) info.findViewById(R.id.lin_address);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                toolbar_place_details = (Toolbar) info.findViewById(R.id.toolbar_place_details);
//            }


            message = getArguments().getString("message");
            name = getArguments().getString("name");

//        String message = getActivity().getIntent().getStringExtra("message");

            Log.d(TAG,"message");
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = new JSONArray(message);
                String address_str = jsonArray.getString(0);
                String phone_str = jsonArray.getString(1);
                String price_str = jsonArray.getString(2);
                String rating_str = jsonArray.getString(3);
                String google_page_str = jsonArray.getString(4);
                String website_str = jsonArray.getString(5);
                String priceLevel;

                if(price_str !="null"){
                    int int_price = Integer.parseInt(price_str);
                    if(int_price==1){
                        priceLevel = "$";
                    }else if(int_price==2){
                        priceLevel = "$$";
                    }else if(int_price==3){
                        priceLevel = "$$$";
                    }else if(int_price==4){
                        priceLevel = "$$$$";
                    }else{
                        priceLevel = "";
                    }
                }else{
                    priceLevel = "";
                }

                if(rating_str == "null"){
                    rating_str = "";
                }

                address.setText(address_str);
                phone.setText(phone_str);
                price.setText(priceLevel);
//                rating.setText(rating_str);
                ratingbar.setRating(Float.parseFloat(rating_str));
                google_page.setText(google_page_str);
                website.setText(website_str);

//                setSupportActionBar(toolbar_place_details);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                    toolbar_place_details.setTitle(name);
//                }

                Linkify.addLinks(google_page, Linkify.ALL);
                Linkify.addLinks(website, Linkify.ALL);
                Linkify.addLinks(phone, Linkify.ALL);

                if (address.getParent() != null) {
                    ((ViewGroup) address.getParent()).removeView(address); // <- fix
                }
                ll.addView(address);
//            ll.addView(address);
//            ll.addView(phone);
//            ll.addView(price);
//            ll.addView(rating);
//            ll.addView(google_page);
//            ll.addView(website);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        return info;

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
