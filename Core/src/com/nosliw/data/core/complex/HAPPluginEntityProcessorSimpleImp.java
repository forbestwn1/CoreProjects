package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

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
	public HAPExecutableEntity process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPExecutableEntity out = null;
		try {
			HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
			HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
			HAPDefinitionEntityInDomain entityDef = definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
			out = this.m_exeEntityClass.newInstance();
			out.setEntityType(this.getEntityType());
			this.process(out, entityDef, processContext);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	abstract protected void process(HAPExecutableEntity entityExe, HAPDefinitionEntityInDomain entityDef, HAPContextProcessor processContext);

}
