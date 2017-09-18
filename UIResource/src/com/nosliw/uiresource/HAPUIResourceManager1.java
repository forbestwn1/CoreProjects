package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPDataTypeManager;

public class HAPUIResourceManager1 extends HAPSerializableImp{

	private Map<String, HAPUIResource> m_uiResource;
	
	private Map<String, String> m_uiResourceScripts;
	
	private HAPConfigure m_setting;
	
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPUIResourceManager1(HAPConfigure setting, HAPDataTypeManager dataTypeMan) {
		this.m_dataTypeMan = dataTypeMan;
		this.m_uiResource = new LinkedHashMap<String, HAPUIResource>();
		this.m_uiResourceScripts = new LinkedHashMap<String, String>();
//		this.createDefaultConfiguration();
		this.m_setting = setting;
	}
/*
	private void createDefaultConfiguration(){
		this.m_setting = new HAPConfigureImp();
	}
	
	public void addUIResource(HAPUIResource resource){
		String name = resource.getId();
		this.m_uiResource.put(name, resource);
		this.m_uiResourceScripts.put(name, this.readUIResourceScript(name));
	}
	
	public HAPUIResource getUIResource(String name){
		return this.m_uiResource.get(name);
	}
	
	public HAPUIResource[] getAllUIResource(){
		return this.m_uiResource.values().toArray(new HAPUIResource[0]);
	}

	//return the script string (an json structure containing block and expression) for ui resource
	public String getUIResourceScript(String name){
		String out = this.m_uiResourceScripts.get(name);
		if(out==null){
			out = this.readUIResourceScript(name);
			this.m_uiResourceScripts.put(name, out);
		}
		return out;
	}
	
	public String readUIResourceScript(String name){
		String fileName = HAPUIResourceUtility.getUIResourceScriptFileName(name, this); 
		String out = HAPFileUtility.readFile(fileName);
		return out;
	}
	
	//get temporate file location
	public String getTempFileLocation(){
		String scriptLocation = this.m_setting.getStringValue(HAPConstant.UIRESOURCEMAN_SETTINGNAME_SCRIPTLOCATION);
		return scriptLocation;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String name : this.m_uiResource.keySet()){
			jsonMap.put(name, this.m_uiResource.get(name).toStringValue(HAPSerializationFormat.JSON_FULL));
		}
	}
		
	@Override
	public String toString(){ return this.toStringValue(HAPSerializationFormat.JSON); }

	protected HAPConfigure getConfiguration(){ return this.m_setting; }
	*/
}
