package de.danoeh.antennapod.activity;

import org.shredzone.flattr4j.model.Thing;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.danoeh.antennapod.asynctask.FlattrClickWorker;
import de.danoeh.antennapod.util.FlattrUtils;
import de.danoeh.antennapod.R;

public class PreferenceActivity extends SherlockPreferenceActivity {
	private static final String TAG = "PreferenceActivity";

	private static final String PREF_FLATTR_THIS_APP = "prefFlattrThisApp";
	private static final String PREF_FLATTR_AUTH = "pref_flattr_authenticate";
	private static final String PREF_FLATTR_REVOKE = "prefRevokeAccess";
	private static final String PREF_ABOUT = "prefAbout";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		findPreference(PREF_FLATTR_THIS_APP).setOnPreferenceClickListener(
				new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Thing appThing = FlattrUtils
								.getAppThing(PreferenceActivity.this);
						if (appThing != null) {
							new FlattrClickWorker(PreferenceActivity.this,
									appThing.getUrl()).execute();
						}
						return true;
					}
				});
		findPreference(PREF_FLATTR_REVOKE).setOnPreferenceClickListener(
				new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						FlattrUtils.revokeAccessToken(PreferenceActivity.this);
						checkItemVisibility();
						return true;
					}

				});

		findPreference(PREF_ABOUT).setOnPreferenceClickListener(
				new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						PreferenceActivity.this.startActivity(new Intent(
								PreferenceActivity.this, AboutActivity.class));
						return true;
					}

				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkItemVisibility();
	}

	@SuppressWarnings("deprecation")
	private void checkItemVisibility() {
		boolean hasFlattrToken = FlattrUtils.hasToken();
		findPreference(PREF_FLATTR_AUTH).setEnabled(!hasFlattrToken);
		findPreference(PREF_FLATTR_REVOKE).setEnabled(hasFlattrToken);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(PreferenceActivity.this,
					MainActivity.class));
			break;
		default:
			return false;
		}
		return true;
	}

}
