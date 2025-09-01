package com.nosliw.core.application.common.bricktypecriteria;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPCriteriaBrickType extends HAPSerializableImp{

	//brick type criteria
	private HAPIdBrickType m_brickTypeId;
	
	//restrain
	private List<HAPRestrainBrickType> m_restrains;
	
	
	
	
}
