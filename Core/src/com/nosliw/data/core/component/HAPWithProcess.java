package com.nosliw.data.core.component;

import com.nosliw.data.core.process.HAPDefinitionProcessSuite;

public interface HAPWithProcess {

	HAPDefinitionProcessSuite getProcessSuite();

	void setProcessSuite(HAPDefinitionProcessSuite processSuite);
}
