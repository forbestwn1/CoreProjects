package com.nosliw.data.core.structure.value;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.structure.HAPRoot;

public interface HAPStructureValueDefinition extends HAPSerializable, HAPStructureValue{

	@HAPAttribute
	public static final String TYPE = "type";

	String getType();
	
	boolean isFlat();

	boolean isEmpty();
	
	@Override
	HAPRoot getRoot(String id);

	void updateRootName(HAPUpdateName nameUpdate);
	
	void hardMergeWith(HAPStructureValueDefinition context);

}
