package com.nosliw.data.core.domain.common.interactive;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainInteractive extends HAPPluginEntityDefinitionInDomainImp{

	private Class<? extends HAPDefinitionInteractive> m_interactiveClass;
	
	public HAPPluginEntityDefinitionInDomainInteractive(String entityType, Class<? extends HAPDefinitionInteractive> interactiveClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, HAPDefinitionEntityInteractive.class, runtimeEnv);
		this.m_interactiveClass = interactiveClass;
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		try {
			JSONObject jsonObj = (JSONObject)obj; 
			
			HAPDefinitionEntityInteractive entity = (HAPDefinitionEntityInteractive)this.getEntity(entityId, parserContext);
			HAPDefinitionInteractive interactiveDef = this.m_interactiveClass.newInstance();
			interactiveDef.buildObject(jsonObj.getJSONObject(HAPDefinitionEntityInteractive.ATTR_INTERACTIVE), HAPSerializationFormat.JSON);
			entity.setInteractive(interactiveDef);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isComplexEntity() {   return false;  }
}