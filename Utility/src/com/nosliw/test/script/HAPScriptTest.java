package com.nosliw.test.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.value.HAPUtilityJsonValue;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskScriptExpressionGroup;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoScriptExpressionGroup;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression1.resource.HAPResourceDefinitionScriptGroup;
import com.nosliw.data.core.script.expression1.resource.HAPUtilityScriptResource;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;

public class HAPScriptTest {

	public static void main(String[] args) {

		try {
			String id = "test1";
			String script = "test1";
			String testData = "testData1";

			HAPResourceId resourceId = HAPUtilityScriptResource.buildResourceId(id);
			
			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			HAPResourceDefinitionScriptGroup scriptGroupDef = runtimeEnvironment.getScriptManager().getScriptDefinition(resourceId, null);
			
			HAPExecutableScriptGroup scriptExe = runtimeEnvironment.getScriptManager().getScript(resourceId, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null));

			Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(scriptGroupDef, testData);
			Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(scriptExe.getValueStructureDefinitionWrapper().getValueStructureBlock(), input);
			
			Map<String, Object> varInput = new LinkedHashMap<String, Object>();
			for(String varName : scriptExe.discoverVariables()) {
				Object varValue = HAPUtilityJsonValue.getValue(inputById, new HAPComplexPath(varName));
				if(varValue!=null)   varInput.put(varName, varValue);					
			}

			HAPRuntimeTaskExecuteRhinoScriptExpressionGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionGroup(new HAPInfoRuntimeTaskScriptExpressionGroup(scriptExe, script, varInput, null), runtimeEnvironment);
			HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

			System.out.println(out);

//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
		catch(Throwable e) {
			e.printStackTrace();
//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
	}
}
