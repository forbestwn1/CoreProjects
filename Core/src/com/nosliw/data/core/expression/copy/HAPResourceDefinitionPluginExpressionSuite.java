package com.nosliw.data.core.expression.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginExpressionSuite implements HAPPluginResourceDefinition{

	
	private HAPParserProcessDefinition m_processParser;
	
	public HAPResourceDefinitionPluginExpressionSuite() {
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSIONSUITE;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPDefinitionProcessSuite suite = null;
		try {
			HAPResourceIdExpressionSuite processSuiteResourceId = new HAPResourceIdExpressionSuite(resourceId);
			suite = HAPImporterProcessSuiteDefinition.readProcessSuiteDefinitionFromFile(new FileInputStream(new File(HAPFileUtility.getProcessFolder()+processSuiteResourceId.getId()+".process")), m_activityPluginMan);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return suite;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return HAPParserProcessDefinition.parsePocessSuite(jsonObj, this.m_activityPluginMan);
	}
}
