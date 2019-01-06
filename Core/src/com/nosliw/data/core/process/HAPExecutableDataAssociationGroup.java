package com.nosliw.data.core.process;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionNode;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextPath;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociationGroup extends HAPSerializableImp implements HAPExecutable{

	@HAPAttribute
	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String PATHMAPPING = "pathMapping";

	@HAPAttribute
	public static String CONVERTFUNCTION = "convertFunction";
	
	private HAPDefinitionDataAssociationGroup m_definition;
	
	//process purpose
	private HAPContextFlat m_context;
	
	//mapping from in path to out path, it is for runtime 
	private Map<String, String> m_pathMapping;
	
	public HAPExecutableDataAssociationGroup(HAPDefinitionDataAssociationGroup definition) {
		this.m_definition = definition;
	}

	public HAPContextFlat getContext() {   return this.m_context;   }
	public void setContext(HAPContextFlat context) {   this.m_context = context;   }

	public void setPathMapping(Map<String, String> mapping) {    this.m_pathMapping = mapping;    }
	public Map<String, String> getPathMapping() {  return this.m_pathMapping;  }

	//update output root name
	public void updateOutputRootName(HAPUpdateName nameUpdate) {
		//update path mapping
		Map<String, String> processedPathMapping = new LinkedHashMap<String, String>();
		for(String p1 : m_pathMapping.keySet()) {
			HAPContextPath cPath = new HAPContextPath(m_pathMapping.get(p1));
			HAPContextPath cPath1 = new HAPContextPath(new HAPContextDefinitionRootId(nameUpdate.getUpdatedName(cPath.getRootElementId().getFullName())), cPath.getSubPath());
			processedPathMapping.put(p1, cPath1.getFullPath());
		}
		this.m_pathMapping = processedPathMapping;

		//update context
		this.m_context.updateRootName(nameUpdate);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATHMAPPING, HAPJsonUtility.buildMapJson(m_pathMapping));
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		jsonMap.put(CONVERTFUNCTION, this.buildConvertFunction().getScript());
		typeJsonMap.put(CONVERTFUNCTION, HAPScript.class);
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}
		
	private HAPScript buildConvertFunction() {
		//build init output object 
		JSONObject output = new JSONObject();
		HAPContext context = this.m_context.getContext();
		for(String contextEle : context.getElementNames()) {
			Object contextEleJson = this.buildJsonValue(context.getElement(contextEle).getDefinition());

			if(contextEleJson!=null) {
				JSONObject parentJsonObj = output;
				HAPContextDefinitionRootId rootId = new HAPContextDefinitionRootId(contextEle);
				String categary = rootId.getCategary();
				if(HAPBasicUtility.isStringNotEmpty(categary)) {
					parentJsonObj = output.optJSONObject(categary);
					if(parentJsonObj==null) {
						parentJsonObj = new JSONObject();
						output.put(categary, parentJsonObj);
					}
				}
				parentJsonObj.put(rootId.getName(), contextEleJson);
			}
		}
		
		//build dynamic part 
		StringBuffer dynamicScript = new StringBuffer();
		for(String fromPath : this.m_pathMapping.keySet()) {
			String toPath = this.m_pathMapping.get(fromPath);
			String script = "output = utilFunction.setValue(output, "+ buildJSArrayFromContextPath(toPath) +", input, "+ buildJSArrayFromContextPath(fromPath) +");\n";
			dynamicScript.append(script);
		}
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));
		templateParms.put("outputDyanimicValueBuild", dynamicScript.toString());

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(this.getClass(), "DataAssociationFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPScript(script);
	}

	private String buildJSArrayFromContextPath(String path) {
		List<String> pathSegs = new ArrayList<String>();
		HAPContextPath contextPath = new HAPContextPath(path);
		if(HAPBasicUtility.isStringNotEmpty(contextPath.getRootElementId().getCategary()))  pathSegs.add(contextPath.getRootElementId().getCategary());
		pathSegs.add(contextPath.getRootElementId().getName());
		pathSegs.addAll(Arrays.asList(contextPath.getPathSegments()));
		String pathSegsStr = HAPJsonUtility.buildArrayJson(pathSegs.toArray(new String[0]));
		return pathSegsStr;
	}
	
	private Object buildJsonValue(HAPContextDefinitionElement contextDefEle) {
		switch(contextDefEle.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
		{
			HAPContextDefinitionLeafConstant constantEle = (HAPContextDefinitionLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
		{
			HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)contextDefEle;
			JSONObject out = new JSONObject();
			for(String childName : nodeEle.getChildren().keySet()) {
				Object childJsonValue = this.buildJsonValue(nodeEle.getChild(childName));
				if(childJsonValue!=null) {
					out.put(childName, childJsonValue);
				}
			}
			return out;
		}
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
		{
			HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)contextDefEle;
			return new JSONObject();
		}
		default:
			return null;
		}
	}
	
	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		return null;
	}
}
