package com.example.jaldbaazi_theurgentrental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;


    public class SliderAdapter extends PagerAdapter {


        Context context;
        LayoutInflater layoutInflater;

        public SliderAdapter(Context context) {
            this.context = context;
        }


        int images[] = {
                R.drawable.screen_one,
                R.drawable.screen_two,
                R.drawable.screent_three,
                R.drawable.screen_four,
                R.drawable.screen_five,
        };

        int headings[] = {
                R.string.onboarding_screen1_title,
                R.string.onboarding_screen2_title,
                R.string.onboarding_screen3_title,
                R.string.onboarding_screen4_title,
                R.string.onboarding_screen5_title,


        };

        int descriptions[]={
                R.string.onboarding_screen1_description,
                R.string.onboarding_screen2_description,
                R.string.onboarding_screen3_description,
                R.string.onboarding_screen4_description,
                R.string.onboarding_screen5_description,

        };


        @Override
        public int getCount() {
            return headings.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==(ConstraintLayout) object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view =layoutInflater.inflate(R.layout.slides_layout,container,false);

            ImageView imageView=view.findViewById(R.id.slider_image);
            TextView heading =view.findViewById(R.id.slider_heading);
            TextView desc=view.findViewById(R.id.slider_desc);


            imageView.setImageResource(images[position]);
            heading.setText(headings[position]);
            desc.setText(descriptions[position]);


            container.addView(view);


            return view;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ConstraintLayout)object);
        }
    }



