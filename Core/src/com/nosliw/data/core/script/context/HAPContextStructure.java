package com.nosliw.data.core.script.context;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPContextStructure extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type";

	String getType();
	
	boolean isFlat();

	boolean isEmpty();
	
	HAPContextDefinitionRoot getElement(String eleName, boolean createIfNotExist);
	
	HAPContextStructure cloneContextStructure();

	void hardMergeWith(HAPContextStructure context);

}
