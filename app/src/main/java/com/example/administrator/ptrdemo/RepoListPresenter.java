package com.example.administrator.ptrdemo;

import android.os.AsyncTask;

import com.example.administrator.ptrdemo.view.PtrPageView;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fragment在做视图工作(实现了PtrPagerView视图接口,也就是下拉和上拉功能视图接口)
 * <p/>
 * Presenter里做了具体要做的业务(下拉刷新获取数据,上拉加载更多数据),以及视图的触发
 * <p/>
 * 直接使用MVP库Mosby库
 * 让你的应用简单清晰的使用MVP架构方式来构建开发
 * <p/>
 * 依赖mosby
 * Created by Administrator on 2016/7/4 0004.
 */
public class RepoListPresenter extends MvpNullObjectBasePresenter<PtrPageView> {

    //    下拉刷新视图层的业务逻辑-------------------------------------------------
    public void loadData() {
        new LoadDataTask().execute();
    }

    //    上拉加载更多视图层的业务逻辑---------------------------------------------
    public void loadMore() {
        new LoadMoreTask().execute();
    }

    private static int count = 0;

    private final class LoadDataTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
//            模拟网络链接
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int size = new Random().nextInt(40);
            final ArrayList<String> loadDatas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                loadDatas.add("我是第" + i + "条数据");
            }
            return loadDatas;
        }

        @Override
        protected void onPostExecute(List<String> datas) {
            super.onPostExecute(datas);
            int size = datas.size();
//            模拟空数据时的(视图)情况
            if (size == 0) {
                getView().showEmptyView();//listview不可见了,空的textview可见
            }
//            模拟错误数据时的(视图)情况
            else if (size == 1) {
                getView().showErrorView("unknow error");//listview不可见,空的textView不可见,错误的textview可见
            }
//            模拟正常获取到了数据的(视图)情况
            else {
                getView().showContentView();//显示内容视图(让listview能显示)
//                视图进行数据刷新
                getView().refreshData(datas);
            }
//            停至结束这次下来刷新
            getView().stopRefresh();
        }
    }

    private final class LoadMoreTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            显示加载中
            getView().showLoadMoreLoading();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
//            模拟加载更多时,网络链接
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final ArrayList<String> loadDatas = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                loadDatas.add("我是loadMore的第" + i + "条数据");
            }
            return loadDatas;
        }

        @Override
        protected void onPostExecute(List<String> datas) {
            super.onPostExecute(datas);
//            将加载的数据加载到视图上
            getView().addMoreData(datas);
//            隐藏加载中
            getView().hideLoadMore();
        }
    }

}
