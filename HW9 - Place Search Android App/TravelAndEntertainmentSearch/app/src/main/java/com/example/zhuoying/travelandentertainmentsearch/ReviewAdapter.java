package com.example.zhuoying.travelandentertainmentsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {


    Context mContext;
    ArrayList<ReviewItem> mData;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listner){
        mListener = listner;
    }

    public ReviewAdapter(Context mContext, ArrayList<ReviewItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_reviews,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v,mListener);

        return vHolder;

    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getReviewName());
        holder.tv_rating.setRating(Float.parseFloat(mData.get(position).getRating()));
        holder.tv_time.setText(mData.get(position).getReviewTime());
        holder.tv_review.setText(mData.get(position).getReview());
        Picasso.get().load(mData.get(position).getPhotoUrl()).fit().centerInside().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private RatingBar tv_rating;
        private TextView tv_time;
        private TextView tv_review;
        private ImageView img;


        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);


            tv_name = (TextView) itemView.findViewById(R.id.firstLine);
            tv_rating = (RatingBar) itemView.findViewById(R.id.rateBar);
            tv_time = (TextView) itemView.findViewById(R.id.secondLine);
            tv_review = (TextView) itemView.findViewById(R.id.thirdLine);
            img = (ImageView) itemView.findViewById(R.id.icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);

                        }
                    }

                }
            });




        }

    }
    public void updateData(ArrayList<ReviewItem> mData) {
        mData.clear();
        for (ReviewItem item : mData) {
            mData.add(item);
        }
        notifyDataSetChanged();

    }

}
