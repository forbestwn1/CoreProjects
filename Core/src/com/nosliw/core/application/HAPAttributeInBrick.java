package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPAttributeInBrick extends HAPExecutableImpEntityInfo implements HAPTreeNode{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";

	@HAPAttribute
	public static final String PATHFROMROOT = "pathFromRoot";

	private HAPWrapperValueInAttribute m_valueWrapper;
	
	//path from root
	private HAPPath m_pathFromRoot;

	public HAPAttributeInBrick() {}
	
	public HAPAttributeInBrick(String attrName, HAPWrapperValueInAttribute valueWrapper) {
		this.setName(attrName);
		this.setValueInfo(valueWrapper);
	}
	
	public HAPWrapperValueInAttribute getValueWrapper() {	return this.m_valueWrapper;	}
	
	public void setValueInfo(HAPWrapperValueInAttribute valueInfo) {
		this.m_valueWrapper = valueInfo;
	}

	@Override
	public HAPPath getPathFromRoot() {    return this.m_pathFromRoot;    }
	
	public void setPathFromRoot(HAPPath path) {    this.m_pathFromRoot = path;     }

	@Override
	public Object getNodeValue() {   return this.getValueWrapper();   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_pathFromRoot!=null) {
			jsonMap.put(PATHFROMROOT, this.m_pathFromRoot.toString());
		}
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_valueWrapper.getResourceDependency(runtimeInfo, resourceManager));
	}
}
