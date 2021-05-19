package com.nosliw.data.core.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

@HAPEntityWithAttribute
public interface HAPExecutableDataAssociation extends HAPExecutable, HAPEntityInfo{

	@HAPAttribute
	public static String TYPE = "type";

//	@HAPAttribute
//	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String INPUT = "input";

	@HAPAttribute
	public static String OUTPUT = "output";

	String getType();

//	HAPDefinitionDataAssociation getDefinition();

	HAPContainerStructure getInput();
	
	HAPOutputStructure getOutput();
	
}
