package com.nosliw.servlet.app.browsecomplexentity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtimeenv.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.servlet.HAPServiceServlet;
import com.nosliw.servlet.core.HAPInitServlet;

@HAPEntityWithAttribute
public class HAPBrowseComplexEntityServlet extends HAPServiceServlet{

	@HAPAttribute
	final public static String COMMAND_RESOURCES = "getAllResources";
	
	@HAPAttribute
	final public static String COMMAND_CONFIGURES = "getAllConfigures";
	
	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) throws Exception {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = (HAPRuntimeEnvironmentImpBrowser)this.getServletContext().getAttribute(HAPInitServlet.NAME_RUNTIME_ENVIRONMENT);

		Object outObj = null;
		if(COMMAND_RESOURCES.equals(command)) {
			outObj =  this.buildComplexEntityContainer(runtimeEnvironment);	
		}
		else if(COMMAND_CONFIGURES.equals(command)) {
			outObj =  this.buildConfiguresList(runtimeEnvironment);	
		}
		
		HAPServiceData out = HAPServiceData.createSuccessData(outObj);
		return out;
	}

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
		List<HAPContainerComplexEntity> entitys =  new HAPBrowseComplexEntityServlet().buildComplexEntityContainer(runtimeEnvironment);		
		System.out.println(HAPUtilityJson.formatJson(HAPManagerSerialize.getInstance().toStringValue(entitys, HAPSerializationFormat.JSON)));
	}
	
	private List<HAPContainerComplexEntity> buildComplexEntityContainer(HAPRuntimeEnvironmentImpBrowser runtimeEnv){
		List<HAPContainerComplexEntity> out = new ArrayList<HAPContainerComplexEntity>();
		
		List<String> complexEntityTypes = new ArrayList<String>();
		complexEntityTypes.addAll(runtimeEnv.getDomainEntityExecutableManager().getComplexEntityTypes());
		Collections.sort(complexEntityTypes);
		for(String complexEntityType : complexEntityTypes) {
			HAPContainerComplexEntity entityContainer = new HAPContainerComplexEntity(complexEntityType);
			
			String basePath = HAPSystemFolderUtility.getResourceFolder(complexEntityType);
			
			List<String> resourceIds = new ArrayList<String>();
			Set<File> childrenFile = HAPUtilityFile.getChildren(basePath);
			for(File childFile : childrenFile) {
				String resourceId = null;
				if(childFile.isDirectory()) {
					resourceId = childFile.getName();
				}
				else {
					resourceId = HAPUtilityFile.getFileCoreName(childFile.getName());
				}
				resourceIds.add(resourceId);
			}
			
			Collections.sort(resourceIds);
			
			for(String resourceId : resourceIds) {
				entityContainer.addComplexEntityInfo(new HAPInfoComplexEntity(new HAPResourceIdSimple(complexEntityType, resourceId)));
			}
			
			out.add(entityContainer);
		}
		
		return out;
	}
	
	private List<String> buildConfiguresList(HAPRuntimeEnvironmentImpBrowser runtimeEnv){
		List<String> out = new ArrayList<String>();
		
		String basePath = HAPSystemFolderUtility.getResourceFolder(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE);
		Set<File> childrenFile = HAPUtilityFile.getChildren(basePath);
		List<File> childrenFileSorted = HAPUtilityFile.sortFiles(childrenFile);
		for(File childFile : childrenFileSorted) {
			if(!childFile.isDirectory()) {
				out.add(HAPUtilityFile.getFileCoreName(childFile.getName()));
			}
		}
		return out;
	}
}
