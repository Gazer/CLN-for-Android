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

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;

public class MapUtils
{
	/**
	 * Fits the map with the passed in points so all points are visible.
	 * @param mapController MapView controller
	 * @param points list of points you want the map to contain
	 *
	 * http://sdhillon.com/find-the-center-and-span-of-a-set-of-points-android-and-iphone-example/
	 */
	static public void fitPoints(MapController mc, List<Overlay> overlays)
	{
		// set min and max for two points
		int nwLat = -90 * 1000000;
		int nwLng = 180 * 1000000;
		int seLat = 90 * 1000000;
		int seLng = -180 * 1000000;

		// find bounding lats and lngs
		for (Overlay o : overlays) {
			nwLat = Math.max(nwLat, ((OverlayPosition)o).getPoint().getLatitudeE6());
			nwLng = Math.min(nwLng, ((OverlayPosition)o).getPoint().getLongitudeE6());
			seLat = Math.min(seLat, ((OverlayPosition)o).getPoint().getLatitudeE6());
			seLng = Math.max(seLng, ((OverlayPosition)o).getPoint().getLongitudeE6());
		}
		GeoPoint center = new GeoPoint((nwLat + seLat) / 2, (nwLng + seLng) / 2);
		// add padding in each direction
		int spanLatDelta = (int) (Math.abs(nwLat - seLat) * 1.1);
		int spanLngDelta = (int) (Math.abs(seLng - nwLng) * 1.1);

		// fit map to points
		mc.animateTo(center);
		mc.zoomToSpan(spanLatDelta, spanLngDelta);
	}

}
