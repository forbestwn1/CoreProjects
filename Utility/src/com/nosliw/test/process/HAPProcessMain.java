package com.nosliw.test.process;

import java.io.FileNotFoundException;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;
import com.nosliw.data.core.script.context.data.HAPContextDataFactory;
import com.nosliw.data.core.script.context.data.HAPContextDataFlat;

public class HAPProcessMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "expression";
		String id = "main";
		String testData = "testData2";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPExecutableProcess processExe = runtimeEnvironment.getProcessManager().getProcess(HAPUtilityProcess.buildResourceId(suite, id), null);
		
		HAPContextDataFlat input = new HAPContextDataFlat();;
		if(testData!=null) {
			HAPAttachmentContainer attachmentContainer = processExe.getDefinition().getAttachmentContainer();
			HAPAttachmentEntity attachment = (HAPAttachmentEntity)attachmentContainer.getElement(HAPConstant.RUNTIME_RESOURCE_TYPE_DATA, testData);
			JSONObject testDataObj = attachment.getEntity();
			input = HAPContextDataFactory.newContextDataFlat(testDataObj);
		}
		
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
