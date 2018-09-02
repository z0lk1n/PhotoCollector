package online.z0lk1n.android.instagram_lite.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import online.z0lk1n.android.instagram_lite.R;
import online.z0lk1n.android.instagram_lite.activity.MainActivity;
import online.z0lk1n.android.instagram_lite.util.Const;
import online.z0lk1n.android.instagram_lite.util.Preferences;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String NAME = "1b7cc406-5e05-431f-9cbe-cc1401f03152";
    private static final String TAG = "SettingsFragment";

    private Preferences preferences;
    private Preference prefDefaultTheme;
    private Preference prefLightTheme;
    private Preference prefDarkTheme;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
        init();
        initListener();
    }

    private void init() {
        setHasOptionsMenu(true);
        preferences = new Preferences(getActivity());
        prefDefaultTheme = findPreference(Const.KEY_PREF_DEFAULT_THEME);
        prefLightTheme = findPreference(Const.KEY_PREF_LIGHT_THEME);
        prefDarkTheme = findPreference(Const.KEY_PREF_DARK_THEME);
    }

    private void initListener() {
        prefDefaultTheme.setOnPreferenceClickListener(preference -> {
            changeTheme(R.style.ThemeStandard);
            return true;
        });
        prefLightTheme.setOnPreferenceClickListener(preference -> {
            changeTheme(R.style.ThemeStandard_Light);
            return true;
        });
        prefDarkTheme.setOnPreferenceClickListener(preference -> {
            changeTheme(R.style.ThemeStandard_Dark);
            return true;
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeTheme(int theme) {
        if (preferences.getTheme() != theme) {
            preferences.setTheme(theme);
            TaskStackBuilder.create(getActivity())
                    .addNextIntent(new Intent(getActivity(), MainActivity.class))
                    .addNextIntent(getActivity().getIntent())
                    .startActivities();
        }
    }
}