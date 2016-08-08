package com.nosliw.entity.data;

import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;

public abstract class HAPBaseData extends HAPDataImp{

	private HAPEntityDefinitionManager m_entityDefMan;
	
	public HAPBaseData(HAPDataType dataType, HAPEntityDefinitionManager entityDefMan) {
		super(dataType);
		this.m_entityDefMan = entityDefMan;
	}
	
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){
		return this.m_entityDefMan;
	}

}
