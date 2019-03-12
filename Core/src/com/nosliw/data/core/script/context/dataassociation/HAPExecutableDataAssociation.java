package com.nosliw.data.core.script.context.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.runtime.HAPExecutable;

@HAPEntityWithAttribute
public interface HAPExecutableDataAssociation extends HAPExecutable, HAPSerializable{

	@HAPAttribute
	public static String TYPE = "type";

	String getType();

	HAPOutputStructure getOutput();
}
