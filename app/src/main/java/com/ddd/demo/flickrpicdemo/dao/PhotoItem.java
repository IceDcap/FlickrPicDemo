package com.ddd.demo.flickrpicdemo.dao;

/**
 * Created by administrator on 14-9-24.
 */
public class PhotoItem {

    private String id;
    private String owner;
    private String url;

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {

        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getUrl() {
        return url;
    }

    public String getPhotPageUrl() {
        return "http://www.flickr.com/photos/" + owner + "/" + id;
    }
}
