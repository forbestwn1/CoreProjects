package com.nosliw.uiresource.page.story.design;

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
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPValueMapping;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReferenceExternal;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.story.HAPBuilderShow;
import com.nosliw.data.core.story.HAPConnectionEnd;
import com.nosliw.data.core.story.HAPInfoNodeChild;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.element.node.HAPStoryNodeConstant;
import com.nosliw.data.core.story.element.node.HAPStoryNodeScript;
import com.nosliw.data.core.story.element.node.HAPStoryNodeService;
import com.nosliw.data.core.story.element.node.HAPStoryNodeVariable;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIHtml;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUITag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPBuilderPageSimple extends HAPEntityInfoImp implements HAPBuilderShow{

	private HAPManagerServiceDefinition m_serviceDefMan;
	
	private HAPParserPage m_pageParser;
	
	private HAPManagerUITag m_tagManager;
	
	private HAPStory m_story;
	
	private HAPValueStructureDefinitionFlat m_valueStructure;
	
	public HAPBuilderPageSimple(HAPManagerServiceDefinition serviceDefMan, HAPManagerUITag tagManager, HAPParserPage pageParser) {
		this.m_serviceDefMan = serviceDefMan;
		this.m_tagManager = tagManager;
		this.m_pageParser = pageParser;
	}

	@Override
	public HAPResourceDefinition1 buildShow(HAPStory story) {
		this.m_story = story;
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		//constant
		HAPValueStructureDefinitionFlat valueStructure = buildValueStructure();
		this.m_valueStructure = valueStructure;
		templateParms.put("valuestructure", valueStructure.toStringValue(HAPSerializationFormat.JSON));
		
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
		
		//script
		String script = this.buildScript();
		templateParms.put("script", script);
		
		InputStream pageTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPBuilderPageSimple.class, "page_framework.temp");
		String pageContent = HAPStringTemplateUtil.getStringValue(pageTemplateStream, templateParms);

		HAPDefinitionUIUnitPage pageDef = null;
		try {
			pageDef = this.m_pageParser.parseUIDefinition(null, pageContent);
		}
		catch(Exception e){
			pageDef = new HAPDefinitionUIUnitPage(null, pageContent);
			e.printStackTrace();
		}
		return pageDef;
	}
	
	private String buildScript() {
		StringBuffer out = new StringBuffer();
		Set<HAPStoryNode> scriptNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstantShared.STORYNODE_TYPE_SCRIPT);
		for(HAPStoryNode node : scriptNodes) {
			HAPStoryNodeScript scriptNode = (HAPStoryNodeScript)node;
			out.append("\n");
			out.append(scriptNode.getScript());
			out.append("\n");
		}
		return out.toString();
	}
	
	private HAPValueStructureDefinitionFlat buildValueStructure() {
		HAPValueStructureDefinitionFlat valueStructure = new HAPValueStructureDefinitionFlat();
		Set<HAPStoryNode> varNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstantShared.STORYNODE_TYPE_VARIABLE);
		for(HAPStoryNode node : varNodes) {
			HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)node;
			HAPVariableInfo varInfo = varNode.getVariableInfo();
			HAPRootStructure contextEle = new HAPRootStructure(new HAPElementStructureLeafData(varInfo.getDataInfo()));
			contextEle.setDefaultValue(varInfo.getDefaultValue());
			contextEle.setName(varNode.getVariableInfo().getName());
			valueStructure.addRoot(contextEle);
		}
		return valueStructure;
	}

	private List<HAPAttachment> buildConstant() {
		List<HAPAttachment> attachs = new ArrayList<HAPAttachment>();
		Set<HAPStoryNode> constantNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstantShared.STORYNODE_TYPE_CONSTANT);
		for(HAPStoryNode node : constantNodes) {
			HAPStoryNodeConstant constantNode = (HAPStoryNodeConstant)node;
			HAPData constantData = constantNode.getData();
			if(constantData!=null) {
				//build service attachment
				HAPAttachmentEntity atta = new HAPAttachmentEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE);
				atta.setEntity(new JSONObject(constantData.toStringValue(HAPSerializationFormat.JSON)));
				attachs.add(atta);
			}
		}
		return attachs;
	}

	
	private List<HAPAttachment> buildService() {
		List<HAPAttachment> attachs = new ArrayList<HAPAttachment>();
		Set<HAPStoryNode> serviceNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstantShared.STORYNODE_TYPE_SERVICE);
		for(HAPStoryNode node : serviceNodes) {
			HAPStoryNodeService serviceNode = (HAPStoryNodeService)node;
			//build service attachment
			HAPAttachmentReference refAttr = new HAPAttachmentReferenceExternal(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, serviceNode.getReferenceId()));
			refAttr.setName(serviceNode.getName());
			attachs.add(refAttr);
		}
		return attachs;
	}
	
	private List<HAPDefinitionServiceUse> buildServiceUse() {
		List<HAPDefinitionServiceUse> out = new ArrayList<HAPDefinitionServiceUse>();
		
		Set<HAPStoryNode> serviceNodes = HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstantShared.STORYNODE_TYPE_SERVICE);
		for(HAPStoryNode node : serviceNodes) {
			HAPStoryNodeService serviceNode = (HAPStoryNodeService)node;
			HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
			
			String serviceName = node.getName();
			serviceUseDef.setProvider(serviceName);
			serviceUseDef.setName("service");
			
			{
				//input
				HAPValueMapping serviceParmMapping = new HAPValueMapping();
				List<HAPStoryNode> serviceInputNodes = HAPUtilityStory.getChildNode(serviceNode, HAPConstantShared.SERVICE_CHILD_INPUT, this.m_story);
				if(!serviceInputNodes.isEmpty()) {
					HAPStoryNode serviceInputNode = serviceInputNodes.get(0); 
					List<HAPInfoNodeChild> parmNodeInfos = HAPUtilityStory.getChildNode(serviceInputNode, this.m_story);
					for(HAPInfoNodeChild parmNodeInfo : parmNodeInfos) {
						HAPStoryNode parmNode = parmNodeInfo.getChildNode();
						List<HAPConnectionEnd> varsEnd =  HAPUtilityStory.getConnectionEnd(parmNode, HAPConstantShared.STORYCONNECTION_TYPE_DATAIO, HAPConstantShared.STORYNODE_PROFILE_DATAIN, null, HAPConstantShared.STORYNODE_PROFILE_DATAOUT, this.m_story);
						for(HAPConnectionEnd varEnd : varsEnd) {
							HAPStoryNode parmInputNode = (HAPStoryNode)this.m_story.getElement(varEnd.getNodeRef());
							if(parmInputNode.isEnable()) {
								String inputNodeType = parmInputNode.getType();
								if(HAPConstantShared.STORYNODE_TYPE_CONSTANT.equals(inputNodeType)) {
									HAPStoryNodeConstant constantInputNode = (HAPStoryNodeConstant)parmInputNode;
									HAPData constantData = constantInputNode.getData();
									if(constantData!=null) {
										serviceParmMapping.addItem(parmNodeInfo.getConnection().getChildId(), new HAPRootStructure(new HAPElementStructureLeafConstant(constantData)));
									}
								}
								else if(HAPConstantShared.STORYNODE_TYPE_VARIABLE.equals(inputNodeType)) {
									HAPStoryNodeVariable varInputNode = (HAPStoryNodeVariable)parmInputNode;
									serviceParmMapping.addItem(parmNodeInfo.getConnection().getChildId(), new HAPRootStructure(new HAPElementStructureLeafRelative(varInputNode.getVariableInfo().getName())));
								}
							}
						}
					}
					if(!serviceParmMapping.isEmpty()) {
						HAPDefinitionDataAssociationMapping inputMapping = new HAPDefinitionDataAssociationMapping();
						inputMapping.addAssociation(null, serviceParmMapping);
						serviceUseDef.getDataAssociations().setInDataAssociation(inputMapping);
					}
				}
			}
			
			{
				//output
				HAPValueMapping serviceParmMapping = new HAPValueMapping();
				List<HAPStoryNode> serviceResultNodes = HAPUtilityStory.getChildNode(serviceNode, HAPConstantShared.SERVICE_CHILD_RESULT, this.m_story);
				if(!serviceResultNodes.isEmpty()) {
					HAPStoryNode serviceResultNode = serviceResultNodes.get(0);
					List<HAPInfoNodeChild> parmNodeInfos = HAPUtilityStory.getChildNode(serviceResultNode, this.m_story);
					for(HAPInfoNodeChild parmNodeInfo : parmNodeInfos) {
						HAPStoryNode parmNode = parmNodeInfo.getChildNode();
						List<HAPConnectionEnd> varsEnd =  HAPUtilityStory.getConnectionEnd(parmNode, HAPConstantShared.STORYCONNECTION_TYPE_DATAIO, HAPConstantShared.STORYNODE_PROFILE_DATAOUT, HAPConstantShared.STORYNODE_TYPE_VARIABLE, HAPConstantShared.STORYNODE_PROFILE_DATAIN, this.m_story);
						for(HAPConnectionEnd varEnd : varsEnd) {
							HAPStoryNode parmInputNode = (HAPStoryNode)this.m_story.getElement(varEnd.getNodeRef());
							String inputNodeType = parmInputNode.getType();
							if(HAPConstantShared.STORYNODE_TYPE_VARIABLE.equals(inputNodeType)) {
								HAPStoryNodeVariable varInputNode = (HAPStoryNodeVariable)parmInputNode;
								serviceParmMapping.addItem(varInputNode.getVariableInfo().getName(), new HAPRootStructure(new HAPElementStructureLeafRelative(parmNodeInfo.getConnection().getChildId())));
							}
						}
					}
				}
				if(!serviceParmMapping.isEmpty()) {
					HAPDefinitionDataAssociationMapping outputMapping = new HAPDefinitionDataAssociationMapping();
					outputMapping.addAssociation(null, serviceParmMapping);
					serviceUseDef.getDataAssociations().addOutDataAssociation("success", outputMapping);
				}
			}
			if(serviceUseDef.getDataAssociations().getInDataAssociation()!=null || serviceUseDef.getDataAssociations().getOutDataAssociations()!=null) {
				out.add(serviceUseDef);
			}
		}
		return out;
	}
	
	private HAPHtmlSegment buildPage() {
		HAPHtmlSegment out = new HAPHtmlSegment();
		HAPStoryNode pageNode = null;
		for(HAPStoryNode node : HAPUtilityStory.getStoryNodeByType(this.m_story, HAPConstantShared.STORYNODE_TYPE_PAGE)) {
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
			if(HAPConstantShared.STORYNODE_TYPE_UIDATA.equals(uiNodeType)||HAPConstantShared.STORYNODE_TYPE_UITAGOTHER.equals(uiNodeType)) {
				HAPStoryNodeUITag uiDataNode = (HAPStoryNodeUITag)uiNode;
				HAPHtmlTag tag = new HAPHtmlTag(uiDataNode.getTagName());
				
				//attributes
				Map<String, String> attrs =  uiDataNode.getAttributes();
				for(String attrName : attrs.keySet()) {
					tag.addAttribute(new HAPTagAttribute(attrName, attrs.get(attrName)));
				}
				
				//child node
				List<HAPInfoNodeChild> childrenNodesInfo =  HAPUtilityStory.getChildNode(uiNode, this.m_story);
				for(HAPInfoNodeChild childNodeInfo : childrenNodesInfo) {
					HAPHtml html = this.buildUI(childNodeInfo.getChildNode());
					if(html!=null)	tag.addBodySegment(html);
				}
				out = tag;
			}
			else if(HAPConstantShared.STORYNODE_TYPE_HTML.equals(uiNodeType)) {
				HAPStoryNodeUIHtml uiHtmlNode = (HAPStoryNodeUIHtml)uiNode;
				String html = uiHtmlNode.getHtml();
				
				Map<String, StringBuffer> placeHolders = new LinkedHashMap<String, StringBuffer>();
				List<HAPInfoNodeChild> childrenNodesInfo =  HAPUtilityStory.getChildNode(uiNode, this.m_story);
				for(HAPInfoNodeChild childNodeInfo : childrenNodesInfo) {
					String childId = childNodeInfo.getConnection().getChildId();
					StringBuffer text = placeHolders.get(childId);
					if(text==null) {
						text = new StringBuffer();
						placeHolders.put(childId, text);
					}
					text.append(this.buildUI(childNodeInfo.getChildNode()).toString());
				}
				
				for(String key : placeHolders.keySet()) {
					html = html.replace("{{"+key+"}}", placeHolders.get(key).toString());
				}
				
				//remove idle place holder
				int startI = html.indexOf("{{");
				while(startI!=-1) {
					int endI = html.indexOf("}}");
					String placeHolderStr = html.substring(startI, endI+2);
					html = html.replace(placeHolderStr, "");
					startI = html.indexOf("{{");
				}
				
				HAPHtmlText textHtml = new HAPHtmlText(html);
				out = textHtml;
			}
		}
		return out;
	}
}
