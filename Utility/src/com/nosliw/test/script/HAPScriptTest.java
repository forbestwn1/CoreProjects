package com.nosliw.test.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.value.HAPUtilityJsonValue;
import com.nosliw.data.core.domain.entity.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.task.HAPInfoRuntimeTaskScript;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;
import com.nosliw.data.core.script.expression.resource.HAPUtilityScriptResource;
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
			Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(scriptExe.getValueStructureDefinitionWrapper().getValueStructure(), input);
			
			Map<String, Object> varInput = new LinkedHashMap<String, Object>();
			for(String varName : scriptExe.discoverVariables()) {
				Object varValue = HAPUtilityJsonValue.getValue(inputById, new HAPComplexPath(varName));
				if(varValue!=null)   varInput.put(varName, varValue);					
			}

			HAPRuntimeTaskExecuteScript task = new HAPRuntimeTaskExecuteScript(new HAPInfoRuntimeTaskScript(scriptExe, script, varInput, null), runtimeEnvironment);
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
