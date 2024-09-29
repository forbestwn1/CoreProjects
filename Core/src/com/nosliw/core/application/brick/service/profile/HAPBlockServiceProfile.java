package com.nosliw.core.application.brick.service.profile;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;

//contains all information related with service definition
@HAPEntityWithAttribute
public interface HAPBlockServiceProfile extends HAPWithBlockInteractiveTask, HAPEntityInfo{

	public static final String CHILD_INTERFACE = "interface";
	
	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String DISPLAY = "display";

	List<String> getTags();

	HAPDisplayResourceNode getDisplayResource();
	
}
