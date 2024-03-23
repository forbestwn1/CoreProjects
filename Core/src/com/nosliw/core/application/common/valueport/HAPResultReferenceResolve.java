package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPElementStructure;

//store result for resolve structure element reference path
//this can be used:
//    variable using
//    variable definition
//    data association
@HAPEntityWithAttribute
public class HAPResultReferenceResolve extends HAPSerializableImp{
	
	@HAPAttribute
	public static final String ELEREFERENCE = "eleReference";
	
	@HAPAttribute
	public static final String STRUCTUREID = "structureId";
	
	@HAPAttribute
	public static final String ROOTNAME = "rootName";
	
	@HAPAttribute
	public static final String ELEMENTINFOORIGINAL = "elementInfoOriginal";
	
	@HAPAttribute
	public static final String ELEMENTINFOSOLID = "elementInfoSolid";
	
	@HAPAttribute
	public static final String FINALELEMENT = "finalElement";
	
	
	public HAPReferenceElement eleReference;
	
	//resolved structure runtime id
	public String structureId;

	//resolved root name in structure
	public String rootName;
	
	//solving result through original structure 
	public HAPResultDesendantResolve elementInfoOriginal;
	
	//solving result through solid structure
	public HAPResultDesendantResolve elementInfoSolid;
	
	//final element, solid (maybe logic element which embeded in real element)
	public HAPElementStructure finalElement;
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.eleReference!=null) {
			jsonMap.put(ELEREFERENCE, this.eleReference.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(STRUCTUREID, this.structureId);
		jsonMap.put(ROOTNAME, this.rootName);
		if(this.elementInfoOriginal!=null) {
			jsonMap.put(ELEMENTINFOORIGINAL, this.elementInfoOriginal.toStringValue(HAPSerializationFormat.JSON));
		}
		if(this.elementInfoSolid!=null) {
			jsonMap.put(ELEMENTINFOSOLID, this.elementInfoSolid.toStringValue(HAPSerializationFormat.JSON));
		}
		if(this.finalElement!=null) {
			jsonMap.put(FINALELEMENT, this.finalElement.toStringValue(HAPSerializationFormat.JSON));
		}
	}
	
}
