package relawan.moviecatalogue.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.notification.ReleaseNotification;
import relawan.moviecatalogue.notification.ReminderNotification;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private String REMINDER, RELEASE, LANGUAGE;

    private SwitchPreference reminderNotify, releaseNotify;
    private Preference language;

    private ReminderNotification reminderNotification;
    private ReleaseNotification releaseNotification;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
        init();
        setupValue();


    }

    private void init() {

        REMINDER = getResources().getString(R.string.pref_reminder);
        RELEASE = getResources().getString(R.string.pref_release);
        LANGUAGE = getResources().getString(R.string.pref_language);

        reminderNotify = findPreference(REMINDER);
        releaseNotify = findPreference(RELEASE);
        language = findPreference(LANGUAGE);


    }

    private void setupValue() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        reminderNotify.setChecked(sharedPreferences.getBoolean(REMINDER, false));
        reminderNotification = new ReminderNotification();
        releaseNotify.setChecked(sharedPreferences.getBoolean(RELEASE, false));
        releaseNotification = new ReleaseNotification();

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(LANGUAGE))   {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);

        }
        return super.onPreferenceTreeClick(preference);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        if (key.equals(REMINDER))   {

            boolean isReminder = sharedPreferences.getBoolean(REMINDER, false);
            reminderNotify.setChecked(isReminder);


            if (isReminder) {

                reminderNotification.setReminder(getActivity());
                Log.d(TAG, "reminder berhasil");
                Toast.makeText(getActivity(), "REMINDER berhasil", Toast.LENGTH_LONG).show();

            } else {

                reminderNotification.cancelReminder(getActivity());
                Toast.makeText(getActivity(), "reminder gagal", Toast.LENGTH_LONG).show();
            }


        }

        if (key.equals(RELEASE)) {

            boolean isRelease = sharedPreferences.getBoolean(RELEASE, false);
            releaseNotify.setChecked(isRelease);


            if (isRelease) {

                releaseNotification.setRelease(getActivity());
                Log.d(TAG, "release berhasil");
                Toast.makeText(getActivity(), "RELEASE berhasil", Toast.LENGTH_LONG).show();

            } else {

                releaseNotification.cancelRelease(getActivity());
                Toast.makeText(getActivity(), "release gagal", Toast.LENGTH_LONG).show();

            }

        }

    }


}
