package com.nosliw.core.application.common.event;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.core.application.common.structure.HAPStructure;

public class HAPEventDefinition extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATADEFINITION = "dataDefinition";

	private HAPStructure m_dataDefinition;
	
	public HAPEventDefinition() {
		
	}
	
	public HAPStructure getDataDefinition() {    return this.m_dataDefinition;     }
    public void setDataDefinition(HAPStructure dataDef) {    this.m_dataDefinition = dataDef;      }	
	
}
