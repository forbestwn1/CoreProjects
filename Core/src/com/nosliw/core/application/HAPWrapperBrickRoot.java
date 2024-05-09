package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPWrapperBrickRoot extends HAPExecutableImp implements HAPTreeNodeBrick, HAPWithBrick{

	private HAPBrick m_brick;

	public HAPWrapperBrickRoot(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	@Override
	public HAPBrick getBrick() {   return this.m_brick;     }
	public void setEntity(HAPBrick entity) {     this.m_brick = entity;     }

	@Override
	public HAPInfoTreeNode getTreeNodeInfo() {  return new HAPInfoTreeNode(new HAPPath(), null);  }

	@Override
	public Object getNodeValue() {  return this.getBrick();   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brick.toResourceData(runtimeInfo).toString());
	}

}
