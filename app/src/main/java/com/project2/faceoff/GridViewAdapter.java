package com.project2.faceoff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by q on 2017-07-11.
 */
public class GridViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<GridViewItem> gridViewItemList = new ArrayList<GridViewItem>() ;

    // GridViewAdapter의 생성자
    public GridViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현

    public int getCount() {
        return gridViewItemList.size() ;
    }

    public class WebViewClickListener implements View.OnTouchListener {
        private int position;
        private ViewGroup vg;
        private WebView wv;

        public WebViewClickListener(WebView wv, ViewGroup vg, int position) {
            this.vg = vg;
            this.position = position;
            this.wv = wv;
        }

        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_CANCEL:
                    return true;
                case MotionEvent.ACTION_UP:
                    sendClick();
                    return true;
            }

            return false;
        }

        public void sendClick() {
            GridView lv = (GridView) vg;
            lv.performItemClick(wv, position, 0);
        }
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        WebView imageWebView = (WebView) convertView.findViewById(R.id.webview) ;


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        GridViewItem gridViewItem = gridViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영

        imageWebView.loadUrl(gridViewItem.getUrl());
        imageWebView.getSettings().setLoadWithOverviewMode(true);
        imageWebView.getSettings().setUseWideViewPort(true);
        imageWebView.setVerticalScrollBarEnabled(false);
        imageWebView.setOnTouchListener(new WebViewClickListener(imageWebView, parent, position));
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현

    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현

    public Object getItem(int position) {
        return gridViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String url) {
        GridViewItem item = new GridViewItem();
        item.setUrl(url);
        gridViewItemList.add(item);
    }
}