package info.androidhive.glide.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;

import info.androidhive.glide.HackyViewPager;
import info.androidhive.glide.R;
import info.androidhive.glide.model.Image;
import pl.droidsonroids.gif.GifImageView;


public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    private Button btn_donload;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    PhotoViewAttacher photoViewAttacher;
    Image image;
    GifImageView  gif_animation;
    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        btn_donload = (Button) v.findViewById(R.id.btn_donload);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        setHasOptionsMenu(true);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " из " + images.size());

        image = images.get(position);
        lblTitle.setText(image.getName());
        lblDate.setText(image.getTimestamp());
        btn_donload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Загрузка файла...", Toast.LENGTH_SHORT).show();

                if (CheckPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    createApplicationFolder();
                } else {
                    RequestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_RUNTIME_PERMISSION);
                }


            }
        });
    }
    public void createApplicationFolder() {
        File dir = new File(Environment.getExternalStorageDirectory(), "PHOTO");
        dir.mkdir();

        String url = image.getLarge();
        Log.e("URL", url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(image.getName());
        request.setTitle(image.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }


        //dqwedwqdeqwdqwd
        request.setDestinationInExternalPublicDir("PHOTO/",image.getName()+".jpg");

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case REQUEST_RUNTIME_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // you have permission go ahead
                    createApplicationFolder();
                } else {
                    // you do not have permission show toast.
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission)) {
            } else {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Permission},
                        Code);
            }
        }
    }


    public boolean CheckPermission(Context context, String Permission) {
        if (ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }



    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);


          //  PhotoView imageViewPreview = (PhotoView) view.findViewById(R.id.photo_view);
            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.photo_view);
          //  gif_animation = (GifImageView) view.findViewById(R.id.gif_animation);
            //photoViewAttacher = new PhotoViewAttacher(imageViewPreview);

            Image image = images.get(position);

            Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            gif_animation.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            gif_animation.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
                    .crossFade()
                    .override(650, 220)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

//            photoViewAttacher.update();
            container.addView(view);


            viewPager = new HackyViewPager(getContext());

            return view;
        }


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
