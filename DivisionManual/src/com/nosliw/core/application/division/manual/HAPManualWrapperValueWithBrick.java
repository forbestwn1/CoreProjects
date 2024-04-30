package com.nosliw.core.application.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPManualWrapperValueWithBrick extends HAPManualWrapperValue implements HAPManualWithBrick{

	private HAPManualBrick m_brick;
	
	private HAPIdBrickType m_brickTypeId;

	public HAPManualWrapperValueWithBrick(String valueType, HAPIdBrickType brickTypeId) {
		super(valueType);
		this.m_brickTypeId = brickTypeId;
	}
	
	@Override
	public HAPManualBrick getBrick() {	return this.m_brick;	}
	public void setBrick(HAPManualBrick brick) {    this.m_brick = brick;    }
	
	@Override
	public HAPIdBrickType getBrickTypeId() {   return this.m_brickTypeId;    }
	public void setEntityTypeId(HAPIdBrickType entityTypeId) {    this.m_brickTypeId = entityTypeId;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_brick!=null) {
			jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(BRICKTYPEID, this.m_brickTypeId.toStringValue(HAPSerializationFormat.JSON));
	}
}
