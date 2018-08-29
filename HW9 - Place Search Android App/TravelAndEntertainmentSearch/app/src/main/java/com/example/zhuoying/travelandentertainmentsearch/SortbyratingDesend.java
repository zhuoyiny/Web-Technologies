package com.example.zhuoying.travelandentertainmentsearch;

import java.util.Comparator;

public class SortbyratingDesend implements Comparator<ReviewItem> {
    @Override
    public int compare(ReviewItem a, ReviewItem b){
        return Integer.parseInt(b.getRating()) - Integer.parseInt(a.getRating());
    }
}
