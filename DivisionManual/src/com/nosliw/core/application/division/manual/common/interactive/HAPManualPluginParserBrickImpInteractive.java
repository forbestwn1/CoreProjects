package com.nosliw.core.application.division.manual.common.interactive;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPDefinitionInteractive;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpInteractive extends HAPPluginEntityDefinitionInDomainImpSimple{

	private Class<? extends HAPDefinitionInteractive> m_interactiveClass;
	
	public HAPManualPluginParserBrickImpInteractive(String entityType, Class<? extends HAPDefinitionInteractive> interactiveClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, HAPManualBrickInteractive.class, runtimeEnv);
		this.m_interactiveClass = interactiveClass;
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		try {
			HAPManualBrickInteractive entity = (HAPManualBrickInteractive)this.getEntity(entityId, parserContext);
			HAPDefinitionInteractive interactiveDef = this.m_interactiveClass.newInstance();
			JSONObject jsonObj = (JSONObject)jsonValue;
			interactiveDef.buildObject(jsonObj.getJSONObject(HAPManualBrickInteractive.ATTR_INTERACTIVE), HAPSerializationFormat.JSON);
			entity.setExpressionInteractive(interactiveDef);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
