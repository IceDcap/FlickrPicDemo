package com.ddd.demo.flickrpicdemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

import com.ddd.demo.flickrpicdemo.fragment.ImagePagerFragment;
import com.ddd.demo.flickrpicdemo.fragment.PhotoPageFragment;

/**
 * Created by administrator on 14-9-24.
 */
public class PhotoPageActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null)
            return new PhotoPageFragment();
        else
            return new ImagePagerFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }
}
