package sahil.quickNotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {
    PreferenceHandler handler;
    private int boardingLayouts[];
    ViewPager onBoardPager;
    private TextView dots[];
    private FloatingActionButton sampleButton;
    LinearLayout dotsLayout;
    OnboardPagerAdapter adapter;
    /*
    * Activity that is launched on very first start
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        handler = new PreferenceHandler(this);
        if (!handler.isFirstLogin())
        {
            /*
            * If it is not the first launch then directly go to login page
            * */
            startActivity(new Intent(FirstActivity.this,LoginActivity.class));
            finish();
        }
        else
        {
            /*
            * Update shared Preferences in either case
            * */
        }

        init();
        addShiftingDots(0);

        onBoardPager.setAdapter(adapter);
        onBoardPager.addOnPageChangeListener(adapter);

        //Listener for shifting onboarding screen and moving to login screen
        sampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curPage=getCurrentPage();
                if(curPage<=boardingLayouts.length-1)
                {
                    onBoardPager.setCurrentItem(curPage);
                }
                else
                {
                    View v=onBoardPager.getRootView();
                    String s=((EditText) v.findViewById(R.id.pinSetter)).getText().toString();
                    if(s.length()==4)
                    {
                        setPasswordandLogin(s);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Pin should be exactly of 4 numbers",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setPasswordandLogin(String s) {
        handler.setPreference(PreferenceHandler.pin,s);
        handler.setFirstLogin();
        handler.setPreference(PreferenceHandler.loginStatus,getString(R.string.login_state_true));
        startActivity(new Intent(FirstActivity.this,LoginActivity.class));
        finish();
    }

    private void init() {

        boardingLayouts=new int[]{R.layout.on_board_screen_1,R.layout.on_board_screen_b};
        onBoardPager=(ViewPager) findViewById(R.id.onBoardPager);
        dotsLayout=(LinearLayout) findViewById(R.id.dots);
        sampleButton=(FloatingActionButton) findViewById(R.id.sampleButton);
        adapter=new OnboardPagerAdapter();
    }

    private void addShiftingDots(int pageNumber) {

        /*
        * Function to change color of active dot at screen bottom, to denote currently active activity
        * */
        dots=new TextView[boardingLayouts.length];
        int activeColor[]=getResources().getIntArray(R.array.array_dot_active);
        int inactive[]=getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            if(i!=pageNumber)
            dots[i].setTextColor(inactive[i]);
            else
            dots[i].setTextColor(activeColor[i]);
            dotsLayout.addView(dots[i]);
        }
    }

    private int getCurrentPage()
    {
        return onBoardPager.getCurrentItem()+1;
    }

    public class OnboardPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
        /*
        * Adapter to handle page movements
        * */

        OnboardPagerAdapter() {       }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v=layoutInflater.inflate(boardingLayouts[position],container,false);
            container.addView(v);

            return v;
        }

        @Override
        public int getCount() {
            return boardingLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position==0)
            {
                sampleButton.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
            }
            else if(position==boardingLayouts.length-1)
            {
                sampleButton.setImageResource(R.drawable.ic_check);
            }
        }

        @Override
        public void onPageSelected(int position) {
            addShiftingDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

}
