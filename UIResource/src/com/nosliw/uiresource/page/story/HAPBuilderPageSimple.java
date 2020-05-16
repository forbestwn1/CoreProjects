package com.nosliw.uiresource.page.story;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.dynamic.HAPOutputBuilder;
import com.nosliw.data.core.resource.dynamic.HAPParmDefinition;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.service.provide.HAPDefinitionService;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.story.HAPConnectionEnd;
import com.nosliw.data.core.story.HAPResourceBuilder;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;
import com.nosliw.uiresource.page.tag.HAPUITagQueryResult;
import com.nosliw.uiresource.page.tag.HAPUITageQuery;

public class HAPBuilderPageSimple extends HAPEntityInfoImp implements HAPResourceBuilder{

	private HAPManagerServiceDefinition m_serviceDefMan;
	
	private HAPParserPage m_pageParser;
	
	private HAPUITagManager m_tagManager;
	
	private HAPStory m_story;
	
	public HAPBuilderPageSimple(HAPManagerServiceDefinition serviceDefMan, HAPUITagManager tagManager, HAPParserPage pageParser) {
		this.m_serviceDefMan = serviceDefMan;
		this.m_tagManager = tagManager;
		this.m_pageParser = pageParser;
	}

	@Override
	public HAPResourceDefinition buildResourceDefinition(HAPStory story) {
		this.m_story = story;
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		HAPContext context = buildContext();
		templateParms.put("context", context.toStringValue(HAPSerializationFormat.JSON));
		InputStream pageTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPBuilderPageSimple.class, "page_framework.temp");
		String pageContent = HAPStringTemplateUtil.getStringValue(pageTemplateStream, templateParms);

		HAPDefinitionUIPage pageDef = null;
		try {
			pageDef = this.m_pageParser.parseUIDefinition(null, pageContent);
		}
		catch(Exception e){
			pageDef = new HAPDefinitionUIPage(null, pageContent);
			e.printStackTrace();
		}
		return pageDef;
	}
	
	private HAPContext buildContext() {
		HAPContext context = new HAPContext();
		Set<HAPStoryNode> varNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_VARIABLE);
		for(HAPStoryNode varNode : varNodes) {
			HAPContextDefinitionRoot rootEle = HAPParserContext.parseContextRootFromJson((JSONObject)varNode.getEntity());
			context.addElement(rootEle);
		}
		return context;
	}

	private List<HAPAttachment> buildService() {
		List<HAPAttachment> attachs = new ArrayList<HAPAttachment>();
		Set<HAPStoryNode> serviceNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_SERVICE);
		for(HAPStoryNode serviceNode : serviceNodes) {
			//build service attachment
			HAPAttachmentReference refAttr = new HAPAttachmentReference(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
			refAttr.buildObject(serviceNode.getEntity(), HAPSerializationFormat.JSON);
			attachs.add(refAttr);			
		}
		return attachs;
	}
	
	private List<HAPDefinitionServiceUse> buildServiceUse() {
		List<HAPDefinitionServiceUse> out = new ArrayList<HAPDefinitionServiceUse>();
		
		Set<HAPStoryNode> serviceNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_SERVICE);
		for(HAPStoryNode serviceNode : serviceNodes) {
			HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
			//
			HAPContext serviceParmMapping = new HAPContext();
			HAPStoryNode serviceInputNode = HAPUtilityStory.getChildNode(serviceNode, HAPConstant.SERVICE_CHILD_INPUT, this.m_story);
			if(serviceInputNode!=null) {
				Map<Object, HAPStoryNode> parmNodes = HAPUtilityStory.getChildNode(serviceInputNode, this.m_story);
				for(Object key : parmNodes.keySet()) {
					String parmName = (String)key;
					HAPStoryNode parmNode = parmNodes.get(key);
					Set<HAPConnectionEnd> varsEnd =  HAPUtilityStory.getConnectionEnd(parmNode, HAPConstant.CONNECTION_TYPE_DATAIO, HAPConstant.STORYNODE_PROFILE_DATAOUT, HAPConstant.STORYNODE_TYPE_VARIABLE, HAPConstant.STORYNODE_PROFILE_DATAIO, this.m_story);
					for(HAPConnectionEnd varEnd : varsEnd) {
						HAPContextDefinitionRoot rootEle = HAPParserContext.parseContextRootFromJson((JSONObject)this.m_story.getNode(varEnd.getNodeId()).getEntity());
						serviceParmMapping.addElement(parmName, new HAPContextDefinitionLeafRelative(rootEle.getId()));
					}
				}
			}
			
			HAPStoryNode serviceResultNode = HAPUtilityStory.getChildNode(serviceNode, HAPConstant.SERVICE_CHILD_RESULT, this.m_story);
			
			if(serviceUseDef.getServiceMapping().getInputMapping()!=null || serviceUseDef.getServiceMapping().getOutputMapping()!=null) {
				out.add(serviceUseDef);
			}
		}
		return out;
	}
	
	private HAPHtmlSegment buildPage() {
		HAPHtmlSegment out = new HAPHtmlSegment();

		HAPStoryNode pageNode = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_PAGE).iterator().next();
		
//		for(HAPStoryNode uiNode : uiNodes) {
//			HAPUI ui = new HAPUI();
//			ui.buildObject(uiNode.getEntity(), HAPSerializationFormat.JSON);
//		}
		
		return out;
	}
	
	private HAPHtmlSegment buildUI(HAPStoryNode uiNode) {
		HAPHtmlSegment out = new HAPHtmlSegment();

//		Set<HAPStoryNode> uiNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_UI);
//		for(HAPStoryNode uiNode : uiNodes) {
//			HAPUI ui = new HAPUI();
//			ui.buildObject(uiNode.getEntity(), HAPSerializationFormat.JSON);
//		}
		
		return out;
	}
	
	
	
	
	public HAPOutputBuilder build(Set<HAPParmDefinition> parms) {
		String serviceId = (String)this.findParmById("service", parms).getData().getValue();
		
		HAPDefinitionService serviceDef = this.m_serviceDefMan.getDefinition(serviceId);
		HAPServiceInterface serviceInterface = serviceDef.getStaticInfo().getInterface();
		
		HAPHtmlSegment htmlContent = this.buildHtml(serviceInterface);
		HAPContext context = this.buildContext(serviceInterface);
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("htmlContent", htmlContent.toString());
		templateParms.put("serviceId", serviceId);
		templateParms.put("context", context.toStringValue(HAPSerializationFormat.JSON));
		
		InputStream pageTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPBuilderPageSimple.class, "page_framework.temp");
		String pageContent = HAPStringTemplateUtil.getStringValue(pageTemplateStream, templateParms);

		HAPDefinitionUIPage pageDef = null;
		try {
			pageDef = this.m_pageParser.parseUIDefinition(null, pageContent);
		}
		catch(Exception e){
			pageDef = new HAPDefinitionUIPage(null, pageContent);
			e.printStackTrace();
		}
		
		HAPOutputBuilder out = new HAPOutputBuilder();
		out.setResourceDefinition(pageDef);
		return out;
	}

	private HAPHtmlSegment buildHtml(HAPServiceInterface serviceInterface) {
		HAPHtmlSegment out = new HAPHtmlSegment();
		HAPServiceResult serviceResult = serviceInterface.getResult(HAPConstant.SERVICE_RESULT_SUCCESS);
		HAPHtmlSegment outputHtml = this.buildHtml(serviceResult);
		out.addSegment(outputHtml);
		return out;
	}
	
	private HAPHtmlSegment buildHtml(HAPServiceResult serviceResult) {
		HAPHtmlSegment out = new HAPHtmlSegment();
		Map<String, HAPServiceOutput> outputs = serviceResult.getOutput();
		for(String name : outputs.keySet()) {
			HAPServiceOutput output = outputs.get(name);
			HAPUITageQuery uiTagQuery = new HAPUITageQuery();
			uiTagQuery.setDataTypeCriteria(output.getCriteria());
			HAPUITagQueryResult tagQueryResult = this.m_tagManager.getDefaultUITagDefnition(uiTagQuery);
			HAPHtmlTag tagHtml = new HAPHtmlTag(tagQueryResult.getTag());
			tagHtml.addAttribute(new HAPTagAttribute("data", name));
			out.addSegment(tagHtml);
		}
		return out;
	}
	
	private HAPContext buildContext(HAPServiceInterface serviceInterface) {
		
		return new HAPContext();
	}
	
	private HAPParmDefinition findParmById(String id, Set<HAPParmDefinition> parms) {
		for(HAPParmDefinition parm : parms) {
			if(id.equals(parm.getId())) {
				return parm;
			}
		}
		return null;
	}

}
