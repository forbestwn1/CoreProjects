package com.nosliw.data.core.component;

import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcessSuite;

public interface HAPWithProcess {

	HAPResourceDefinitionProcessSuite getProcessSuite();

	void setProcessSuite(HAPResourceDefinitionProcessSuite processSuite);
}
