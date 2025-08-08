package com.nosliw.core.xxx.application.common.structure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstant;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.core.xxx.application.valueport.HAPReferenceElement;

public class HAPUtilityStructure {

	//get rid of relative, replace with solid definition
	public static HAPElementStructure solidateStructureElement(HAPElementStructure raw) {
		String type = raw.getType();
		if(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION.equals(type)) {
			HAPElementStructureLeafRelativeForDefinition forDefinition = (HAPElementStructureLeafRelativeForDefinition)raw;
			return forDefinition.getResolveInfo().getSolidElement().cloneStructureElement();
		}
		else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE.equals(type)) {
			HAPElementStructureLeafRelativeForValue forValue = (HAPElementStructureLeafRelativeForValue)raw;
			return forValue.getDefinition().cloneStructureElement();
		}
		else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(type)) {
			HAPElementStructureNode out = new HAPElementStructureNode();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)raw;
			Map<String, HAPElementStructure> children = nodeStructureElement.getChildren();
			for(String childName : children.keySet()) {
				out.addChild(childName, solidateStructureElement(children.get(childName)));
			}
			return out;
		}
		else {
			return raw.cloneStructureElement();
		}
	}
	
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
		
		HAPUtilityElement.traverseElement(expect, new HAPProcessorStructureElement() {
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
	
}
