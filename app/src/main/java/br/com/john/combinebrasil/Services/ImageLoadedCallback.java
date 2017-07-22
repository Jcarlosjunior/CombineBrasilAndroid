package br.com.john.combinebrasil.Services;

import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

/**
 * Created by GTAC on 21/07/2017.
 */

public class ImageLoadedCallback implements Callback {
    public ProgressBar progressBar;



    public  ImageLoadedCallback(ProgressBar progBar){
        progressBar = progBar;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}