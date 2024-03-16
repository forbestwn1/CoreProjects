package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickComplex;

public abstract class HAPPluginProcessorEntityDefinitionComplexImp implements HAPPluginProcessorEntityDefinitionComplex{

	private Class<? extends HAPBrickComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginProcessorEntityDefinitionComplexImp(String entityType, Class<? extends HAPBrickComplex> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }


	@Override
	public void processEntity(HAPPath pathFromRoot, HAPContextProcess processContext) {}

	
	@Override
	public void extendConstantValue(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}
	
	//process definition before value context
	@Override
	public void processValueContext(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}
	@Override
	public void postProcessValueContext(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}
	@Override
	public void postProcessValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {}
	
	@Override
	public void processEntity(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {	}
	@Override
	public void postProcessEntity(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext) {	}

//	protected HAPManualEntity getEntityDefinition(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
//		return processContext.getCurrentBundle().getDefinitionDomain().getEntityInfoDefinition(entityExe.getDefinitionEntityId()).getEntity();
//	}
//	
//	public Pair<HAPManualEntityComplex,HAPExecutableEntityComplex> getEntityPair(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext) {
//		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
//		HAPManualEntityComplex entityDef = (HAPManualEntityComplex)currentBundle.getDefinitionDomain().getEntityInfoDefinition(currentBundle.getDefinitionEntityIdByExecutableEntityId(exeEntityId)).getEntity();
//		HAPExecutableEntityComplex entityExe = currentBundle.getExecutableDomain().getEntityInfoExecutable(exeEntityId).getEntity();
//		return Pair.of(entityDef, entityExe);
//	}
	
	@Override
	public HAPBrickComplex newExecutable() {
		HAPBrickComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
//			out.setEntityType(this.getEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
}
