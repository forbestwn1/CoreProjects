package com.nosliw.data.core.structure.temp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.valuestructure1.HAPElementContextStructureValueExecutable;
import com.nosliw.data.core.valuestructure1.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPUtilityContext {

	public static Map<String, Object> discoverContantsValueFromContextStructure(HAPValueStructure contextStructure) {
		HAPExecutableValueStructure flatContext = buildExecuatableValueStructure(contextStructure);
		return flatContext.getConstantValue();
	}
	
	public static HAPExecutableValueStructure buildFlatContextFromContextStructure(HAPValueStructure contextStructure) {
		HAPExecutableValueStructure out = null;
		String type = contextStructure.getDataType();
		if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			out = buildFlatContextFromContextGroup((HAPValueStructureDefinitionGroup)contextStructure);
		}
		else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			out = buildFlatContextFromContext((HAPValueStructureDefinitionFlat)contextStructure);
		}
		return out;
	}

	public static HAPExecutableValueStructure buildFlatContextFromContext(HAPValueStructureDefinitionFlat context) {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		for(String name : context.getRootNames()) {
			out.addRootToCategary(context.getRoot(name), name);
		}
		return out;
	}
	
	public static HAPExecutableValueStructure buildFlatContextFromContextGroup(HAPValueStructureDefinitionGroup context) {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		
		List<String> categarys = Arrays.asList(HAPValueStructureDefinitionGroup.getAllCategariesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPRootStructure> eles = context.getRootsByCategary(categary);
			for(String localName : eles.keySet()) {
//				String globalName = new HAPIdContextDefinitionRoot(categary, localName).getFullName();
				Set<String> aliases = new HashSet<String>();
				aliases.add(localName);
				aliases.add(globalName);
				out.addRoot(new HAPElementContextStructureValueExecutable(eles.get(localName), categary), globalName, aliases);
			}
		}
		return out;
	}
	
	public static HAPElementStructure getDescendant(HAPValueStructure context, HAPReferenceElementInStructure path) {
		if(context.getDataType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			return getDescendant((HAPValueStructureDefinitionGroup)context, path.getElementPath());
		}
		else {
			return getDescendant((HAPValueStructureDefinitionFlat)context, path.getPath());
		}
	}
	
	public static HAPElementStructure getDescendant(HAPValueStructureDefinitionFlat context, String path) {
		HAPElementStructure out = null;
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPRootStructure root = context.getRoot(complexPath.getRoot());
		if(root!=null) {
			out = getDescendant(root.getDefinition(), complexPath.getPathStr());
		}
		return out;
	}
	
	public static HAPElementStructure getDescendant(HAPValueStructureDefinitionGroup contextGroup, String categary, String path) {
		HAPElementStructure out = null;
		HAPValueStructureDefinitionFlat context = contextGroup.getFlat(categary);
		if(context!=null)   out = getDescendant(context, path);
		return out;
	}

	public static HAPElementStructure getDescendant(HAPValueStructureDefinitionGroup contextGroup, String path) {
		HAPReferenceElementInStructure contextPath = new HAPReferenceElementInStructure(path);
		return getDescendant(contextGroup, contextPath.getRootReference().getCategary(), contextPath.getPath());
	}
	
	public static void updateDataDescendant(HAPValueStructureDefinitionGroup contextGroup, String categary, String path, HAPElementStructureLeafData dataEle) {
		updateDataDescendant(contextGroup.getFlat(categary), path, dataEle);
	}

	public static void updateDataDescendant(HAPValueStructureDefinitionFlat context, String path, HAPElementStructureLeafData dataEle) {
		setDescendant(context, path, dataEle);
//		HAPComplexPath cpath = new HAPComplexPath(path);
//		HAPContextDefinitionRoot root = context.getElement(cpath.getRootName());
//		if(cpath.getPathSegs().length==0 && root!=null && root.getDefinition().getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
//			//for data root replacement, not replace whole, just replace criteria
//			((HAPContextDefinitionLeafData)root.getDefinition()).setCriteria(dataEle.getCriteria());
//		}
//		else {
//			setDescendant(context, path, dataEle);
//		}
	}

	public static void setDescendant(HAPValueStructure contextStructure, HAPReferenceElementInStructure contextPath, HAPElementStructure ele) {
		HAPRootStructure targetRoot = contextStructure.getRoot(contextPath.getRootReference().getFullName(), true);

	}
	
	public static void setDescendant(HAPValueStructureDefinitionGroup contextGroup, String categary, String path, HAPElementStructure ele) {
//		setDescendant(contextGroup.getContext(categary), path, ele);
		setDescendant(contextGroup, new HAPReferenceElementInStructure(categary, path), ele);
	}

	public static void setDescendant(HAPValueStructureDefinitionFlat context, String path, HAPElementStructure ele) {
		setDescendant(context, new HAPReferenceElementInStructure((String)null, path), ele);
	}
	
	public static boolean isContextDefinitionElementConstant(HAPElementStructure ele) {   return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(ele.getType());   }
	
	//context root name can be like a.b.c and a.b.d
	//these two root name can be consolidated to one root name with a and child of b.c and b.d
	public static HAPValueStructureDefinitionFlat consolidateContextRoot(HAPValueStructureDefinitionFlat context) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		
		for(String rootName : context.getRootNames()) {
			HAPElementStructure def = context.getRoot(rootName).getDefinition();
			HAPUtilityContext.setDescendant(out, rootName, def);
		}
		return out;
	}
	
	public static String getContextGroupInheritMode(HAPInfo info) {  
		String out = HAPConstant.INHERITMODE_CHILD;
		if("false".equals(info.getValue(HAPValueStructureDefinitionGroup.INFO_INHERIT)))  out = HAPConstant.INHERITMODE_NONE;
		return out;				
	}
 
	public static void setContextGroupInheritModeNone(HAPInfo info) {		info.setValue(HAPValueStructureDefinitionGroup.INFO_INHERIT, "false");	}
	public static void setContextGroupInheritModeChild(HAPInfo info) {		info.setValue(HAPValueStructureDefinitionGroup.INFO_INHERIT, "true");	}
	
	public static boolean getContextGroupPopupMode(HAPInfo info) {  
		boolean out = true;
		if("false".equals(info.getValue(HAPValueStructureDefinitionGroup.INFO_POPUP)))  out = false;
		return out;				
	} 
 
	public static boolean getContextGroupEscalateMode(HAPInfo info) {  
		boolean out = false;
		if("true".equals(info.getValue(HAPValueStructureDefinitionGroup.INFO_ESCALATE)))  out = true;
		return out;				
	} 
	
	public static HAPValueStructureDefinitionGroup buildContextGroupFromContext(HAPValueStructureDefinitionFlat context) {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		for(String rootName : context.getRootNames()) {
			HAPRootStructure root = context.getRoot(rootName);
//			HAPIdContextDefinitionRoot rootId = new HAPIdContextDefinitionRoot(rootName);
			out.addRoot(rootId.getVariableName(), root, rootId.getCategary());
		}
		return out;
	}

	//build interited node from parent
	public static HAPRootStructure createRelativeContextDefinitionRoot(HAPValueStructureDefinitionGroup parentContextGroup, String contextCategary, String refPath, Set<String> excludedInfo) {
		return createRootWithRelativeElement(parentContextGroup.getElement(contextCategary, refPath), contextCategary, refPath, excludedInfo);
	}

	public static HAPInfoReferenceResolve resolveReferencedContextElement(HAPReferenceElementInStructure contextPath, HAPValueStructure parentContext){
		if(parentContext==null)   return null;
		HAPInfoReferenceResolve out = null;
		String contextType = parentContext.getDataType();
		if(contextType.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			out = analyzeElementReference(contextPath, (HAPValueStructureDefinitionGroup)parentContext, null, null);
		}
		else {
			out = ((HAPValueStructureDefinitionFlat)parentContext).discoverChild(contextPath.getRootReference().getVariableName(), contextPath.getSubPath());
			//process remaining path
			out = processContextElementRefResolve(out);
		}
		
		return out;
	}
	
	
	public static HAPValueStructureDefinitionGroup hardMerge(HAPValueStructureDefinitionGroup child, HAPValueStructureDefinitionGroup parent) {
		HAPValueStructureDefinitionGroup out = null;
		if(child==null) out = parent.cloneValueStructureGroup();
		else {
			out = child.cloneValueStructureGroup();
			out.hardMergeWith(parent);;
		}
		return out;
	}
	
	


}
