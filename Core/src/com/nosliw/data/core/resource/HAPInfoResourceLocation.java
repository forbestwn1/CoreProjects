package com.nosliw.data.core.resource;

import java.io.File;

public class HAPInfoResourceLocation {

	//for resource in a folder, the base path
	private String m_basePath;
	
	//core file
	private File m_file;
	
	public HAPInfoResourceLocation(File file, String basePath) {
		this.m_file = file;
		this.m_basePath = basePath;
	}
	
	public String getBasePath() {    return this.m_basePath;    }
	
	public File getFiile() {   return this.m_file;    }
	
}
