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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.MyLocation;
import ar.com.labs3dg.android.cln.model.MyLocation.LocationResult;

public class SearchType extends Activity implements OnClickListener
{
	ImageButton closest;
	ImageButton zone;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_type);

		closest = (ImageButton)findViewById(R.id.btn_closest);
		zone = (ImageButton)findViewById(R.id.btn_zone);

		closest.setOnClickListener(this);
		zone.setOnClickListener(this);
	}

	@Override
	public void onClick(View button)
	{
		if (button.equals(closest)) {
			MyLocation myLocation = new MyLocation();

			if (!myLocation.getLocation(this, locationResult)) {
				Toast.makeText(this, R.string.position_not_found, Toast.LENGTH_SHORT).show();
				showZoneSearch();
				dialog = null;
			} else {
				dialog = ProgressDialog.show(this, "", "Detectando posición...", true);
			}
		} else if (button.equals(zone)) {
			showZoneSearch();
		}
	}

	public LocationResult locationResult = new LocationResult()
	{
		@Override
		public void gotLocation(final Location location)
		{
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			showLocation(location);
		}
	};

	private void showLocation(Location location)
	{
		Intent intent = new Intent(getApplicationContext(), FilterType.class);
		intent.putExtra("latitude", location.getLatitude());
		intent.putExtra("longitude", location.getLongitude());
		startActivity(intent);
	}

	private void showZoneSearch()
	{
		Intent intent = new Intent(getApplicationContext(), ZoneSearch.class);
		startActivity(intent);
	}

}
