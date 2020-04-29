package com.nosliw.uiresource.page.template;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.service.provide.HAPDefinitionService;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.template.HAPBuilderResourceDefinition;
import com.nosliw.data.core.template.HAPOutputBuilder;
import com.nosliw.data.core.template.HAPParmDefinition;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPBuilderPageSimple extends HAPEntityInfoImp implements HAPBuilderResourceDefinition{

	private HAPManagerServiceDefinition m_serviceDefMan;
	
	private HAPParserPage m_pageParser;
	
	private HAPUITagManager m_tagManager;
	
	@Override
	public HAPOutputBuilder build(Set<HAPParmDefinition> parms) {

		String serviceId = "";
		
		HAPDefinitionService serviceDef = this.m_serviceDefMan.getDefinition(serviceId);
		HAPServiceInterface serviceInterface = serviceDef.getStaticInfo().getInterface();
		
		HAPHtmlSegment htmlContent = this.buildHtml(serviceInterface);
		HAPContext context = this.buildContext(serviceInterface);
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("htmlContent", htmlContent.toString());
		templateParms.put("serviceId", serviceId);
		templateParms.put("context", context.toStringValue(HAPSerializationFormat.JSON));
		
		InputStream pageTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPBuilderPageSimple.class, "page_framework.temp");
		String pageContext = HAPStringTemplateUtil.getStringValue(pageTemplateStream, templateParms);

		HAPDefinitionUIPage pageDef = this.m_pageParser.parseUIDefinition(null, pageContext);
		
		HAPOutputBuilder out = new HAPOutputBuilder();
		out.setResourceDefinition(pageDef);
		return out;
	}

	private HAPHtmlSegment buildHtml(HAPServiceInterface serviceInterface) {
		HAPHtmlSegment out = new HAPHtmlSegment();
		
		
		
		return out;
	}
	
	private HAPHtmlSegment buildHtml(HAPServiceResult serviceResult) {
		serviceResult.get
	}
	
	private HAPContext buildContext(HAPServiceInterface serviceInterface) {
		return new HAPContext();
	}
	
	private HAPHtmlSegment buildHtml(HAPDataTypeCriteria dataTypeCriteria) {
		
	}

	@Override
	public String getResourceType() {  return HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE;  }
	
}
