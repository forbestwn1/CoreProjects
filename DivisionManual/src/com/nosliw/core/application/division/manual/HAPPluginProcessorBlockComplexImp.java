package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.HAPIdBrickType;

public abstract class HAPPluginProcessorBlockComplexImp implements HAPPluginProcessorBlockComplex{

	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginProcessorBlockComplexImp(HAPIdBrickType entityType) {
		this.m_brickTypeId = entityType;
	}

	@Override
	public HAPIdBrickType getBrickType() {    return this.m_brickTypeId;    }


	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	
	@Override
	public void extendConstantValue(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	//process definition before value context
	@Override
	public void processValueContext(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContext(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContextExtension(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	@Override
	public void processEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}
	@Override
	public void postProcessEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}

//	protected HAPManualBrick getEntityDefinition(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
//		return processContext.getCurrentBundle().getDefinitionDomain().getEntityInfoDefinition(entityExe.getDefinitionEntityId()).getEntity();
//	}
//	
//	public Pair<HAPManualBrickBlockComplex,HAPExecutableEntityComplex> getEntityPair(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext) {
//		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
//		HAPManualBrickBlockComplex entityDef = (HAPManualBrickBlockComplex)currentBundle.getDefinitionDomain().getEntityInfoDefinition(currentBundle.getDefinitionEntityIdByExecutableEntityId(exeEntityId)).getEntity();
//		HAPExecutableEntityComplex entityExe = currentBundle.getExecutableDomain().getEntityInfoExecutable(exeEntityId).getEntity();
//		return Pair.of(entityDef, entityExe);
//	}
}
