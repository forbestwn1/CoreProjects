package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;

public class HAPContextStructureEmpty extends HAPSerializableImp implements HAPContextStructure{

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
	public boolean isEmpty() {   return true;   }

	@Override
	public HAPContextDefinitionRoot getElement(String eleName) {	return null;	}

	@Override
	public HAPContextStructure cloneContextStructure() {
		return  new HAPContextStructureEmpty(this.m_isFlat);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}

	@Override
	public void hardMergeWith(HAPContextStructure context) {
	}

}
