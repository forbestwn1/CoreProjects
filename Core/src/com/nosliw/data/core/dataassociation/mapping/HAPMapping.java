package com.nosliw.data.core.dataassociation.mapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPStructure;

public class HAPMapping implements HAPStructure{

	@HAPAttribute
	public static final String MAPPING = "mapping";
	
	private Map<String, HAPRootStructure> m_rootById;

	
	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStructureType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPRootStructure getRoot(String localId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPRootStructure> getAllRoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPRootStructure> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPInfoAlias> discoverRootAliasById(String id) {
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

}
