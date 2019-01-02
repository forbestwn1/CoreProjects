package com.nosliw.test.context;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;

public class HAPContextParser {

	public static void main(String[] args) {
		
		
		String contextContent = HAPFileUtility.readFile(HAPContextParser.class.getResourceAsStream("context.json"));
		HAPContextGroup contextGroup = HAPParserContext.parseContextGroup(new JSONObject(contextContent));
		System.out.println(contextGroup);
		
		HAPRuntimeEnvironmentImpRhino runtimeEnv = new HAPRuntimeEnvironmentImpRhino();
		HAPEnvContextProcessor processEvn = new HAPEnvContextProcessor(HAPExpressionManager.dataTypeHelper, runtimeEnv.getRuntime(), runtimeEnv.getExpressionSuiteManager(), null);
		
		HAPContextGroup contextGroup1 = HAPProcessorContext.processStatic(contextGroup, null, new HAPConfigureContextProcessor(), processEvn);
		System.out.println(contextGroup1);
		HAPContextGroup contextGroup2 = HAPProcessorContext.processRelative(contextGroup1, null, new HAPConfigureContextProcessor(), processEvn);

		runtimeEnv.getRuntime().close();
	
	}

}
