package com.nosliw.common.path;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPComplexPath {

	private String m_fullName;

	private HAPPath m_path;
	
	private String m_rootName;
	
	public HAPComplexPath(String rootName, String path){
		this.m_rootName = rootName;
		this.m_path = new HAPPath(path);
		this.m_fullName = HAPNamingConversionUtility.cascadePath(this.m_rootName, this.m_path.getPath());
	}
	
	public HAPComplexPath(String fullName){
		if(HAPBasicUtility.isStringNotEmpty(fullName)){
			this.m_fullName = fullName;
			
			int index = this.m_fullName.indexOf(HAPConstant.SEPERATOR_PATH);
			if(index==-1){
				//name only
				this.m_rootName = this.m_fullName;
			}
			else{
				this.m_rootName = this.m_fullName.substring(0, index);
				this.m_path = new HAPPath(this.m_fullName.substring(index+1));
			}
		}
	}
	
	public String getRootName(){
		return this.m_rootName;
	}
	
	public String getPath(){
		if(this.m_path==null)   return null;
		return this.m_path.getPath();
	}

	public String[] getPathSegs(){
		if(this.m_path==null)  return new String[0];
		return this.m_path.getPathSegs();
	}
	
	public String getFullName(){
		return this.m_fullName;
	}
}
