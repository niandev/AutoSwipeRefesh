package com.brucetoo.autoswiperefesh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private QuickAdapter<String> adapter;
    private String[] strs = new String[]{"content 1","content 2","content 3","content 4","content 2","content 2","content 2","content 2","content 2","content 2"};
    private View progress;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                adapter.addAll(Arrays.asList(strs));
                listView.removeFooterView(progress);
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refresh);
        listView = (ListView) this.findViewById(R.id.listview);

        progress = getLayoutInflater().inflate(R.layout.footer_view,null);
        adapter = new QuickAdapter<String>(this,R.layout.list_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.text,item);
            }
        };

        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == adapter.getCount() - 1) {
                        listView.addFooterView(progress);
                        //imitate getting data
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(1);
                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set auto refresh when first reach page
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }, 500);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(Arrays.asList(strs));
                refreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

}
