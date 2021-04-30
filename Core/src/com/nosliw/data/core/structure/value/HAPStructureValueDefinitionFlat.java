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
public class HAPStructureValueDefinitionFlat extends HAPSerializableImp implements HAPStructureValueDefinition{

	@HAPAttribute
	public static final String FLAT = "flat";

	private Map<String, HAPRoot> m_roots;
	
	public HAPStructureValueDefinitionFlat(){
		this.empty();
	}

	@Override
	public String getType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT;	}

	@Override
	public boolean isFlat() {	return true;	}

	@Override
	public boolean isEmpty() {  return this.m_roots.isEmpty(); }

	@Override
	public HAPRoot getRoot(String name, boolean createIfNotExist) {  
		HAPRoot out = this.m_roots.get(name);
		if(createIfNotExist && out==null) 	out = this.newRoot(name);
		return out;
	}

	public HAPRoot getRoot(String name) {    return this.m_roots.get(name);   }
	
	@Override
	public void hardMergeWith(HAPStructureValueDefinition parent){
		if(parent!=null) {
			if(parent.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPStructureValueDefinitionFlat context  = (HAPStructureValueDefinitionFlat)parent;
				Map<String, HAPRoot> eles = context.getRoots();
				for(String rootName : eles.keySet()){
					this.addRoot(rootName, eles.get(rootName).cloneRoot());
				}
			}
			else  throw new RuntimeException();
		}
	}

	public void empty() {
		this.m_roots = new LinkedHashMap<String, HAPRoot>();
	}

	//mark all the element in context as processed
	public void processed() {   
		for(HAPRoot ele : this.m_roots.values()) 	ele.getDefinition().processed();
	}

	public Set<String> getRootNames(){  return this.m_roots.keySet();   }
	public Map<String, HAPRoot> getRoots(){  return this.m_roots;  }
	
	
	public HAPRoot addRoot(String name, String localId, HAPRoot root) {
		root.setName(name);
		root.setLocalId(localId);
		this.m_roots.put(name, root);	
		return root;
	}
	
	public HAPRoot addRoot(String name, HAPRoot rootEle){	return this.addRoot(name, name, rootEle);	}

	public HAPRoot addRoot(String name, String id, HAPElement structureEle) {   return this.addRoot(name, id, new HAPRoot(structureEle));  }
	public HAPRoot addRoot(String name, HAPElement structureEle) {   return this.addRoot(name, new HAPRoot(structureEle));  }
	
	public HAPRoot newRoot(String name) {  return this.addRoot(name, new HAPRoot());  }

	@Override
	
	public void updateReferenceName(HAPUpdateName nameUpdate) {
		//update context
		for(String eleName : new HashSet<String>(this.getRootNames())) {
			HAPRoot root = this.getRoot(eleName);
			HAPUtilityContext.processContextRootElement(root, eleName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getElement();
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

	public HAPStructureValueDefinitionFlat toSolidContext() {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		for(String name :this.getRootNames()) {
			out.addRoot(name, this.getRoot(name).getId(), this.getRoot(name).getDefinition().getSolidStructureElement().cloneStructureElement());
		}
		return out;
	}
	
	//build another context which only include variable node in current context
	public HAPStructureValueDefinitionFlat getVariableContext() {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		for(String name : this.m_roots.keySet()) {
			HAPRoot contextRoot = this.getRoot(name);
			if(!contextRoot.isConstant()) {
				out.addRoot(name, contextRoot.getId(), contextRoot.cloneRoot());
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
		HAPRoot contextRoot = this.m_roots.get(rootName);
		if(contextRoot==null)  return null;
		
		HAPInfoReferenceResolve out = new HAPInfoReferenceResolve(); 
		out.referredRoot = contextRoot;
		if(contextRoot.isConstant()) {
			out.referedRealSolidElement = null;
			out.remainPath = path;
		}
		else {
			HAPElement outSolidNodeEle = contextRoot.getDefinition().getSolidStructureElement();
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
			out.referedRealElement = outRefNodeEle;
			out.referedRealSolidElement = outSolidNodeEle;
			out.remainPath = remainingPath;
		}
		return out;
	}

	@Override
	public HAPStructureValueDefinition cloneStructure() {	return this.cloneContext();	}

	public HAPStructureValueDefinitionFlat cloneContextBase() {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		return out;
	}
	
	public HAPStructureValueDefinitionFlat cloneContext() {
		HAPStructureValueDefinitionFlat out = this.cloneContextBase();
		this.toContext(out);
		return out;
	}
	
	public void toContext(HAPStructureValueDefinitionFlat out) {
		out.empty();
		for(String name : this.m_roots.keySet()) {
			out.addRoot(name, this.m_roots.get(name).getId(), this.m_roots.get(name).cloneRoot());
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
		jsonMap.put(FLAT, HAPJsonUtility.buildJson(m_roots, HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPStructureValueDefinitionFlat) {
			HAPStructureValueDefinitionFlat context = (HAPStructureValueDefinitionFlat)obj;
			if(context.getRootNames().equals(this.getRootNames())) {
				for(String eleName : this.getRootNames()) {
					out = this.getRoot(eleName).equals(context.getRoot(eleName));
					if(!out)  
						break;
				}
				return true;
			}
		}
		return out;
	}

}
