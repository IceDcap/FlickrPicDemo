package com.ddd.demo.flickrpicdemo.activity;

import android.support.v4.app.Fragment;

import com.ddd.demo.flickrpicdemo.fragment.GalleryFragment;


public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new GalleryFragment();
    }
}
