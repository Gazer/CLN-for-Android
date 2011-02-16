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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationMarkerOverlay extends Overlay implements OverlayPosition
{
	private final Bitmap marker;
	private final LookupPosition position;

	public LocationMarkerOverlay(final Bitmap marker, final LookupPosition position)
	{
		this.marker = marker;
		this.position = position;

		Log.d("LocationOverlay", "" + position.getLatitude());
	}

	@Override
	public void draw(final Canvas canvas, final MapView mapView, final boolean shadow)
	{
		drawMapLocation(canvas, mapView);
	}

	private void drawMapLocation(final Canvas canvas, final MapView mapView )
	{
		final Point screenCoords = new Point();
		GeoPoint g = new GeoPoint((int)(position.getLatitude() * 1E6), (int)(position.getLongitude() * 1E6));
		mapView.getProjection().toPixels(g, screenCoords);

		canvas.drawBitmap(marker, screenCoords.x - marker.getWidth()/2, screenCoords.y - marker.getHeight(), null);
	}

	@Override
	public GeoPoint getPoint()
	{
		return new GeoPoint((int)(position.getLatitude() * 1E6), (int)(position.getLongitude() * 1E6));
	}
}
