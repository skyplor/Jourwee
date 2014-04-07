package com.algomized.android.jourwee.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.util.Communicator;
import com.algomized.android.jourwee.view.fragment.LocationListFragment;
import com.algomized.android.jourwee.view.fragment.RouteLocationFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

@EActivity(R.layout.route)
public class RouteActivity extends Activity implements Communicator
{
	public static final String LOG_TAG = RouteActivity.class.getName();
	int userType;
	final int searchlocation = 1;

	// @ViewById
	// AutoCompleteTextView originInput;
	//
	// @ViewById
	// AutoCompleteTextView destInput;
	//
	// @ViewById
	// Button searchBtn;

	@FragmentById
	RouteLocationFragment fragment1;

	@FragmentById
	LocationListFragment fragment2;

	@StringRes
	static String google_browser_api_key;

	@AfterViews
	void init()
	{
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		Bundle bundle = getIntent().getExtras();

		// BroadcastReceiver receiver = new BroadcastReceiver()
		// {
		// @Override
		// public void onReceive(Context context, Intent intent)
		// {
		// String action = intent.getAction();
		// if (action != null)
		// {
		// if (action.equals("SEARCH_ORIGIN"))
		// {
		// startActionBarSearch(Constants.ORIGIN);
		// }
		// else if (action.equals("SEARCH_DEST"))
		// {
		// startActionBarSearch(Constants.DESTINATION);
		// }
		// }
		// }
		// };
		// if (bundle != null)
		// {
		// userType = bundle.getInt(Constants.KEY_USERTYPE);
		// if (userType == 0)
		// searchBtn.setText(driverStr);
		// else
		// searchBtn.setText(riderStr);
		// }
		// else
		// searchBtn.setText("Do a search");
		//
		// originInput.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_list));
	}

	@Override
	public void respond(String data, int inputType)
	{
		fragment1.insertText(data, inputType);

	}

	@Override
	public void startActionBarSearch(int inputType)
	{
		Log.d(LOG_TAG, "startActionBarSearch");
		// onSearchRequested();
		SearchLocationActivity_.intent(this).searchExtra(inputType).startForResult(searchlocation);
		// Intent intent = new Intent(this, SearchLocationActivity_.class);
		// intent.setAction(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, "");
		// startActivityForResult(intent, inputType);

		// Catch the result back and insert into fragment1
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

		return true;
	}

	// @Touch
	// void originInput(View v, MotionEvent event)
	// {
	// if (MotionEvent.ACTION_UP == event.getAction())
	// {
	// // SearchLocationActivity_.intent(this).searchExtra("").startForResult(ORIGIN);
	// Intent intent = new Intent(this, SearchLocationActivity_.class);
	// intent.setAction(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, "");
	// startActivityForResult(intent, ORIGIN);
	// }
	// }
	//
	// @Touch
	// void destInput(View v, MotionEvent event)
	// {
	// if (MotionEvent.ACTION_UP == event.getAction())
	// {
	// SearchLocationActivity_.intent(this).startForResult(DESTINATION);
	// }
	// }
	//
	// @Click
	// void searchBtn()
	// {
	// // Button is clicked...
	// Log.d(LOG_TAG, "Search Button is clicked!");
	// // Go to MapActivity
	// MapsActivity_.intent(this).start();
	// }
	//
	@OnActivityResult(searchlocation)
	void onOriginResult(Intent intent)
	{
		if (intent != null)
		{
			String data = intent.getStringExtra("data");
			int inputType = intent.getIntExtra("InputType", 0);
			fragment1.insertText(data, inputType);
		}
	}

	// @OnActivityResult(Constants.DESTINATION)
	// void onDestResult(Intent data)
	// {
	// String destination = data.getStringExtra("data");
	// destInput.setText(destination);
	// }

}

class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable
{
	private ArrayList<String> resultList;
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId);
	}

	@Override
	public int getCount()
	{
		return resultList.size();
	}

	@Override
	public String getItem(int index)
	{
		return resultList.get(index);
	}

	@Override
	public Filter getFilter()
	{
		Filter filter = new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				FilterResults filterResults = new FilterResults();
				if (constraint != null)
				{
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				if (results != null && results.count > 0)
				{
					notifyDataSetChanged();
				}
				else
				{
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	private ArrayList<String> autocomplete(String input)
	{
		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try
		{
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false&key=" + RouteActivity.google_browser_api_key);
			sb.append("&components=country:sg");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			Log.d(RouteActivity.LOG_TAG, "url: " + sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStream is = conn.getInputStream();
			InputStreamReader in = new InputStreamReader(is);

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1)
			{
				jsonResults.append(buff, 0, read);
			}

			Log.d(RouteActivity.LOG_TAG, "JSON RESULT: " + jsonResults.toString());
		}
		catch (MalformedURLException e)
		{
			Log.e(RouteActivity.LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		}
		catch (IOException e)
		{
			Log.e(RouteActivity.LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
		}

		try
		{
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++)
			{
				resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
			}
		}
		catch (JSONException e)
		{
			Log.e(RouteActivity.LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}
}
