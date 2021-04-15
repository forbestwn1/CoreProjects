package com.nosliw.data.core.script.context;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPContextStructure extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type";

	String getType();
	
	boolean isFlat();

	boolean isEmpty();
	
	HAPContextDefinitionRoot getElement(String eleName, boolean createIfNotExist);

	void updateRootName(HAPUpdateName nameUpdate);
	
	HAPContextStructure cloneContextStructure();

	void hardMergeWith(HAPContextStructure context);

}
