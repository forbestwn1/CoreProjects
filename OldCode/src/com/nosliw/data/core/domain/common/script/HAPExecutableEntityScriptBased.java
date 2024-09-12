package com.nosliw.data.core.domain.common.script;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.resource.HAPResourceId;

@HAPEntityWithAttribute
public interface HAPExecutableEntityScriptBased {

	@HAPAttribute
	public static final String SCRIPT = "script";

	@HAPAttribute
	public static final String RESOURCEID = "resourceId";


	void setScript(String script);

	void setResrouceId(HAPResourceId resourceId);
}
