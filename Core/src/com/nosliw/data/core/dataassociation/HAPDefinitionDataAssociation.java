package com.nosliw.data.core.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPDefinitionDataAssociation extends HAPEntityInfo, HAPSerializable{

	@HAPAttribute
	public static String TYPE = "type";

	String getType();

	HAPDefinitionDataAssociation cloneDataAssocation();

}
