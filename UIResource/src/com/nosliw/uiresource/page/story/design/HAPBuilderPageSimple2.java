package com.nosliw.uiresource.page.story.design;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.dynamic.HAPBuilderResourceDefinition;
import com.nosliw.data.core.resource.dynamic.HAPOutputBuilder;
import com.nosliw.data.core.resource.dynamic.HAPParmDefinition;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.service.provide.HAPDefinitionService;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;
import com.nosliw.uiresource.page.tag.HAPUITagQueryResult;
import com.nosliw.uiresource.page.tag.HAPUITageQuery;

public class HAPBuilderPageSimple2 extends HAPEntityInfoImp implements HAPBuilderResourceDefinition{

	private HAPManagerServiceDefinition m_serviceDefMan;
	
	private HAPParserPage m_pageParser;
	
	private HAPUITagManager m_tagManager;
	
	public HAPBuilderPageSimple2(HAPManagerServiceDefinition serviceDefMan, HAPUITagManager tagManager, HAPParserPage pageParser) {
		this.m_serviceDefMan = serviceDefMan;
		this.m_tagManager = tagManager;
		this.m_pageParser = pageParser;
	}
	
	@Override
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
		
		InputStream pageTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPBuilderPageSimple2.class, "page_framework.temp");
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
			HAPUITagQueryResult tagQueryResult = this.m_tagManager.getDefaultUITag(uiTagQuery);
			HAPHtmlTag tagHtml = new HAPHtmlTag(tagQueryResult.getTag());
			tagHtml.addAttribute(new HAPTagAttribute("data", name));
			out.addSegment(tagHtml);
		}
		return out;
	}
	
	private HAPContext buildContext(HAPServiceInterface serviceInterface) {
		
		return new HAPContext();
	}
	
	@Override
	public String getResourceType() {  return HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE;  }
	
	private HAPParmDefinition findParmById(String id, Set<HAPParmDefinition> parms) {
		for(HAPParmDefinition parm : parms) {
			if(id.equals(parm.getId())) {
				return parm;
			}
		}
		return null;
	}
}
