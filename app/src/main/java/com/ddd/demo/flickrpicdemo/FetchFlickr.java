package com.ddd.demo.flickrpicdemo;

import android.net.Uri;
import android.util.Log;

import com.ddd.demo.flickrpicdemo.dao.PhotoItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 14-9-24.
 */
public class FetchFlickr {
    private static final String TAG = "FetchFlickr";
    private static final String ENDPOINT = "https://www.flickr.com/services/rest";
    private static final String API_KEY = "c795b3de35c9dbd679a289eafcc769b1";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String PARAM_EXTRAS = "extras";
    private static final String EXTRA_SMALL_URL = "url_s";
    private static final String PRAM_TEXT = "text";

    private static final String XML_PHOTO = "photo";
    public static List<PhotoItem> items = new ArrayList<PhotoItem>();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    public String getXML(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String[] getPicUrls() {
        if (items.size() != 0){
            items.clear();
        }
            String requestUrl = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();
        Log.d(TAG, "request url: " + requestUrl);
        try {
            String response = getXML(requestUrl);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));
            String[] picUrls = new String[100];
            int i = 0;
            int eventType = parser.next();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                PhotoItem item = new PhotoItem();
                if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
                    String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
                    String id = parser.getAttributeValue(null, "id");
                    String owner = parser.getAttributeValue(null, "owner");
                    picUrls[i++] = smallUrl;
                    item.setId(id);
                    item.setOwner(owner);
                    item.setUrl(smallUrl);
                    items.add(item);
                }
                eventType = parser.next();
            }
            return picUrls;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
    }
}
