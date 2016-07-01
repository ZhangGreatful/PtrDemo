package com.example.administrator.ptrdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class RepoListFragment extends Fragment implements PtrView<List<String>> {

    @Bind(R.id.lvRepos)
    ListView              listView;
    @Bind(R.id.ptrClassicFramLayout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Bind(R.id.emptyView)
    TextView              emptyView;
    @Bind(R.id.errorView)
    TextView              errorView;

    private ArrayAdapter<String> adpter;
    private ArrayList<String> datas = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//        下拉刷新
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                执行加载数据的方法
                loadData();

            }
        });
        adpter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adpter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //    这是视图层的业务逻辑-----------------------------
//    当出现错误或空信息时,点击可以继续加载
    @OnClick({R.id.errorView,R.id.emptyView})
    public void loadData() {
        final int size=new Random().nextInt(5);
//        加载数据
//        加载中,出现不同的情况,应该显示不同的视图
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
//                    showMessage("Unknow Error");
                    return;
                }
                datas.clear();
                for (int i = 1; i < size; i++) {
                    datas.add("我是第" + (++count) + "条数据");
                }
                asyncLoadData(size);
            }
        }).start();
    }

    private void asyncLoadData( final int size) {

//        视图要到UI线程里加载
       ptrClassicFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                //        模拟空数据时的(视图)情况
                if (size == 0) {
                    showEmptyView();//listView不可见了,空的textView可见了
                }
                //        模拟错误数据时(视图)的情况
                else if (size == 1) {
                    showErrorView("unknow error");//listView不可见了,空的textView不可见了,错误的textView可见
                }
                //        模拟正常获取到数据(视图)的情况
                else {
                    //        显示内容视图(让ListView能显示)
                    showContentView();

                    //            视图进行数据刷新
                    refreshData(datas);
                }
                //        停止结束这次下来刷新
                stopRefresh();
            }
        });


    }

    private static int count;

    //    -----这是视图层的实现------------------------------------------------------
    @Override
    public void showContentView() {
        ptrClassicFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String msg) {
        ptrClassicFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        ptrClassicFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void refreshData(List<String> datas) {
        adpter.clear();
        adpter.addAll(datas);
        adpter.notifyDataSetChanged();
    }

    @Override
    public void stopRefresh() {
        ptrClassicFrameLayout.refreshComplete();//下拉刷新完成
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
