package com.project2.faceoff;

/**
 * Created by q on 2017-07-11.
 */

public class ListViewItem {
    private String urlStr;
    private String nameStr;

    public void setName(String name){
        nameStr = name;
    }
    public void setUrl(String url) { urlStr = url; }
    public String getName(){
        return this.nameStr;
    }
    public String getUrl() { return this.urlStr; }
}
