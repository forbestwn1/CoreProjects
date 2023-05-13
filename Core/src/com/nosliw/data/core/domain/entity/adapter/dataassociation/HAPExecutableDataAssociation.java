package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

@HAPEntityWithAttribute
public interface HAPExecutableDataAssociation extends HAPExecutable, HAPEntityInfo{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String DIRECTION = "direction";

	@HAPAttribute
	public static String INPUT = "input";

	@HAPAttribute
	public static String OUTPUT = "output";

	String getType();

	String getDireciton();
	
	HAPContainerStructure getInput();
	
	HAPOutputStructure getOutput();
	
}
