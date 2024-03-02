package com.nosliw.data.core.domain.entity;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualInfoAttributeValue;

//attribute definition
public class HAPDefinitionEmbededValue extends HAPSerializableImp{

	//parent info definition
	public static final String PARENT = "parent";
	
	private HAPManualInfoAttributeValue m_valueInfo;
	
	private HAPPath m_pathFromRoot;

	private HAPManualEntity m_parent;
	
	
}
