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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import ar.com.labs3dg.android.cln.LocationMarkerOverlay;
import ar.com.labs3dg.android.cln.LookupPosition;
import ar.com.labs3dg.android.cln.MapUtils;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.VenueMarkerOverlay;
import ar.com.labs3dg.android.cln.model.Client;
import ar.com.labs3dg.android.cln.model.Venue;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class VenuesMapActivity extends MapActivity
{
	MapView mapView;
	MapController mc;
	HashMap<Integer, Bitmap> icons;
	String data_url;
	ArrayList<Venue> venues;
	LookupPosition position = null;

	class LoadVenues extends AsyncTask<Void, Void, Boolean>
	{
		List<Venue> list;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			VenuesMapActivity.this.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0)
		{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(VenuesMapActivity.this);
			String card = pref.getString("card", "");

			Client client = new Client();
			Log.d("MAP", "Loading " + VenuesMapActivity.this.getUrl());
			list = client.getVenuesFrom(VenuesMapActivity.this.getUrl(), card);
			return client.hasMore;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			VenuesMapActivity.this.addVenues(list);
			VenuesMapActivity.this.setProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.map);

		icons = new HashMap<Integer, Bitmap>();
		icons.put(6838, BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_restaurantes));
		icons.put(6839, BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_spa));
		icons.put(7279, BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_cines));
		icons.put(7290, BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_cines));
		icons.put(7296, BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_compras));

		mapView = (MapView)findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls( true );
		mc = mapView.getController();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			data_url = extras.getString("data_url");
			position = new LookupPosition(extras.getDouble("latitude"), extras.getDouble("longitude"));

			venues = new ArrayList<Venue>();

			new LoadVenues().execute();
		} else {
			Log.d("VenuesMapActivity", "No extras");
			this.finish();
			return;
		}

		LocationMarkerOverlay user_overlay = new LocationMarkerOverlay(BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_user), position);
		mapView.getOverlays().add(user_overlay);
	}

	private void processVenues()
	{
		if (venues.size() == 0) {
			Toast.makeText(getApplicationContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		for(Venue v: venues) {
			VenueMarkerOverlay overlay = new VenueMarkerOverlay(this, position, icons.get(v.category_id), v);
			mapView.getOverlays().add(overlay);
		}

		MapUtils.fitPoints(mc, mapView.getOverlays());
	}

	private String getUrl()
	{
		return data_url.replace("{PAGE}", "" + 1);
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		MapUtils.fitPoints(mc, mapView.getOverlays());
	}

	private static final int MENU_HOME = 1;
	private static final int MENU_LISTVIEW = 2;

	@Override
	public boolean onCreateOptionsMenu( final Menu menu )
	{
		menu.add(0, MENU_LISTVIEW, 0, "Ver Lista").setIcon(android.R.drawable.ic_menu_manage);
		menu.add(0, MENU_HOME, 0, "Inicio").setIcon(R.drawable.home);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) {
		case MENU_HOME:
			Intent homeIntent = new Intent(this, SearchType.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			return true;
		case MENU_LISTVIEW:
			Intent intent = new Intent(this, VenuesActivity.class);
			intent.putExtra("data_url", data_url);
			intent.putExtra("venues", venues);
			startActivity(intent);
			return true;
		}
		return false;
	}

	public void addVenues(List<Venue> list)
	{
		for(Venue v: list) {
			venues.add(v);
		}
		processVenues();
	}

}
