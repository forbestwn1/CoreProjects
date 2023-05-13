package com.nosliw.servlet.app.browsecomplexentity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.servlet.HAPServiceServlet;
import com.nosliw.servlet.core.HAPInitServlet;

public class HAPBrowseComplexEntityServlet extends HAPServiceServlet{

	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) throws Exception {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = (HAPRuntimeEnvironmentImpBrowser)this.getServletContext().getAttribute(HAPInitServlet.NAME_RUNTIME_ENVIRONMENT);
		List<HAPContainerComplexEntity> entitys =  this.buildComplexEntityContainer(runtimeEnvironment);		
		HAPServiceData out = HAPServiceData.createSuccessData(entitys);
		return out;
	}

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
		List<HAPContainerComplexEntity> entitys =  new HAPBrowseComplexEntityServlet().buildComplexEntityContainer(runtimeEnvironment);		
		System.out.println(HAPUtilityJson.formatJson(HAPSerializeManager.getInstance().toStringValue(entitys, HAPSerializationFormat.JSON)));
	}
	
	private List<HAPContainerComplexEntity> buildComplexEntityContainer(HAPRuntimeEnvironmentImpBrowser runtimeEnv){
		List<HAPContainerComplexEntity> out = new ArrayList<HAPContainerComplexEntity>();
		
		Set<String> complexEntityTypes = runtimeEnv.getDomainEntityExecutableManager().getComplexEntityTypes();
		for(String complexEntityType : complexEntityTypes) {
			HAPContainerComplexEntity entityContainer = new HAPContainerComplexEntity(complexEntityType);
			
			String basePath = HAPSystemFolderUtility.getResourceFolder(complexEntityType);
			
			Set<File> childrenFile = HAPUtilityFile.getChildren(basePath);
			for(File childFile : childrenFile) {
				String resourceId = null;
				if(childFile.isDirectory()) {
					resourceId = childFile.getName();
				}
				else {
					resourceId = childFile.getName();
				}
				entityContainer.addComplexEntityInfo(new HAPInfoComplexEntity(new HAPResourceIdSimple(complexEntityType, resourceId)));
			}
			
			out.add(entityContainer);
		}
		
		return out;
	}
}
