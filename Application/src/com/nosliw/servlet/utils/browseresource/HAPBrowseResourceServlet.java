package com.nosliw.servlet.utils.browseresource;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPChildrenComponentId;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;
import com.nosliw.servlet.HAPBaseServlet;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.application.HAPDefinitionApp;
import com.nosliw.uiresource.application.HAPDefinitionAppEntryWrapper;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.resource.HAPResourceIdUIModule;

public class HAPBrowseResourceServlet extends HAPBaseServlet{

	private HAPUIResourceManager m_uiResourceMan;
	
	@Override
	protected void doGet (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		m_uiResourceMan = (HAPUIResourceManager)request.getServletContext().getAttribute("uiResourceManager");
		Map<String, HAPResourceNodeContainerByType> resourceTree = this.buildResourceTree();
		HAPServiceData serviceData = HAPServiceData.createSuccessData(resourceTree);
		this.printContent(serviceData, response);
	}

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
		HAPUIResourceManager uiResourceMan = runtimeEnvironment.getUIResourceManager();
		HAPBrowseResourceServlet that = new HAPBrowseResourceServlet();
		that.m_uiResourceMan = uiResourceMan;
		Map<String, HAPResourceNodeContainerByType> resourceTree = that.buildResourceTree();
		System.out.println(HAPJsonUtility.buildJson(resourceTree, HAPSerializationFormat.JSON));
	}
	
	private Map<String, HAPResourceNodeContainerByType> buildResourceTree(){
		Map<String, HAPResourceNodeContainerByType> out = new LinkedHashMap<String, HAPResourceNodeContainerByType>();
		{
			HAPResourceNodeContainerByType byType = this.buildAllApp(); 
			out.put(byType.getType(), byType);
		}

		{
			HAPResourceNodeContainerByType byType = this.buildAllModule(); 
			out.put(byType.getType(), byType);
		}

		{
			HAPResourceNodeContainerByType byType = this.buildAllPage(); 
			out.put(byType.getType(), byType);
		}

		return out;
	}
	
	private HAPResourceNodeContainerByType buildAllApp(){
		HAPResourceNodeContainerByType out = new HAPResourceNodeContainerByType(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPP);
		Set<File> files = HAPFileUtility.getAllFiles(HAPFileUtility.getMiniAppFolder());
		for(File file : files) {
			HAPResourceNode node = createResourceNodeApp(HAPResourceIdFactory.newInstance(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPP, HAPFileUtility.getFileName(file)));
			out.addElement(node);
		}
		return out;
	}

	private HAPResourceNodeContainerByType buildAllModule(){
		HAPResourceNodeContainerByType out = new HAPResourceNodeContainerByType(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE);
		Set<File> files = HAPFileUtility.getAllFiles(HAPFileUtility.getUIModuleFolder());
		for(File file : files) {
			String resourceId = HAPFileUtility.getFileName(file);
			HAPResourceNode node = createResourceNodeModule(resourceId, new HAPResourceIdUIModule(resourceId), null);
			out.addElement(node);
		}
		return out;
	}

	private HAPResourceNodeContainerByType buildAllPage(){
		HAPResourceNodeContainerByType out = new HAPResourceNodeContainerByType(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
		Set<File> files = HAPFileUtility.getAllFiles(HAPFileUtility.getUIPageFolder());
		for(File file : files) {
			String resourceId = HAPFileUtility.getFileName(file);
			HAPResourceNode node = createResourceNodePage(resourceId, new HAPResourceIdUIModule(resourceId), null);
			out.addElement(node);
		}
		return out;
	}

	private HAPResourceNode createResourceNodePage(String name, HAPResourceId resourceId, HAPInfo info) {
		HAPResourceNode out = new HAPResourceNode(name);
		out.setResourceId(resourceId);
		String url = "http://localhost:8082/nosliw/page.html?name="+resourceId.getIdLiterate();
		out.setUrl(url);
		try {
			HAPDefinitionUIPage pageDef = this.m_uiResourceMan.getUIPageDefinition(resourceId, null);
			this.processChildren(out, pageDef.getChildrenComponentId());
		}
		catch(Throwable e) {
//			e.printStackTrace();
		}
		return out;
	}

	private HAPResourceNode createResourceNodeModule(String name, HAPResourceId resourceId, HAPInfo info) {
		HAPResourceNode out = new HAPResourceNode(name);
		out.setResourceId(resourceId);
		String url = "http://localhost:8082/nosliw/module_framework7.html?name="+resourceId.getIdLiterate()+"&setting=setting";
		out.setUrl(url);
		try {
			HAPDefinitionModule moduleDef = this.m_uiResourceMan.getModuleDefinition(resourceId, null);
			this.processChildren(out, moduleDef.getChildrenComponentId());
		}
		catch(Throwable e) {
//			e.printStackTrace();
		}
		return out;
	}


	private HAPResourceNode createResourceNodeAppEntry(String name, HAPResourceId resourceId, HAPInfo info) {
		HAPResourceNode out = new HAPResourceNode(name);
		out.setResourceId(resourceId);
		String url = "http://localhost:8082/nosliw/app.html?name="+resourceId.getIdLiterate()+"&setting=setting";
		out.setUrl(url);
		try {
			HAPDefinitionAppEntryWrapper appEntryDef = this.m_uiResourceMan.getMiniAppEntryDefinition(resourceId, null);
			this.processChildren(out, appEntryDef.getChildrenComponentId());
		}
		catch(Throwable e) {
//			e.printStackTrace();
		}
		return out;
	}

	private HAPResourceNode createResourceNodeApp(HAPResourceId resourceId) {
		HAPDefinitionApp appDef = m_uiResourceMan.getMiniAppDefinition(resourceId, null);
		HAPResourceNode out = new HAPResourceNode(appDef.getId());
		this.processChildren(out, appDef.getChildrenComponentId());
		return out;
	}
	
	private void processChildren(HAPResourceNode resourceNode, HAPChildrenComponentIdContainer childrenComponentId) {
		Map<String, List<HAPChildrenComponentId>> children = childrenComponentId.getChildren();
		for(String type : children.keySet()) {
			for(HAPChildrenComponentId componentId : children.get(type)) {
				HAPResourceNode childNode = null;
				if(type.equals(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPP)) {
					
				}
				else if(type.equals(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY)) {
					childNode = createResourceNodeAppEntry(componentId.getName(), componentId.getResourceId(), componentId.getInfo());
				}
				else if(type.equals(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE)) {
					childNode = createResourceNodeModule(componentId.getName(), componentId.getResourceId(), componentId.getInfo());
				}
				else if(type.equals(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE)) {
					childNode = createResourceNodePage(componentId.getName(), componentId.getResourceId(), componentId.getInfo());
				}
				resourceNode.addChild(type, childNode);
			}
		}
	}
	
}
