package com.nosliw.data.core.task111.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task111.HAPLogTask;

public interface HAPExecutorStep {

	HAPResultStep execute(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms, Map<String, HAPData> referencedData, HAPLogTask taskLog);
}
