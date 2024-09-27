package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualWithBrick;

public class HAPManualDefinitionWrapperBrickRoot extends HAPEntityInfoImp implements HAPManualWithBrick, HAPTreeNode{

	public static final String INFO = "info";

	private HAPManualDefinitionBrick m_brick;

	public HAPManualDefinitionWrapperBrickRoot() {	}

	public HAPManualDefinitionWrapperBrickRoot(HAPManualDefinitionBrick brick) {
		this.m_brick = brick;
	}
	
	@Override
	public HAPManualDefinitionBrick getBrick() {    return this.m_brick;    }
	public void setBrick(HAPManualDefinitionBrick brick) {    
		this.m_brick = brick;     
	}

	@Override
	public HAPIdBrickType getBrickTypeId() {   return this.getBrick().getBrickTypeId();   }

	
	@Override
	public HAPPath getPathFromRoot() {  return null;  }

	@Override
	public Object getNodeValue() {  return this.m_brick;  }

	public HAPManualDefinitionWrapperBrickRoot cloneBrickWrapper() {
		HAPManualDefinitionWrapperBrickRoot out = new HAPManualDefinitionWrapperBrickRoot();
		this.cloneToEntityInfo(out);
		out.m_brick = this.m_brick;
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JSON));
	}
}
