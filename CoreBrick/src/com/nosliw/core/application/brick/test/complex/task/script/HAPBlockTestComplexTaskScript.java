package com.nosliw.core.application.brick.test.complex.task.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugExecutable;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;

@HAPEntityWithAttribute
public interface HAPBlockTestComplexTaskScript extends HAPBrick, HAPWithBlockInteractiveTask, HAPWithBlockInteractiveExpression, HAPWithScriptReference, HAPWithVariableDebugExecutable{

	@HAPAttribute
	public static String PARM = "parm";

	Map<String, Object> getParms();


}
