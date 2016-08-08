package com.nosliw.common.path;

import com.nosliw.common.pattern.HAPNamingConversionUtility;

public class HAPPath {

	private String[] m_pathSegs;
	private String m_name;
	private String m_path;
	
	public HAPPath(String name, String path, String[] pathSegs){
		this.m_name = name;
		this.m_path = path;
		this.m_pathSegs = pathSegs;
	}
	
	public HAPPath(String path){
		HAPPath pathObj = HAPNamingConversionUtility.parsePath(path);
		this.m_name = pathObj.m_name;
		this.m_path = pathObj.m_path;
		this.m_pathSegs = pathObj.m_pathSegs;
	}
	
	public String getPath(){
		return this.m_path;
	}

	public String getName(){
		return this.m_name;
	}
	
	public String[] getPathSegs(){
		return this.m_pathSegs;
	}
	
}
