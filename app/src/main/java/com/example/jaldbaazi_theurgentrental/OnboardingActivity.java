package com.example.jaldbaazi_theurgentrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;

    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStarted,skipbtn,nextbtn;
    Animation animation;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted=findViewById(R.id.get_started_btn);
        skipbtn=findViewById(R.id.skip_btn);
        nextbtn=findViewById(R.id.next_btn);

//call Adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
//         calling adddot
        addDots(0);

        viewPager.addOnPageChangeListener(changeListener);

    }

    public void skip(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

    public void next(View view){
        viewPager.setCurrentItem(currentPos+1);

    }

    public void letsGetStarted(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
    }


    private void addDots(int position){
      dots=new TextView[5];

      dotsLayout.removeAllViews();

      for(int i=0;i<dots.length;i++){
          dots[i]=new TextView(this);
          dots[i].setText(HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY));
          dots[i].setTextSize(35);
          dotsLayout.addView(dots[i]);


      }
      if(dots.length>0){
          dots[position].setTextColor(getResources().getColor(R.color.my_color));
      }
    }

//    changing dots
    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        addDots(position);
        currentPos = position;

        int lastPosition = 0;
        if (position == 0 || position == 1 || position == 2 || position == 3) {
            letsGetStarted.setVisibility(View.INVISIBLE);
        } else {
            if (position > lastPosition) {
                // Slide to the right
                animation = AnimationUtils.loadAnimation(OnboardingActivity.this, R.anim.bottom_anime);
                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            } else if (position < lastPosition) {
                // Slide to the left
                letsGetStarted.setVisibility(View.INVISIBLE);
            }
        }

        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
};
}