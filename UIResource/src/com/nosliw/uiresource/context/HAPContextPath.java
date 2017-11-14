package com.nosliw.uiresource.context;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPContextPath {

	private String m_rootEleName;
	
	private String m_path;
	
	public HAPContextPath(String fullPath){
		int index = fullPath.indexOf(HAPConstant.SEPERATOR_PATH);
		if(index==-1){
			this.m_rootEleName = fullPath;
		}
		else{
			this.m_rootEleName = fullPath.substring(0, index);
		}
	}
	
	public HAPContextPath(String rootEleName, String path){
		this.m_rootEleName = rootEleName;
		this.m_path = path;
	}
	
	public String getRootElementName(){  return this.m_rootEleName;  }
	
	public String getPath(){  return this.m_path;  }
	
	public String getFullPath(){  return HAPNamingConversionUtility.cascadeComponentPath(this.m_rootEleName, this.m_path);   }
	
	public String[] getPathSegments(){
		if(HAPBasicUtility.isStringEmpty(m_path))  return new String[0];
		else  return HAPNamingConversionUtility.parseComponentPaths(m_path);     
	}
	
}
