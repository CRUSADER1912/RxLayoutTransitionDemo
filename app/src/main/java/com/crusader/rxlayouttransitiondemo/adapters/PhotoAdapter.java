package com.crusader.rxlayouttransitiondemo.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crusader.rxlayouttransitiondemo.R;
import com.crusader.rxlayouttransitiondemo.activities.DetailActivity;
import com.crusader.rxlayouttransitiondemo.data.model.Photo;
import com.crusader.rxlayouttransitiondemo.utils.IntentUtil;

import java.util.List;

/**
 * Created by CRUSADER on 6/2/2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private final List<Photo> photos;
    private final Activity host;
    private final int requestedPhotoWidth;

    public PhotoAdapter(@NonNull Activity activity, @NonNull List<Photo> photos) {
        this.photos = photos;
        this.host = activity;
        requestedPhotoWidth = host.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                final Photo photo = photos.get(position);
                final Intent intent = new Intent(host, DetailActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra(IntentUtil.PHOTO, photo);
                intent.putExtra(IntentUtil.FONT_SIZE, holder.mTxtAuthor.getTextSize());
                intent.putExtra(IntentUtil.PADDING,
                        new Rect(holder.mTxtAuthor.getPaddingLeft(),
                                holder.mTxtAuthor.getPaddingTop(),
                                holder.mTxtAuthor.getPaddingRight(),
                                holder.mTxtAuthor.getPaddingBottom()));
                intent.putExtra(IntentUtil.TEXT_COLOR,
                        holder.mTxtAuthor.getCurrentTextColor());

                Pair<View, String> authorPair = new Pair<View, String>(
                        holder.mTxtAuthor, host.getString(R.string.transition_author));
                Pair<View, String> photoPair = new Pair<View, String>(
                        holder.mImgPhoto, host.getString(R.string.transition_photo));
                View decorView = host.getWindow().getDecorView();
                View statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
                View navBackground = decorView.findViewById(android.R.id.navigationBarBackground);
                Pair statusPair = null;
                Pair navPair = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    statusPair = Pair.create(statusBackground,
                            statusBackground.getTransitionName());
                    navPair = Pair.create(navBackground, navBackground.getTransitionName());
                }

                host.startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(host,
                                authorPair, photoPair, statusPair, navPair).toBundle());
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Photo mPhoto = photos.get(position);
        holder.mTxtAuthor.setText(mPhoto.author);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.mTxtAuthor.setTransitionName(host.getResources().getString(R.string.transition_author) + mPhoto.id);
//            holder.mImgPhoto.setTransitionName(host.getResources().getString(R.string.transition_photo) + mPhoto.id);
//        }
        Glide.with(host)
                .load(mPhoto.getPhotoUrl(requestedPhotoWidth))
                .placeholder(R.color.placeholder)
                .override(480, 400)
                .into(holder.mImgPhoto);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtAuthor;
        public ImageView mImgPhoto;

        public MyViewHolder(View view) {
            super(view);
            mTxtAuthor = (TextView) view.findViewById(R.id.author);
            mImgPhoto = (ImageView) view.findViewById(R.id.photo);
        }
    }
}
