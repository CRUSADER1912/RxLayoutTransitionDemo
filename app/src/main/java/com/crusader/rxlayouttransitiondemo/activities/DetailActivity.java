package com.crusader.rxlayouttransitiondemo.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crusader.rxlayouttransitiondemo.R;
import com.crusader.rxlayouttransitiondemo.custom.ThreeTwoImageView;
import com.crusader.rxlayouttransitiondemo.data.model.Photo;
import com.crusader.rxlayouttransitiondemo.utils.IntentUtil;

import java.util.List;

public class DetailActivity extends BaseActivity {

    private SharedElementCallback elementCallback;
    private TextView mTxtAuthor;

    private float targetTextSize;
    private ColorStateList targetTextColors;
    private LinearLayout mDescription;
    private ThreeTwoImageView mPhoto;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSharedElementCallback();

        int requestedPhotoWidth = getResources().getDisplayMetrics().widthPixels;
        Photo mPhotoData = (Photo) getIntent().getParcelableExtra(IntentUtil.PHOTO);

        setEnterSharedElementCallback(elementCallback);
        int slideDuration = getResources().getInteger(R.integer.detail_desc_slide_duration);

        mTxtAuthor.setText(mPhotoData.author);

        Glide.with(this)
                .load(mPhotoData.getPhotoUrl(requestedPhotoWidth))
                .placeholder(R.color.placeholder)
                .override(480, 400)
                .into(mPhoto);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.description);
            slide.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator
                    .linear_out_slow_in));
            slide.setDuration(slideDuration);
            getWindow().setEnterTransition(slide);
        }
    }

    @Override
    public void initSetContentView() {
        setContentView(R.layout.activity_detail);
    }

    @Override
    public void initComponents() {
        mTxtAuthor = (TextView) findViewById(R.id.author);
        mDescription = (LinearLayout) findViewById(R.id.description);
        mPhoto = (ThreeTwoImageView) findViewById(R.id.photo);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void initListeners() {

    }

    private void initSharedElementCallback() {
        elementCallback = new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames,
                                             List<View> sharedElements,
                                             List<View> sharedElementSnapshots) {
                targetTextSize = mTxtAuthor.getTextSize();
                targetTextColors = mTxtAuthor.getTextColors();
                mTxtAuthor.setTextColor(getIntent().getIntExtra(IntentUtil.TEXT_COLOR, Color.BLACK));
                float textSize = getIntent().getFloatExtra(IntentUtil.FONT_SIZE, targetTextSize);
                mTxtAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                Rect padding = getIntent().getParcelableExtra(IntentUtil.PADDING);
                mTxtAuthor.setPadding(padding.left, padding.top, padding.right, padding.bottom);
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                                           List<View> sharedElements,
                                           List<View> sharedElementSnapshots) {
                mTxtAuthor.setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
                mTxtAuthor.setTextColor(targetTextColors);
                forceSharedElementLayout(mDescription);
            }
        };
    }

    private void forceSharedElementLayout(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getHeight(),
                View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }
}
