package com.nosliw.core.application.division.manual;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPManualInfoEntity extends HAPEntityInfoImp implements HAPManualWithEntity, HAPTreeNode{

	public static final String INFO = "info";

	private HAPManualEntity m_entity;

	public HAPManualInfoEntity() {	}

	@Override
	public HAPManualEntity getEntity() {    return this.m_entity;    }
	public void setEntity(HAPManualEntity entity) {    
		this.m_entity = entity;     
	}

	@Override
	public HAPIdBrickType getEntityTypeId() {   return this.getEntity().getEntityTypeId();   }

	
	@Override
	public HAPPath getPathFromRoot() {  return null;  }

	@Override
	public Object getNodeValue() {  return this.m_entity;  }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}
}
