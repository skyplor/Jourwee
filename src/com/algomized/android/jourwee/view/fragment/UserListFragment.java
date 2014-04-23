package com.algomized.android.jourwee.view.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import com.algomized.android.jourwee.Constants;
import com.algomized.android.jourwee.R;
import com.algomized.android.jourwee.model.JourPlace;
import com.algomized.android.jourwee.model.JourRoute;
import com.algomized.android.jourwee.model.JourUser;
import com.android.volley.VolleyLog;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@EFragment(R.layout.userlist_fragment)
public class UserListFragment extends Fragment
{
	@ViewById
	ListView userList;

	ArrayList<String> jUsernameList;

	// @AfterViews
	// void init()
	// {
	// // attach adapter to listview...
	// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, null);
	// // userList.
	// }
	public void setAdapter(List<JourUser> jUserList)
	{
		jUsernameList = new ArrayList<String>();
		// attach adapter to listview...
		for (JourUser jUser : jUserList)
		{
			jUsernameList.add(jUser.getUsername());
		}
		StableArrayAdapter adapter = new StableArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, jUsernameList);
		userList.setAdapter(adapter);
	}
	
	@ItemClick
	void userList(Cursor c)
	{
		// Open either user details or select user for ride
	}

	private class StableArrayAdapter extends ArrayAdapter<String>
	{

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects)
		{
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i)
			{
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position)
		{
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds()
		{
			return true;
		}

	}
}
