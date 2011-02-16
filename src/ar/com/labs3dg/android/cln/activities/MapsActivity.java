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

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import ar.com.labs3dg.android.cln.LocationMarkerOverlay;
import ar.com.labs3dg.android.cln.LookupPosition;
import ar.com.labs3dg.android.cln.MapUtils;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.VenueMarkerOverlay;
import ar.com.labs3dg.android.cln.model.Venue;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapsActivity extends MapActivity
{
	private Venue venue;
	public MapView mapView;
	private MapController mc;
	Bitmap marker;

	public HashMap<Integer, Bitmap> icons;

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
		LookupPosition position;
		if (extras != null) {
			venue = extras.getParcelable("venue");
			position = new LookupPosition(extras.getDouble("latitude"), extras.getDouble("longitude"));
		} else {
			Log.d("MapActivity", "No extras");
			this.finish();
			return;
		}

		VenueMarkerOverlay overlay = new VenueMarkerOverlay(this, position, icons.get(venue.category_id), venue);
		overlay.setClickeable(false);
		mapView.getOverlays().add(overlay);

		LocationMarkerOverlay user_overlay = new LocationMarkerOverlay(BitmapFactory.decodeResource(getResources(), R.drawable.map_ic_user), position);
		mapView.getOverlays().add(user_overlay);

		MapUtils.fitPoints(mc, mapView.getOverlays());
	}


	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
