package com.nosliw.servlet.app.browseresource;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.servlet.HAPServiceServlet;

public class HAPBrowseResourceServlet extends HAPServiceServlet{

//	private HAPUIResourceManager m_uiResourceMan;

	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

/*	
	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		m_uiResourceMan = (HAPUIResourceManager)this.getServletContext().getAttribute("uiResourceManager");
		HAPResourceNodeContainer resourceTree = this.buildResourceTree();
		HAPServiceData out = HAPServiceData.createSuccessData(resourceTree);
		return out;
	}
	
	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
		HAPUIResourceManager uiResourceMan = runtimeEnvironment.getUIResourceManager();
		HAPBrowseResourceServlet that = new HAPBrowseResourceServlet();
		that.m_uiResourceMan = uiResourceMan;
		HAPResourceNodeContainer resourceTree = that.buildResourceTree();
		System.out.println(HAPUtilityJson.buildJson(resourceTree, HAPSerializationFormat.JSON));
	}
	
	private HAPResourceNodeContainer buildResourceTree(){
		HAPResourceNodeContainer out = new HAPResourceNodeContainer();
		{
			HAPResourceNodeContainerByType byType = this.buildAllApp(); 
			out.addTypeResourceContainer(byType);
		}

		{
			HAPResourceNodeContainerByType byType = this.buildAllModule(); 
			out.addTypeResourceContainer(byType);
		}

		{
			HAPResourceNodeContainerByType byType = this.buildAllPage(); 
			out.addTypeResourceContainer(byType);
		}

		return out;
	}
	
	private HAPResourceNodeContainerByType buildAllApp(){
		HAPResourceNodeContainerByType out = new HAPResourceNodeContainerByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPP);
		Set<File> files = HAPUtilityFile.getAllFiles(HAPSystemFolderUtility.getMiniAppFolder());
		for(File file : files) {
			HAPResourceNode node = createResourceNodeApp(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPP, HAPUtilityFile.getFileName(file)));
			out.addElement(node);
		}
		return out;
	}

	private HAPResourceNodeContainerByType buildAllModule(){
		HAPResourceNodeContainerByType out = new HAPResourceNodeContainerByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE);
		Set<File> files = HAPUtilityFile.getAllFiles(HAPSystemFolderUtility.getUIModuleFolder());
		for(File file : files) {
			String resourceId = HAPUtilityFile.getFileName(file);
			HAPResourceNode node = createResourceNodeModule(resourceId, new HAPResourceIdUIModule(resourceId), null);
			out.addElement(node);
		}
		return out;
	}

	private HAPResourceNodeContainerByType buildAllPage(){
		HAPResourceNodeContainerByType out = new HAPResourceNodeContainerByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
		Set<File> files = HAPUtilityFile.getAllFiles(HAPSystemFolderUtility.getUIPageFolder());
		for(File file : files) {
			String resourceId = HAPUtilityFile.getFileName(file);
			HAPResourceNode node = createResourceNodePage(resourceId, new HAPResourceIdUIResource(resourceId), null);
			out.addElement(node);
		}
		return out;
	}

	private HAPResourceNode createResourceNodePage(String name, HAPResourceId resourceId, HAPInfo info) {
		HAPResourceNode out = new HAPResourceNode(name);
		out.setResourceId(resourceId);
		String url = "/nosliw/page.html?name="+resourceId.getCoreIdLiterate();
		out.setUrl(url);
		try {
			HAPDefinitionUIUnitPage pageDef = this.m_uiResourceMan.getUIPageDefinition(resourceId, null);
			this.processChildren(out, pageDef.getChildrenReferencedResource());
		}
		catch(Throwable e) {
//			e.printStackTrace();
		}
		return out;
	}

	private HAPResourceNode createResourceNodeModule(String name, HAPResourceId resourceId, HAPInfo info) {
		HAPResourceNode out = new HAPResourceNode(name);
		out.setResourceId(resourceId);
		String url = "/nosliw/module_framework7.html?name="+resourceId.getCoreIdLiterate();
		out.setUrl(url);
		try {
			HAPDefinitionModule moduleDef = this.m_uiResourceMan.getModuleDefinition(resourceId, null);
			this.processChildren(out, moduleDef.getChildrenReferencedResource());
		}
		catch(Throwable e) {
//			e.printStackTrace();
		}
		return out;
	}


	private HAPResourceNode createResourceNodeAppEntry(String name, HAPResourceId resourceId, HAPInfo info) {
		HAPResourceNode out = new HAPResourceNode(name);
		out.setResourceId(resourceId);
		String url = "/nosliw/app.html?name="+resourceId.getCoreIdLiterate();
		out.setUrl(url);
		try {
			HAPDefinitionAppEntry appEntryDef = this.m_uiResourceMan.getMiniAppEntryDefinition(resourceId, null);
			this.processChildren(out, appEntryDef.getChildrenReferencedResource());
		}
		catch(Throwable e) {
//			e.printStackTrace();
		}
		return out;
	}

	private HAPResourceNode createResourceNodeApp(HAPResourceId resourceId) {
		HAPDefinitionApp appDef = m_uiResourceMan.getMiniAppDefinition(resourceId, null);
		HAPResourceNode out = new HAPResourceNode(appDef.getId());
		this.processChildren(out, appDef.getChildrenReferencedResource());
		return out;
	}
	
	private void processChildren(HAPResourceNode resourceNode, HAPContainerChildReferenceResource childrenComponentId) {
		Map<String, List<HAPInfoChildResource>> children = childrenComponentId.getChildren();
		for(String type : children.keySet()) {
			for(HAPInfoChildResource componentId : children.get(type)) {
				HAPResourceNode childNode = null;
				if(type.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPP)) {
					
				}
				else if(type.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPENTRY)) {
					childNode = createResourceNodeAppEntry(componentId.getName(), componentId.getResourceId(), componentId.getInfo());
				}
				else if(type.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE)) {
					childNode = createResourceNodeModule(componentId.getName(), componentId.getResourceId(), componentId.getInfo());
				}
				else if(type.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE)) {
					childNode = createResourceNodePage(componentId.getName(), componentId.getResourceId(), componentId.getInfo());
				}
				resourceNode.addChild(type, childNode);
			}
		}
	}
*/	
}
