package com.example.zhuoying.travelandentertainmentsearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;

public class DetailsActivity extends AppCompatActivity {

    private Context mContext;
    private String place_id;
    private Fragment infoFragment;
    private Fragment photosFragment;
    private Fragment mapFragment;
    private Fragment reviewsFragment;
    private TabLayout tabLayout;
    String message;
    String message_detail;
    String yelp;
    private String[] arr;
    private String name;
    private String addr;
    private String address;
    private String website;
    private String google_review;
    private String lati;
    private String longi;
    private static final String TAG = "DetailsActivity";
    private LinearLayout mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private MenuItem share_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIncomingIntent();

        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_place_details);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        createTabIcons();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        final ImageView fav = findViewById(R.id.favorite);




    }

    private void createTabIcons() {

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_info);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_photos);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_map);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_reviews);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        share_item = menu.findItem(R.id.share);
        share_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent tweet = new Intent();
                String message = "Check out "+name+" located at "+addr+ "\n"+ "Website: "+ website;
                tweet.setData(Uri.parse("https://twitter.com/intent/tweet/?text=" + Uri.encode(message)));
                startActivity(tweet);
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label1);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    if(infoFragment == null) {
                        infoFragment = new infoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("message", message_detail);
                        Log.d(TAG, message_detail);
                        infoFragment.setArguments(bundle);
                    }
                    return infoFragment;
                case 1:
                    if(photosFragment == null) {
                        photosFragment = new photosFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("place_id", place_id);
                        photosFragment.setArguments(bundle);
                    }
                    return photosFragment;
                case 2:
                    if(mapFragment == null) {
                        mapFragment = new mapFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("lati", lati);
                        bundle.putString("longi", longi);
                        bundle.putString("name", name);
                        Log.d(TAG, "......."+lati+"........zhuoying: "+longi);
                        mapFragment.setArguments(bundle);
                    }
                    return mapFragment;
                case 3:
                    if(reviewsFragment == null) {
                        reviewsFragment = new reviewsFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("google_review", google_review);
                        bundle.putString("yelp_id", yelp);
                        reviewsFragment.setArguments(bundle);
                    }
                    return reviewsFragment;
            }
            return null;

    }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }



    @SuppressLint("ResourceType")
    private void getIncomingIntent(){
        Log.d(TAG, "getting incoming intent");
        if(getIntent().hasExtra("message")&& getIntent().hasExtra("position")){
            message = getIntent().getStringExtra("message");
            message_detail = getIntent().getStringExtra("message_detail");
            yelp = getIntent().getStringExtra("yelp_id");
            Log.d(TAG, "yelp: "+yelp);
            String position_string = getIntent().getStringExtra("position");
//            String position_string = Integer.toString (getIntent().getStringExtra("position"));
//            int ip = Integer.parseInt(getIntent().getStringExtra("position").toString());
            int position = Integer.parseInt(position_string);
            Log.d(TAG, "message_detail: "+message_detail);
            Log.d(TAG, "position: "+position);

            JSONArray jsonArray = new JSONArray();
            JSONArray json_details_arr = new JSONArray();
            try {
                jsonArray = new JSONArray(message);
                json_details_arr = new JSONArray(message_detail);
            }catch(JSONException e){
                e.printStackTrace();
            }


            String[] img_array = new String[0];
            String[] names_array = new String[0];
            String[] address_array = new String[0];
            String[] placeid_array = new String[0];
            String[] lati_array = new String[0];
            String[] longi_array = new String[0];
            String next_page_token;
            try {
                img_array = getStringArray(2,jsonArray);
                names_array = getStringArray(3,jsonArray);
                address_array = getStringArray(4,jsonArray);
                placeid_array = getStringArray(5,jsonArray);
                lati_array = getStringArray(6,jsonArray);
                longi_array = getStringArray(7,jsonArray);
                name = names_array[position];
                lati = lati_array[position];
                longi = longi_array[position];
                addr = json_details_arr.getString(0);
                website = json_details_arr.getString(5);
                google_review = json_details_arr.getString(8);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            place_id = placeid_array[position];
            Log.d(TAG, place_id);


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
