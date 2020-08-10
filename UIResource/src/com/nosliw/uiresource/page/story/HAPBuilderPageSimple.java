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
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.story.HAPBuilderShow;
import com.nosliw.data.core.story.HAPConnectionEnd;
import com.nosliw.data.core.story.HAPInfoNodeChild;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.element.node.HAPStoryNodeConstant;
import com.nosliw.data.core.story.element.node.HAPStoryNodeService;
import com.nosliw.data.core.story.element.node.HAPStoryNodeUIData;
import com.nosliw.data.core.story.element.node.HAPStoryNodeVariable;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPBuilderPageSimple extends HAPEntityInfoImp implements HAPBuilderShow{

	private HAPManagerServiceDefinition m_serviceDefMan;
	
	private HAPParserPage m_pageParser;
	
	private HAPUITagManager m_tagManager;
	
	private HAPStory m_story;
	
	private HAPContext m_context;
	
	public HAPBuilderPageSimple(HAPManagerServiceDefinition serviceDefMan, HAPUITagManager tagManager, HAPParserPage pageParser) {
		this.m_serviceDefMan = serviceDefMan;
		this.m_tagManager = tagManager;
		this.m_pageParser = pageParser;
	}

	@Override
	public HAPResourceDefinition buildShow(HAPStory story) {
		this.m_story = story;
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		//constant
		HAPContext context = buildContext();
		this.m_context = context;
		templateParms.put("context", context.toStringValue(HAPSerializationFormat.JSON));
		
		//service provider
		List<HAPAttachment> servicesAttachment = this.buildService();
		templateParms.put("serviceProvider", HAPJsonUtility.buildJson(servicesAttachment, HAPSerializationFormat.JSON));

		//service use
		List<HAPDefinitionServiceUse> servciesUse = buildServiceUse();
		templateParms.put("serviceUse", HAPJsonUtility.buildJson(servciesUse, HAPSerializationFormat.JSON));

		//constants
		List<HAPAttachment> constantAttachment = this.buildConstant();
		
		//html
		HAPHtmlSegment htmlContent = this.buildPage();
		templateParms.put("htmlContent", htmlContent.toString());
		
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
		for(HAPStoryNode node : varNodes) {
			HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)node;
			context.addElement(varNode.getVariableName(), new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(varNode.getDataType())));
		}
		return context;
	}

	private List<HAPAttachment> buildConstant() {
		List<HAPAttachment> attachs = new ArrayList<HAPAttachment>();
		Set<HAPStoryNode> constantNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_CONSTANT);
		for(HAPStoryNode node : constantNodes) {
			HAPStoryNodeConstant constantNode = (HAPStoryNodeConstant)node;
			//build service attachment
			HAPAttachmentEntity atta = new HAPAttachmentEntity(HAPConstant.RUNTIME_RESOURCE_TYPE_DATA);
			atta.setEntity(new JSONObject(constantNode.getData().toStringValue(HAPSerializationFormat.JSON)));
			attachs.add(atta);
		}
		return attachs;
	}

	
	private List<HAPAttachment> buildService() {
		List<HAPAttachment> attachs = new ArrayList<HAPAttachment>();
		Set<HAPStoryNode> serviceNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_SERVICE);
		for(HAPStoryNode node : serviceNodes) {
			HAPStoryNodeService serviceNode = (HAPStoryNodeService)node;
			//build service attachment
			HAPAttachmentReference refAttr = new HAPAttachmentReference(new HAPResourceIdSimple(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE, serviceNode.getReferenceId()));
			refAttr.setName(serviceNode.getName());
			attachs.add(refAttr);
		}
		return attachs;
	}
	
	private List<HAPDefinitionServiceUse> buildServiceUse() {
		List<HAPDefinitionServiceUse> out = new ArrayList<HAPDefinitionServiceUse>();
		
		Set<HAPStoryNode> serviceNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_SERVICE);
		for(HAPStoryNode node : serviceNodes) {
			HAPStoryNodeService serviceNode = (HAPStoryNodeService)node;
			HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
			
			String serviceName = node.getName();
			serviceUseDef.setProvider(serviceName);
			serviceUseDef.setName(serviceName);
			
			{
				//input
				HAPContext serviceParmMapping = new HAPContext();
				HAPStoryNode serviceInputNode = HAPUtilityStory.getChildNode(serviceNode, HAPConstant.SERVICE_CHILD_INPUT, this.m_story);
				if(serviceInputNode!=null) {
					List<HAPInfoNodeChild> parmNodeInfos = HAPUtilityStory.getChildNode(serviceInputNode, this.m_story);
					for(HAPInfoNodeChild parmNodeInfo : parmNodeInfos) {
						HAPStoryNode parmNode = parmNodeInfo.getChildNode();
						List<HAPConnectionEnd> varsEnd =  HAPUtilityStory.getConnectionEnd(parmNode, HAPConstant.STORYCONNECTION_TYPE_DATAIO, HAPConstant.STORYNODE_PROFILE_DATAIN, HAPConstant.STORYNODE_TYPE_VARIABLE, HAPConstant.STORYNODE_PROFILE_DATAOUT, this.m_story);
						for(HAPConnectionEnd varEnd : varsEnd) {
							HAPStoryNode parmInputNode = this.m_story.getNode(varEnd.getNodeId());
							String inputNodeType = parmInputNode.getType();
							if(HAPConstant.STORYNODE_TYPE_CONSTANT.equals(inputNodeType)) {
								
							}
							else if(HAPConstant.STORYNODE_TYPE_VARIABLE.equals(inputNodeType)) {
								HAPStoryNodeVariable varInputNode = (HAPStoryNodeVariable)parmInputNode;
								serviceParmMapping.addElement(parmNodeInfo.getConnection().getChildId(), new HAPContextDefinitionLeafRelative(varInputNode.getVariableName()));
							}
						}
					}
					if(!serviceParmMapping.isEmpty()) {
						HAPDefinitionDataAssociationMapping inputMapping = new HAPDefinitionDataAssociationMapping();
						inputMapping.addAssociation(null, serviceParmMapping);
						serviceUseDef.getServiceMapping().setInputMapping(inputMapping);
					}
				}
			}
			
			{
				//output
				HAPContext serviceParmMapping = new HAPContext();
				HAPStoryNode serviceResultNode = HAPUtilityStory.getChildNode(serviceNode, HAPConstant.SERVICE_CHILD_RESULT, this.m_story);
				if(serviceResultNode!=null) {
					List<HAPInfoNodeChild> parmNodeInfos = HAPUtilityStory.getChildNode(serviceResultNode, this.m_story);
					for(HAPInfoNodeChild parmNodeInfo : parmNodeInfos) {
						HAPStoryNode parmNode = parmNodeInfo.getChildNode();
						List<HAPConnectionEnd> varsEnd =  HAPUtilityStory.getConnectionEnd(parmNode, HAPConstant.STORYCONNECTION_TYPE_DATAIO, HAPConstant.STORYNODE_PROFILE_DATAOUT, HAPConstant.STORYNODE_TYPE_VARIABLE, HAPConstant.STORYNODE_PROFILE_DATAIN, this.m_story);
						for(HAPConnectionEnd varEnd : varsEnd) {
							HAPStoryNode parmInputNode = this.m_story.getNode(varEnd.getNodeId());
							String inputNodeType = parmInputNode.getType();
							if(HAPConstant.STORYNODE_TYPE_VARIABLE.equals(inputNodeType)) {
								HAPStoryNodeVariable varInputNode = (HAPStoryNodeVariable)parmInputNode;
								serviceParmMapping.addElement(varInputNode.getVariableName(), new HAPContextDefinitionLeafRelative(parmNodeInfo.getConnection().getChildId()));
							}
						}
					}
				}
				if(!serviceParmMapping.isEmpty()) {
					HAPDefinitionDataAssociationMapping outputMapping = new HAPDefinitionDataAssociationMapping();
					outputMapping.addAssociation(null, serviceParmMapping);
					serviceUseDef.getServiceMapping().addOutputMapping("success", outputMapping);
				}
			}
			if(serviceUseDef.getServiceMapping().getInputMapping()!=null || serviceUseDef.getServiceMapping().getOutputMapping()!=null) {
				out.add(serviceUseDef);
			}
		}
		return out;
	}
	
	private HAPHtmlSegment buildPage() {
		HAPHtmlSegment out = new HAPHtmlSegment();
		HAPStoryNode pageNode = null;
		for(HAPStoryNode node : HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstant.STORYNODE_TYPE_PAGE)) {
			pageNode = node;
			break;
		}
		
		List<HAPInfoNodeChild> childrenNodesInfo =  HAPUtilityStory.getChildNode(pageNode, this.m_story);
		for(HAPInfoNodeChild childNodeInfo : childrenNodesInfo) {
			HAPHtml html = this.buildUI(childNodeInfo.getChildNode());
			out.addSegment(html);
		}
		return out;
	}
	
	private HAPHtml buildUI(HAPStoryNode uiNode) {
		HAPHtml out = null;
		
		if(uiNode.isEnable()) {
			String uiNodeType = uiNode.getType();
			if(HAPConstant.STORYNODE_TYPE_UIDATA.equals(uiNodeType)) {
				HAPStoryNodeUIData uiDataNode = (HAPStoryNodeUIData)uiNode;
				HAPHtmlTag tag = new HAPHtmlTag(uiDataNode.getTagName());
				List<HAPConnectionEnd> varEnds = HAPUtilityStory.getConnectionEnd(uiNode, HAPConstant.STORYCONNECTION_TYPE_DATAIO, null, HAPConstant.STORYNODE_TYPE_VARIABLE, null, this.m_story);
				for(HAPConnectionEnd varEnd : varEnds) {
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)this.m_story.getNode(varEnd.getNodeId());
					tag.addAttribute(new HAPTagAttribute("data", varNode.getVariableName()));
				}

				List<HAPInfoNodeChild> childrenNodesInfo =  HAPUtilityStory.getChildNode(uiNode, this.m_story);
				for(HAPInfoNodeChild childNodeInfo : childrenNodesInfo) {
					HAPHtml html = this.buildUI(childNodeInfo.getChildNode());
					if(html!=null)	tag.addBodySegment(html);
				}
				out = tag;
			}
		}
		return out;
	}
}
