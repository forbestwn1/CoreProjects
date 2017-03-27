package com.nosliw.data.core.runtime.js;

import java.io.File;

public class HAPJSLibraryManager {

	private static HAPJSLibraryManager m_instance;
	
	static public HAPJSLibraryManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPJSLibraryManager();
		}
		return m_instance;
	}
	
	public File getLibrary(String library){
		return null;
	}
	
}
