package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickComplex;
import com.nosliw.core.application.HAPIdBrickType;

public abstract class HAPPluginProcessorBrickComplexImp implements HAPPluginProcessorBrickComplex{

	private Class<? extends HAPBrickComplex> m_exeBrickClass;
	
	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginProcessorBrickComplexImp(HAPIdBrickType entityType, Class<? extends HAPBrickComplex> exeBrickClass) {
		this.m_brickTypeId = entityType;
		this.m_exeBrickClass = exeBrickClass;
	}

	@Override
	public HAPIdBrickType getBrickType() {    return this.m_brickTypeId;    }


	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcess processContext) {}

	
	@Override
	public void extendConstantValue(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}
	
	//process definition before value context
	@Override
	public void processValueContext(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}
	@Override
	public void postProcessValueContext(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}
	@Override
	public void postProcessValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {}
	
	@Override
	public void processEntity(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {	}
	@Override
	public void postProcessEntity(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext) {	}

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
