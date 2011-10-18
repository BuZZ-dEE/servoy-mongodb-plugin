/*
 * Copyright (c) 2009, Servoy-stuff
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 * http://www.fsf.org/licensing/licenses/lgpl.txt
 */
package com.softwear.servoy;

//import org.mozilla.javascript.Function;

import com.servoy.j2db.plugins.IClientPluginAccess;
import com.softwear.plugins.mongodb.MongoDBScriptObject;

/**
 * @author Servoy Stuff
 * http://www.servoy-stuff.net/
 */
public abstract class MongoDBAbstractProvider {

	protected IClientPluginAccess application;
	protected MongoDBScriptObject parent;
	public String dialogName;
	
	public MongoDBAbstractProvider(IClientPluginAccess app, MongoDBScriptObject parent) {
		this.application = app;
		this.parent = parent;
	}

}
