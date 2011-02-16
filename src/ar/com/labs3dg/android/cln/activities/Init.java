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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.Client;

public class Init extends Activity
{
	class PreloadTask extends AsyncTask<Void, Void, Boolean>
	{
		String error;

		@Override
		protected Boolean doInBackground(Void... arg0)
		{
			Client c = new Client();
			if (Client.Categories == null) {
				Client.Categories = c.getCategories();

				if (Client.Categories.isEmpty()) {
					error = "El servidor no responde. Intente nuevamente más tarde.";
					return false;
				}
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (result) {
				Intent homeIntent = new Intent(Init.this, CardSelector.class);
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(homeIntent);
			} else {
				Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
			}
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);

		new PreloadTask().execute();
	}
}
