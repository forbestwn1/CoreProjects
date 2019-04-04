package com.nosliw.data.core.script.context;

import com.nosliw.common.utils.HAPConstant;

public class HAPContextStructureEmpty implements HAPContextStructure{

	private boolean m_isFlat;
	
	private HAPContextStructureEmpty(boolean isFlat) {
		this.m_isFlat = isFlat;
	}
	
	public static HAPContextStructureEmpty flatStructure() {   return new HAPContextStructureEmpty(true);  }
	public static HAPContextStructureEmpty notFlatStructure() {   return new HAPContextStructureEmpty(false);  }

	public HAPContextStructureEmpty oppositeFlatStructure() {return new HAPContextStructureEmpty(!this.m_isFlat);  }
	
	@Override
	public String getType() {	return HAPConstant.CONTEXTSTRUCTURE_TYPE_EMPTY;	}

	@Override
	public boolean isFlat() {  return this.m_isFlat;  }

	@Override
	public HAPContextDefinitionRoot getElement(String eleName) {	return null;	}

	@Override
	public HAPContextStructure cloneContextStructure() {
		return  new HAPContextStructureEmpty(this.m_isFlat);
	}

}
