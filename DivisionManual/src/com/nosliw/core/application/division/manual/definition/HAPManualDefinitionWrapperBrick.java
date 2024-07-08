package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualWithBrick;

public class HAPManualDefinitionWrapperBrick extends HAPEntityInfoImp implements HAPManualWithBrick, HAPTreeNode{

	public static final String INFO = "info";

	private HAPManualDefinitionBrick m_entity;

	public HAPManualDefinitionWrapperBrick() {	}

	@Override
	public HAPManualDefinitionBrick getBrick() {    return this.m_entity;    }
	public void setEntity(HAPManualDefinitionBrick entity) {    
		this.m_entity = entity;     
	}

	@Override
	public HAPIdBrickType getBrickTypeId() {   return this.getBrick().getBrickTypeId();   }

	
	@Override
	public HAPPath getPathFromRoot() {  return null;  }

	@Override
	public Object getNodeValue() {  return this.m_entity;  }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}
}
