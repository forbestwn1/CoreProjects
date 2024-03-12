package com.nosliw.data.core.entity.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.entity.HAPEntityExecutableComplex;

public abstract class HAPPluginProcessorEntityDefinitionComplexImp implements HAPPluginProcessorEntityDefinitionComplex{

	private Class<? extends HAPEntityExecutableComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginProcessorEntityDefinitionComplexImp(String entityType, Class<? extends HAPEntityExecutableComplex> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }

	@Override
	public void extendConstantValue(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}
	
	//process definition before value context
	@Override
	public void processValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}
	@Override
	public void postProcessValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}
	@Override
	public void postProcessValueContextExtension(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {}
	
	@Override
	public void processEntity(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {	}
	@Override
	public void postProcessEntity(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {	}

	protected HAPManualEntity getEntityDefinition(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
		return processContext.getCurrentBundle().getDefinitionDomain().getEntityInfoDefinition(entityExe.getDefinitionEntityId()).getEntity();
	}
	
	public Pair<HAPManualEntityComplex,HAPExecutableEntityComplex> getEntityPair(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPManualEntityComplex entityDef = (HAPManualEntityComplex)currentBundle.getDefinitionDomain().getEntityInfoDefinition(currentBundle.getDefinitionEntityIdByExecutableEntityId(exeEntityId)).getEntity();
		HAPExecutableEntityComplex entityExe = currentBundle.getExecutableDomain().getEntityInfoExecutable(exeEntityId).getEntity();
		return Pair.of(entityDef, entityExe);
	}
	
	@Override
	public HAPEntityExecutableComplex newExecutable() {
		HAPEntityExecutableComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
//			out.setEntityType(this.getEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
}
