//package com.example.zhuoying.travelandentertainmentsearch;
//
//import android.content.Context;
//import android.provider.ContactsContract;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class MailAdapter extends RecyclerView.Adapter {
//
//    private List mEmailData;
//    private Context mContext;
//
//    public static class MailViewHolder extends RecyclerView.ViewHolder {
//
//        TextView mIcon;
//        TextView mSender;
//        TextView mEmailTitle;
//        TextView mEmailDetails;
//        TextView mEmailTime;
//        ImageView mFavorite;
//
//        public MailViewHolder(View itemView) {
//            super(itemView);
//
//            mIcon = itemView.findViewById(R.id.tvIcon);
//            mSender = itemView.findViewById(R.id.tvEmailSender);
//            mEmailTitle = itemView.findViewById(R.id.tvEmailTitle);
//            mEmailDetails = itemView.findViewById(R.id.tvEmailDetails);
//            mEmailTime = itemView.findViewById(R.id.tvEmailTime);
//            mFavorite = itemView.findViewById(R.id.ivFavorite);
//
//        }
//    }
//
//
//    public MailAdapter(Context mContext, List mEmailData) {
//        this.mEmailData = mEmailData;
//        this.mContext = mContext;
//    }
//
////    @Override
////    public MailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
////                parent, false);
////        return new MailViewHolder(view);
////    }
//
//    // - get element from your dataset at this position
//    // - replace the contents of the view with that element
//    @Override
//    public void onBindViewHolder(MailViewHolder holder, int position) {
//
//        holder.mSender.setText(mEmailData.get(position).getmSender());
//        holder.mEmailTitle.setText(mEmailData.get(position).getmTitle());
//        holder.mEmailDetails.setText(mEmailData.get(position).getmDetails());
//        holder.mEmailTime.setText(mEmailData.get(position).getmTime());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mEmailData.size();
//    }
//
//
//
//
//}
////
////class MailViewHolder extends RecyclerView.ViewHolder {
////
////    TextView mIcon;
////    TextView mSender;
////    TextView mEmailTitle;
////    TextView mEmailDetails;
////    TextView mEmailTime;
////    ImageView mFavorite;
////
////    public MailViewHolder(View itemView) {
////        super(itemView);
////
////        mIcon = itemView.findViewById(R.id.tvIcon);
////        mSender = itemView.findViewById(R.id.tvEmailSender);
////        mEmailTitle = itemView.findViewById(R.id.tvEmailTitle);
////        mEmailDetails = itemView.findViewById(R.id.tvEmailDetails);
////        mEmailTime = itemView.findViewById(R.id.tvEmailTime);
////        mFavorite = itemView.findViewById(R.id.ivFavorite);
////
////    }
////}
