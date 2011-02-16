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

public class Category
{
	public int id;
	public String name;
	public List<Category> subcategories = new ArrayList<Category>();

	static public List<Category> FromString(List<String> l)
	{
		List<Category> list = new ArrayList<Category>();

		for(String s: l) {
			String[] data = s.split("\\{#\\}");
			Category c = new Category();
			c.id = Integer.parseInt(data[0]);
			c.name = data[1];

			list.add(c);
		}
		return list;

	}
}
