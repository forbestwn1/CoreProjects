package com.nosliw.test.process;

import java.io.FileNotFoundException;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.runtimeenv.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.core.xxx.application1.common.structure.data.HAPContextDataFlat;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPUtilityProcess;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteProcessRhino;

public class HAPProcessMain {

	static public void main(String[] args) throws FileNotFoundException {
		String suite = "expression";
		String id = "main";
		String testData = "testData2";
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPExecutableProcess processExe = runtimeEnvironment.getProcessManager().getProcess(HAPUtilityProcess.buildResourceId(suite, id), null);
		
		HAPContextDataFlat input = HAPUtilityAttachment.getTestDataFromAttachment(processExe.getTask(), testData);
		
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input, runtimeEnvironment.getResourceManager());
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
