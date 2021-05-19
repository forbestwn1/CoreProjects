package com.nosliw.data.core.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRoot;

public class HAPValueStructureDefinitionEmpty extends HAPSerializableImp implements HAPValueStructureDefinition{

	private boolean m_isFlat;
	
	private HAPValueStructureDefinitionEmpty(boolean isFlat) {
		this.m_isFlat = isFlat;
	}
	
	public static HAPValueStructureDefinitionEmpty flatStructure() {   return new HAPValueStructureDefinitionEmpty(true);  }
	public static HAPValueStructureDefinitionEmpty notFlatStructure() {   return new HAPValueStructureDefinitionEmpty(false);  }

	public HAPValueStructureDefinitionEmpty oppositeFlatStructure() {return new HAPValueStructureDefinitionEmpty(!this.m_isFlat);  }
	
	@Override
	public String getStructureType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY;	}

	@Override
	public boolean isFlat() {  return this.m_isFlat;  }

	@Override
	public boolean isEmpty() {   return true;   }

	@Override
	public HAPRoot getRoot(String rootId) {	return null;	}

	@Override
	public List<HAPRoot> getAllRoots(){   return new ArrayList<HAPRoot>();      }

	@Override
	public HAPValueStructureDefinition cloneStructure() {
		return  new HAPValueStructureDefinitionEmpty(this.m_isFlat);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getStructureType());
	}

	@Override
	public void hardMergeWith(HAPValueStructureDefinition context) {
	}

	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		return new ArrayList<HAPRoot>();
	}

	@Override
	public List<HAPInfoAlias> discoverRootAliasById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPRoot addRoot(HAPReferenceRoot rootReference, HAPRoot root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

}
