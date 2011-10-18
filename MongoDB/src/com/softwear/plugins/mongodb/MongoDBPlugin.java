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

import java.beans.PropertyChangeEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
//import java.net.URL;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.servoy.j2db.plugins.IClientPlugin;
import com.servoy.j2db.plugins.IClientPluginAccess;
import com.servoy.j2db.plugins.PluginException;
import com.servoy.j2db.preference.PreferencePanel;
import com.servoy.j2db.scripting.IScriptObject;
import com.servoy.j2db.util.Debug;
import com.softwear.plugins.mongodb.MongoDBScriptObject;

public class MongoDBPlugin implements IClientPlugin {

	private static final String PLUGIN_NAME = "MongoDB";

	private IClientPluginAccess application;
	private MongoDBScriptObject iso;
	private Icon icon;
	
	public void initialize(IClientPluginAccess app) throws PluginException {
		this.application = app;
	}
	
	public Icon getImage() {
		if(icon == null) {
			icon = new ImageIcon(getClass().getResource("res/mongoicon.gif"));
		}
		return icon;
	}

	public String getName() {
		return PLUGIN_NAME;
	}

	public PreferencePanel[] getPreferencePanels() {
		return null;
	}

	public IScriptObject getScriptObject() {
		if (iso == null){
			iso = new MongoDBScriptObject(application);
		}
		return iso;
	}

	public Properties getProperties() {
    	Properties props = new Properties();
        props.put(DISPLAY_NAME, "MongoDB Plugin");
        return props;
	}

	public void load() throws PluginException {
		// ignore
	}

	public void unload() throws PluginException {
		if (application != null) {
			try {
				if (iso != null) {
					iso.dispose();
					iso = null;
				}
			} catch (Throwable ex) {
				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				Debug.error(sw.toString());
			}
		}
	}

	public void propertyChange(PropertyChangeEvent arg0) {
		// ignore
	}
	
	public boolean isWeb() {
		return (application != null && application.getApplicationType() != IClientPluginAccess.CLIENT);
	}

}
