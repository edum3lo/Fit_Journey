package com.stackmobile.fit_journey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList;

    public ImageAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePathList.get(position));
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
