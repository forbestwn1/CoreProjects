package com.nosliw.data.core.valuestructure;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPValueStructureDefinition extends HAPSerializable, HAPValueStructure{

	@HAPAttribute
	public static final String TYPE = "type";

	boolean isFlat();

	boolean isEmpty();
	
	@Override
	void updateRootName(HAPUpdateName nameUpdate);
	
	void hardMergeWith(HAPValueStructureDefinition context);

}
