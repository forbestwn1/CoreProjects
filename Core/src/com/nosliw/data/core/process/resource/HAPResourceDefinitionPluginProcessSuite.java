package com.nosliw.data.core.process.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginProcessSuite implements HAPPluginResourceDefinition{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPResourceDefinitionPluginProcessSuite(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESSSUITE;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPDefinitionProcessSuite suite = null;
		try {
			HAPResourceIdProcessSuite processSuiteResourceId = new HAPResourceIdProcessSuite(resourceId);
			suite = HAPImporterProcessSuiteDefinition.readProcessSuiteDefinitionFromFile(new FileInputStream(new File(HAPFileUtility.getProcessFolder()+processSuiteResourceId.getId()+".process")), m_activityPluginMan);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return suite;
	}
}
