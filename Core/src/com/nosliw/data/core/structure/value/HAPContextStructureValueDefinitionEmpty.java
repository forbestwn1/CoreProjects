package com.nosliw.data.core.structure.value;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPRoot;

public class HAPContextStructureValueDefinitionEmpty extends HAPSerializableImp implements HAPContextStructureValueDefinition{

	private boolean m_isFlat;
	
	private HAPContextStructureValueDefinitionEmpty(boolean isFlat) {
		this.m_isFlat = isFlat;
	}
	
	public static HAPContextStructureValueDefinitionEmpty flatStructure() {   return new HAPContextStructureValueDefinitionEmpty(true);  }
	public static HAPContextStructureValueDefinitionEmpty notFlatStructure() {   return new HAPContextStructureValueDefinitionEmpty(false);  }

	public HAPContextStructureValueDefinitionEmpty oppositeFlatStructure() {return new HAPContextStructureValueDefinitionEmpty(!this.m_isFlat);  }
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY;	}

	@Override
	public boolean isFlat() {  return this.m_isFlat;  }

	@Override
	public boolean isEmpty() {   return true;   }

	@Override
	public HAPRoot getElement(String eleName, boolean createIfNotExist) {	return null;	}

	@Override
	public HAPContextStructureValueDefinition cloneContextStructure() {
		return  new HAPContextStructureValueDefinitionEmpty(this.m_isFlat);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}

	@Override
	public void hardMergeWith(HAPContextStructureValueDefinition context) {
	}

	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		// TODO Auto-generated method stub
		
	}

}
