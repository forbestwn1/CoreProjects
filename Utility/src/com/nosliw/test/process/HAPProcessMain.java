package com.nosliw.test.process;

import java.io.FileNotFoundException;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;
import com.nosliw.data.core.script.context.data.HAPContextDataFlat;

public class HAPProcessMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "expression";
		String id = "main";
		String testData = "testData2";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPExecutableProcess processExe = runtimeEnvironment.getProcessManager().getProcess(HAPUtilityProcess.buildResourceId(suite, id), null);
		
		HAPContextDataFlat input = HAPAttachmentUtility.getTestDataFromAttachment(processExe.getDefinition(), testData);
		
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input, runtimeEnvironment.getResourceManager());
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
