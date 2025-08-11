package com.nosliw.core.application.common.datadefinition;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPDefinitionResult extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATADEFINITION = "dataDefinition";

	private HAPDataDefinitionReadonly m_dataDefinition;
	
	
	
	public HAPDataDefinitionReadonly getDataDefinition() {		return this.m_dataDefinition;	}

}
