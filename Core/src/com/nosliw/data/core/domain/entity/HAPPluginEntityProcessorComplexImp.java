package com.nosliw.data.core.domain.entity;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public abstract class HAPPluginEntityProcessorComplexImp implements HAPPluginEntityProcessorComplex{

	private Class<? extends HAPExecutableEntityComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginEntityProcessorComplexImp(String entityType, Class<? extends HAPExecutableEntityComplex> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }

	@Override
	public void extendConstantValue(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}
	
	//process definition before value context
	@Override
	public void processValueContext(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}
	@Override
	public void postProcessValueContext(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}
	@Override
	public void postProcessValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {}
	
	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {	}
	@Override
	public void postProcessEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {	}

	protected HAPDefinitionEntityInDomain getEntityDefinition(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
		return processContext.getCurrentBundle().getDefinitionDomain().getEntityInfoDefinition(entityExe.getDefinitionEntityId()).getEntity();
	}
	
	public Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> getEntityPair(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDefinitionEntityInDomainComplex entityDef = (HAPDefinitionEntityInDomainComplex)currentBundle.getDefinitionDomain().getEntityInfoDefinition(currentBundle.getDefinitionEntityIdByExecutableEntityId(exeEntityId)).getEntity();
		HAPExecutableEntityComplex entityExe = currentBundle.getExecutableDomain().getEntityInfoExecutable(exeEntityId).getEntity();
		return Pair.of(entityDef, entityExe);
	}
	
	@Override
	public HAPExecutableEntityComplex newExecutable() {
		HAPExecutableEntityComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
			out.setEntityType(this.getEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
}
