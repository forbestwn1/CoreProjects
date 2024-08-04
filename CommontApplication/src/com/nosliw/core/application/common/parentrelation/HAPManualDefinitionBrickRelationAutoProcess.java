package com.nosliw.core.application.common.parentrelation;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDefinitionBrickRelationAutoProcess extends HAPManualDefinitionBrickRelation{

	public static String AUTOPROCESS = "autoProcess";
	
	private boolean m_autoProcess;
	
	public HAPManualDefinitionBrickRelationAutoProcess() {
		super(HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
	}
	
	public boolean isAutoProcess() {   return this.m_autoProcess;   }
}
