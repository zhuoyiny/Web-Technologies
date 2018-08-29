package com.example.zhuoying.travelandentertainmentsearch;

public class ReviewItem {

    // time is utc millionseconds

    private String name;
    private String rating;
    private String time;
    private String review;
    private String photoUrl;
    private String link;

    public ReviewItem(String mName, String mRating, String mTime, String mReview, String mPhotoUrl, String mLink) {
        name = mName;
        rating = mRating;
        time = mTime;
        review = mReview;
        photoUrl = mPhotoUrl;
        link = mLink;
    }


    public String getReviewName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getReviewTime() {
        return time;
    }

    public String getReview() {
        return review;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getLink(){
        return link;
    }
}
