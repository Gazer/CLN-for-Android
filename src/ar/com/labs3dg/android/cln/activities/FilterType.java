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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import ar.com.labs3dg.android.cln.LookupPosition;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.Client;

public class FilterType extends Activity implements OnClickListener
{
	Button all;
	Button type;

	LookupPosition position = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_type);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			position = new LookupPosition(extras);
		} else {
			Toast.makeText(this, "No se pudo determinar tu posición", Toast.LENGTH_SHORT).show();
			this.finish();
		}

		all = (Button)findViewById(R.id.btn_all);
		type = (Button)findViewById(R.id.btn_type);

		all.setOnClickListener(this);
		type.setOnClickListener(this);
	}

	@Override
	public void onClick(View button)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String card = pref.getString("card", "");

		if (button.equals(all)) {
			ArrayList<String> params = new ArrayList<String>();
			params.add("http://serviciosclub.lnol.com.ar/codamation/getComercios");
			params.add("-");
			params.add("-");
			params.add("-");
			params.add("" + position.getLatitude());
			params.add("" + position.getLongitude());
			params.add(Client.PER_PAGE);
			params.add("{PAGE}");
			params.add(card);
			params.add("/");

			String url = Client.join(params.iterator(), "/");

			Intent intent = new Intent(getApplicationContext(), VenuesMapActivity.class);
			intent.putExtra("title", "Todo");
			intent.putExtra("data_url", url);
			intent.putExtra("latitude", position.getLatitude());
			intent.putExtra("longitude", position.getLongitude());
			startActivity(intent);
		} else if (button.equals(type)) {
			Intent intent = new Intent(getApplicationContext(),  CategorySearch.class);
			intent.putExtra("latitude", position.getLatitude());
			intent.putExtra("longitude", position.getLongitude());
			startActivity(intent);
		}
	}
}
