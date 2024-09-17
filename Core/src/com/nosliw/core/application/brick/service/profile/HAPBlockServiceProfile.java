package com.nosliw.core.application.brick.service.profile;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPBrick;

//contains all information related with service definition
@HAPEntityWithAttribute
public interface HAPBlockServiceProfile extends HAPBrick, HAPEntityInfo{

	public static final String CHILD_INTERFACE = "interface";
	
	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";

	HAPEntityOrReference getServiceInterface();
	
	List<String> getTags();
	
}
