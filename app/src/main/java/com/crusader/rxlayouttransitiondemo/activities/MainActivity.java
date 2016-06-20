package com.crusader.rxlayouttransitiondemo.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.crusader.rxlayouttransitiondemo.R;
import com.crusader.rxlayouttransitiondemo.adapters.PhotoAdapter;
import com.crusader.rxlayouttransitiondemo.application.LayoutTransitionApplication;
import com.crusader.rxlayouttransitiondemo.data.UnsplashService;
import com.crusader.rxlayouttransitiondemo.data.model.Photo;
import com.crusader.rxlayouttransitiondemo.grid.GridMarginDecoration;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity {

    private RecyclerView grid;
    private ProgressBar empty;
    private PhotoAdapter adapter;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Inject
    UnsplashService unsplashService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRecyclerView();

        resolveDependencies();

//        Retrofit retrofit = new Retrofit.Builder()
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(UnsplashService.ENDPOINT)
//                .build();
//
//        UnsplashService unsplashService = retrofit.create(UnsplashService.class);
//
        Observable<List<Photo>> photoObservable = unsplashService.getFeed();

        mSubscriptions.add(photoObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Photo>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Photo> photos) {
//                        for (int i = 0; i < photos.size(); i++) {
//                            Log.d(TAG, "onNext: "+photos.get(i).author);
//                        }
//                        Log.d(TAG, "onNext: "+photos.size());
                        adapter = new PhotoAdapter(MainActivity.this,
                                photos.subList(0, 21));
                        grid.setAdapter(adapter);
                        empty.setVisibility(View.GONE);
                    }
                }));

    }

    private void resolveDependencies() {
        ((LayoutTransitionApplication)getApplication())
                .getmApiComponent()
                .inject(MainActivity.this);
    }

    @Override
    public void initSetContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initComponents() {
        grid = (RecyclerView) findViewById(R.id.image_grid);
        empty = (ProgressBar) findViewById(android.R.id.empty);
    }

    @Override
    public void initListeners() {

    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) grid.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /* emulating https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0B6Okdz75tqQsck9lUkgxNVZza1U/style_imagery_integration_scale1.png */
                switch (position % 6) {
                    case 5:
                        return 3;
                    case 3:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        grid.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
        grid.setHasFixedSize(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
