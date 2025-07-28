package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.common.valueport.HAPWithBothsideValuePort;
import com.nosliw.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public interface HAPBrick extends HAPSerializable, HAPEntityOrReference, HAPWithBothsideValuePort, HAPWithResourceDependency{

	@HAPAttribute
	public final static String BRICKTYPE = "brickType"; 

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 

	HAPIdBrickType getBrickType();

	List<HAPAttributeInBrick> getAttributes();

}
