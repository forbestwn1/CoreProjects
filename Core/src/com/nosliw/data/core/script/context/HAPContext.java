package com.nosliw.data.core.script.context;

import java.util.HashSet;
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
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPContext extends HAPSerializableImp implements HAPContextStructure{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, HAPContextDefinitionRoot> m_elements;
	
	public HAPContext(){
		this.empty();
	}

	@Override
	public String getType() {	return HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT;	}

	@Override
	public boolean isFlat() {	return true;	}

	@Override
	public HAPContextDefinitionRoot getElement(String name) {  return this.m_elements.get(name);   }

	@Override
	public void hardMergeWith(HAPContextStructure parent){
		if(parent!=null) {
			if(parent.getType().equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPContext context  = (HAPContext)parent;
				Map<String, HAPContextDefinitionRoot> eles = context.getElements();
				for(String rootName : eles.keySet()){
					this.m_elements.put(rootName, eles.get(rootName));
				}
			}
			else  throw new RuntimeException();
		}
	}

	public void empty() {
		this.m_elements = new LinkedHashMap<String, HAPContextDefinitionRoot>();
	}

	//mark all the element in context as processed
	public void processed() {   
		for(HAPContextDefinitionRoot ele : this.m_elements.values()) 	ele.getDefinition().processed();
	}

	public Set<String> getElementNames(){  return this.m_elements.keySet();   }
	public Map<String, HAPContextDefinitionRoot> getElements(){  return this.m_elements;  }
	public void addElement(String id, HAPContextDefinitionRoot rootEle){
		rootEle.setId(id);
		this.m_elements.put(id, rootEle);	
	}
	public void addElement(HAPContextDefinitionRoot rootEle){	this.m_elements.put(rootEle.getId(), rootEle);	}
	public void addElement(String name, HAPContextDefinitionElement contextEle) {   this.m_elements.put(name, new HAPContextDefinitionRoot(contextEle));  }
	
	public void updateRootName(HAPUpdateName nameUpdate) {
		//update context
		for(String eleName : new HashSet<String>(this.getElementNames())) {
			HAPContextDefinitionRoot root = this.getElement(eleName);
			root.setId(nameUpdate.getUpdatedName(root.getId()));
			HAPUtilityContext.processContextDefElement(root.getDefinition(), new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele instanceof HAPContextDefinitionLeafRelative) {
						HAPContextDefinitionLeafRelative relative = (HAPContextDefinitionLeafRelative)ele;
						if(HAPConstant.DATAASSOCIATION_RELATEDENTITY_SELF.equals(relative.getParent())) {
							//update local relative path
							HAPContextPath path = relative.getPath();
							relative.setPath(new HAPContextPath(new HAPContextDefinitionRootId(path.getRootElementId().getCategary(), nameUpdate.getUpdatedName(path.getRootElementId().getName())), path.getSubPath()));
						}
					}
					return true;
				}

				@Override
				public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
					return true;
				}}, null);
			//update root name
			this.m_elements.remove(eleName);
			this.addElement(nameUpdate.getUpdatedName(eleName), root);
		}
	}
	
	public void updateReferenceName(HAPUpdateName nameUpdate) {
		//update context
		for(String eleName : new HashSet<String>(this.getElementNames())) {
			HAPContextDefinitionRoot root = this.getElement(eleName);
			HAPUtilityContext.processContextDefElement(root.getDefinition(), new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele instanceof HAPContextDefinitionLeafRelative) {
						HAPContextDefinitionLeafRelative relative = (HAPContextDefinitionLeafRelative)ele;
						if(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT.equals(relative.getParent())) {
							//update local relative path
							HAPContextPath path = relative.getPath();
							relative.setPath(new HAPContextPath(new HAPContextDefinitionRootId(path.getRootElementId().getCategary(), nameUpdate.getUpdatedName(path.getRootElementId().getName())), path.getSubPath()));
						}
					}
					return true;
				}

				@Override
				public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
					return true;
				}}, null);
		}
	}

	public HAPContext toSolidContext() {
		HAPContext out = new HAPContext();
		for(String name :this.getElementNames()) {
			out.addElement(name, this.getElement(name).getDefinition().getSolidContextDefinitionElement());
		}
		return out;
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
				HAPContextDefinitionElement outEle = contextRoot.getDefinition().getSolidContextDefinitionElement();
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

	@Override
	public HAPContextStructure cloneContextStructure() {	return this.cloneContext();	}

	public HAPContext cloneContextBase() {
		HAPContext out = new HAPContext();
		return out;
	}
	
	public HAPContext cloneContext() {
		HAPContext out = this.cloneContextBase();
		this.toContext(out);
		return out;
	}
	
	public void toContext(HAPContext out) {
		out.empty();
		for(String name : this.m_elements.keySet()) {
			out.addElement(name, this.m_elements.get(name).cloneContextDefinitionRoot());
		}
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
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(m_elements, HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPContext) {
			HAPContext context = (HAPContext)obj;
			if(context.getElementNames().equals(this.getElementNames())) {
				for(String eleName : this.getElementNames()) {
					out = this.getElement(eleName).equals(context.getElement(eleName));
					if(!out)  
						break;
				}
				return true;
			}
		}
		return out;
	}
}
