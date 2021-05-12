package com.nosliw.data.core.valuestructure;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.structure.HAPRoot;

public interface HAPValueStructureDefinition extends HAPSerializable, HAPValueStructure{

	@HAPAttribute
	public static final String TYPE = "type";

	boolean isFlat();

	boolean isEmpty();
	
	@Override
	HAPRoot getRoot(String id);

	Set<String> getAliasesById(String id);
	
	@Override
	void updateRootName(HAPUpdateName nameUpdate);
	
	void hardMergeWith(HAPValueStructureDefinition context);

}
