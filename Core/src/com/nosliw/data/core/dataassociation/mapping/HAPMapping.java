package com.nosliw.data.core.dataassociation.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPStructure;

public class HAPMapping extends HAPSerializableImp implements HAPStructure{

	@HAPAttribute
	public static final String MAPPING = "mapping";
	
	private Map<String, HAPRootStructure> m_rootById;

	@Override
	public String getStructureType() {  return HAPConstantShared.STRUCTURE_TYPE_MAPPING; }

	@Override
	public List<HAPRootStructure> getAllRoots() {   return new ArrayList<HAPRootStructure>(this.m_rootById.values());      } 
	
	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPRootStructure getRoot(String localId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPRootStructure> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPRootStructure addRoot(HAPReferenceRoot rootReference, HAPRootStructure root) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRootName(HAPUpdateName updateName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPStructure cloneStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPInfoAlias> discoverRootAliasById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPReferenceRoot getRootReferenceById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPInfo getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(HAPInfo info) {
		// TODO Auto-generated method stub
		
	}

}
