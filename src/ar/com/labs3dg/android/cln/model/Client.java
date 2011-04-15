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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Client
{
	public boolean hasMore = false;

	static public String PER_PAGE = "20";

	/* Prelodeable data */
	static public List<Category> Categories = null;

	static public String[] States = {
		"Ciudad de Buenos Aires",
		"Gran Buenos Aires",
		"Lugares de Veraneo",
		"Córdoba"
	};
	static public String[] CABA = {
		"Todos",
		"Abasto",
		"Almagro",
		"Balvanera",
		"Barrancas",
		"Barrio Norte",
		"Barrio River",
		"Belgrano",
		"Caballito",
		"Capital",
		"Chacarita",
		"Colegiales",
		"Congreso",
		"Flores",
		"Liniers",
		"Mataderos",
		"Monte Castro",
		"Montserrat",
		"Nuñez",
		"Once",
		"Palermo",
		"Parque Patricios",
		"Puerto Madero",
		"Recoleta",
		"Retiro",
		"Saavedra",
		"San Cristóbal",
		"San Telmo",
		"Villa Crespo",
		"Villa del Parque",
		"Villa Devoto",
		"Villa Urquiza",
	};
	static public String[] BSAS = {
		"Acassuso",
		"Adrogue",
		"Avellaneda",
		"Banfield",
		"Beccar",
		"Benavídez",
		"Berazategui",
		"Bernal",
		"Boulogne",
		"Campana",
		"Cañuelas",
		"Castelar",
		"City Bell",
		"Ciudad Evita",
		"Ciudad Jardín",
		"Escobar",
		"Esteban Echeverría",
		"Ezeiza",
		"General Pacheco",
		"Haedo",
		"Hurlingham",
		"Ituzaingo",
		"La Lucila",
		"La Plata",
		"Lanús",
		"Lomas de Zamora",
		"Luján",
		"Malvinas Argentinas",
		"Martínez",
		"Maschwitz",
		"Merlo",
		"Monte Grande",
		"Moreno",
		"Morón",
		"Muñíz",
		"Olivos",
		"Pilar",
		"Quilmes",
		"Ramos Mejía",
		"Ranelagh",
		"Remedios de Escalada",
		"San Fernando",
		"San Isidro",
		"San Justo",
		"San Martín",
		"San Miguel",
		"Sarandí",
		"Temperley",
		"Tigre",
		"Tortuguitas",
		"Vicente López",
		"Villa Ballester",
		"Wilde",
		"Zarate"
	};
	static public String[] CentrosTuristicos = {
		"Mar del Plata",
		"Pinamar",
		"Cariló",
		"Punta del Este",
		"Villa Gesell",
		"Miramar",
		"Valeria del mar",
		"San Bernardo",
		"Mar de Ajó",
		"Mar de Las Pampas",
		"Ostende",
		"San Clemente del Tuyú",
		"La Lucila",
		"Las Toninas",
		"Mar Chiquita",
		"Chapadmalal",
		"Chascomus",
		"Costa del Este",
		"Mar del Sur",
		"Mar del Tuyú",
		"Santa Clara del Mar",
		"Santa Teresita",
	};
	static public String[] Empty = {};

	public List<Address> getDireccion(String state, String zone, String address)
	{
		List<Address> locations = new ArrayList<Address>();

		String url = "http://serviciosclub.lnol.com.ar/codamation/getDirecciones/"+address.replace(" ", "%20")+"/"+zone.replace(" ", "%20")+"/"+state.replace(" ", "%20")+"/";
		String data = doRequest(new HttpGet(url));

		ResponseParser r = new ResponseParser(data, false);

		if (r.is_ok) {

			for(List<String> list: r.result) {
				locations.add(Address.Parse(list.get(0)));
			}
		}

		return locations;
	}

	public List<Venue> getHighlited(String card)
	{
		hasMore = false;
		return getVenues("http://serviciosclub.lnol.com.ar/codamation/getDestacados/-/-/"+card+"/");
	}

	public List<Category> getCategories()
	{
		hasMore = false;
		List<Category> list = new ArrayList<Category>();

		String data = doRequest(new HttpGet("http://serviciosclub.lnol.com.ar/codamation/getCategorias"));
		ResponseParser r = new ResponseParser(data);

		if (r.is_ok) {
			hasMore = r.has_more;
			// Leo las categories
			list = Category.FromString(r.result.get(0));

			for(int i=1; i< r.result.size(); i++) {
				for(String s: r.result.get(i)) {
					String[] data1 = s.split("\\{#\\}");
					int category_id = Integer.parseInt(data1[0]);
					int id = Integer.parseInt(data1[1]);
					String name = data1[2];

					Category c = new Category();
					c.id = id;
					c.name = name;
					for(Category cat: list) {
						Log.d("CLIENTE", "" + cat.id + " ==? " + category_id);
						if (cat.id == category_id) {
							cat.subcategories.add(c);
							break;
						}
					}
				}
			}

		} else {
			Log.e("CLIENT", "No se pudo parsear : " + data);
		}

		return list;
	}

	public List<Venue> getVenues(Double lat, Double lon) {
		String url = "http://serviciosclub.lnol.com.ar/codamation/getComercios/-/-/-/"+lat+"/"+lon+"/"+PER_PAGE+"/-/-/";

		return getVenues(url);
	}

	public List<Venue> getVenues(String url) {
		List<Venue> list = new ArrayList<Venue>();
		String data = doRequest(new HttpGet(url));
		ResponseParser r = new ResponseParser(data);

		if (r.is_ok) {
			hasMore = r.has_more;
			for(String s: r.result.get(0)) {
				try {
					list.add(Venue.FromString(s));
				} catch (Exception e) {
					Log.e("CLIENT", "Unparseable entity "+e+": " + s);
				}
			}
		} else {
			Log.e("CLIENT", "No se pudo parsear : " + data);
		}

		return list;
	}

	private String doRequest(HttpUriRequest request)
	{
		DefaultHttpClient client = new DefaultHttpClient();
		String result = "no data yet";
		try {
			Log.i("CLIENT", "Fetching " + request.getMethod() + ":" + request.getURI());
			HttpResponse responseGet = client.execute(request);
			HttpEntity entity = responseGet.getEntity();
			if (entity != null) {
				result = convertStreamToString(entity.getContent());
				Log.i("REQUEST", result);
			}
		} catch (Exception e) {
			Log.e("CLIENT", "doRequest : " + e.toString());
		}
		return result;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public final static String HIGHLATED_VENUES = "destacados";

	public List<Venue> getVenuesFrom(String dataUrl, String card) {
		if (dataUrl.equals(HIGHLATED_VENUES)) {
			return getHighlited(card);
		}

		return getVenues(dataUrl);
	}

	@SuppressWarnings("unchecked")
	public static String join(Iterator iterator, String separator)
	{
		// handle null, zero and one elements before building a buffer
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			if (first == null) {
				return "";
			}

			return first.toString();
		}
		// two or more elements
		StringBuffer buf =  new StringBuffer(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first);
		}
		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}
}
