package com.nosliw.data.core.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPStructure;

public class HAPValueStructureDefinitionEmpty extends HAPValueStructureDefinitionImp{

	private boolean m_isFlat;
	
	public HAPValueStructureDefinitionEmpty() {}
	
	private HAPValueStructureDefinitionEmpty(boolean isFlat) {
		this.m_isFlat = isFlat;
	}
	
	public static HAPValueStructureDefinitionEmpty flatStructure() {   return new HAPValueStructureDefinitionEmpty(true);  }
	public static HAPValueStructureDefinitionEmpty notFlatStructure() {   return new HAPValueStructureDefinitionEmpty(false);  }

	public HAPValueStructureDefinitionEmpty oppositeFlatStructure() {return new HAPValueStructureDefinitionEmpty(!this.m_isFlat);  }
	
	@Override
	public String getStructureType() {	return HAPConstantShared.STRUCTURE_TYPE_VALUEEMPTY;	}

	@Override
	public boolean isFlat() {  return this.m_isFlat;  }

	@Override
	public boolean isEmpty() {   return true;   }

	@Override
	public HAPRootStructure getRoot(String rootId) {	return null;	}

	@Override
	public List<HAPRootStructure> getAllRoots(){   return new ArrayList<HAPRootStructure>();      }

	@Override
	public HAPStructure cloneStructure() {   return this.cloneValueStructure(); }

	@Override
	public HAPValueStructure cloneValueStructure() {		return  new HAPValueStructureDefinitionEmpty(this.m_isFlat);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getStructureType());
	}

	//whether root is inherited by child
	@Override
	public boolean isInheriable(String rootId) {   return true;	}

	@Override
	public void hardMergeWith(HAPValueStructure context) {
	}

	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPRootStructure> resolveRoot(HAPReferenceRootInStrucutre rootReference, boolean createIfNotExist) {
		return new ArrayList<HAPRootStructure>();
	}

	@Override
	public List<HAPInfoAlias> discoverRootAliasById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPRootStructure addRoot(HAPReferenceRootInStrucutre rootReference, HAPRootStructure root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPReferenceRootInStrucutre getRootReferenceById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExternalVisible(String rootId) {
		// TODO Auto-generated method stub
		return false;
	}

}
