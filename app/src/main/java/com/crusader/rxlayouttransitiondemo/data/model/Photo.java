package com.crusader.rxlayouttransitiondemo.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crusader.rxlayouttransitiondemo.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.Locale;

/**
 * Model class representing data returned from unsplash.it
 */
public class Photo extends AbstractItem<Photo, Photo.MyViewHolder> implements Parcelable {

    /*{
        "format": "jpeg",
        "width": 5616,
        "height": 3744,
        "filename": "0000_yC-Yzbqy7PY.jpeg",
        "id": 0,
        "author": "Alejandro Escamilla",
        "author_url": "https://unsplash.com/alejandroescamilla",
        "post_url": "https://unsplash.com/photos/yC-Yzbqy7PY"
    }*/

    private  String format;
    private  int width;
    public  int height;
    private  String filename;
    public  long id;
    public  String author;
    private  String author_url;
    private  String post_url;

    private static final String PHOTO_URL_BASE = "https://unsplash.it/%d?image=%d";

    //the ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends MyViewHolder> FACTORY = new ItemFactory();

//    public Photo(String format,
//                 int width,
//                 int height,
//                 String filename,
//                 long id,
//                 String author,
//                 String author_url,
//                 String post_url) {
//        this.format = format;
//        this.width = width;
//        this.height = height;
//        this.filename = filename;
//        this.id = id;
//        this.author = author;
//        this.author_url = author_url;
//        this.post_url = post_url;
//    }

    private Photo(Parcel in) {
        format = in.readString();
        width = in.readInt();
        height = in.readInt();
        filename = in.readString();
        id = in.readLong();
        author = in.readString();
        author_url = in.readString();
        post_url = in.readString();
    }

    public Photo() {
    }

    public String getPhotoUrl(int requestWidth) {
        return String.format(Locale.getDefault(), PHOTO_URL_BASE, requestWidth, id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(format);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(filename);
        dest.writeLong(id);
        dest.writeString(author);
        dest.writeString(author_url);
        dest.writeString(post_url);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int getType() {
        return R.id.fastadapter_sample_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.photo_item;
    }

    @Override
    public void bindView(MyViewHolder holder) {
        super.bindView(holder);

//        Photo mPhoto = photos.get(position);

        //get the context
        Context ctx = holder.itemView.getContext();

        final int requestedPhotoWidth = ctx.getResources().getDisplayMetrics().widthPixels;

        holder.mTxtAuthor.setText(author);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.mTxtAuthor.setTransitionName(host.getResources().getString(R.string.transition_author) + mPhoto.id);
//            holder.mImgPhoto.setTransitionName(host.getResources().getString(R.string.transition_photo) + mPhoto.id);
//        }
        Glide.with(ctx)
                .load(getPhotoUrl(requestedPhotoWidth))
                .placeholder(R.color.placeholder)
                .override(480, 400)
                .into(holder.mImgPhoto);
    }

    @Override
    public ViewHolderFactory<? extends MyViewHolder> getFactory() {
        return FACTORY;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtAuthor;
        ImageView mImgPhoto;

        MyViewHolder(View view) {
            super(view);
            mTxtAuthor = (TextView) view.findViewById(R.id.author);
            mImgPhoto = (ImageView) view.findViewById(R.id.photo);
        }

        public TextView getmTxtAuthor() {
            return mTxtAuthor;
        }

        public ImageView getmImgPhoto() {
            return mImgPhoto;
        }
    }

    /**
     * our ItemFactory implementation which creates the ViewHolder for our adapter.
     * It is highly recommended to implement a ViewHolderFactory as it is 0-1ms faster for ViewHolder creation,
     * and it is also many many times more efficient if you define custom listeners on views within your item.
     */
    protected static class ItemFactory implements ViewHolderFactory<MyViewHolder> {
        public MyViewHolder create(View v) {
            return new MyViewHolder(v);
        }
    }
}
