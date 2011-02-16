package ar.com.labs3dg.android.cln.model;

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
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

public class ResponseParser
{
	public boolean has_more = false;
	public boolean is_ok;
	public List<List<String>> result;

	public ResponseParser(String s)
	{
		this(s, true);
	}

	public ResponseParser(String s, boolean has_pages)
	{
		String[] array = TextUtils.split(s, "#\\|#");
		result = new ArrayList<List<String>>();

		if (array[0].equals("OK")) {
			is_ok = true;
		} else {
			Log.e("CLIENT", "Se esperaba 'OK' == " + array[0]);
			is_ok = false;
			return;
		}

		for(int i=1; i < array.length - (has_pages ? 1 : 0); i++) {
			String[] pairs = array[i].split("\\{\\|\\}");
			List<String> l = new ArrayList<String>();
			for(String pair: pairs) {
				l.add(pair);
			}
			result.add(l);
		}

		if (has_pages) {
			try {
				String[] pairs = array[array.length-1].trim().split("\\{#\\}");
				has_more = pairs[1].equals("More");

				Log.d("ResponseParser", "Total : " + pairs[0]);
				Log.d("ResponseParser", "More  : " + pairs[1] + " ("+has_more+")");
			} catch (Exception e) {
				Log.d("ResponseParser", e.toString());
			}
		}
	}
}
