package com.example.zhuoying.travelandentertainmentsearch;

import java.util.Comparator;

public class Sortbyrating implements Comparator<ReviewItem> {
    @Override
    public int compare(ReviewItem a, ReviewItem b){
        return Integer.parseInt(a.getRating()) - Integer.parseInt(b.getRating());
    }
}
