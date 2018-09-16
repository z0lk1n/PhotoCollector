package online.z0lk1n.android.instagram_lite.presentation.ui.fullscreenphoto;

import android.os.Bundle;
import android.os.Handler;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.z0lk1n.android.instagram_lite.R;
import online.z0lk1n.android.instagram_lite.util.Const;
import online.z0lk1n.android.instagram_lite.util.managers.PhotoManager;

public final class FullscreenPhotoActivity extends MvpAppCompatActivity {

    public static final String NAME = "b9e6ee8c-3f43-457d-ad77-4d99891ef7bc";
    private static final String TAG = "FullscreenPhotoActivity";

    @BindView(R.id.toolbar_fullscreen)
    Toolbar toolbar;
    @BindView(R.id.fullscreen_photo)
    ImageView imageView;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler hideHandler = new Handler();
    private final Runnable hideRunnable = this::hide;
    private boolean isVisible;

    private final Runnable hidePart2Runnable = () ->
            imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    private final Runnable showPart2Runnable = () -> {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_photo);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        isVisible = true;

        int width = PhotoManager.calculateWidthOfPhoto(this, 1);
        int height = PhotoManager.calculateHeightOfPhoto(this);
        File file = new File(getIntent().getStringExtra(Const.KEY_FULLSCREEN_PHOTO));
        switch (PhotoManager.getOrientationPhoto(file.getPath())) {
            case ExifInterface.ORIENTATION_NORMAL:
                PhotoManager.setPhoto(imageView, file, width, 0);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                PhotoManager.setPhoto(imageView, file, 0, height);
                break;
            default:
                PhotoManager.setPhoto(imageView, file, width, width);
                break;
        }

        imageView.setOnClickListener(view -> toggle());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, UI_ANIMATION_DELAY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void toggle() {
        if (isVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        isVisible = false;
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        isVisible = true;
        hideHandler.removeCallbacks(hidePart2Runnable);
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY);
    }
}