package com.nosliw.data.core.script.expression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginScript implements HAPPluginResourceDefinition{

	private HAPParserExpression m_expressionParser;
	
	public HAPResourceDefinitionPluginScript(HAPParserExpression expressionParser) {
		this.m_expressionParser = expressionParser;
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_SCRIPT;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPResourceDefinitionScriptGroup scriptResourceDefinition = null;
		try {
			scriptResourceDefinition = HAPImporterScriptDefinition.readScriptResourceDefinitionFromFile(new FileInputStream(new File(HAPSystemFolderUtility.getExpressionFolder()+resourceId.getId()+".res")), this.m_expressionParser);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return scriptResourceDefinition;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return HAPParserScriptResourceDefinition.parseScriptResourceDefinition(jsonObj);
	}
}
