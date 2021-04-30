package com.nosliw.data.core.structure.value;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPRoot;

public class HAPStructureValueDefinitionEmpty extends HAPSerializableImp implements HAPStructureValueDefinition{

	private boolean m_isFlat;
	
	private HAPStructureValueDefinitionEmpty(boolean isFlat) {
		this.m_isFlat = isFlat;
	}
	
	public static HAPStructureValueDefinitionEmpty flatStructure() {   return new HAPStructureValueDefinitionEmpty(true);  }
	public static HAPStructureValueDefinitionEmpty notFlatStructure() {   return new HAPStructureValueDefinitionEmpty(false);  }

	public HAPStructureValueDefinitionEmpty oppositeFlatStructure() {return new HAPStructureValueDefinitionEmpty(!this.m_isFlat);  }
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY;	}

	@Override
	public boolean isFlat() {  return this.m_isFlat;  }

	@Override
	public boolean isEmpty() {   return true;   }

	@Override
	public HAPRoot getRoot(String eleName, boolean createIfNotExist) {	return null;	}

	@Override
	public HAPStructureValueDefinition cloneStructure() {
		return  new HAPStructureValueDefinitionEmpty(this.m_isFlat);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}

	@Override
	public void hardMergeWith(HAPStructureValueDefinition context) {
	}

	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		// TODO Auto-generated method stub
		
	}

}
