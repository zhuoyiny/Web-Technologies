//package com.example.zhuoying.travelandentertainmentsearch;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.w3c.dom.Text;
//
//public class info extends android.support.v4.app.Fragment {
//
//    private static final String TAG = "Info";
//    private String message;
//    private TextView address;
//    private TextView phone;
//    private TextView price;
//    private TextView rating;
//    private TextView google_page;
//    private TextView website;
//    private LinearLayout ll;
//    private View info;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        if(info == null) {
//        info = inflater.inflate(R.layout.info_layout, container, false);
//        address = (TextView) info.findViewById(R.id.real_address);
//        phone = (TextView) info.findViewById(R.id.real_phone);
//        price = (TextView) info.findViewById(R.id.real_price);
////        rating = (TextView) info.findViewById(R.id.real_rating);
//        google_page = (TextView) info.findViewById(R.id.real_google_page);
//        website = (TextView) info.findViewById(R.id.real_website);
//        ll = (LinearLayout) info.findViewById(R.id.lin_address);
//
//        String message = getArguments().getString("message");
////        String message = getActivity().getIntent().getStringExtra("message");
//
//            Log.d(TAG,message);
//        JSONArray jsonArray = new JSONArray();
//        try {
//            jsonArray = new JSONArray(message);
//            String address_str = jsonArray.getString(0);
//            String phone_str = jsonArray.getString(1);
//            String price_str = jsonArray.getString(2);
//            String rating_str = jsonArray.getString(3);
//            String google_page_str = jsonArray.getString(4);
//            String website_str = jsonArray.getString(5);
//
//            address.setText(address_str);
//            phone.setText(phone_str);
//            price.setText(price_str);
//            rating.setText(rating_str);
//            google_page.setText(google_page_str);
//            website.setText(website_str);
//
//
//            if (address.getParent() != null) {
//                ((ViewGroup) address.getParent()).removeView(address); // <- fix
//            }
//            ll.addView(address);
////            ll.addView(address);
////            ll.addView(phone);
////            ll.addView(price);
////            ll.addView(rating);
////            ll.addView(google_page);
////            ll.addView(website);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
//        return info;
//
//}
//}
