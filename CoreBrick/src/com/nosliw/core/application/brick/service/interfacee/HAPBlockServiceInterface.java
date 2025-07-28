package com.nosliw.core.application.brick.service.interfacee;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;

//static information for a service. readable, query for service
//information needed during configuration time
@HAPEntityWithAttribute
public interface HAPBlockServiceInterface extends HAPBrick, HAPEntityInfo{

	public static final String CHILD_INTERFACE = "interface";

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String DISPLAY = "display";

	HAPBlockInteractiveInterfaceTask getTaskInteractiveInterface();
	
	List<String> getTags();
	
	HAPDisplayResourceNode getDisplayResource();

}
