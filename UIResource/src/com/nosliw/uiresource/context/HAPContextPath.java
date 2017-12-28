package com.nosliw.uiresource.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPContextPath extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOTNAME  = "rootEleName";

	@HAPAttribute
	public static final String PATH  = "path";
	
	private String m_rootEleName;
	
	private String m_path;
	
	public HAPContextPath(){}
	
	public HAPContextPath(String fullPath){
		int index = fullPath.indexOf(HAPConstant.SEPERATOR_PATH);
		if(index==-1){
			this.m_rootEleName = fullPath;
		}
		else{
			this.m_rootEleName = fullPath.substring(0, index);
			this.m_path = fullPath.substring(index+1);
		}
	}
	
	public HAPContextPath(String rootEleName, String path){
		this.m_rootEleName = rootEleName;
		this.m_path = path;
	}
	
	public HAPContextPath appendSegment(String seg){
		HAPContextPath out = new HAPContextPath();
		out.m_rootEleName = this.m_rootEleName;
		out.m_path = HAPNamingConversionUtility.cascadeComponentPath(m_path, seg);
		return out;
	}
	
	public String getRootElementName(){  return this.m_rootEleName;  }
	
	public String getPath(){		return this.m_path==null?"":this.m_path;	}
	
	public String getFullPath(){  return HAPNamingConversionUtility.cascadeComponentPath(this.m_rootEleName, this.m_path);   }
	
	public String[] getPathSegments(){
		if(HAPBasicUtility.isStringEmpty(m_path))  return new String[0];
		else  return HAPNamingConversionUtility.parseComponentPaths(m_path);     
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOTNAME, this.m_rootEleName);
		jsonMap.put(PATH, this.m_path);
	}
}
