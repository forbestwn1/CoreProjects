package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPExecutorStep {

	HAPResultStep execute(HAPExecutableStep step, Map<String, HAPData> parms, Map<String, HAPData> referencedData);
}
