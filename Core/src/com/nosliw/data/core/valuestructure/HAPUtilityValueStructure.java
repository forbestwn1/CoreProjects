package com.nosliw.data.core.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafConstant;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPElementNode;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.structure.HAPUtilityStructure;

public class HAPUtilityValueStructure {

	public static HAPContainerVariableCriteriaInfo discoverDataVariablesInContext(HAPValueStructureDefinition structure) {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesInStructure(structure);
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			HAPComplexPath path = new HAPComplexPath(idPath);
			String id = path.getRootName();
			Set<String> aliases = structure.getAliasesById(id);
			out.addVariableCriteriaInfo(dataVarsInfoByIdPath.get(idPath), idPath, aliases);
		}
		return out;
	}
	
	
	//find all data variables in context 
	public static Map<String, HAPInfoCriteria> discoverDataVariablesInStructure(HAPValueStructure structure){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		for(HAPRoot root : structure.getAllRoots()){
			if(!root.isConstant()){
				discoverDataVariableInElement(root.getLocalId(), root.getDefinition(), out);
			}
		}
		return out;
	}

	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverDataVariableInElement(String path, HAPElement contextDefEle, Map<String, HAPInfoCriteria> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverDataVariableInElement(path, relativeEle.getSolidStructureElement(), criterias);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			HAPElementLeafData dataEle = (HAPElementLeafData)contextDefEle;
			HAPInfoCriteria varInfo = HAPInfoCriteria.buildCriteriaInfo(dataEle.getCriteria());
//			varInfo.setId(path);
			criterias.put(path, varInfo);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			HAPElementNode nodeEle = (HAPElementNode)contextDefEle;
			for(String childName : nodeEle.getChildren().keySet()) {
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				discoverDataVariableInElement(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}


	//build another context which only include variable node in current context
	public HAPValueStructureDefinitionFlat getVariableContext() {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(String name : this.m_roots.keySet()) {
			HAPRoot contextRoot = this.getRoot(name);
			if(!contextRoot.isConstant()) {
				out.addRoot(name, contextRoot.getId(), contextRoot.cloneRoot());
			}			
		}
		return out;
	}
	

	public static HAPValueStructureDefinitionFlat updateRootName(HAPValueStructureDefinitionFlat structure, HAPUpdateName nameUpdate) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		
		for(String rootName : structure.getRootNames()) {
			HAPRoot root = structure.getRoot(rootName).cloneRoot();
			
			
			
		}
		
		//update context
		for(String rootName : new HashSet<String>(this.getRootNames())) {
			HAPRoot root = this.getRoot(rootName);
			root.setName(nameUpdate.getUpdatedName(root.getName()));
			HAPUtilityContext.processContextRootElement(root, rootName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getElement();
						if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(relative.getParent())) {
							//update local relative path
							HAPReferenceElement path = relative.getPathFormat();
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootReference().getCategary(), nameUpdate.getUpdatedName(path.getRootReference().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
			//update root name
			this.m_roots.remove(rootName);
			this.addRoot(nameUpdate.getUpdatedName(rootName), root);
		}
	}

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
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootReference().getCategary(), nameUpdate.getUpdatedName(path.getRootReference().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
		}
	}

	
	public static Map<String, HAPMatchers> mergeRoot(HAPRoot origin, HAPRoot expect, boolean modifyStructure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		
		HAPUtilityStructure.traverseElement(expect, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
				String path = eleInfo.getElementPath().getPathStr();
				mergeElement(getDescendant(origin.getDefinition(), path), eleInfo.getElement(), modifyStructure, matchers, path, runtimeEnv);
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
		
		return matchers;
	}

	public static Map<String, HAPMatchers> mergeElement(HAPElement originDef, HAPElement expectDef, boolean modifyStructure, String path, HAPRuntimeEnvironment runtimeEnv){
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		mergeElement(originDef, expectDef, modifyStructure, matchers, null, runtimeEnv);
		return matchers;
	}
	
	//merge origin context def with child context def to expect context out
	//also generate matchers from origin to expect
	public static void mergeElement(HAPElement originDef1, HAPElement expectDef1, boolean modifyStructure, Map<String, HAPMatchers> matchers, String path, HAPRuntimeEnvironment runtimeEnv){
		if(path==null)  path = "";
		//merge is about solid
		HAPElement originDef = originDef1.getSolidStructureElement();
		HAPElement expectDef = expectDef1.getSolidStructureElement();
		String type = expectDef.getType();
		
		if(originDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			switch(type) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafConstant dataOrigin = (HAPElementLeafConstant)originDef.getSolidStructureElement();
				HAPElementLeafData dataExpect = (HAPElementLeafData)expectDef;
				//cal matchers
				HAPMatchers matcher = HAPCriteriaUtility.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(new HAPDataTypeCriteriaId(dataOrigin.getDataValue().getDataTypeId(), null)), dataExpect.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			}
		}
		else if(expectDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {  //kkkkk
			switch(originDef.getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafData dataOrigin = (HAPElementLeafData)originDef;
				 HAPElementLeafConstant dataExpect = (HAPElementLeafConstant)expectDef.getSolidStructureElement();
				//cal matchers
				HAPMatchers matcher = HAPCriteriaUtility.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataOrigin.getCriteria()), new HAPDataTypeCriteriaId(dataExpect.getDataValue().getDataTypeId(), null), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			}
		}
		else {
			if(!originDef.getType().equals(type))   HAPErrorUtility.invalid("");   //not same type, error
			switch(type) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafData dataOrigin = (HAPElementLeafData)originDef.getSolidStructureElement();
				HAPElementLeafData dataExpect = (HAPElementLeafData)expectDef;
				//cal matchers
				HAPMatchers matcher = HAPCriteriaUtility.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataOrigin.getCriteria()), dataExpect.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			{
				HAPElementNode nodeOrigin = (HAPElementNode)originDef;
				HAPElementNode nodeExpect = (HAPElementNode)expectDef;
				for(String nodeName : nodeExpect.getChildren().keySet()) {
					HAPElement childNodeExpect = nodeExpect.getChildren().get(nodeName);
					HAPElement childNodeOrigin = nodeOrigin.getChildren().get(nodeName);
					if(childNodeOrigin!=null || modifyStructure) {
						switch(childNodeExpect.getType()) {
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementLeafData();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementNode();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						default :
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = childNodeExpect.cloneStructureElement();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							break;
						}
					}
				}
				break;
				}
			}
			}
		}
	}

}
