package com.nosliw.data.core.script.context;

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
	
	private HAPContextRootNodeId m_rootNodeId;
	
	private String m_path;
	
	public HAPContextPath(){}
	
	public HAPContextPath(String fullPath){
		int index = fullPath.indexOf(HAPConstant.SEPERATOR_PATH);
		if(index==-1){
			this.m_rootNodeId = new HAPContextRootNodeId(fullPath);
		}
		else{
			this.m_rootNodeId = new HAPContextRootNodeId(fullPath.substring(0, index));
			this.m_path = fullPath.substring(index+1);
		}
	}
	
	public HAPContextPath(String categary, String path){
		int index = path.indexOf(HAPConstant.SEPERATOR_PATH);
		if(index==-1){
			this.m_rootNodeId = new HAPContextRootNodeId(categary, path);
		}
		else{
			this.m_rootNodeId = new HAPContextRootNodeId(categary, path.substring(0, index));
			this.m_path = path.substring(index+1);
		}
	}

	public HAPContextPath(String contextCategary, String rootEleName, String path){
		this.m_rootNodeId = new HAPContextRootNodeId(contextCategary, rootEleName);
		this.m_path = path;
	}

	public HAPContextPath appendSegment(String seg){
		HAPContextPath out = new HAPContextPath();
		out.m_rootNodeId = this.m_rootNodeId.clone();
		out.m_path = HAPNamingConversionUtility.cascadePath(m_path, seg);
		return out;
	}
	
//	public String getRootElementName(){  return this.m_rootNodeId.getName();  }
	public HAPContextRootNodeId getRootElementId() {  return this.m_rootNodeId;   }
	
	public String getPath(){		return this.m_path==null?"":this.m_path;	}
	
	public String getFullPath(){  return HAPNamingConversionUtility.cascadePath(this.m_rootNodeId.getFullName(), this.m_path);   }
	
	public HAPContextPath clone() {
		HAPContextPath out = new HAPContextPath();
		out.m_path = this.m_path;
		out.m_rootNodeId = this.m_rootNodeId.clone();
		return out;
	}
	
	public String[] getPathSegments(){
		if(HAPBasicUtility.isStringEmpty(m_path))  return new String[0];
		else  return HAPNamingConversionUtility.parseComponentPaths(m_path);     
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOTNAME, this.m_rootNodeId.getFullName());
		jsonMap.put(PATH, this.m_path);
	}
}
