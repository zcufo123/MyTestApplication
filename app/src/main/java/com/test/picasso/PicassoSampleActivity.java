package com.test.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.test.mytestapplication.R;

public class PicassoSampleActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    int i = 0;
    Button btnDrawableImage, btnUrlImage, btnErrorImage, btnPlaceholderImage, btnCallback, btnResizeImage, btnRotateImage, btnScaleImage, btnTarget;

    private static final String URL = "https://developer.android.com/static/images/home/android-p-clear-bg-with-shadow-@1x.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso_sample);
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        btnDrawableImage = (Button) findViewById(R.id.btnDrawable);
        btnUrlImage = (Button) findViewById(R.id.btnUrl);
        btnPlaceholderImage = (Button) findViewById(R.id.btnPlaceholder);
        btnErrorImage = (Button) findViewById(R.id.btnError);
        btnCallback = (Button) findViewById(R.id.btnCallBack);
        btnResizeImage = (Button) findViewById(R.id.btnResize);
        btnRotateImage = (Button) findViewById(R.id.btnRotate);
        btnScaleImage = (Button) findViewById(R.id.btnScale);
        btnTarget = (Button) findViewById(R.id.btnTarget);

        btnDrawableImage.setOnClickListener(this);
        btnPlaceholderImage.setOnClickListener(this);
        btnUrlImage.setOnClickListener(this);
        btnCallback.setOnClickListener(this);
        btnResizeImage.setOnClickListener(this);
        btnErrorImage.setOnClickListener(this);
        btnRotateImage.setOnClickListener(this);
        btnScaleImage.setOnClickListener(this);
        btnTarget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDrawable:
                Picasso.get().load(R.drawable.image).into(imageView);
                break;
            case R.id.btnPlaceholder:
                Picasso.get().load("www.google.com").placeholder(R.drawable.placeholder).into(imageView);
                break;
            case R.id.btnUrl:
                Picasso.get().load(URL).placeholder(R.drawable.placeholder).into(imageView);
                break;
            case R.id.btnError:
                Picasso.get().load("www.google.com").placeholder(R.drawable.placeholder).error(R.drawable.error).into(imageView);
                break;
            case R.id.btnCallBack:
                Picasso.get().load("www.google.com").error(R.mipmap.ic_launcher).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("TAG", "onSuccess");
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btnResize:
                Picasso.get().load(R.drawable.image).resize(200, 200).into(imageView);
                break;
            case R.id.btnRotate:
                Picasso.get().load(R.drawable.image).rotate(90f).into(imageView);
                break;
            case R.id.btnScale:

                if (i == 3)
                    i = 0;

                else {
                    if (i == 0) {
                        Picasso.get().load(R.drawable.image).fit().into(imageView);
                        Toast.makeText(getApplicationContext(), "Fit", Toast.LENGTH_SHORT).show();
                    } else if (i == 1) {
                        Picasso.get().load(R.drawable.image).resize(200, 200).centerCrop().into(imageView);
                        Toast.makeText(getApplicationContext(), "Center Crop", Toast.LENGTH_SHORT).show();
                    } else if (i == 2) {
                        Picasso.get().load(R.drawable.image).resize(200, 200).centerInside().into(imageView);
                        Toast.makeText(getApplicationContext(), "Center Inside", Toast.LENGTH_SHORT).show();
                    }
                    i++;
                }
                break;

            case R.id.btnTarget:
                Picasso.get().load(URL).placeholder(R.drawable.placeholder).error(R.drawable.error).into(target);
                break;
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            imageView.setImageDrawable(errorDrawable);
            System.out.println(e.toString());
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            imageView.setImageDrawable(placeHolderDrawable);
        }
    };

    private final void runCache() {
        Picasso.get().load(URL).placeholder(R.drawable.placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        Picasso.get().load(URL).placeholder(R.drawable.placeholder).memoryPolicy(MemoryPolicy.NO_STORE).into(imageView);

        Picasso.get().load(URL).placeholder(R.drawable.placeholder).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
    }

    private final void runFade() {
        Picasso.get().load(URL).placeholder(R.drawable.placeholder).error(R.drawable.error).noFade().into(imageView);
        Picasso.get().load(URL).noPlaceholder().error(R.drawable.error).noFade().into(imageView);
    }

    private final void runControl() {
        Picasso.get().load(URL).tag("Me").into(imageView);
        Picasso.get().pauseTag("Me");
        Picasso.get().resumeTag("Me");
        Picasso.get().cancelTag("Me");
        Picasso.get().cancelTag("Me");
        Picasso.get().cancelRequest(imageView);
    }

    private final void runPriority() {
        Picasso.get().load(URL).priority(Picasso.Priority.HIGH).into(imageView);
    }
}