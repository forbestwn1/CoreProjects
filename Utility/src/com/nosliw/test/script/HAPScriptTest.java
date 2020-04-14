package com.nosliw.test.script;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;
import com.nosliw.data.core.script.expression.resource.HAPUtilityScriptResource;

public class HAPScriptTest {

	public static void main(String[] args) {

		try {
			String id = "test1";
			String script = "test2";
			String testData = "testData1";

			HAPResourceId resourceId = HAPUtilityScriptResource.buildResourceId(id);
			
			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			HAPResourceDefinitionScriptGroup scriptGroupDef = runtimeEnvironment.getScriptManager().getScriptDefinition(resourceId, null);
			
			HAPExecutableScriptGroup scriptExe = runtimeEnvironment.getScriptManager().getScript(resourceId, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null));

			Map<String, Object> input = HAPAttachmentUtility.getTestValueFromAttachment(scriptGroupDef, testData);

			
			HAPRuntimeTaskExecuteScript task = new HAPRuntimeTaskExecuteScript(scriptExe, script, input, null);
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
