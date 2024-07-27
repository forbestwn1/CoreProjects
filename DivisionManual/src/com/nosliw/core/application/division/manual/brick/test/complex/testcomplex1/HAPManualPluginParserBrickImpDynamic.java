package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.HAPWithValueContext;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.common.attachment.HAPManualDefinitionAttachment;
import com.nosliw.core.application.division.manual.common.attachment.HAPManualUtilityParserAttachment;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImp;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpDynamic extends HAPManualDefinitionPluginParserBrickImp{

	public final static String PREFIX_IGNORE = "ignore";

	public HAPManualPluginParserBrickImpDynamic(HAPIdBrickType entityTypeId, Class<? extends HAPManualDefinitionBrick> entityClass, HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		JSONArray jsonArray = (JSONArray)jsonValue;
		
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			HAPEntityInfo info = HAPUtilityEntityInfo.buildEntityInfoFromJson(jsonObj, HAPManualDefinitionAttributeInBrick.INFO);

			if(HAPUtilityEntityInfo.isEnabled(info)) {
				String attrName = info.getName();
//				System.out.println(attrName);
				if(!attrName.startsWith(PREFIX_IGNORE)) {
					if(attrName.equals(HAPWithAttachment.ATTACHMENT)) {
						HAPManualDefinitionAttachment attachment = HAPManualUtilityParserAttachment.parseAttachmentJson(jsonObj.getJSONObject(HAPWithAttachment.ATTACHMENT), parseContext, getManualDivisionEntityManager()); 
						entityDefinition.setAttachment(attachment);
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
				out.entityType = HAPUtilityBrickId.parseBrickTypeIdAggresive(pair.getLeft(), this.getBrickManager()); 
				str = pair.getRight();
			}
		}

		{
			if(HAPUtilityBasic.isStringNotEmpty(str)) {
				Pair<String, String> pair = this.parseString(str);
				out.adapterType = HAPUtilityBrickId.parseBrickTypeIdAggresive(pair.getLeft(), this.getBrickManager());
				str = pair.getRight();
			}
		}

		out.isComplex = HAPManualDefinitionUtilityBrick.isBrickComplex(out.entityType, this.getManualDivisionEntityManager()); 
		
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
