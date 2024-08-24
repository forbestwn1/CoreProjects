package com.nosliw.core.application.common.structure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultDesendantResolve;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityStructure {

	public static HAPRootStructure getRootFromStructure(HAPStructure1 structure, String rootRefLiterate) {
		HAPReferenceRootInStrucutre rootRef = HAPUtilityStructureReference.parseRootReferenceLiterate(rootRefLiterate, structure.getStructureType());
		return getRootFromStructure(structure, rootRef);
	}

	public static HAPRootStructure getRootFromStructure(HAPStructure1 structure, HAPReferenceRootInStrucutre rootRef) {
		List<HAPRootStructure> roots = structure.resolveRoot(rootRef, false);
		if(roots==null || roots.size()==0) {
			return null;
		}
		return roots.get(0);
	}

	public static HAPElementStructure getDescendant(HAPElementStructure element, String path) {
		HAPElementStructure out = element;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegments()) {
			if(out!=null) {
				out = out.getChild(pathSeg);
			} else {
				throw new RuntimeException();
			}
		}
		return out;
	}

	public static HAPResultDesendantResolve resolveDescendant(HAPElementStructure element, String path) {
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
						if(solidEle==null)
						 {
							return null;   //not valid path
						}
					}
					if(solidEle==null) {
						remainingPath = remainingPath.appendSegment(pathSeg);
					} else{
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
		HAPResultDesendantResolve out = new HAPResultDesendantResolve();
		out.resolvedElement = solvedElment;
		if(remainingPath!=null) {
			out.remainPath = remainingPath;
		}
		out.solvedPath = solvedPath;
		return out;
	}

	public static void setDescendantByNamePath(HAPStructure1 targetStructure, HAPComplexPath path, HAPElementStructure ele) {
		HAPReferenceRootInStrucutre rootRef = HAPUtilityStructureReference.parseRootReferenceLiterate(path.getRoot(), targetStructure.getStructureType());
		HAPRootStructure root = getRootFromStructure(targetStructure, rootRef);
		if(root==null) {
			root = new HAPRootStructure();
			root = targetStructure.addRoot(rootRef, root);
		}
		setDescendant(root, path.getPath(), ele);
	}
	
	public static void setDescendant(HAPStructure targetStructure, HAPComplexPath path, HAPElementStructure ele) {
		setDescendant(targetStructure.getRoot(path.getRoot(), true), path.getPath(), ele);
	}
	
	public static void setDescendant(HAPRootStructure targetRoot, HAPPath path, HAPElementStructure ele) {
		String[] pathSegs = path.getPathSegments();
		if(pathSegs.length==0) {
			if(targetRoot.getDefinition()!=null && !targetRoot.getDefinition().getType().equals(ele.getType()))
			 {
				HAPErrorUtility.invalid("");  //should be same type
			}
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
			 {
				HAPErrorUtility.invalid("");  //should be same type
			}
			((HAPElementStructureNode)parentEle).addChild(seg, ele);
		}
	}

	public static HAPElementStructure traverseElement(HAPElementStructure element, String startPath, HAPProcessorStructureElement processor, Object value) {
		return traverseElement(new HAPInfoElement(element, new HAPComplexPath(startPath)), processor, value);
	}
	
	//traverse through all the context definition element, and process it
	//if return not null, then means new context element
	private static HAPElementStructure traverseElement(HAPInfoElement elementInfo, HAPProcessorStructureElement processor, Object value) {
		HAPElementStructure out = null;
		HAPElementStructure element = elementInfo.getElement(); 
		HAPComplexPath path = elementInfo.getElementPath();
		Pair<Boolean, HAPElementStructure> processOut = processor.process(elementInfo, value);
		boolean going = true;
		if(processOut!=null) {
			if(processOut.getLeft()!=null) {
				going = processOut.getLeft();
			}
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
		relativeEle.setReference(new HAPReferenceElement(parentStructure, refPath));
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
			relativeEle.setReference(new HAPReferenceElement(parentStructure, elePath));
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
	public static Map<String, Object> discoverConstantValue(HAPStructure1 structure){
		//discover cosntant value by id
		Map<String, Object> constantsById = new LinkedHashMap<String, Object>();
		for(HAPRootStructure root : structure.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorStructureElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
						HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)eleInfo.getElement();
						Object value = constantEle.getDataValue();
						if(value==null) {
							value = constantEle.getValue();
						}
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
		
		HAPUtilityStructure.traverseElement(root, new HAPProcessorStructureElement() {
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

	public static List<HAPRootStructure> resolveRoot(String rootRefLiterate, HAPStructure1 structure, boolean createIfNotExist) {
		HAPReferenceRootInStrucutre rootReference = HAPUtilityStructureReference.parseRootReferenceLiterate(rootRefLiterate, structure.getStructureType());
		List<HAPRootStructure> out = structure.resolveRoot(rootReference, createIfNotExist);
		return out;
	}
	
	public static HAPRootStructure getRootByName(String name, HAPStructure1 structure) {
		List<HAPRootStructure> allRoots = resolveRoot(name, structure, false);
		if(allRoots==null || allRoots.size()==0) {
			return null;
		} else {
			return allRoots.get(0);
		}
	}
	
	public static HAPRootStructure addRoot(HAPStructure1 structure, String rootReference, HAPRootStructure root) {
		return structure.addRoot(HAPUtilityStructureReference.parseRootReferenceLiterate(rootReference, structure.getStructureType()), root);
	}

	public static Map<String, HAPMatchers> mergeRoot(HAPRootStructure origin, HAPRootStructure expect, boolean modifyStructure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		
		HAPUtilityStructure.traverseElement(expect, new HAPProcessorStructureElement() {
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
	public static void mergeElement(HAPElementStructure fromDef1, HAPElementStructure toDef1, boolean modifyStructure, List<HAPPathElementMapping> mappingPaths, String path, HAPRuntimeEnvironment runtimeEnv){
		if(path==null) {
			path = "";
		}
		//merge is about solid
		HAPElementStructure fromDef = fromDef1.getSolidStructureElement();
		HAPElementStructure toDef = toDef1.getSolidStructureElement();
		String toType = toDef.getType();
		
		if(fromDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			switch(toType) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementStructureLeafConstant dataFrom = (HAPElementStructureLeafConstant)fromDef.getSolidStructureElement();
				HAPElementStructureLeafData dataTo = (HAPElementStructureLeafData)toDef;
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(new HAPDataTypeCriteriaId(dataFrom.getDataValue().getDataTypeId(), null)), dataTo.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				mappingPaths.add(new HAPPathElementMappingConstantToVariable(dataFrom.getValue(), path, matcher));
				break;
			}
			default:
			{
				HAPErrorUtility.invalid("");
			}
			}
		}
		else if(toDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {  //kkkkk
			HAPErrorUtility.invalid("");
//			switch(fromDef.getType()) {
//			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
//			{
//				HAPElementStructureLeafData dataFrom = (HAPElementStructureLeafData)fromDef;
//				 HAPElementStructureLeafConstant dataTo = (HAPElementStructureLeafConstant)toDef.getSolidStructureElement();
//				//cal matchers
//				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataFrom.getCriteria()), new HAPDataTypeCriteriaId(dataTo.getDataValue().getDataTypeId(), null), runtimeEnv.getDataTypeHelper()); 
//				mappingPaths.put(path, matcher);
//				break;
//			}
//			}
		}
		else {
			if(!fromDef.getType().equals(toType))
			 {
				HAPErrorUtility.invalid("");   //not same type, error
			}
			switch(toType) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementStructureLeafData dataFrom = (HAPElementStructureLeafData)fromDef.getSolidStructureElement();
				HAPElementStructureLeafData dataTo = (HAPElementStructureLeafData)toDef;
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataFrom.getCriteria()), dataTo.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				mappingPaths.add(new HAPPathElementMappingVariableToVariable(path, matcher==null?new HAPMatchers():matcher));
				break;
			}
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			{
				HAPElementStructureNode nodeFrom = (HAPElementStructureNode)fromDef;
				HAPElementStructureNode nodeTo = (HAPElementStructureNode)toDef;
				for(String nodeName : nodeTo.getChildren().keySet()) {
					HAPElementStructure childNodeTo = nodeTo.getChildren().get(nodeName);
					HAPElementStructure childNodeFrom = nodeFrom.getChildren().get(nodeName);
					String childPath = HAPUtilityNamingConversion.cascadePath(path, nodeName);
					if(childNodeFrom!=null || modifyStructure) {
						switch(childNodeTo.getType()) {
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
						{
							if(childNodeFrom==null) {
								childNodeFrom = new HAPElementStructureLeafData();
								nodeFrom.addChild(nodeName, childNodeFrom);
							}
							mergeElement(childNodeFrom, childNodeTo, modifyStructure, mappingPaths, childPath, runtimeEnv);
							break;
						}
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
						{
							if(childNodeFrom==null) {
								childNodeFrom = new HAPElementStructureNode();
								nodeFrom.addChild(nodeName, childNodeFrom);
							}
							mergeElement(childNodeFrom, childNodeTo, modifyStructure, mappingPaths, childPath, runtimeEnv);
							break;
						}
						default :
						{
							if(childNodeFrom==null) {
								childNodeFrom = childNodeTo.cloneStructureElement();
								nodeFrom.addChild(nodeName, childNodeFrom);
							}
							break;
						}
					}
				}
				break;
				}
			}
			default : 
			{
				mappingPaths.add(new HAPPathElementMappingVariableToVariable(path, new HAPMatchers()));
			}
			}
		}
	}

}
