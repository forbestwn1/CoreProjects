package com.nosliw.core.application.brick.task.script.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;

@HAPEntityWithAttribute
public interface HAPBlockTaskTaskScript extends HAPBrick, HAPWithBlockInteractiveTask, HAPWithScriptReference{

	@HAPAttribute
	public static String EXTRA = "extra";
	
	Object getExtra();
	
}

