package com.crusader.rxlayouttransitiondemo.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crusader.rxlayouttransitiondemo.R;
import com.crusader.rxlayouttransitiondemo.application.LayoutTransitionApplication;
import com.crusader.rxlayouttransitiondemo.data.UnsplashService;
import com.crusader.rxlayouttransitiondemo.data.model.Photo;
import com.crusader.rxlayouttransitiondemo.grid.GridMarginDecoration;
import com.crusader.rxlayouttransitiondemo.utils.IntentUtil;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

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
    private FastItemAdapter<Photo> fastAdapter;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Inject
    UnsplashService unsplashService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRecyclerView();
        grid.setAdapter(fastAdapter);

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
                        fastAdapter.add(photos.subList(0, 21));
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
        fastAdapter = new FastItemAdapter<>();
    }

    @Override
    public void initListeners() {
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<Photo>() {
            @Override
            public boolean onClick(View v, IAdapter<Photo> adapter, Photo photo, int position) {
                if (position == RecyclerView.NO_POSITION) return true;

                TextView mTxtAuthor = photo.getViewHolder(v).getmTxtAuthor();
                ImageView mImgPhoto = photo.getViewHolder(v).getmImgPhoto();
                final Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra(IntentUtil.PHOTO, photo);
                intent.putExtra(IntentUtil.FONT_SIZE, mTxtAuthor.getTextSize());
                intent.putExtra(IntentUtil.PADDING,
                        new Rect(mTxtAuthor.getPaddingLeft(),
                                mTxtAuthor.getPaddingTop(),
                                mTxtAuthor.getPaddingRight(),
                                mTxtAuthor.getPaddingBottom()));
                intent.putExtra(IntentUtil.TEXT_COLOR,
                        mTxtAuthor.getCurrentTextColor());

                Pair<View, String> authorPair = new Pair<View, String>(
                        mTxtAuthor, getString(R.string.transition_author));
                Pair<View, String> photoPair = new Pair<View, String>(
                        mImgPhoto, getString(R.string.transition_photo));
                View decorView = getWindow().getDecorView();
                View statusBackground = null;
                View navBackground = null;
                Pair statusPair = null;
                Pair navPair = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
                    navBackground = decorView.findViewById(android.R.id.navigationBarBackground);
                    statusPair = Pair.create(statusBackground,
                            statusBackground.getTransitionName());
                    if(navBackground != null) {
                        navPair = Pair.create(navBackground, navBackground.getTransitionName());
                    }
                }

                if(navPair != null){
                    startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                    authorPair, photoPair, statusPair, navPair).toBundle());
                }else{
                    startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                                    authorPair, photoPair, statusPair).toBundle());
                }
                return true;
            }
        });

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
