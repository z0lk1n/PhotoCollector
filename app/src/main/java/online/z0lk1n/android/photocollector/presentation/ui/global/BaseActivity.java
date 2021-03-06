package online.z0lk1n.android.photocollector.presentation.ui.global;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.MvpAppCompatActivity;

import online.z0lk1n.android.photocollector.R;
import online.z0lk1n.android.photocollector.util.Preferences;

@SuppressLint("Registered")
public class BaseActivity extends MvpAppCompatActivity {

    @Override
    public void setTheme(int resId) {
        super.setTheme(getCurrentTheme());
    }

    protected int getCurrentTheme() {
        switch (new Preferences(BaseActivity.this).getTheme()) {
            case LIGHT:
                return R.style.ThemeStandard_Light;
            case DARK:
                return R.style.ThemeStandard_Dark;
            case STANDARD:
            default:
                return R.style.ThemeStandard;
        }
    }
}
