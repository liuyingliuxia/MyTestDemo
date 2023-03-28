package com.lingong.mytest.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lingong.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ZDS
 * @Date:2023/3/28
 * @Desc:
 */
public class SwipeListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private LinearLayout mHeaderLayout;
    private ImageView mArrowImage;
    private TextView mHeaderText;
    private int mHeaderHeight;
    private boolean mIsRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipelist);

        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView = findViewById(R.id.listView);
        mHeaderLayout = findViewById(R.id.headerLayout);
        mArrowImage = findViewById(R.id.arrowImage);
        mHeaderText = findViewById(R.id.headerText);

        // 获取头布局的高度
        ViewTreeObserver observer = mHeaderLayout.getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mHeaderHeight = mHeaderLayout.getHeight();
                    mHeaderLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }

        // 绑定ListView的滚动监听器，处理箭头旋转动画
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstView = view.getChildAt(0);
                    if (firstView != null && firstView.getTop() == 0) {
                        int scrollY = -firstView.getTop();
                        if (scrollY >= mHeaderHeight && !mIsRefresh) {
                            mArrowImage.animate().rotation(-180);
                        } else {
                            mArrowImage.animate().rotation(0);
                        }
                    }
                }
            }
        });

        // 模拟数据
        final List<String> dataList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            dataList.add("Item" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        mIsRefresh = true;
        mArrowImage.animate().rotation(0).start();
        mHeaderText.setText("正在刷新...");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SystemClock.sleep(2000);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mIsRefresh = false;
                mSwipeRefreshLayout.setRefreshing(false);
                mHeaderText.setText("下拉刷新");
            }
        }.execute();
    }

}