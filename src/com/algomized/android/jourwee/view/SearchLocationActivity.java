package com.algomized.android.jourwee.view;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.util.Communicator;
import com.algomized.android.jourwee.util.location.PlacesSuggestionProvider;
import com.algomized.android.jourwee.view.fragment.LocationListFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

@EActivity(R.layout.location_search)
public class SearchLocationActivity extends Activity implements Communicator // implements LoaderCallbacks<Cursor>
{
	// GoogleMap mGoogleMap;
	//
	@Extra
	int searchExtra;
	
	@FragmentById
	LocationListFragment fragment2;
	
	Intent intent = null;

	private static final String LOG_TAG = SearchLocationActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	

		Log.d(LOG_TAG, "Created Search Activity");
		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the options menu from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.location_action_search).getActionView();
		// Tells your app's SearchView to use this activity's searchable configuration
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		// Do not iconify the widget; expand it by default
		searchView.setIconifiedByDefault(false);
		
		fragment2.setInputType(searchExtra);

		return true;
	}

	// @Override
	// public Loader<Cursor> onCreateLoader(int arg0, Bundle query)
	// {
	// CursorLoader cLoader = null;
	//
	// if (arg0 == 0)
	// cLoader = new CursorLoader(getBaseContext(), PlacesSuggestionProvider.SEARCH_URI, null, null, new String[]{query.getString("query")}, null);
	// else if (arg0 == 1)
	// cLoader = new CursorLoader(getBaseContext(), PlacesSuggestionProvider.DETAILS_URI, null, null, new String[]{query.getString("query")}, null);
	// return cLoader;
	// }
	//
	// @Override
	// public void onLoadFinished(Loader<Cursor> arg0, Cursor c)
	// {
	// showLocations(c);
	// }
	//
	// @Override
	// public void onLoaderReset(Loader<Cursor> arg0)
	// {
	// // TODO Auto-generated method stub
	//
	// }

	private void handleIntent(Intent intent)
	{
//		intent = new Intent(this, RouteActivity_.class);
		if (intent.getAction() != null)
		{
			if (intent.getAction().equals(Intent.ACTION_SEARCH))
			{
				// doSearch(intent.getStringExtra(SearchManager.QUERY));
			}
			else if (intent.getAction().equals(Intent.ACTION_VIEW))
			{
				// getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
			}
		}
	}

	// private void doSearch(String query)
	// {
	// Bundle data = new Bundle();
	// data.putString("query", query);
	// getSupportLoaderManager().restartLoader(0, data, this);
	// }
	//
	// private void getPlace(String query)
	// {
	// Bundle data = new Bundle();
	// data.putString("query", query);
	// getSupportLoaderManager().restartLoader(1, data, this);
	// }

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_search:
				onSearchRequested();
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void respond(String data, int inputType)
	{
		// TODO Auto-generated method stub
		intent = getIntent();
		intent.putExtra("data", data);
		intent.putExtra("InputType", inputType);
		setResult(Activity.RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void startActionBarSearch(int inputType)
	{
		// TODO Auto-generated method stub
		
	}

	// private void showLocations(Cursor c)
	// {
	// MarkerOptions markerOptions = null;
	// LatLng position = null;
	// mGoogleMap.clear();
	// while (c.moveToNext())
	// {
	// markerOptions = new MarkerOptions();
	// position = new LatLng(Double.parseDouble(c.getString(1)), Double.parseDouble(c.getString(2)));
	// markerOptions.position(position);
	// markerOptions.title(c.getString(0));
	// mGoogleMap.addMarker(markerOptions);
	// }
	// if (position != null)
	// {
	// CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
	// mGoogleMap.animateCamera(cameraPosition);
	// }
	// }

}
