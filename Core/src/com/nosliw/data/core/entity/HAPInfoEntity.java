package com.nosliw.data.core.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPInfoEntity extends HAPExecutableImp implements HAPTreeNode, HAPWithEntity{

	@HAPAttribute
	public static final String ENTITY = "entity";

	private HAPEntityExecutable m_entityExe;

	public HAPInfoEntity(HAPEntityExecutable entity) {
		this.m_entityExe = entity;
	}
	
	@Override
	public HAPEntityExecutable getEntity() {   return this.m_entityExe;     }
	public void setEntity(HAPEntityExecutable entity) {     this.m_entityExe = entity;     }

	@Override
	public HAPPath getPathFromRoot() {  return null;  }

	@Override
	public Object getNodeValue() {  return this.getEntity();   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITY, this.m_entityExe.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_entityExe.toResourceData(runtimeInfo).toString());
	}

	@Override
	public HAPIdEntityType getEntityTypeId() {   return this.getEntity().getEntityTypeId();  }

}
