package com.nosliw.core.application.brick.service.interfacee;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.division.manual.executable.HAPBrickWithEntityInfoSimple;

@HAPEntityWithAttribute
public class HAPBrickServiceInterface1 extends HAPBrickWithEntityInfoSimple{

	@HAPAttribute
	public static String INTERACTIVEINTERFACE = "interactiveInterface";


	public HAPBlockInteractiveInterfaceTask getInterface() {}
	
	
//	@Override
//	public HAPDefinitionInteractive cloneInteractiveDefinition() {
//		HAPBrickServiceInterface1 out = new HAPBrickServiceInterface1();
//		this.cloneToInteractive(out);
//		return out;
//	}

}
