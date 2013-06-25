package ru.terra.jbrss.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import ru.terra.jbrss.R;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
