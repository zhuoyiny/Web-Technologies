package com.example.zhuoying.travelandentertainmentsearch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Sortbytime implements Comparator<ReviewItem> {
    @Override
    public int compare(ReviewItem a, ReviewItem b){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(a.getReviewTime());
            Date date2 = sdf.parse(b.getReviewTime());
            return date1.compareTo(date2);
        }catch (ParseException e){
            return 0;
        }
    }
}
