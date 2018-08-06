package com.nosliw.data.core.flow;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

//application that contains multiple tasks
public class HAPDefinitionTaskSuite extends HAPDefinitionComponent{

	@HAPAttribute
	public static String TASK = "task";

	private List<HAPDefinitionTask> m_tasks;
	

}
