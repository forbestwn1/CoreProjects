package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, HAPContextDefinitionRoot> m_elements;
	
	public HAPContext(){
		this.empty();
	}
	
	public void empty() {
		this.m_elements = new LinkedHashMap<String, HAPContextDefinitionRoot>();
	}
	
	public Set<String> getElementNames(){  return this.m_elements.keySet();   }
	public Map<String, HAPContextDefinitionRoot> getElements(){  return this.m_elements;  }
	public HAPContextDefinitionRoot getElement(String name) {  return this.m_elements.get(name);   }
	public void addElement(String name, HAPContextDefinitionRoot rootEle){	this.m_elements.put(name, rootEle);	}
	
	public void hardMergeWith(HAPContext context){
		Map<String, HAPContextDefinitionRoot> eles = context.getElements();
		for(String rootName : eles.keySet()){
			this.m_elements.put(rootName, eles.get(rootName));
		}
	}

	//find all constants in context, including constants defined in leaf
	public Map<String, Object> getConstantValue(){
		Map<String, Object> out = new LinkedHashMap<String, Object>();

		for(String name : this.m_elements.keySet()) {
			HAPContextDefinitionRoot contextRoot = this.getElement(name);
			HAPUtilityContext.processContextDefElementWithPathInfo(contextRoot.getDefinition(), new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object path) {
					if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT)) {
						HAPContextDefinitionLeafConstant constantEle = (HAPContextDefinitionLeafConstant)ele;
						Object value = constantEle.getDataValue();
						if(value==null)   value = constantEle.getValue();
						out.put((String)path, value);
					}
					return true;
				}

				@Override
				public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
					return true;
				}
			}, name);
		}

		
		for(String name : this.m_elements.keySet()) {
			HAPContextDefinitionRoot contextRoot = this.getElement(name);
			if(contextRoot.isConstant()) {
				HAPContextDefinitionLeafConstant constantRootNode = (HAPContextDefinitionLeafConstant)contextRoot.getDefinition();
				Object value = constantRootNode.getDataValue();
				if(value==null)   value = constantRootNode.getValue();
				out.put(name, value);
			}
		}
		return out;
	}
	
	//build another context which only include variable node in current context
	public HAPContext getVariableContext() {
		HAPContext out = new HAPContext();
		for(String name : this.m_elements.keySet()) {
			HAPContextDefinitionRoot contextRoot = this.getElement(name);
			if(!contextRoot.isConstant()) {
				out.addElement(name, contextRoot.cloneContextDefinitionRoot());
			}			
		}
		return out;
	}
	
	//discover child node according to path
	//may not find exact match child node according to path
	//   return[0]   base node
	//   return[1]   closest child node
	//   return[2]   remaining path
	public void discoverChild(String rootName, String path, HAPInfoRelativeContextResolve resolved){
		HAPContextDefinitionRoot contextRoot = this.m_elements.get(rootName);
		resolved.rootNode = contextRoot;
		if(contextRoot!=null) {
			if(contextRoot.isConstant()) {
				resolved.referedNode = null;
				resolved.remainPath = path;
			}
			else {
				HAPContextDefinitionElement outEle = contextRoot.getDefinition();
				String remainingPath = null;
				String[] pathSegs = HAPNamingConversionUtility.parseComponentPaths(path);
				for(String pathSeg : pathSegs){
					if(remainingPath==null) {
						HAPContextDefinitionElement ele = null;
						if(HAPConstant.CONTEXT_ELEMENTTYPE_NODE.equals(outEle.getType())) 	ele = ((HAPContextDefinitionNode)outEle).getChildren().get(pathSeg);
						if(ele==null) 		remainingPath = pathSeg;
						else 	outEle = ele;
					}
					else {
						remainingPath = HAPNamingConversionUtility.cascadePath(remainingPath, pathSeg);
					}
				}
				resolved.referedNode = outEle;
				resolved.remainPath = remainingPath;
			}
		}
	}

	public HAPContext cloneContextBase() {
		HAPContext out = new HAPContext();
		return out;
	}
	
	public HAPContext cloneContext() {
		HAPContext out = this.cloneContextBase();
		for(String name : this.m_elements.keySet()) {
			out.addElement(name, this.m_elements.get(name).cloneContextDefinitionRoot());
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			HAPParserContext.parseContext(jsonObj, this);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(m_elements, HAPSerializationFormat.JSON));
	}
}
