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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import ar.com.labs3dg.android.cln.LookupPosition;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.Client;

public class ZoneSearch extends Activity implements OnClickListener
{
	Spinner state;
	Spinner zone;
	EditText address;

	class GetLocationTask extends AsyncTask<String, Void, LookupPosition>
	{
		private Dialog dialog;

		@Override
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(ZoneSearch.this, "", "Espere...", true);
		}

		@Override
		protected LookupPosition doInBackground(String... arg0)
		{
			Client c = new Client();

			return c.getDireccion(arg0[0], arg0[1], arg0[2]);
		}

		@Override
		protected void onPostExecute(LookupPosition result)
		{
			dialog.dismiss();
			if (result != null) {
				Intent intent = new Intent(getApplicationContext(), FilterType.class);
				intent.putExtra("latitude", result.getLatitude());
				intent.putExtra("longitude", result.getLongitude());
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "El servidor no responde o la dirección no existe. Reintente.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone_search);

		state = (Spinner)findViewById(R.id.state);
		zone = (Spinner)findViewById(R.id.zone);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Client.States);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		state.setAdapter(adapter);
		state.setOnItemSelectedListener(onStateClicked);

		Button findBtn = (Button)findViewById(R.id.find);
		findBtn.setOnClickListener(this);

		address = (EditText)findViewById(R.id.address);
	}

	private final OnItemSelectedListener onStateClicked = new OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> adapter, View arg1, int position, long id)
		{
			ArrayAdapter<String> adapter1 = null;

			if (position == 0) {
				// CABA
				adapter1 = new ArrayAdapter<String>(ZoneSearch.this, android.R.layout.simple_spinner_item, Client.CABA);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				zone.setEnabled(true);
			} else if (position == 1) {
				// BSAS
				adapter1 = new ArrayAdapter<String>(ZoneSearch.this, android.R.layout.simple_spinner_item, Client.BSAS);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				zone.setEnabled(true);
			} else if (position == 2){
				// Centros Turisticos
				adapter1 = new ArrayAdapter<String>(ZoneSearch.this, android.R.layout.simple_spinner_item, Client.CentrosTuristicos);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				zone.setEnabled(true);
			} else {
				adapter1 = new ArrayAdapter<String>(ZoneSearch.this, android.R.layout.simple_spinner_item, Client.Empty);
				zone.setEnabled(false);
			}

			zone.setAdapter(adapter1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {}
	};

	@Override
	public void onClick(View arg0)
	{
		new GetLocationTask().execute(getState(), getZone(), getAddress());
	}

	private String getAddress()
	{
		String txt = address.getText().toString();

		if (txt.trim().length() == 0) {
			txt = "-";
		}

		return txt;
	}

	private String getState()
	{
		int position = state.getSelectedItemPosition();

		if (position == 2) {
			if (getZone().equals("Punta del Este")) {
				return "Uruguay";
			}

			return "Buenos Aires";
		}

		return Client.States[position];
	}

	private String getZone()
	{
		String txt = "-";
		int position = state.getSelectedItemPosition();

		if (position == 0) {
			txt = Client.CABA[zone.getSelectedItemPosition()];
		} else if (position == 1) {
			txt = Client.BSAS[zone.getSelectedItemPosition()];
		} else if (position == 2) {
			txt = Client.CentrosTuristicos[zone.getSelectedItemPosition()];
		}

		if (txt.equals("Todos")) {
			txt = "-";
		}

		return txt;
	}
}
