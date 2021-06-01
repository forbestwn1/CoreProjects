package com.nosliw.data.core.valuestructure;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPValueStructureDefinition extends HAPSerializable, HAPValueStructure{

	boolean isFlat();

	boolean isEmpty();
	
	@Override
	void updateRootName(HAPUpdateName nameUpdate);
	
	void hardMergeWith(HAPValueStructureDefinition context);

}
