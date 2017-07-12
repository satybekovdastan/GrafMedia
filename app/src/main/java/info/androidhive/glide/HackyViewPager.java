package info.androidhive.glide;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by Dastan on 24.06.2017.
 */

public class HackyViewPager extends ViewPager {

        public HackyViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                //uncomment if you really want to see these errors
                //e.printStackTrace();
                return false;
            }
        }
}
