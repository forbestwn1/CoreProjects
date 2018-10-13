package com.nosliw.data.core.task;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.script.context.HAPContextGroup;

//application that contains multiple tasks
public class HAPDefinitionTaskSuite extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATACONTEXT = "context";

	@HAPAttribute
	public static String TASK = "task";

	private String m_name;
	
	private String m_description;
	
	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_dataContext;

	private List<HAPDefinitionTask> m_tasks;
	
	private HAPManagerTask m_taskMan;
	
	public HAPDefinitionTaskSuite(HAPManagerTask taskManager) {
		this.m_taskMan = taskManager;
	}
	
}
