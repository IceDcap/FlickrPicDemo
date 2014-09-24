package com.ddd.demo.flickrpicdemo.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ddd.demo.flickrpicdemo.FetchFlickr;
import com.ddd.demo.flickrpicdemo.activity.PhotoPageActivity;
import com.ddd.demo.flickrpicdemo.R;
import com.ddd.demo.flickrpicdemo.dao.PhotoItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by administrator on 14-9-24.
 */
public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private GridView mGridView;
    private String[] picUrls;
    DisplayImageOptions options;
    private boolean checkPageByWebSite = false;
    ArrayList<PhotoItem> items;
    private int mWhat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        updateUi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position, checkPageByWebSite);
            }
        });
        return view;
    }

    private void startImagePagerActivity(int position, boolean byWebView) {
        PhotoItem item = FetchFlickr.items.get(position);
        Intent intent = new Intent(getActivity(), PhotoPageActivity.class);
        if (!byWebView) {
            intent.putExtra(ImagePagerFragment.IMAGES, picUrls);
            intent.putExtra(ImagePagerFragment.IMAGE_POSITION, position);
        } else {
            Uri photoPageUri = Uri.parse(item.getPhotPageUrl());
            intent.setData(photoPageUri);
        }
        startActivity(intent);
    }

    class FetchPicTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            return new FetchFlickr().getPicUrls();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            picUrls = strings;
            setupAdapter();
        }
    }

    void setupAdapter() {
        if (getActivity() == null || mGridView == null) return;
        if (picUrls != null) {
            mGridView.setAdapter(new ImageAdapter());
        }
    }

    class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return picUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return picUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.item_grid_view, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }
            ImageLoader.getInstance().displayImage(picUrls[position], imageView, options);
            return imageView;
        }
    }

    @Override
    @TargetApi(11)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                updateUi();
                return true;
            case R.id.photoInfo:
                showSwitchDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    @TargetApi(11)
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            Log.d(TAG, "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
//
//        }
    }

    private void showSwitchDialog() {
        new AlertDialog.Builder(getActivity()).setTitle("Select a way to photo check!")
                .setSingleChoiceItems(new String[]{"只看图片", "进入Flickr查看"}, mWhat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "===which===" + which);
                        switch (which) {
                            case 0:
                                checkPageByWebSite = false;
                                mWhat = 0;
                                break;
                            case 1:
                                checkPageByWebSite = true;
                                mWhat = 1;
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void updateUi() {
        new FetchPicTask().execute();
    }
}
