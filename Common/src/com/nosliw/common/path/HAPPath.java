package com.nosliw.common.path;

import com.nosliw.common.utils.HAPNamingConversionUtility;

public class HAPPath {

	private String m_path;
	
	private String[] m_pathSegs = new String[0];

	public HAPPath(String path){
		this.m_path = path;
		this.m_pathSegs = HAPNamingConversionUtility.parsePaths(this.m_path);
	}
	
	public String getPath(){
		return this.m_path;
	}

	public String[] getPathSegs(){
		return this.m_pathSegs;
	}
	
	public HAPPath clonePath() {
		HAPPath out = new HAPPath(this.m_path);
		return out;
	}

}
