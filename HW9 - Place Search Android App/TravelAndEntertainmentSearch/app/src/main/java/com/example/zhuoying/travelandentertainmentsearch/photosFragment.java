package com.example.zhuoying.travelandentertainmentsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.places.GeoDataClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link photosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link photosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class photosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Images";
    private GeoDataClient mGeoDataClient;
    private View photos;
    private ImageView photo_view;
    private String place_id;
    private Bitmap bitmap;
    private Bitmap[] bitmap_array;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter photosAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLinearLayoutManager;

    private PlacePhotoMetadataBuffer photoMetadataBuffer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public photosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment photosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static photosFragment newInstance(String param1, String param2) {
        photosFragment fragment = new photosFragment();
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
        if(photos == null) {
            photos = inflater.inflate(R.layout.display_photos, container, false);
//            photo_view = new ImageView(getActivity());

            mRecyclerView = (RecyclerView) photos.findViewById(R.id.recyclerphotos);
            mRecyclerView.setHasFixedSize(true);

            mLinearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

//            photo_view = (ImageView) photos.findViewById(R.id.imgs);
            place_id = getArguments().getString("place_id");

            Log.d(TAG, place_id);

            getPhotos(place_id);

            Log.d(TAG, "555555555");
//            ImageView mImageView = new ImageView(getActivity());
//            mImageView.setImageBitmap(bitmap);
//            photo_view.setImageBitmap(bitmap);

        }





        return photos;
    }


    // Request photos and metadata for the specified place.
    private void getPhotos(String placeId) {
//        final String placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4";
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                photoMetadataBuffer = photos.getPhotoMetadata();

                bitmap_array = new Bitmap[photoMetadataBuffer.getCount()];

                for(int i=0; i<photoMetadataBuffer.getCount(); i++) {
                    get_Photos(i);
                }
                Log.d(TAG, ""+photoMetadataBuffer.getCount());

                Log.d(TAG, "get_photo_successfully");
//                        photo_view.setImageBitmap(bitmap);
                photosAdapter = new photosAdapter(getContext(), bitmap_array);
                mRecyclerView.setAdapter(photosAdapter);

            }
        });
    }


    private void get_Photos(final int num){
        // Get the first photo in the list.
        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(num);
        // Get the attribution text.
        CharSequence attribution = photoMetadata.getAttributions();
        // Get a full-size bitmap for the photo.
        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                bitmap = photo.getBitmap();
                bitmap_array[num] = bitmap;
            }
        });
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
