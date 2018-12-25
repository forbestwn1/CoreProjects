package com.nosliw.data.core.process;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPDefinitionDataAssociationGroup extends HAPContext{

	private HAPInfo m_info;
	
	
	public HAPDefinitionDataAssociationGroup() {
		this.m_info = new HAPInfoImpSimple();
	}
	
	public HAPInfo getInfo() {   return this.m_info;   }
	
}
