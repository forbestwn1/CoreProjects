package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;

public abstract class HAPPluginEntityProcessorSimpleImp implements HAPPluginEntityProcessorSimple{

	private Class<? extends HAPExecutableEntity> m_exeEntityClass;
	
	private String m_entityType;

	public HAPPluginEntityProcessorSimpleImp(String entityType, Class<? extends HAPExecutableEntity> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;  }

	@Override
	public void process(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPManualEntity entityDef = definitionDomain.getEntityInfoDefinition(entityExe.getDefinitionEntityId()).getEntity();
		this.process(entityExe, entityDef, processContext);
	}
	
	@Override
	public HAPExecutableEntity newExecutable() {
		HAPExecutableEntity out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
			out.setEntityType(this.getEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}

	abstract protected void process(HAPExecutableEntity entityExe, HAPManualEntity entityDef, HAPContextProcessor processContext);

}
