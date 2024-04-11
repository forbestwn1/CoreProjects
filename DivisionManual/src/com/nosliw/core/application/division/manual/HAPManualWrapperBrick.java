package com.nosliw.core.application.division.manual;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPManualWrapperBrick extends HAPEntityInfoImp implements HAPManualWithBrick, HAPTreeNode{

	public static final String INFO = "info";

	private HAPManualBrick m_entity;

	public HAPManualWrapperBrick() {	}

	@Override
	public HAPManualBrick getBrick() {    return this.m_entity;    }
	public void setEntity(HAPManualBrick entity) {    
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
