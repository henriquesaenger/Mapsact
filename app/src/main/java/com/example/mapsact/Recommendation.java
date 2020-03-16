package com.example.mapsact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import net.librec.recommender.content.HFTRecommender;

public class Recommendation extends FragmentActivity {

    //HFTRecommender hftRecommender= new HFTRecommender();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_activity);


    }
}
