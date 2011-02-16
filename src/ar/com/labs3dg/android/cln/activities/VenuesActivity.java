package ar.com.labs3dg.android.cln.activities;

/*
 * Club La Nacion for Android by 3DGLabs
 * Copyright (C) 2011  3DGLabs <info@3dglabs.com.ar>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.Client;
import ar.com.labs3dg.android.cln.model.Venue;

public class VenuesActivity extends ListActivity implements OnClickListener
{
	private String data_url;

	private MessageAdapter adapter;
	private final ArrayList<Venue> venues = new ArrayList<Venue>();

	private HashMap<Integer, Bitmap> icons;
	private View btnMore;
	private int currentPage = 2;
	private String card;

	class LoadVenues extends AsyncTask<Void, Void, Boolean>
	{
		List<Venue> list;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			VenuesActivity.this.btnMore.setEnabled(false);
			VenuesActivity.this.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0)
		{
			Client client = new Client();
			list = client.getVenuesFrom(VenuesActivity.this.getUrl(), card);
			return client.hasMore;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			VenuesActivity.this.addVenues(list);
			VenuesActivity.this.setProgressBarIndeterminateVisibility(false);

			if (result) {
				VenuesActivity.this.btnMore.setEnabled(true);
			}
		}
	}

	private class MessageAdapter extends ArrayAdapter<Venue>
	{
		private final ArrayList<Venue> items;

		public MessageAdapter(Context context, int textViewResourceId, ArrayList<Venue> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.venue_row, null);
			}
			Venue m = items.get(position);
			TextView name = (TextView) v.findViewById(R.id.name);
			ImageView icon = (ImageView) v.findViewById(R.id.category);

			name.setText(m.name);
			icon.setImageBitmap(VenuesActivity.this.icons.get(m.category_id));
			return v;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.venues_activity);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		card = pref.getString("card", "");

		icons = new HashMap<Integer, Bitmap>();
		icons.put(6838, BitmapFactory.decodeResource(getResources(), R.drawable.ico_gastronomia));
		icons.put(6839, BitmapFactory.decodeResource(getResources(), R.drawable.ico_cuidadopersonal));
		icons.put(7279, BitmapFactory.decodeResource(getResources(), R.drawable.ico_entretenimiento));
		icons.put(7290, BitmapFactory.decodeResource(getResources(), R.drawable.ico_espectaculos));
		icons.put(7296, BitmapFactory.decodeResource(getResources(), R.drawable.ico_compras));

		ListView view = getListView();

		view.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long l) {
				Venue venue = venues.get(position);

				Intent intent = new Intent(getApplicationContext(), VenueActivity.class);
				intent.putExtra("venue", venue);
				startActivity(intent);
			}
		});

		adapter = new MessageAdapter(this, R.layout.venue_row, venues);
		setListAdapter(adapter);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			data_url = extras.getString("data_url");
			ArrayList<Venue> vs = extras.getParcelableArrayList("venues");
			addVenues(vs);
		} else {
			Log.d("VenuesActivity", "No extras");
			this.finish();
			return;
		}

		btnMore = findViewById(R.id.more);
		btnMore.setOnClickListener(this);
	}

	public void addVenues(List<Venue> list)
	{
		for(Venue v: list) {
			venues.add(v);
		}
		adapter.notifyDataSetChanged();
	}

	private String getUrl()
	{
		return data_url.replace("{PAGE}", "" + currentPage);
	}

	@Override
	public void onClick(View arg0)
	{
		currentPage++;
		new LoadVenues().execute();
	}
}

