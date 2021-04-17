package com.nosliw.data.core.script.expression;

import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithDataContext;
import com.nosliw.data.core.common.HAPWithEntityElement;

public interface HAPDefinitionScriptGroup extends  
		HAPWithEntityElement<HAPDefinitionScriptEntity>,
		HAPEntityInfoWritable, 
		HAPWithDataContext, 
		HAPWithConstantDefinition{

}
