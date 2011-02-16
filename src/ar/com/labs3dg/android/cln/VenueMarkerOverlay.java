package ar.com.labs3dg.android.cln;

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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import ar.com.labs3dg.android.cln.activities.VenueActivity;
import ar.com.labs3dg.android.cln.model.Venue;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class VenueMarkerOverlay extends Overlay implements OverlayPosition
{
	private final Bitmap marker;
	private final Venue venue;
	private final Activity baseActivity;
	private final LookupPosition location;
	private boolean clickeable = true;

	public VenueMarkerOverlay(Activity baseActivity, LookupPosition location, final Bitmap marker, final Venue station)
	{
		this.marker = marker;
		this.venue = station;
		this.baseActivity = baseActivity;
		this.location = location;
	}

	@Override
	public boolean onTap(final GeoPoint p, final MapView mapView)
	{
		if (!clickeable) {
			return false;
		}

		final RectF hitTestRecr = new RectF();

		final Point screenCoords = new Point();
		GeoPoint g = new GeoPoint((int)(venue.lat * 1E6), (int)(venue.lon* 1E6));
		mapView.getProjection().toPixels(g, screenCoords);

		hitTestRecr.set(-marker.getWidth()/2,-marker.getHeight(),marker.getWidth()/2,0);
		hitTestRecr.offset(screenCoords.x,screenCoords.y);

		mapView.getProjection().toPixels(p, screenCoords);

		if (!hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
			return false;
		}

		Intent intent = new Intent(baseActivity.getApplicationContext(), VenueActivity.class);
		intent.putExtra("venue", venue);
		intent.putExtra("latitude", location.getLatitude());
		intent.putExtra("longitude", location.getLongitude());
		baseActivity.startActivity(intent);

		return true;
	}

	@Override
	public void draw(final Canvas canvas, final MapView mapView, final boolean shadow)
	{
		drawMapLocation(canvas, mapView);
	}

	private void drawMapLocation(final Canvas canvas, final MapView mapView )
	{
		final Point screenCoords = new Point();
		GeoPoint g = new GeoPoint((int)(venue.lat * 1E6), (int)(venue.lon* 1E6));
		mapView.getProjection().toPixels(g, screenCoords);

		canvas.drawBitmap(marker, screenCoords.x - marker.getWidth()/2, screenCoords.y - marker.getHeight(), null);
	}

	public void setClickeable(boolean b)
	{
		this.clickeable  = b;
	}

	@Override
	public GeoPoint getPoint()
	{
		return new GeoPoint((int)(venue.lat * 1E6), (int)(venue.lon* 1E6));
	}
}
