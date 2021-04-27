package com.nosliw.uiresource.page.tag;

import java.io.File;
import java.util.Iterator;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.value.HAPRhinoDataUtility;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPParserContext;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;

public class HAPUITagDefinitionParser {

	public static HAPUITagDefinition parseFromFile(File file){
		HAPUITagDefinition out = null;
		try {
			Context cx = Context.enter();
	        Scriptable scope = cx.initStandardObjects(null);

			String content = "var out="+HAPFileUtility.readFile(file) + "; out;";
			NativeObject defObjJS = (NativeObject)cx.evaluateString(scope, content, file.getName(), 1, null);

			String type = (String)defObjJS.get(HAPUITagDefinition.TYPE);

	    	if(HAPConstantShared.UITAG_TYPE_DATA.equals(type)) out = new HAPUITagDefinitionData();
	    	else if(HAPConstantShared.UITAG_TYPE_CONTROL.equals(type)) out = new HAPUITagDefinitionControl();
	    	else out = new HAPUITagDefinitionOther();

	    	if(out!=null) {
		    	parseUITagDefinition(out, defObjJS);
	    	}
	    	
	    	if(HAPConstantShared.UITAG_TYPE_DATA.equals(type)) parseUITagDefinitionData((HAPUITagDefinitionData)out, defObjJS);
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
            // Exit from the context.
            Context.exit();
        }	
		return out;
	}
	
	private static void parseUITagDefinitionData(HAPUITagDefinitionData definition, NativeObject defObjJS) {
		HAPElementLeafRelative eleDef = (HAPElementLeafRelative)definition.getContext().getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE).getElement("internal_data").getDefinition();
		definition.setDataTypeCriteria(((HAPElementLeafData)eleDef.getDefinition()).getCriteria());
	}
	
	private static void parseUITagDefinition(HAPUITagDefinition definition, NativeObject defObjJS) throws Exception {
		definition.setId((String)defObjJS.get(HAPEntityInfo.ID));
		definition.setName((String)defObjJS.get(HAPEntityInfo.NAME));
    	definition.setScript(Context.toString(defObjJS.get(HAPUITagDefinition.SCRIPT)));
    	definition.setDescription(Context.toString(defObjJS.get(HAPEntityInfo.DESCRIPTION)));
		definition.setBase((String)defObjJS.get(HAPUITagDefinition.BASE));
    	
		//parse context
		HAPContextUITagDefinition context = definition.getContext();
		NativeObject contextObj = (NativeObject)defObjJS.get(HAPUITagDefinition.CONTEXT);
		JSONObject contextJson = (JSONObject)HAPRhinoDataUtility.toJson(contextObj);
		HAPUITagDefinitionParser.parseContextInTagDefinition(contextJson, context);
		
		//parse dependency
		NativeObject requiresObj = (NativeObject)defObjJS.get(HAPUITagDefinition.REQUIRES);
		if(requiresObj!=null){
			JSONObject requiresJson = (JSONObject)HAPRhinoDataUtility.toJson(requiresObj);
			Iterator<String> typeIt = requiresJson.keys();
			while(typeIt.hasNext()){
				String resourceType = typeIt.next();
				JSONObject requiresForTypeJson = requiresJson.optJSONObject(resourceType);
				Iterator<String> aliasIt = requiresForTypeJson.keys();
				while(aliasIt.hasNext()){
					String alias = aliasIt.next();
					String resourceIdLiterate = requiresForTypeJson.optString(alias);
					definition.addResourceDependency(new HAPResourceDependency(HAPFactoryResourceId.newInstance(resourceType, resourceIdLiterate), alias));
				}
			}
		}

		//attribute definition
		NativeArray attributesArrayObj = (NativeArray)defObjJS.get(HAPUITagDefinition.ATTRIBUTES);
		for(int i=0; i<attributesArrayObj.size(); i++) {
			JSONObject attrDefJson = (JSONObject)HAPRhinoDataUtility.toJson(attributesArrayObj.get(i));
			HAPUITagDefinitionAttribute attrDef = new HAPUITagDefinitionAttribute();
			attrDef.buildObject(attrDefJson, HAPSerializationFormat.JSON);
			definition.addAttributeDefinition(attrDef);
		}
		
		//event definition
		NativeArray eventDefObjs = (NativeArray)defObjJS.get(HAPUITagDefinition.EVENT);
		if(eventDefObjs!=null) {
			for(int i=0; i<eventDefObjs.size(); i++) {
				JSONObject eventDefJson = (JSONObject)HAPRhinoDataUtility.toJson(eventDefObjs.get(i));
				HAPDefinitionUIEvent eventDef = new HAPDefinitionUIEvent();
				eventDef.buildObject(eventDefJson, HAPSerializationFormat.JSON);
				definition.addEventDefinition(eventDef);
			}
		}
	}
	
	//parse 
	public static void parseContextInTagDefinition(JSONObject contextJson, HAPContextUITagDefinition contextOut){
		HAPParserContext.parseContextGroup(contextJson, contextOut);
	}
}
