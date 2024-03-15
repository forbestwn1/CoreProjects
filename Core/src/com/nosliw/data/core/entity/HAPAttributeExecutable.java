package com.nosliw.data.core.entity;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPAttributeExecutable extends HAPExecutableImpEntityInfo implements HAPTreeNode{

	@HAPAttribute
	public static final String VALUEINFO = "valueInfo";

	private HAPInfoAttributeValue m_valueInfo;
	
	//path from root
	private HAPPath m_pathFromRoot;

	public HAPAttributeExecutable() {}
	
	public HAPAttributeExecutable(String attrName, HAPInfoAttributeValue attrValue) {
		this.setName(attrName);
		this.setValueInfo(attrValue);
	}
	
	public HAPInfoAttributeValue getValueInfo() {	return this.m_valueInfo;	}
	
	public void setValueInfo(HAPInfoAttributeValue valueInfo) {
		this.m_valueInfo = valueInfo;
	}

	@Override
	public HAPPath getPathFromRoot() {    return this.m_pathFromRoot;    }
	
	public void setPathFromRoot(HAPPath path) {    this.m_pathFromRoot = path;     }

	@Override
	public Object getNodeValue() {   return this.getValueInfo();   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEINFO, this.m_valueInfo.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEINFO, this.m_valueInfo.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_valueInfo.getResourceDependency(runtimeInfo, resourceManager));
	}
}
