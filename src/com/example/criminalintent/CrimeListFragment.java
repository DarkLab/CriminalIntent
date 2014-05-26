package com.example.criminalintent;

import java.text.DateFormat;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.criminalintent.R.id;

public class CrimeListFragment extends ListFragment {
	private static final String TAG = "CrimeListFragment";
	ArrayList<Crime> mCrimes;
	private boolean mSubtitleVisible;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		getActivity().setTitle(R.string.crime_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();

		setListAdapter(new CrimeListAdapter(mCrimes));

		setRetainInstance(true);
		mSubtitleVisible = false;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		View emptyView = inflater.inflate(R.layout.empty_view, container, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		listView.setEmptyView(emptyView);

		return v;
	}

//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//
//		TextView tView = new TextView(getActivity());
//		tView.setText(getResources().getString(R.string.empty_list));
//		getListView().setEmptyView(tView);
//	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeListAdapter) getListAdapter()).getItem(position);
		Log.d(TAG, c.getTitle() + " was clicked");

		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
	}

	private class CrimeListAdapter extends ArrayAdapter<Crime> {
		TextView titleTextView;
		TextView dateTextView;
		CheckBox solvedCheckBox;

		public CrimeListAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_crime, null);

			}

			Crime c = getItem(position);

			titleTextView = (TextView) convertView
					.findViewById(id.crime_list_item_titleTextView);
			dateTextView = (TextView) convertView
					.findViewById(id.crime_list_item_dateTextView);
			solvedCheckBox = (CheckBox) convertView
					.findViewById(id.crime_list_item_solvedCheckBox);
			titleTextView.setText(c.getTitle());
			dateTextView.setText(DateFormat.getDateTimeInstance().format(
					c.getDate()));
			solvedCheckBox.setChecked(c.isSolved());

			return convertView;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		((CrimeListAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
			Intent i = new Intent(getActivity(), CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivityForResult(i, 0);
			return true;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				mSubtitleVisible = true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				mSubtitleVisible = false;
				item.setTitle(R.string.show_subtitle);
			}

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
