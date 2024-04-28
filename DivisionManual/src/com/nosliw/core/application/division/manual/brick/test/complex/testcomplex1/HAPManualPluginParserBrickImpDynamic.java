package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImp;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpDynamic extends HAPPluginParserBrickImp{

	public final static String PREFIX_IGNORE = "ignore";

	public HAPManualPluginParserBrickImpDynamic(HAPIdBrickType entityTypeId, Class<? extends HAPManualBrick> entityClass, HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		JSONArray jsonArray = (JSONArray)jsonValue;
		
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualAttribute.INFO);

			if(HAPUtilityEntityInfo.isEnabled(info)) {
				String attrName = info.getName();
//				System.out.println(attrName);
				if(!attrName.startsWith(PREFIX_IGNORE)) {
					if(attrName.equals(HAPWithAttachment.ATTACHMENT)) {
						this.parseBrickAttributeSelfJson(entityDefinition, jsonObj, attrName, HAPUtilityBrick.parseBrickTypeId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT), null, parseContext);							
					}
					else if(attrName.equals(HAPWithValueContext.VALUECONTEXT)) {
						this.parseBrickAttributeSelfJson(entityDefinition, jsonObj, attrName, HAPManualEnumBrickType.VALUECONTEXT_100, null, parseContext);							
					}
					else {
						Object entityObj = jsonObj.opt(attrName);
						if(isAttributeEnabledJson(entityObj)) {
							HAPAttributeEntityInfo attributeInfo = this.parseAttributeInfo(attrName);
							this.parseBrickAttributeSelfJson(entityDefinition, jsonObj, attrName, attributeInfo.entityType, attributeInfo.adapterType, parseContext);							
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
				out.entityType = HAPUtilityBrick.parseBrickTypeIdAggresive(pair.getLeft(), this.getEntityManager()); 
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.adapterType = HAPUtilityBrick.parseBrickTypeIdAggresive(pair.getLeft(), this.getEntityManager());
				str = pair.getRight();
			}
		}

		out.isComplex = HAPManualUtilityBrick.isBrickComplex(out.entityType, getEntityManager()); 
		
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
		public HAPIdBrickType entityType;
		public String attirbuteName;
		public boolean isComplex;
		public String containerType;
		public HAPIdBrickType adapterType;
	}
	
}
