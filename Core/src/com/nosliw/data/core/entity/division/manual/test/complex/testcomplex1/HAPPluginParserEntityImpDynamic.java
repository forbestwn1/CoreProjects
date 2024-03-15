package com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPUtilityEntity;
import com.nosliw.data.core.entity.division.manual.HAPContextParse;
import com.nosliw.data.core.entity.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.data.core.entity.division.manual.HAPManualAttribute;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPPluginParserEntityImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpDynamic extends HAPPluginParserEntityImp{

	public final static String PREFIX_IGNORE = "ignore";

	public HAPPluginParserEntityImpDynamic(HAPIdEntityType entityTypeId, Class<? extends HAPManualEntity> entityClass, HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualEntity entityDefinition, Object jsonValue, HAPContextParse parseContext) {
		JSONArray jsonArray = (JSONArray)jsonValue;
		
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualAttribute.INFO);

			if(HAPUtilityEntityInfo.isEnabled(info)) {
				String attrName = info.getName();
//				System.out.println(attrName);
				if(!attrName.startsWith(PREFIX_IGNORE)) {
					if(attrName.equals(HAPWithAttachment.ATTACHMENT)) {
						this.parseEntityAttributeSelfJson(entityDefinition, jsonObj, attrName, HAPUtilityEntity.parseEntityTypeId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT), null, parseContext);							
					}
					else if(attrName.equals(HAPWithValueContext.VALUECONTEXT)) {
						this.parseEntityAttributeSelfJson(entityDefinition, jsonObj, attrName, HAPUtilityEntity.parseEntityTypeId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT), null, parseContext);							
					}
					else {
						Object entityObj = jsonObj.opt(attrName);
						if(isAttributeEnabledJson(entityObj)) {
							HAPAttributeEntityInfo attributeInfo = this.parseAttributeInfo(attrName);
							this.parseEntityAttributeSelfJson(entityDefinition, jsonObj, attrName, attributeInfo.entityType, attributeInfo.adapterType, parseContext);							
						}
					}
				}
			}
		}
	}

	//name_(none|set|list|container)_entitytype_adapterType
	private HAPAttributeEntityInfo parseAttributeInfo(String attrName) {
		HAPAttributeEntityInfo out = new HAPAttributeEntityInfo();

		String str = attrName;
		
		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.attirbuteName = pair.getLeft();
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.entityType = HAPUtilityEntity.parseEntityTypeIdAggresive(pair.getLeft(), this.getEntityManager()); 
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.adapterType = HAPUtilityEntity.parseEntityTypeIdAggresive(pair.getLeft(), this.getEntityManager());
				str = pair.getRight();
			}
		}

		out.isComplex = HAPUtilityEntity.isEntityComplex(out.entityType, getEntityManager()); 
		
		return out;
	}
	
	private Pair<String, String> parseString(String str){
		String seperator = "-";
		int index = str.indexOf(seperator);
		if(index!=-1) {
			return Pair.of(str.substring(0, index), str.substring(index+1));
		}
		else {
			return Pair.of(str, null);
		}
	}
	
	class HAPAttributeEntityInfo {
		public HAPIdEntityType entityType;
		public String attirbuteName;
		public boolean isComplex;
		public String containerType;
		public HAPIdEntityType adapterType;
	}
	
}
