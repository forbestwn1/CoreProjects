package com.nosliw.data.core.script.expression;

import java.io.InputStream;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.script.expression.resource.HAPParserScriptResourceDefinition;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;

public class HAPImporterScriptDefinition {

	public static HAPResourceDefinitionScriptGroup readScriptResourceDefinitionFromFile(InputStream inputStream, HAPParserExpression expressionParser){
		HAPResourceDefinitionScriptGroup suite = null;
		try{
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject contentJson = HAPJsonUtility.newJsonObject(content);
			suite = HAPParserScriptResourceDefinition.parseScriptResourceDefinition(contentJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}

}
