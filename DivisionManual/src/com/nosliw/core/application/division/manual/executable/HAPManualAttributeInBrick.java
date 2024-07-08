package com.nosliw.core.application.division.manual.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAdapter;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.HAPWrapperValueOfValue;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPManualAttributeInBrick extends HAPAttributeInBrick implements HAPTreeNodeBrick, HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";

	@HAPAttribute
	public static final String ADAPTER = "adapter";

	@HAPAttribute
	public static final String PATHFROMROOT = "pathFromRoot";

	private HAPWrapperValue m_valueWrapper;
	
	private List<HAPManualAdapter> m_adapter;
	
	private HAPInfoTreeNode m_tempTreeNodeInfo;
	
	public HAPManualAttributeInBrick() {
		this.m_adapter = new ArrayList<HAPManualAdapter>();
	}
	
	public HAPManualAttributeInBrick(String attrName, HAPWrapperValue valueWrapper) {
		this();
		this.setName(attrName);
		this.setValueWrapper(valueWrapper);
	}
	
	@Override
	public HAPWrapperValue getValueWrapper() {	return this.m_valueWrapper;	}
	public void setValueWrapper(HAPWrapperValue valueInfo) {	
		this.m_valueWrapper = valueInfo;
		this.synTreeNodeInfoInBrick();
	}

	public void addAdapter(HAPManualAdapter adapter) {    this.m_adapter.add(adapter);    }
	@Override
	public List<HAPAdapter> getAdapters(){    return (List)this.m_adapter;    }
	public List<HAPManualAdapter> getManualAdapters(){    return this.m_adapter;    }
	
	public void setValueOfValue(Object value) {		this.setValueWrapper(new HAPWrapperValueOfValue(value));	}
	public void setValueOfBrick(HAPManualBrick brick) {		this.setValueWrapper(new HAPManualWrapperValueOfBrick(brick));	}
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
			((HAPManualWrapperValueOfBrick)this.m_valueWrapper).getManualBrick().setTreeNodeInfo(m_tempTreeNodeInfo);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_valueWrapper!=null) {
			jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
		}
		
		List<String> adapterJsonList = new ArrayList<String>();
		for(HAPManualAdapter adapter : this.m_adapter) {
			adapterJsonList.add(adapter.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ADAPTER, HAPUtilityJson.buildArrayJson(adapterJsonList.toArray(new String[0])));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JAVASCRIPT));

		List<String> adapterJsonList = new ArrayList<String>();
		for(HAPManualAdapter adapter : this.m_adapter) {
			adapterJsonList.add(adapter.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ADAPTER, HAPUtilityJson.buildArrayJson(adapterJsonList.toArray(new String[0])));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_valueWrapper.buildResourceDependency(dependency, runtimeInfo);
		
		for(HAPManualAdapter adapter : this.m_adapter) {
			adapter.buildResourceDependency(dependency, runtimeInfo);
		}
	}
}
