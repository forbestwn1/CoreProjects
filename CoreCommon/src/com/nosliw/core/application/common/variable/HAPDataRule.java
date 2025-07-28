package com.nosliw.core.application.common.variable;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public interface HAPDataRule extends HAPEntityInfo{

	@HAPAttribute
	public static String RULETYPE = "ruleType";

	@HAPAttribute
	public static String PATH = "path";

	String getRuleType();
	
	String getPath();
	
	void setPath(String path);
	
	//apply the rule and verify if the data is valid
	HAPServiceData verify(HAPData data, HAPRuntimeEnvironment runtimeEnv);
	
	void process(HAPDataTypeCriteria criteria, HAPRuntimeEnvironment runtimeEnv);

	HAPDataRule cloneDataRule();
}
