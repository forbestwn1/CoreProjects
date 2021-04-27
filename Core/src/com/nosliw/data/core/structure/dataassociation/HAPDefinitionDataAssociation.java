package com.nosliw.data.core.structure.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;

public interface HAPDefinitionDataAssociation extends HAPEntityInfo, HAPSerializable{

	@HAPAttribute
	public static String TYPE = "type";

	String getType();

	void updateInputVarName(HAPUpdateName updateName);
	
	void updateOutputVarName(HAPUpdateName updateName);
	
	HAPDefinitionDataAssociation cloneDataAssocation();

}
