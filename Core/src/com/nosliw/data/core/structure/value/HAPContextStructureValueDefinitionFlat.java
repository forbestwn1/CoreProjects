package com.nosliw.data.core.structure.value;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPElementNode;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityContext;

@HAPEntityWithAttribute
public class HAPContextStructureValueDefinitionFlat extends HAPSerializableImp implements HAPContextStructureValueDefinition{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, HAPRoot> m_elements;
	
	public HAPContextStructureValueDefinitionFlat(){
		this.empty();
	}

	@Override
	public String getType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT;	}

	@Override
	public boolean isFlat() {	return true;	}

	@Override
	public boolean isEmpty() {  return this.m_elements.isEmpty(); }

	@Override
	public HAPRoot getElement(String name, boolean createIfNotExist) {  
		HAPRoot out = this.m_elements.get(name);
		if(createIfNotExist && out==null) 	out = this.addElement(name);
		return out;
	}

	public HAPRoot getElement(String name) {    return this.m_elements.get(name);   }
	
	@Override
	public void hardMergeWith(HAPContextStructureValueDefinition parent){
		if(parent!=null) {
			if(parent.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPContextStructureValueDefinitionFlat context  = (HAPContextStructureValueDefinitionFlat)parent;
				Map<String, HAPRoot> eles = context.getElements();
				for(String rootName : eles.keySet()){
					this.addElement(rootName, eles.get(rootName).cloneContextDefinitionRoot());
				}
			}
			else  throw new RuntimeException();
		}
	}

	public void empty() {
		this.m_elements = new LinkedHashMap<String, HAPRoot>();
	}

	//mark all the element in context as processed
	public void processed() {   
		for(HAPRoot ele : this.m_elements.values()) 	ele.getDefinition().processed();
	}

	public Set<String> getElementNames(){  return this.m_elements.keySet();   }
	public Map<String, HAPRoot> getElements(){  return this.m_elements;  }
	
	
	public HAPRoot addElement(String name, String localId, HAPRoot rootEle) {
		rootEle.setName(name);
		rootEle.setLocalId(localId);
		this.m_elements.put(name, rootEle);	
		return rootEle;
	}
	
	public HAPRoot addElement(String name, HAPRoot rootEle){	return this.addElement(name, name, rootEle);	}
	public HAPRoot addElement(String name) {  return this.addElement(name, new HAPRoot());  }
	public HAPRoot addElement(String name, String id, HAPElement contextEle) {   return this.addElement(name, id, new HAPRoot(contextEle));  }
	public HAPRoot addElement(String name, HAPElement contextEle) {   return this.addElement(name, new HAPRoot(contextEle));  }
	
	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		//update context
		for(String eleName : new HashSet<String>(this.getElementNames())) {
			HAPRoot root = this.getElement(eleName);
			root.setName(nameUpdate.getUpdatedName(root.getName()));
			HAPUtilityContext.processContextRootElement(root, eleName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getContextElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getContextElement();
						if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(relative.getParent())) {
							//update local relative path
							HAPReferenceElement path = relative.getPathFormat();
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootStructureId().getCategary(), nameUpdate.getUpdatedName(path.getRootStructureId().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
			//update root name
			this.m_elements.remove(eleName);
			this.addElement(nameUpdate.getUpdatedName(eleName), root);
		}
	}
	
	public void updateReferenceName(HAPUpdateName nameUpdate) {
		//update context
		for(String eleName : new HashSet<String>(this.getElementNames())) {
			HAPRoot root = this.getElement(eleName);
			HAPUtilityContext.processContextRootElement(root, eleName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getContextElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getContextElement();
						if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT.equals(relative.getParent())) {
							//update local relative path
							HAPReferenceElement path = relative.getPathFormat();
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootStructureId().getCategary(), nameUpdate.getUpdatedName(path.getRootStructureId().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
		}
	}

	public HAPContextStructureValueDefinitionFlat toSolidContext() {
		HAPContextStructureValueDefinitionFlat out = new HAPContextStructureValueDefinitionFlat();
		for(String name :this.getElementNames()) {
			out.addElement(name, this.getElement(name).getId(), this.getElement(name).getDefinition().getSolidContextDefinitionElement().cloneContextDefinitionElement());
		}
		return out;
	}
	
	//build another context which only include variable node in current context
	public HAPContextStructureValueDefinitionFlat getVariableContext() {
		HAPContextStructureValueDefinitionFlat out = new HAPContextStructureValueDefinitionFlat();
		for(String name : this.m_elements.keySet()) {
			HAPRoot contextRoot = this.getElement(name);
			if(!contextRoot.isConstant()) {
				out.addElement(name, contextRoot.getId(), contextRoot.cloneContextDefinitionRoot());
			}			
		}
		return out;
	}
	
	//discover child node according to path
	//may not find exact match child node according to path
	//   return[0]   base node
	//   return[1]   closest child node
	//   return[2]   remaining path
	public HAPInfoReferenceResolve discoverChild(String rootName, String path){
		HAPRoot contextRoot = this.m_elements.get(rootName);
		if(contextRoot==null)  return null;
		
		HAPInfoReferenceResolve out = new HAPInfoReferenceResolve(); 
		out.rootNode = contextRoot;
		if(contextRoot.isConstant()) {
			out.referedSolidNode = null;
			out.remainPath = path;
		}
		else {
			HAPElement outSolidNodeEle = contextRoot.getDefinition().getSolidContextDefinitionElement();
			HAPElement outRefNodeEle = contextRoot.getDefinition();
			String remainingPath = null;
			if(HAPBasicUtility.isStringNotEmpty(path)) {
				String[] pathSegs = HAPNamingConversionUtility.parseComponentPaths(path);
				for(String pathSeg : pathSegs){
					if(remainingPath==null) {
						//solid node
						HAPElement solidEle = null;
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(outSolidNodeEle.getType())) {
							solidEle = ((HAPElementNode)outSolidNodeEle).getChildren().get(pathSeg);
						}
						if(solidEle==null) 		remainingPath = pathSeg;
						else{
							outSolidNodeEle = solidEle;
						}

						//real node
						HAPElement refEle = null;
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(outRefNodeEle.getType())) {
							refEle = ((HAPElementNode)outRefNodeEle).getChildren().get(pathSeg);
						}
						if(refEle!=null)  outRefNodeEle = refEle;
					}
					else {
						remainingPath = HAPNamingConversionUtility.cascadePath(remainingPath, pathSeg);
					}
				}
			}
			out.referedNode = outRefNodeEle;
			out.referedSolidNode = outSolidNodeEle;
			out.remainPath = remainingPath;
		}
		return out;
	}

	@Override
	public HAPContextStructureValueDefinition cloneContextStructure() {	return this.cloneContext();	}

	public HAPContextStructureValueDefinitionFlat cloneContextBase() {
		HAPContextStructureValueDefinitionFlat out = new HAPContextStructureValueDefinitionFlat();
		return out;
	}
	
	public HAPContextStructureValueDefinitionFlat cloneContext() {
		HAPContextStructureValueDefinitionFlat out = this.cloneContextBase();
		this.toContext(out);
		return out;
	}
	
	public void toContext(HAPContextStructureValueDefinitionFlat out) {
		out.empty();
		for(String name : this.m_elements.keySet()) {
			out.addElement(name, this.m_elements.get(name).getId(), this.m_elements.get(name).cloneContextDefinitionRoot());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			HAPParserValueStructure.parseparseValueStructureDefinitionFlat(jsonObj, this);
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
		if(obj instanceof HAPContextStructureValueDefinitionFlat) {
			HAPContextStructureValueDefinitionFlat context = (HAPContextStructureValueDefinitionFlat)obj;
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
