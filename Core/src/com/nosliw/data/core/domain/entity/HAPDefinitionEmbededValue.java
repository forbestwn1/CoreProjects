package com.nosliw.data.core.domain.entity;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualWrapperValueInAttribute;

//attribute definition
public class HAPDefinitionEmbededValue extends HAPSerializableImp{

	//parent info definition
	public static final String PARENT = "parent";
	
	private HAPManualWrapperValueInAttribute m_valueInfo;
	
	private HAPPath m_pathFromRoot;

	private HAPManualBrick m_parent;
	
	
}
