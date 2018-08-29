package com.example.zhuoying.travelandentertainmentsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link reviewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link reviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reviewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Reviews";
    private View reviews;
    private String google_review_str;
    private Context mContext;
//    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLinearLayoutManager;

    private RecyclerView myrecyclerview;
    private ArrayList<ReviewItem> lstReview;
    private ArrayList<ReviewItem> googleReview;
    private ArrayList<ReviewItem> yelpReview;
    private ArrayList<ReviewItem> googleSort;
    private ArrayList<ReviewItem> yelpSort;
    private ArrayList<ReviewItem> googleReviewCopy = new ArrayList<>();

    private ArrayList<String> google_name;
    private ArrayList<Integer> google_rating;
    private ArrayList<String> google_time;
    private ArrayList<String> google_text;
    private ArrayList<String> google_img;
    private ArrayList<String> google_url;
    private ArrayList<String> google_iso;

    private ArrayList<String> yelp_name;
    private ArrayList<Integer> yelp_rating;
    private ArrayList<String> yelp_time;
    private ArrayList<String> yelp_text;
    private ArrayList<String> yelp_img;
    private ArrayList<String> yelp_url;


    private JSONArray yelp;


    private ArrayList<ReviewItem> sortbytime;



    boolean selectGoogle = true;
    ReviewAdapter reviewAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public reviewsFragment() {
        google_name = new ArrayList<String>();
        google_rating = new ArrayList<Integer>();
        google_time = new ArrayList<String>();
        google_text = new ArrayList<String>();
        google_img = new ArrayList<String>();
        google_url = new ArrayList<String>();
        google_iso = new ArrayList<String>();

        yelp_name = new ArrayList<String>();
        yelp_rating = new ArrayList<Integer>();
        yelp_time = new ArrayList<String>();
        yelp_text = new ArrayList<String>();
        yelp_img = new ArrayList<String>();
        yelp_url = new ArrayList<String>();

        lstReview = new ArrayList<>();
        googleReview = new ArrayList<>();
        yelpReview = new ArrayList<>();
        googleSort = new ArrayList<>();
        yelpSort = new ArrayList<>();



        sortbytime = new ArrayList<>();



    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static reviewsFragment newInstance(String param1, String param2) {
        reviewsFragment fragment = new reviewsFragment();
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
        if(reviews == null) {
            reviews = inflater.inflate(R.layout.fragment_reviews, container, false);

            myrecyclerview = (RecyclerView) reviews.findViewById(R.id.reviewRecycler);
            myrecyclerview.setHasFixedSize(true);

            mLinearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false);
            myrecyclerview.setLayoutManager(mLinearLayoutManager);


            google_review_str = getArguments().getString("google_review");
            String id =  getArguments().getString("yelp_id");
            Log.d(TAG, "....yelpid: "+id);
            Log.d(TAG, google_review_str);



//            RequestQueue Queue = Volley.newRequestQueue(getContext());
//            JsonObjectRequest JsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET,"http://zhuoyiny-php.us-east-2.elasticbeanstalk.com/yelpreviews?id="+id,null,
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d(TAG, response.toString());
//
//                            String yelp_review = response.toString();
//                            Log.d(TAG, "yelp_review: "+yelp_review);
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e(TAG, error.getMessage(), error);
//                }
//            }
//            );
//            Queue.add(JsonObjectRequest1);



            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            try {
                jsonArray = new JSONArray(google_review_str);

                for (int j = 0; j < jsonArray.length(); j++) {
                    jsonObject = jsonArray.getJSONObject(j);
                    google_name.add(jsonObject.getString("author_name"));
                    google_rating.add(jsonObject.getInt("rating"));
                    google_time.add(jsonObject.getString("time"));
                    google_text.add(jsonObject.getString("text"));
                    google_img.add(jsonObject.getString("profile_photo_url"));
                    google_url.add(jsonObject.getString("author_url"));


                    long utc = Long.parseLong(jsonObject.getString("time"));
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();

                    date.setTime(utc * 1000);

                    String gmtTime = sdf.format(date);
                    google_iso.add(gmtTime);
                }

                Log.d(TAG, google_img.toString());

                for (int j = 0; j < jsonArray.length(); j++) {

                    googleReview.add(new ReviewItem(google_name.get(j), Integer.toString(google_rating.get(j)), google_iso.get(j), google_text.get(j), google_img.get(j), google_url.get(j)));
                    googleSort.add(new ReviewItem(google_name.get(j), Integer.toString(google_rating.get(j)), google_iso.get(j), google_text.get(j), google_img.get(j), google_url.get(j)));
                }

                for (int i = 0; i < googleReview.size(); i++) {
                    googleReviewCopy.add(googleReview.get(i));
                }
                Log.i("googleReviewCopy", googleReviewCopy + "");

                lstReview = googleReview;
                Log.i("googleReview", googleReview + "");
                Log.d("....googleSort", googleSort+"");


//                String yelp_res = getArguments().getString("yelp_review");
//                yelp = new JSONArray(yelp_res);
//                JSONObject card,user;
//
//
//                for (int i=0; i<yelp.length(); i++) {
//                    card = yelp.getJSONObject(i);
//                    user = card.getJSONObject("user");
//                    yelp_name.add(user.getString("name"));
//                    yelp_rating.add(card.getInt("rating"));
//                    yelp_time.add(card.getString("time_created"));
//                    yelp_text.add(card.getString("text"));
//
//                    yelp_img.add(user.getString("image_url"));
//                    yelp_url.add(card.getString("url"));
//
//                    yelpReview.add(new ReviewItem(yelp_name.get(i),Integer.toString(yelp_rating.get(i)),yelp_time.get(i),yelp_text.get(i),yelp_img.get(i),yelp_url.get(i)));
//                    yelpSort.add(new ReviewItem(yelp_name.get(i),Integer.toString(yelp_rating.get(i)),yelp_time.get(i),yelp_text.get(i),yelp_img.get(i),yelp_url.get(i)));
//                }





                reviewAdapter = new ReviewAdapter(getContext(), lstReview);
                myrecyclerview.setAdapter(reviewAdapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }


            Spinner spinner1 = reviews.findViewById(R.id.review);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.review_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String text = parent.getItemAtPosition(position).toString();
                    //lstReview = new ArrayList<>();
                    if (text.equals("Google reviews")) {
                        selectGoogle = true;
//                        lstReview = googleReview;
                    }else {
                        TextView textView = reviews.findViewById(R.id.noreview);
                        textView.setText("No review");
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Spinner spinner2 = reviews.findViewById(R.id.sequence);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.sequence_array, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter2);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String text = parent.getItemAtPosition(position).toString();
                    Log.d("text1", text);
                    Log.d("text2", selectGoogle + "");

                    if (text.equals("Default Order")) {

                        if (selectGoogle) {
                            Log.i("googleReviewCopy", googleReviewCopy+"");
                            Collections.sort(googleReview, new SortbyratingDesend());
                            lstReview = googleReviewCopy;
                            Log.d(TAG, "Default Order");
                            reviewAdapter.updateData(googleReviewCopy);
                        } else {
                            lstReview = yelpReview;
//                            reviewAdapter.notifyDataSetChanged();
                        }
                    }


                    if (text.equals("Highest Rating")) {

                        if (selectGoogle) {
                            Log.i("googleReview", googleReview + "");
                            Log.i("...googleSort", googleSort + "");
//                            Collections.sort(googleSort, new SortbyratingDesend());
                            Collections.sort(googleReview, new SortbyratingDesend());
                            Log.d(TAG, googleSort + "");
                            lstReview = googleSort;
                            Log.d(TAG, "Highest Rating");
                            reviewAdapter.updateData(googleSort);
                        } else {
                            Collections.sort(yelpSort, new SortbyratingDesend());
                            lstReview = yelpSort;
                        }


                    }
                    if (text.equals("Lowest Rating")) {

                        if (selectGoogle) {
//                            Collections.sort(googleSort, new Sortbyrating());
                            Collections.sort(googleReview, new Sortbyrating());
                            lstReview = googleSort;
                            Log.d(TAG, googleSort + "");
                            Log.d(TAG, "lowest Rating");
                            reviewAdapter.updateData(googleSort);
                        } else {
                            Collections.sort(yelpSort, new Sortbyrating());
                            lstReview = yelpSort;
                        }


                    }
                    if (text.equals("Most Recent")) {
                        if (selectGoogle) {
//                            Collections.sort(googleSort, new SortbytimeDescend());
                            Collections.sort(googleReview, new SortbytimeDescend());
                            lstReview = googleSort;
                            Log.d(TAG, googleSort + "");
                            Log.d(TAG, "Most Recent");
                            reviewAdapter.updateData(googleSort);

                        } else {
                            Collections.sort(yelpSort, new SortbytimeDescend());
                            lstReview = yelpSort;
                        }

                    }
                    if (text.equals("Least Recent")) {

                        if (selectGoogle) {
//                            Collections.sort(googleSort, new Sortbytime());
                            Collections.sort(googleReview, new Sortbytime());
                            lstReview = googleSort;
                            Log.d(TAG, googleSort + "");
                            Log.d(TAG, "Least Recent");
                            reviewAdapter.updateData(googleSort);

                        } else {
                            Collections.sort(yelpSort, new Sortbytime());
                            lstReview = yelpSort;
                        }

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            reviewAdapter = new ReviewAdapter(getContext(), lstReview);
            myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            myrecyclerview.setAdapter(reviewAdapter);


            reviewAdapter.setOnItemClickListener(new ReviewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String link = lstReview.get(position).getLink();
                    Intent author = new Intent(Intent.ACTION_VIEW);

                    author.setData(Uri.parse(link));
                    startActivity(author);
                }
            });
//            Toast.makeText(getContext(), Integer.toString(lstReview.size()), Toast.LENGTH_SHORT).show();
        }

        return reviews;
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





















