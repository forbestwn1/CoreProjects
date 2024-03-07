package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public abstract class HAPPluginProcessorEntityDefinitionSimpleImp implements HAPPluginProcessorEntityDefinitionSimple{

	private Class<? extends HAPExecutableEntity> m_exeEntityClass;
	
	private String m_entityType;

	public HAPPluginProcessorEntityDefinitionSimpleImp(String entityType, Class<? extends HAPExecutableEntity> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;  }

	@Override
	public void process(HAPExecutableEntity entityExe, HAPContextProcess processContext) {
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
