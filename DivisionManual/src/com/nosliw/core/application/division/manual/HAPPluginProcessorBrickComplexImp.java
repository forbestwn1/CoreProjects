package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBlockComplex;
import com.nosliw.core.application.HAPIdBrickType;

public abstract class HAPPluginProcessorBrickComplexImp implements HAPPluginProcessorBrickComplex{

	private Class<? extends HAPBlockComplex> m_exeBrickClass;
	
	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginProcessorBrickComplexImp(HAPIdBrickType entityType, Class<? extends HAPBlockComplex> exeBrickClass) {
		this.m_brickTypeId = entityType;
		this.m_exeBrickClass = exeBrickClass;
	}

	@Override
	public HAPIdBrickType getBrickType() {    return this.m_brickTypeId;    }


	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	
	@Override
	public void extendConstantValue(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	//process definition before value context
	@Override
	public void processValueContext(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContext(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContextExtension(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	@Override
	public void processEntity(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}
	@Override
	public void postProcessEntity(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}

//	protected HAPManualBrick getEntityDefinition(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
//		return processContext.getCurrentBundle().getDefinitionDomain().getEntityInfoDefinition(entityExe.getDefinitionEntityId()).getEntity();
//	}
//	
//	public Pair<HAPManualBrickComplex,HAPExecutableEntityComplex> getEntityPair(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext) {
//		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
//		HAPManualBrickComplex entityDef = (HAPManualBrickComplex)currentBundle.getDefinitionDomain().getEntityInfoDefinition(currentBundle.getDefinitionEntityIdByExecutableEntityId(exeEntityId)).getEntity();
//		HAPExecutableEntityComplex entityExe = currentBundle.getExecutableDomain().getEntityInfoExecutable(exeEntityId).getEntity();
//		return Pair.of(entityDef, entityExe);
//	}
}
