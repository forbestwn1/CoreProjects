package com.nosliw.core.application;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPAttributeInBrick extends HAPEntityInfoImp implements HAPTreeNodeBrick, HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";

	@HAPAttribute
	public static final String ADAPTER = "adapter";

	@HAPAttribute
	public static final String PATHFROMROOT = "pathFromRoot";

	private HAPWrapperValue m_valueWrapper;
	
	private Set<HAPAdapter> m_adapter;
	
	private HAPInfoTreeNode m_tempTreeNodeInfo;
	
	public HAPAttributeInBrick() {
		this.m_adapter = new HashSet<HAPAdapter>();
	}
	
	public HAPAttributeInBrick(String attrName, HAPWrapperValue valueWrapper) {
		this();
		this.setName(attrName);
		this.setValueWrapper(valueWrapper);
	}
	
	public HAPWrapperValue getValueWrapper() {	return this.m_valueWrapper;	}
	public void setValueWrapper(HAPWrapperValue valueInfo) {	
		this.m_valueWrapper = valueInfo;
		this.synTreeNodeInfoInBrick();
	}

	public void addAdapter(HAPAdapter adapter) {    this.m_adapter.add(adapter);    }
	public Set<HAPAdapter> getAdapters(){    return this.m_adapter;    }
	
	public void setValueOfValue(Object value) {		this.setValueWrapper(new HAPWrapperValueOfValue(value));	}
	public void setValueOfBrick(HAPBrick brick) {		this.setValueWrapper(new HAPWrapperValueOfBrick(brick));	}
	public void setValueOfResourceId(HAPResourceId resourceId) {		this.setValueWrapper(new HAPWrapperValueOfReferenceResource(resourceId));	}

	@Override
	public HAPInfoTreeNode getTreeNodeInfo() {  return this.m_tempTreeNodeInfo;  }
	public void setTreeNodeInfo(HAPInfoTreeNode treeNodeInfo) {   
		this.m_tempTreeNodeInfo = treeNodeInfo;
		this.synTreeNodeInfoInBrick();
	}

	@Override
	public Object getNodeValue() {  return this.getValueWrapper();   }

	private void synTreeNodeInfoInBrick() {
		if(this.m_tempTreeNodeInfo!=null&&this.m_valueWrapper!=null&&this.m_valueWrapper.getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
			((HAPWrapperValueOfBrick)this.m_valueWrapper).getBrick().setTreeNodeInfo(m_tempTreeNodeInfo);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_valueWrapper!=null) {
			jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
		}
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_valueWrapper.buildResourceDependency(dependency, runtimeInfo);
		
		for(HAPAdapter adapter : this.m_adapter) {
			adapter.buildResourceDependency(dependency, runtimeInfo);
		}
	}
}
