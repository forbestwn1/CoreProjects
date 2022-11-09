package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.reference.HAPInfoDesendantResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInStructureComplex;
import com.nosliw.data.core.structure.temp.HAPProcessorContextDefinitionElement;

public class HAPUtilityStructure {

	public static HAPRootStructure getRootFromStructure(HAPStructure structure, String rootRefLiterate) {
		HAPReferenceRootInStrucutre rootRef = HAPUtilityStructureReference.parseRootReferenceLiterate(rootRefLiterate, structure.getStructureType());
		return getRootFromStructure(structure, rootRef);
	}

	public static HAPRootStructure getRootFromStructure(HAPStructure structure, HAPReferenceRootInStrucutre rootRef) {
		List<HAPRootStructure> roots = structure.resolveRoot(rootRef, false);
		if(roots==null || roots.size()==0)  return null;
		return roots.get(0);
	}

	public static HAPElementStructure getDescendant(HAPElementStructure element, String path) {
		HAPElementStructure out = element;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegments()) {
			if(out!=null)			out = out.getChild(pathSeg);
			else throw new RuntimeException();
		}
		return out;
	}

	public static HAPInfoDesendantResolve resolveDescendant(HAPElementStructure element, String path) {
		HAPElementStructure solvedElment = element;
		HAPPath solvedPath = new HAPPath();
		HAPPath remainingPath = new HAPPath();
		if(HAPUtilityBasic.isStringNotEmpty(path)) {
			String[] pathSegs = new HAPPath(path).getPathSegments();
			for(String pathSeg : pathSegs){
				if(remainingPath.isEmpty()) {
					//solid node
					HAPElementStructure solidEle = null;
					if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(solvedElment.getType())) {
						solidEle = ((HAPElementStructureNode)solvedElment).getChildren().get(pathSeg);
						if(solidEle==null)   return null;   //not valid path
					}
					if(solidEle==null) 		remainingPath = remainingPath.appendSegment(pathSeg);
					else{
						solvedElment = solidEle;
						solvedPath = solvedPath.appendSegment(pathSeg);
					}
				}
				else {
					//remaining path
					remainingPath = remainingPath.appendSegment(pathSeg);
				}
			}
		}
		HAPInfoDesendantResolve out = new HAPInfoDesendantResolve();
		out.resolvedElement = solvedElment;
		if(remainingPath!=null)  out.remainPath = remainingPath;
		out.solvedPath = solvedPath;
		return out;
	}

	public static void setDescendantByNamePath(HAPStructure targetStructure, HAPComplexPath path, HAPElementStructure ele) {
		HAPReferenceRootInStrucutre rootRef = HAPUtilityStructureReference.parseRootReferenceLiterate(path.getRoot(), targetStructure.getStructureType());
		HAPRootStructure root = getRootFromStructure(targetStructure, rootRef);
		if(root==null) {
			root = new HAPRootStructure();
			root = targetStructure.addRoot(rootRef, root);
		}
		setDescendant(root, path.getPath(), ele);
	}
	
	public static void setDescendant(HAPStructure targetStructure, HAPComplexPath path, HAPElementStructure ele) {
		setDescendant(targetStructure.getRoot(path.getRoot()), path.getPath(), ele);
	}
	
	public static void setDescendant(HAPRootStructure targetRoot, HAPPath path, HAPElementStructure ele) {
		String[] pathSegs = path.getPathSegments();
		if(pathSegs.length==0) {
			if(targetRoot.getDefinition()!=null && !targetRoot.getDefinition().getType().equals(ele.getType()))  HAPErrorUtility.invalid("");  //should be same type
			targetRoot.setDefinition(ele);
		}
		else {
			String seg = pathSegs[0];
			HAPElementStructure parentEle = targetRoot.getDefinition();
			if(parentEle==null && pathSegs.length>0) {
				parentEle = new HAPElementStructureNode();
				targetRoot.setDefinition(parentEle);
			}
			for(int i=0; i<pathSegs.length-1; i++) {
				String pathSeg = pathSegs[i]; 
				HAPElementStructure child = parentEle.getChild(pathSeg);
				if(child==null) {
					child = new HAPElementStructureNode();
					((HAPElementStructureNode)parentEle).addChild(pathSeg, child);
				}
				parentEle = child;
				seg = pathSegs[i+1];
			}
			if(((HAPElementStructureNode)parentEle).getChild(seg)!=null && !((HAPElementStructureNode)parentEle).getChild(seg).getType().equals(ele.getType())) 
				HAPErrorUtility.invalid("");  //should be same type
			((HAPElementStructureNode)parentEle).addChild(seg, ele);
		}
	}

	//traverse through all the structure element under root, and process it
	public static void traverseElement(HAPRootStructure root, HAPProcessorContextDefinitionElement processor, Object value) {
		traverseElement(root, root.getLocalId(), processor, value);
	}
	
	public static void traverseElement(HAPRootStructure root, String rootId, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElementStructure processedEle = traverseElement(new HAPInfoElement(root.getDefinition(), new HAPComplexPath(rootId)), processor, value);
		if(processedEle!=null)  root.setDefinition(processedEle);
	}
	
	public static HAPElementStructure traverseElement(HAPElementStructure element, String startPath, HAPProcessorContextDefinitionElement processor, Object value) {
		return traverseElement(new HAPInfoElement(element, new HAPComplexPath(startPath)), processor, value);
	}
	
	//traverse through all the context definition element, and process it
	//if return not null, then means new context element
	private static HAPElementStructure traverseElement(HAPInfoElement elementInfo, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElementStructure out = null;
		HAPElementStructure element = elementInfo.getElement(); 
		HAPComplexPath path = elementInfo.getElementPath();
		Pair<Boolean, HAPElementStructure> processOut = processor.process(elementInfo, value);
		boolean going = true;
		if(processOut!=null) {
			if(processOut.getLeft()!=null)    going = processOut.getLeft();
			if(processOut.getRight()!=null) {
				element = processOut.getRight();
				elementInfo.setElement(element);
				out = element;
			}
		}
		if(going) {
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(element.getType())) {
				HAPElementStructureNode nodeEle = (HAPElementStructureNode)element;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					HAPElementStructure childProcessed = traverseElement(new HAPInfoElement(nodeEle.getChild(childNodeName), path.appendSegment(childNodeName)), processor, value);
					if(childProcessed!=null) {
						//replace with new element
						nodeEle.addChild(childNodeName, childProcessed);
					}
				}
			}
		}
		processor.postProcess(elementInfo, value);
		return out;
	}
	
	public static HAPRootStructure createRootWithRelativeElement(String refPath, String parentStructure) {
		HAPRootStructure out = new HAPRootStructure();
		HAPElementStructureLeafRelative relativeEle = new HAPElementStructureLeafRelative();
		relativeEle.setReference(new HAPReferenceElementInStructureComplex(parentStructure, refPath));
		out.setDefinition(relativeEle);
		return out;
	}
	
	public static HAPRootStructure createRootWithRelativeElement(HAPRootStructure parentNode, String parentStructure, String elePath, Set<String> excludedInfo) {
		HAPRootStructure out = null;
		
		if(parentNode.isConstant()) {
			out = parentNode.cloneRoot();
		}
		else {
			out = new HAPRootStructure();
			out.setInfo(parentNode.getInfo().cloneInfo(excludedInfo));
			HAPElementStructureLeafRelative relativeEle = new HAPElementStructureLeafRelative();
			relativeEle.setReference(new HAPReferenceElementInStructureComplex(parentStructure, elePath));
			relativeEle.setResolvedIdPath(new HAPComplexPath(parentNode.getLocalId(), elePath));
			if(parentNode.getDefinition().isProcessed()) {
//				relativeEle.setDefinition(parentNode.getDefinition().getSolidContextDefinitionElement());
				relativeEle.setDefinition(HAPUtilityStructure.getDescendant(parentNode.getDefinition(), elePath).getSolidStructureElement());
				relativeEle.processed();
			}
			out.setDefinition(relativeEle);
		}
		return out;
	}
	
	//build interited node from parent
	public static HAPRootStructure createRootWithRelativeElement(HAPRootStructure parentNode, String elePath, Set<String> excludedInfo) {
		return createRootWithRelativeElement(parentNode, null, elePath, excludedInfo);
	}

	//find all constants in structure by name, including constants defined in leaf
	public static Map<String, Object> discoverConstantValue(HAPStructure structure){
		//discover cosntant value by id
		Map<String, Object> constantsById = new LinkedHashMap<String, Object>();
		for(HAPRootStructure root : structure.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
						HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)eleInfo.getElement();
						Object value = constantEle.getDataValue();
						if(value==null)   value = constantEle.getValue();
						constantsById.put(eleInfo.getElementPath().getFullName(), value);
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {	}
			}, null);
		}

		//update id with name
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Map<String, Double> namePriorities = new LinkedHashMap<String, Double>();
		for(String idPath : constantsById.keySet()) {
			HAPComplexPath complexIdPath = new HAPComplexPath(idPath);
			List<HAPInfoAlias> namesInfo = structure.discoverRootAliasById(complexIdPath.getRoot());
			for(HAPInfoAlias nameInfo : namesInfo) {
				String name = nameInfo.getName();
				HAPComplexPath complexNamePath = complexIdPath.updateRootName(name);
				Double currentPriroty = namePriorities.get(complexNamePath.getRoot());
				if(currentPriroty==null || nameInfo.getPriority()<currentPriroty) {
					namePriorities.put(name, nameInfo.getPriority());
					out.put(complexNamePath.getFullName(), constantsById.get(idPath));
				}
			}
		}
		return out;
	}	
	
	//discover all the relative elements in context def element
	public static Map<String, HAPElementStructureLeafRelative> discoverRelativeElement(HAPRootStructure root) {
		Map<String, HAPElementStructureLeafRelative> out = new LinkedHashMap<String, HAPElementStructureLeafRelative>();
		
		HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					out.put(eleInfo.getElementPath().getFullName(), (HAPElementStructureLeafRelative)eleInfo.getElement());
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);

		return out;
	}

	public static List<HAPRootStructure> resolveRoot(String rootRefLiterate, HAPStructure structure, boolean createIfNotExist) {
		HAPReferenceRootInStrucutre rootReference = HAPUtilityStructureReference.parseRootReferenceLiterate(rootRefLiterate, structure.getStructureType());
		List<HAPRootStructure> out = structure.resolveRoot(rootReference, createIfNotExist);
		return out;
	}
	
	public static HAPRootStructure getRootByName(String name, HAPStructure structure) {
		List<HAPRootStructure> allRoots = resolveRoot(name, structure, false);
		if(allRoots==null || allRoots.size()==0)  return null;
		else return allRoots.get(0);
	}
	
	public static HAPRootStructure addRoot(HAPStructure structure, String rootReference, HAPRootStructure root) {
		return structure.addRoot(HAPUtilityStructureReference.parseRootReferenceLiterate(rootReference, structure.getStructureType()), root);
	}

	public static Map<String, HAPMatchers> mergeRoot(HAPRootStructure origin, HAPRootStructure expect, boolean modifyStructure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		
		HAPUtilityStructure.traverseElement(expect, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				String path = eleInfo.getElementPath().getPathStr();
				mergeElement(getDescendant(origin.getDefinition(), path), eleInfo.getElement(), modifyStructure, matchers, path, runtimeEnv);
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
		
		return matchers;
	}

	public static Map<String, HAPMatchers> mergeElement(HAPElementStructure originDef, HAPElementStructure expectDef, boolean modifyStructure, String path, HAPRuntimeEnvironment runtimeEnv){
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		mergeElement(originDef, expectDef, modifyStructure, matchers, null, runtimeEnv);
		return matchers;
	}
	
	//merge origin context def with child context def to expect context out
	//also generate matchers from origin to expect
	public static void mergeElement(HAPElementStructure originDef1, HAPElementStructure expectDef1, boolean modifyStructure, Map<String, HAPMatchers> matchers, String path, HAPRuntimeEnvironment runtimeEnv){
		if(path==null)  path = "";
		//merge is about solid
		HAPElementStructure originDef = originDef1.getSolidStructureElement();
		HAPElementStructure expectDef = expectDef1.getSolidStructureElement();
		String type = expectDef.getType();
		
		if(originDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			switch(type) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementStructureLeafConstant dataOrigin = (HAPElementStructureLeafConstant)originDef.getSolidStructureElement();
				HAPElementStructureLeafData dataExpect = (HAPElementStructureLeafData)expectDef;
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(new HAPDataTypeCriteriaId(dataOrigin.getDataValue().getDataTypeId(), null)), dataExpect.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			}
		}
		else if(expectDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {  //kkkkk
			switch(originDef.getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementStructureLeafData dataOrigin = (HAPElementStructureLeafData)originDef;
				 HAPElementStructureLeafConstant dataExpect = (HAPElementStructureLeafConstant)expectDef.getSolidStructureElement();
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataOrigin.getCriteria()), new HAPDataTypeCriteriaId(dataExpect.getDataValue().getDataTypeId(), null), runtimeEnv.getDataTypeHelper()); 
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
				HAPElementStructureLeafData dataOrigin = (HAPElementStructureLeafData)originDef.getSolidStructureElement();
				HAPElementStructureLeafData dataExpect = (HAPElementStructureLeafData)expectDef;
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataOrigin.getCriteria()), dataExpect.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				if(matcher!=null&&!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			{
				HAPElementStructureNode nodeOrigin = (HAPElementStructureNode)originDef;
				HAPElementStructureNode nodeExpect = (HAPElementStructureNode)expectDef;
				for(String nodeName : nodeExpect.getChildren().keySet()) {
					HAPElementStructure childNodeExpect = nodeExpect.getChildren().get(nodeName);
					HAPElementStructure childNodeOrigin = nodeOrigin.getChildren().get(nodeName);
					if(childNodeOrigin!=null || modifyStructure) {
						switch(childNodeExpect.getType()) {
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementStructureLeafData();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPUtilityNamingConversion.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementStructureNode();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPUtilityNamingConversion.cascadePath(path, nodeName), runtimeEnv);
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
