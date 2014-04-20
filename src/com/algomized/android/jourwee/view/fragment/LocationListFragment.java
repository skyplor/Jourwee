package com.algomized.android.jourwee.view.fragment;

import java.util.ArrayList;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.model.Place;
import com.algomized.android.jourwee.util.Communicator;
import com.algomized.android.jourwee.util.location.PlacesSuggestionProvider;
import com.android.volley.VolleyLog;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;

@EFragment(R.layout.locationlist_fragment)
public class LocationListFragment extends Fragment// implements OnQueryTextListener, LoaderCallbacks<Cursor>
{
	private static final String LOG_TAG = LocationListFragment.class.getName();

	@ViewById
	ListView locationList;

	int inputType = Constants.DESTINATION;
	Cursor locationsC;
	// ArrayAdapter<CharSequence> aadapter;
	// An adapter that binds the result Cursor to the ListView
	SimpleCursorAdapter mCursorAdapter;

	Communicator comm;
	LoaderManager loadermanager;
	CursorLoader cursorLoader;
//	boolean fromSuggestion = true;

	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// loadermanager = getLoaderManager();

		// String[] uiBindFrom = { "description" };
		// int[] uiBindTo = { android.R.id.text1 };
		String[] uiBindFrom = null;
		int[] uiBindTo = null;

		/* Empty adapter that is used to display the loaded data */
		mCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, uiBindFrom, uiBindTo, 0);
		locationList.setAdapter(mCursorAdapter);
		comm = (Communicator) getActivity();

		/**
		 * This initializes the loader and makes it active. If the loader specified by the ID already exists, the last created loader is reused. If the loader specified by the ID does not exist, initLoader() triggers the LoaderManager.LoaderCallbacks method onCreateLoader(). This is where you implement the code to instantiate and return a new loader. Use restartLoader() instead of this, to discard
		 * the old data and restart the Loader. Hence, here the given LoaderManager.LoaderCallbacks implementation are associated with the loader.
		 */
		// loadermanager.initLoader(2, null, this);
	}

	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	// {
	// // Inflate the options menu from XML
	// inflater.inflate(R.menu.options_menu, menu);
	// super.onCreateOptionsMenu (menu,inflater);
	//
	// // Get the SearchView and set the searchable configuration
	// SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	// SearchView searchView = (SearchView) menu.findItem(R.id.location_action_search).getActionView();
	//
	// // Tells your app's SearchView to use this activity's searchable configuration
	// ComponentName searchComponent = getComponentName();
	// if (searchComponent != null)
	// {
	// Log.d(LOG_TAG, "Component Name is: " + searchComponent.flattenToString());
	// searchView.setSearchableInfo(searchManager.getSearchableInfo(searchComponent));
	// }
	// else
	// {
	// Log.d(LOG_TAG, "Component Name is null.");
	// }
	// // Do not iconify the widget; expand it by default
	// searchView.setIconifiedByDefault(false);
	// // searchView.setOnQueryTextListener(this);
	//
	// fragment2.setInputType(searchExtra);
	//
	// return true;
	// }
	//
	// /**
	// * This creates and return a new Loader (CursorLoader or custom Loader) for the given ID. This method returns the Loader that is created, but you don't need to capture a reference to it.
	// */
	// public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
	// {
	// Log.d(LOG_TAG, "onCreateLoader: " + arg0);
	// // String[] projection = { DatabaseHandler.UserTable.id, DatabaseHandler.UserTable.name };
	// String[] projection = null;
	//
	// /**
	// * This requires the URI of the Content Provider projection is the list of columns of the database to return. Null will return all the columns selection is the filter which declares which rows to return. Null will return all the rows for the given URI. selectionArgs: You may include ?s in the selection, which will be replaced by the values from selectionArgs, in the order that they appear
	// * in the selection. The values will be bound as Strings. sortOrder determines the order of rows. Passing null will use the default sort order, which may be unordered. To back a ListView with a Cursor, the cursor must contain a column named _ID.
	// */
	//
	// cursorLoader = new CursorLoader(getActivity(), PlacesSuggestionProvider.SUGGESTION_URI, projection, null, null, null);
	//
	// return cursorLoader;
	//
	// }
	//
	// /**
	// * Called when a previously created loader has finished its load. This assigns the new Cursor but does not close the previous one. This allows the system to keep track of the Cursor and manage it for us, optimizing where appropriate. This method is guaranteed to be called prior to the release of the last data that was supplied for this loader. At this point you should remove all use of the
	// old
	// * data (since it will be released soon), but should not do your own release of the data since its loader owns it and will take care of that. The framework would take of closing of old cursor once we return.
	// */
	//
	// public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	// {
	// if (mCursorAdapter != null && cursor != null && cursor.getCount() > 0)
	// {
	// mCursorAdapter.swapCursor(cursor); // swap the new cursor in.
	// Log.d(LOG_TAG, "Cursor count: " + cursor.getCount());
	// }
	// else
	// Log.v(LOG_TAG, "OnLoadFinished: mCursorAdapter is null");
	// }
	//
	// /**
	// * This method is triggered when the loader is being reset and the loader data is no longer available. This is called when the last Cursor provided to onLoadFinished() above is about to be closed. We need to make sure we are no longer using it.
	// */
	//
	// public void onLoaderReset(Loader<Cursor> arg0)
	// {
	// if (mCursorAdapter != null)
	// {
	// Log.d(LOG_TAG, "onLoaderReset: mCursorAdapter is not null");
	// mCursorAdapter.swapCursor(null);
	// }
	// else
	// Log.v(LOG_TAG, "onLoaderReset: mCursorAdapter is null");
	// }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// setHasOptionsMenu(true);
		return null;
	}

	// @Override
	// public void onActivityCreated(Bundle savedInstanceState)
	// {
	// super.onActivityCreated(savedInstanceState);
	// // aadapter = ArrayAdapter.createFromResource(getActivity(), R.array.locations, android.R.layout.simple_list_item_1);
	// // mCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, PlacesSuggestionProvider.SUGGEST_FROM, null, 0);
	// mCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[] { "_id", SearchManager.SUGGEST_COLUMN_TEXT_1 }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);
	// // locationList.setAdapter(aadapter);
	// locationList.setAdapter(mCursorAdapter);
	// comm = (Communicator) getActivity();
	// }

	@ItemClick
	void locationList(Cursor c)
	{
//		fromSuggestion = false;
		Place place = Place.newInstance(c);
		comm.respond(place.name, inputType);
	}

	public void setInputType(int inputType)
	{
		this.inputType = inputType;
	}

	public void setLocations(Cursor c, boolean fromSuggestion)
	{
		locationsC = c;
		int result_count = 0;
		if (c != null)
		{
			Log.d(LOG_TAG, "Cursor is not null. Swapping cursor");
			setAdapter(fromSuggestion);
			mCursorAdapter.swapCursor(c);
			// locationList.
			// mCursorAdapter.changeCursor(c);
			if (c.moveToFirst())
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
		}
	}

	public void setAdapter(boolean fromSuggestion)
	{
		Log.d(LOG_TAG, "in setAdapter");
		String[] from = new String[1];
		if(fromSuggestion)
		{
			VolleyLog.d("from suggestion is true");
//			from[0] = SearchManager.SUGGEST_COLUMN_TEXT_1;
			from[0] = "description";
		}
		else
		{
			VolleyLog.d("from suggestion is false");
			from[0] = "description";
		}
		int[] to = { android.R.id.text1 };

		mCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to, 0);
		locationList.setAdapter(mCursorAdapter);
	}

	public void displayResults(String string)
	{
		// TODO Auto-generated method stub

	}

	// @Override
	// public boolean onQueryTextChange(String newText)
	// {
	// if (!newText.isEmpty() && newText.length() >= 2)
	// {
	// setSuggestionAdapter();
	// Log.d(LOG_TAG, "in onQueryTextChange");
	// loadermanager.restartLoader(2, null, this);
	// }
	// return true;
	// }
	//
	// @Override
	// public boolean onQueryTextSubmit(String query)
	// {
	// // Ignore user submit query
	//
	// return true;
	// }

	// private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	// private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	// private static final String OUT_JSON = "/json";
	//
	// private static final String API_KEY = "YOUR_API_KEY";
	//
	// private ArrayList<String> autocomplete(String input) {
	// ArrayList<String> resultList = null;
	//
	// HttpURLConnection conn = null;
	// StringBuilder jsonResults = new StringBuilder();
	// try {
	// StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	// sb.append("?sensor=false&key=" + API_KEY);
	// sb.append("&components=country:uk");
	// sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	//
	// URL url = new URL(sb.toString());
	// conn = (HttpURLConnection) url.openConnection();
	// InputStreamReader in = new InputStreamReader(conn.getInputStream());
	//
	// // Load the results into a StringBuilder
	// int read;
	// char[] buff = new char[1024];
	// while ((read = in.read(buff)) != -1) {
	// jsonResults.append(buff, 0, read);
	// }
	// } catch (MalformedURLException e) {
	// Log.e(LOG_TAG, "Error processing Places API URL", e);
	// return resultList;
	// } catch (IOException e) {
	// Log.e(LOG_TAG, "Error connecting to Places API", e);
	// return resultList;
	// } finally {
	// if (conn != null) {
	// conn.disconnect();
	// }
	// }
	//
	// try {
	// // Create a JSON object hierarchy from the results
	// JSONObject jsonObj = new JSONObject(jsonResults.toString());
	// JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	//
	// // Extract the Place descriptions from the results
	// resultList = new ArrayList<String>(predsJsonArray.length());
	// for (int i = 0; i < predsJsonArray.length(); i++) {
	// resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	// }
	// } catch (JSONException e) {
	// Log.e(LOG_TAG, "Cannot process JSON results", e);
	// }
	//
	// return resultList;
	// }

	// private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	// private ArrayList<String> resultList;
	//
	// public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	// super(context, textViewResourceId);
	// }
	//
	// @Override
	// public int getCount() {
	// return resultList.size();
	// }
	//
	// @Override
	// public String getItem(int index) {
	// return resultList.get(index);
	// }
	//
	// @Override
	// public Filter getFilter() {
	// Filter filter = new Filter() {
	// @Override
	// protected FilterResults performFiltering(CharSequence constraint) {
	// FilterResults filterResults = new FilterResults();
	// if (constraint != null) {
	// // Retrieve the autocomplete results.
	// resultList = autocomplete(constraint.toString());
	//
	// // Assign the data to the FilterResults
	// filterResults.values = resultList;
	// filterResults.count = resultList.size();
	// }
	// return filterResults;
	// }
	//
	// @Override
	// protected void publishResults(CharSequence constraint, FilterResults results) {
	// if (results != null && results.count > 0) {
	// notifyDataSetChanged();
	// }
	// else {
	// notifyDataSetInvalidated();
	// }
	// }};
	// return filter;
	// }
	// }
}
