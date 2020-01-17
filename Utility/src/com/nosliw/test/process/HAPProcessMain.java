package com.nosliw.test.process;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.component.HAPAttachmentContainer;
import com.nosliw.data.core.component.HAPAttachmentEntity;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPProcessMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "expression";
		String id = "main";
		String testData = "testData2";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPDefinitionProcessSuite suiteObj = HAPUtilityProcess.getProcessSuite(suite, runtimeEnvironment.getProcessDefinitionManager().getPluginManager()); 
		
		HAPExecutableProcess processExe = HAPProcessorProcess.process(id, suiteObj, null, runtimeEnvironment.getProcessDefinitionManager(), new HAPRequirementContextProcessor(HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getServiceManager().getServiceDefinitionManager(), null), new HAPProcessTracker());

		Map<String, HAPData> input = new LinkedHashMap<String, HAPData>();
		if(testData!=null) {
			HAPAttachmentContainer attachmentContainer = processExe.getDefinition().getAttachmentContainer();
			HAPAttachmentEntity attachment = (HAPAttachmentEntity)attachmentContainer.getElement(HAPConstant.RUNTIME_RESOURCE_TYPE_DATA, testData);
			JSONObject testDataObj = attachment.getEntity();
			input = HAPDataUtility.buildDataWrapperMapFromJson(testDataObj);
		}
		
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
