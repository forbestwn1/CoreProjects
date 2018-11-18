package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPVariableInfo;

public interface HAPProcessorActivity {

	/**
	 * Process expression definition
	 * 		parse it to operand
	 * 		solve constant
	 * 		solve reference
	 * 		discovery 
	 * @param id  the id assigned to expression
	 * @param expDef   expression definition need to process
	 * @param contextExpressionDefinitions   other expressions that may need during solving reference
	 * @param variableCriterias   variable criterias that need to respect during discovery
	 * @return
	 */
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			HAPExecutableProcess processExe,
			Map<String, HAPVariableInfo> variables,
			String name,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, 
			HAPProcessContext context
	);

	void postProcess(
			HAPExecutableActivity executableStep,
			HAPDefinitionActivity stepDefinition,
			HAPExecutableProcess taskExpressionExe,
			int index, String name,
			Map<String, HAPDefinitionProcess> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants,
			HAPProcessContext context
	);

}
