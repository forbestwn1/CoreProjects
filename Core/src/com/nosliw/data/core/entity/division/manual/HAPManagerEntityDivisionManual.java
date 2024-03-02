package com.nosliw.data.core.entity.division.manual;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.entity.HAPEntityBundle;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.HAPIdEntity;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPManagerEntity;
import com.nosliw.data.core.entity.HAPPluginProcessorEntity;
import com.nosliw.data.core.entity.division.manual.test.complex.script.HAPPluginParserEntityImpTestComplexScript;
import com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1.HAPPluginParserEntityImpTestComplex1;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPPluginParserEntityImpValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPPluginParserEntityImpValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerEntityDivisionManual implements HAPPluginProcessorEntity{

	private Map<String, HAPInfoEntityProcessor> m_entityProcessorInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerEntityDivisionManual(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityProcessorInfo = new LinkedHashMap<String, HAPInfoEntityProcessor>();
		init();
	}
	
	@Override
	public HAPEntityBundle process(HAPIdEntity entityId) {
		
		HAPInfoEntityLocation entityLocationInfo = HAPUtilityEntityLocation.getEntityLocationInfo(entityId);
		
		HAPContextParse parseContext = new HAPContextParse(entityLocationInfo.getBasePath().getPath(), entityId.getDivision());
		
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualInfoEntity entityDefInfo = this.parseEntityDefinitionInfo(content, entityId.getEntityTypeId(), format, parseContext);
		
		//build parent and path from root

		//process definition
		HAPEntityBundle out = new HAPEntityBundle();
		out.setExtraData(entityDefInfo);
//		this.getEntityProcessorInfo(entityId.getEntityTypeId()).getProcessorPlugin().process(entityDefInfo);
		
		return out;
	}
	
	private void init() {
		this.registerEntityProcessorInfo(HAPEnumEntityType.TEST_COMPLEX_1_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplex1(this, this.m_runtimeEnv), null));
		this.registerEntityProcessorInfo(HAPEnumEntityType.TEST_COMPLEX_SCRIPT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplexScript(this, this.m_runtimeEnv), null));

		this.registerEntityProcessorInfo(HAPEnumEntityType.VALUESTRUCTURE_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpValueStructure(this, this.m_runtimeEnv), null));
		this.registerEntityProcessorInfo(HAPEnumEntityType.VALUECONTEXT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpValueContext(this, this.m_runtimeEnv), null));
	}

	public HAPManualEntity parseEntityDefinition(Object entityObj, HAPIdEntityType entityTypeId, HAPSerializationFormat format, HAPContextParse parseContext) {
		HAPPluginParserEntity entityParserPlugin = this.getEntityProcessorInfo(entityTypeId).getParserPlugin();
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	private HAPManualInfoEntity parseEntityDefinitionInfo(Object entityObj, HAPIdEntityType entityTypeId, HAPSerializationFormat format, HAPContextParse parseContext) {
		HAPManualInfoEntity out = null;
		switch(format) {
		case JSON:
			out = HAPUtilityParserEntityFormatJson.parseEntityInfo((JSONObject)HAPUtilityJson.toJsonObject(entityObj), entityTypeId, parseContext, this, this.getEntityManager());
			break;
		case HTML:
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}
	
	public void registerEntityProcessorInfo(HAPIdEntityType entityTypeId, HAPInfoEntityProcessor processorInfo) {
		this.m_entityProcessorInfo.put(entityTypeId.getKey(), processorInfo);
	}
	
	private HAPInfoEntityProcessor getEntityProcessorInfo(HAPIdEntityType entityTypeId) {
		return this.m_entityProcessorInfo.get(entityTypeId.getKey());
	}
	
	private HAPManagerEntity getEntityManager() {   return this.m_runtimeEnv.getEntityManager();    }
}
