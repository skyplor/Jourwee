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
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

@EActivity(R.layout.location_search)
public class SearchLocationActivity extends Activity implements Communicator, SearchView.OnQueryTextListener, LoaderCallbacks<Cursor>
{
	// GoogleMap mGoogleMap;
	//
	@Extra
	int searchExtra;

	@FragmentById
	LocationListFragment fragment2;

	Intent intent = null;

	SearchView searchView;

	private static final String LOG_TAG = SearchLocationActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, "Created Search Activity");
		handleIntent(getIntent());
		// Initializes the loader
		// getLoaderManager().initLoader(2, null, this);
	}

	private void handleIntent(Intent intent)
	{
		// intent = new Intent(this, RouteActivity_.class);
		if (intent.getAction() != null)
		{
			if (intent.getAction().equals(Intent.ACTION_SEARCH))
			{
				Log.d(LOG_TAG, "In HandleIntent, action = Intent.ACTION_SEARCH");
				doSearch(intent.getStringExtra(SearchManager.QUERY));
			}
			else if (intent.getAction().equals(Intent.ACTION_VIEW))
			{
				Log.d(LOG_TAG, "In HandleIntent, action = Intent.ACTION_VIEW");
				getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the options menu from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.location_action_search).getActionView();

		// Tells your app's SearchView to use this activity's searchable configuration
		ComponentName searchComponent = getComponentName();
		if (searchComponent != null)
		{
			Log.d(LOG_TAG, "Component Name is: " + searchComponent.flattenToString());
			searchView.setSearchableInfo(searchManager.getSearchableInfo(searchComponent));
		}
		else
		{
			Log.d(LOG_TAG, "Component Name is null.");
		}
		// Do not iconify the widget; expand it by default
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.requestFocus();
		searchView.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}
			}
		});

		fragment2.setInputType(searchExtra);

		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle query)
	{
		Log.d(LOG_TAG, "in onCreateLoader");
		CursorLoader cLoader = null;

		if (arg0 == 0)
			cLoader = new CursorLoader(getBaseContext(), PlacesSuggestionProvider.SEARCH_URI, null, null, new String[] { query.getString("query") }, null);
		else if (arg0 == 1)
			cLoader = new CursorLoader(getBaseContext(), PlacesSuggestionProvider.DETAILS_URI, null, null, new String[] { query.getString("query") }, null);
		// else if (arg0 == 2)
		// {
		// // Uri singleUri = ContentUris.withAppendedId(PlacesSuggestionProvider.SUGGESTION_URI, 0);
		// cLoader = new CursorLoader(getBaseContext(), PlacesSuggestionProvider.SUGGESTION_URI, null, null, PlacesSuggestionProvider.SUGGEST_FROM, null);
		// // Cursor cur = cl.loadInBackground();
		// }
		return cLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c)
	{
		// showLocations(c);

		int result_count = 0;
		// c.getString
		Log.d(LOG_TAG, "in onLoadFinished");
		switch (arg0.getId())
		{
			case 0:
				// do some stuff here
				Log.d(LOG_TAG, "Fragment 2 set locations");
				if (c != null && c.moveToFirst())
				{
					do
					{
						result_count++;
						for (int j = 0; j < c.getColumnCount(); j++)
						{
							Log.d(LOG_TAG, "Record " + result_count + ": " + c.getColumnName(j) + " -> " + c.getString(j));
						}
					}
					while (c.moveToNext());
				}
				else if (!c.moveToFirst())
				{
					Log.d(LOG_TAG, "Cursor has no records in onLoadFinished");
					for (int j = 0; j < c.getColumnCount(); j++)
					{
						int tempCol = j + 1;
						Log.d(LOG_TAG, "Column " + tempCol + ": " + c.getColumnName(j));
					}
				}
				else
				{
					Log.d(LOG_TAG, "Cursor is null in onLoadFinished");
				}
				fragment2.setLocations(c);
				break;
			case 1:
				// do some other stuff here
				break;
			case 2:
				Log.d(LOG_TAG, "Fragment 2 set locations");
				if (c != null && c.moveToFirst())
				{
					do
					{
						result_count++;
						for (int j = 0; j < c.getColumnCount(); j++)
						{
							Log.d(LOG_TAG, "Record " + result_count + ": " + c.getColumnName(j) + " -> " + c.getString(j));
						}
					}
					while (c.moveToNext());
				}
				else if (!c.moveToFirst())
				{
					Log.d(LOG_TAG, "Cursor has no records in onLoadFinished");
					for (int j = 0; j < c.getColumnCount(); j++)
					{
						int tempCol = j + 1;
						Log.d(LOG_TAG, "Column " + tempCol + ": " + c.getColumnName(j));
					}
				}
				else
				{
					Log.d(LOG_TAG, "Cursor is null in onLoadFinished");
				}
				fragment2.setLocations(c);
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		Log.d(LOG_TAG, "in onLoaderReset");

	}

	private void doSearch(String query)
	{
		Log.d(LOG_TAG, "In doSearch, query: " + query);
		Bundle data = new Bundle();
		data.putString("query", query);
		// getSupportLoaderManager().restartLoader(0, data, this);
		getLoaderManager().restartLoader(0, data, this);
	}

	private void getPlace(String query)
	{
		Log.d(LOG_TAG, "In getPlace, query: " + query);
		Bundle data = new Bundle();
		data.putString("query", query);
		// getSupportLoaderManager().restartLoader(1, data, this);
		getLoaderManager().restartLoader(1, data, this);
	}

	//
	// private void getSuggestions(String query)
	// {
	// Log.d(LOG_TAG, "In getSuggestions, query: " + query);
	// Bundle data = new Bundle();
	// data.putString("query", query);
	// getLoaderManager().restartLoader(2, data, this);
	// }

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item)
//	{
//		switch (item.getItemId())
//		{
//			case R.id.action_search:
//				onSearchRequested();
//				break;
//		}
//		return super.onMenuItemSelected(featureId, item);
//	}

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

	// @Override
	public boolean onQueryTextChange(String newText)
	{
		if (!newText.isEmpty() && newText.length() >= 2)
		{
			// fragment2.displayResults(newText + "*");
			Log.d(LOG_TAG, "query is: " + newText);
			// searchView.setQuery(newText, true);
			// getSuggestions(newText);

		}
		else
		{
			// myList.setAdapter(defaultAdapter);
			Log.d(LOG_TAG, "Inside onquerytextchange");
		}
		// return false;
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0)
	{
		// set adapter

		return false;
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
