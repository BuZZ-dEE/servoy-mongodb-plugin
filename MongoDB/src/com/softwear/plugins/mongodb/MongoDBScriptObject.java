/*
	Software License Agreement (FreeBSD License) 
	Copyright (c) 2011, Bobby Drake
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following 
	conditions are met:

	Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
	in the documentation and/or other materials provided with the distribution.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
	NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
	THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
	INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
	OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.softwear.plugins.mongodb;


import java.net.UnknownHostException;

import org.bson.*;
import org.bson.types.ObjectId;

import com.mongodb.gridfs.*;

//import org.mozilla.javascript.Function;
//import org.mozilla.javascript.Scriptable;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
//import com.servoy.j2db.Messages;
import com.servoy.j2db.plugins.IClientPluginAccess;
//import com.servoy.j2db.scripting.FunctionDefinition;
import com.servoy.j2db.scripting.IScriptObject;
//import com.servoy.j2db.util.Debug;
//import com.servoy.j2db.util.PersistHelper;
//import com.servoy.j2db.util.Utils;
import com.softwear.plugins.mongodb.MongoDBProvider;
import com.softwear.servoy.MongoDBAbstractProvider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * @author Bobby Drake
 *
 */
public class MongoDBScriptObject implements IScriptObject {
	
	protected IClientPluginAccess application;
	
	public Mongo m = null;
	public BasicDBObject dbObj = null;
	public BasicBSONObject bsonObj = null;
	private static final String VERSION = "1.3";
	private MongoDBAbstractProvider provider;
//	private FunctionDefinition processFunction;
	
	public MongoDBScriptObject(IClientPluginAccess application) {
		this.application = application;
	}
	
	public Mongo js_getMongo(String _host,int _port) {
		try {
			if (m == null) {
				m = new Mongo( _host , _port ); //localhost 27017
				m.setWriteConcern(WriteConcern.NONE);
			} else {
				return m;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	
	public BasicDBObject js_getBasicDBObject() throws UnknownHostException {
		try {
			return new BasicDBObject();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbObj;
	}
	
	public BasicBSONObject js_getBasicBSONObject() throws UnknownHostException {
		try {
			return new BasicBSONObject();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bsonObj;
	}
	
	public GridFS js_getGridFS(DB db) {
		GridFS fs = new GridFS(db); 
		return fs;
	}
	
	public ObjectId js_getObjectID(String id) {
		ObjectId oid = new ObjectId(id);
		return oid;
	}
	
	public BasicDBObject js_getRegExQueryObject(HashMap <String,String>_keyPatternArray,String _pattern) {
		BasicDBObject dbObj = js_getRegExQueryObject(_keyPatternArray,false);
		return dbObj;
	}
	
	public BasicDBObject js_getRegExQueryObject(HashMap <String,String>_keyPatternArray,Boolean _caseSensitive) {
		Pattern pattern;
		BasicDBObject dbObj = new BasicDBObject();
		if (_caseSensitive) 
		{
			Iterator <String>it = _keyPatternArray.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String val = _keyPatternArray.get(key);
				pattern = Pattern.compile(val);
				dbObj.put(key, pattern);
			}
		}
		else
		{
			Iterator <String>it = _keyPatternArray.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String val = _keyPatternArray.get(key);
				pattern = Pattern.compile(val, Pattern.CASE_INSENSITIVE);
				dbObj.put(key, pattern);
			}
		}
		
		return dbObj;
	}
	
	public WriteConcern js_getMongoWriteConcern(String _type) {
		WriteConcern wc = WriteConcern.NONE;
		if (_type.equals("none")) {
			wc = WriteConcern.NONE;
		} else if (_type.equals("normal")) {
			wc = WriteConcern.NORMAL;	
		} else if (_type.equals("safe")) {
			wc = WriteConcern.SAFE;	
		} else if (_type.equals("replicas_safe")) {
			wc = WriteConcern.REPLICAS_SAFE;	
		} else if (_type.equals("fsync_safe")) {
			wc = WriteConcern.FSYNC_SAFE;	
		}
		return wc;
	}
	
	public Boolean js_authMongo(DB _db,String _user,String _password) {
		try {
			char[] charPassword = _password.toCharArray();
			return _db.authenticate(_user, charPassword);
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public String js_getVersion() {
		return VERSION;
	}
	
	/* (non-Javadoc)
	 * @see com.servoy.j2db.scripting.IScriptObject#getAllReturnedTypes()
	 */
	public Class<?>[] getAllReturnedTypes() {
		return null;
	}
	
	public String[] getParameterNames(final String methodName) {
		if ("getMongo".equals(methodName)) {
			return new String[] { "_host" , "_port" };
		} else if ("authMongo".equals(methodName)) {
			return new String[] { "_db","_user","_password" };
		} else if ("authMongo".equals(methodName)) {
			return new String[] { "_db","_user","_password" };
		}
		return null;
	}
	
	public String getSample(final String methodName) {
		if ("getMongo".equals(methodName)) {
			StringBuffer buff = new StringBuffer("// ");
			buff.append(getToolTip(methodName));
			buff.append("\n\t//%%elementName%%.getMongo(\"a\");");
			return buff.toString();
		} else if ("authMongo".equals(methodName)) {
			StringBuffer buff = new StringBuffer("// ");
			buff.append(getToolTip(methodName));
			buff.append("\n\t//%%elementName%%.authMongo(\"a\");");
			return buff.toString();
		} else if ("getBasicDBObject".equals(methodName)) {
			StringBuffer buff = new StringBuffer("// ");
			buff.append(getToolTip(methodName));
			buff.append("\n\t//%%elementName%%.getBasicDBObject(\"a\");");
			return buff.toString();
		} else if ("getRegExQueryObject".equals(methodName)) {
			StringBuffer buff = new StringBuffer("// ");
			buff.append(getToolTip(methodName));
			buff.append("\n\t//%%elementName%%.getRegExQueryObject(\"a\");");
			return buff.toString();
		} else if ("getGridFS".equals(methodName)) {
			StringBuffer buff = new StringBuffer("// ");
			buff.append(getToolTip(methodName));
			buff.append("\n\t//%%elementName%%.getGridFS(\"a\");");
			return buff.toString();
		} else if ("getVersion".equals(methodName)) {
			StringBuffer buff = new StringBuffer("// ");
			buff.append(getToolTip(methodName));
			buff.append("\n\tapplication.output(%%elementName%%.getVersion());");
			return buff.toString();			
		}
		return null;
	}

	public String getToolTip(final String methodName) {
		if ("getMongo".equals(methodName)) {
			return "Starts a connection to MongoDB. http://www.mongodb.org/display/DOCS/Java+Language+Center";
		} else if ("getVersion".equals(methodName)) {
			return "Returns the version of the plugin";
		} else if ("getBasicDBObject".equals(methodName)) {
			return "Returns a DB Object used for inserting, updating, and creating a Mongo query using the .put() method. http://www.mongodb.org/display/DOCS/Querying";
		} else if ("getRegExQueryObject".equals(methodName)) {
			return "Returns a special DB Object used for queries using Java regex patterns http://www.mongodb.org/display/DOCS/Querying";
		} else if ("getGridFS".equals(methodName)) {
			return "Returns a GridFS Object used for inserting large files into MongoDB. http://www.mongodb.org/display/DOCS/GridFS";
		} else if ("authMongo".equals(methodName)) {
			return "Allows you to send authentication information to your MongoDB instance.\n " +
					"Call this method after you start a connection. If your MongoDB is not secured, do not use this method." +
					"http://www.mongodb.org/display/DOCS/Security+and+Authentication";
		}
		return null;
	}

	public boolean isDeprecated(final String methodName) {
		return false;
	}
	
	protected MongoDBAbstractProvider getProvider() {
		if (provider == null) {
			provider = new MongoDBProvider(application, this);
		}
		return provider;
	}
	
	public void dispose() {
		//provider.dispose();
		provider = null;
		//callBackFunction = null;
		application = null;
		m = null;
	}
	
	@SuppressWarnings("unused")
	private boolean isWeb() {
		return (application != null && (application.getApplicationType() == IClientPluginAccess.WEB_CLIENT));
	}

}
