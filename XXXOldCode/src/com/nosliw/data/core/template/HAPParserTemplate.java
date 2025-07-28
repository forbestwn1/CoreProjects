package com.nosliw.data.core.template;

import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentImpEntity;
import com.nosliw.data.core.resource.dynamic.HAPParmDefinition;

public class HAPParserTemplate {

	public static HAPResourceDefinitionTemplate parseFile(String fileName){
		HAPResourceDefinitionTemplate out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPUtilityFile.getFileName(input);
			String source = HAPUtilityFile.readFile(input);
			out = parseContent(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPResourceDefinitionTemplate parseContent(String content, String id) {
		JSONObject jsonObj = new JSONObject(content);
		HAPResourceDefinitionTemplate out = parseTemplateDefinition(jsonObj);
		out.setId(id);
		return out;
	}

	public static HAPResourceDefinitionTemplate parseTemplateDefinition(JSONObject jsonObj) {
		HAPResourceDefinitionTemplate out = new HAPResourceDefinitionTemplate();

		//build complex resource part from json object
		HAPParserEntityComponent.parseComplextResourceDefinition(out, jsonObj);

		//builder id
		out.setBuilderId(jsonObj.getString(HAPResourceDefinitionTemplate.BUILDERID));
		
		//parms
		Map<String, HAPAttachment> attachments = out.getAttachmentsByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TESTDATA);
		for(String setName : attachments.keySet()) {
			HAPAttachmentImpEntity att = (HAPAttachmentImpEntity)attachments.get(setName);
			JSONArray parmJsonArray = att.getEntityJsonArray();
			for(int i=0; i<parmJsonArray.length(); i++) {
				HAPParmDefinition parmDef = new HAPParmDefinition();
				parmDef.buildObject(parmJsonArray.get(i), HAPSerializationFormat.JSON);
				out.addParmDefinition(setName, parmDef);
			}
		}
		
		return out;
	}
}
