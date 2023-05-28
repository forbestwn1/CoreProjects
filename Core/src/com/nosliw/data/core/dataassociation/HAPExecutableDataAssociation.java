package com.nosliw.data.core.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.runtime.HAPExecutable;

@HAPEntityWithAttribute
public interface HAPExecutableDataAssociation extends HAPExecutable, HAPEntityInfo{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String DIRECTION = "direction";

	String getType();

	String getDireciton();
	
}
