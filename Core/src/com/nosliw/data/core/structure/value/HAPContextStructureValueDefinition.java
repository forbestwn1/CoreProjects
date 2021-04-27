package com.nosliw.data.core.structure.value;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.structure.HAPRoot;

public interface HAPContextStructureValueDefinition extends HAPSerializable, HAPContextStructureValue{

	@HAPAttribute
	public static final String TYPE = "type";

	String getType();
	
	boolean isFlat();

	boolean isEmpty();
	
	HAPRoot getElement(String eleName, boolean createIfNotExist);

	void updateRootName(HAPUpdateName nameUpdate);
	
	HAPContextStructureValueDefinition cloneContextStructure();

	void hardMergeWith(HAPContextStructureValueDefinition context);

}
