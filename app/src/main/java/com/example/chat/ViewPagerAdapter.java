package com.example.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {


    Context context;

    int images[] = {

            R.drawable.im4,
            R.drawable.im2,
            R.drawable.im3,
            R.drawable.im1

    };

    int heading[]={
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three,
            R.string.heading_fourth

    };

    public ViewPagerAdapter(Context context){

        this.context = context;

    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view =layoutInflater.inflate(R.layout.slider_layou,container,false);


        ImageView slidetitleimage =(ImageView) view.findViewById(R.id.titleImage);
        TextView slideHeading=(TextView) view.findViewById(R.id.texttitle);
        TextView slideDescription=(TextView) view.findViewById(R.id.textdeccription);


        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(heading[position]);


        container.addView(view);

        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);

    }
}


