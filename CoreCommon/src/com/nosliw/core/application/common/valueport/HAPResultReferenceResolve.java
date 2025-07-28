package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

//store result for resolve structure element reference path
//this can be used:
//    variable using
//    variable definition
//    data association
@HAPEntityWithAttribute
public class HAPResultReferenceResolve extends HAPSerializableImp{
	
	@HAPAttribute
	public static final String BRICKID = "brickId";
	
	@HAPAttribute
	public static final String VALUEPORTSIDE = "valuePortSide";
	
	@HAPAttribute
	public static final String VALUEPORTID = "valuePortId";
	
	@HAPAttribute
	public static final String STRUCTUREID = "structureId";
	
	@HAPAttribute
	public static final String FULLPATH = "fullPath";
	
	@HAPAttribute
	public static final String ROOTNAME = "rootName";

	@HAPAttribute
	public static final String ELEMENTPATH = "elementPath";

	@HAPAttribute
	public static final String ELEMENTINFOORIGINAL = "elementInfoOriginal";
	
	@HAPAttribute
	public static final String ELEMENTINFOSOLID = "elementInfoSolid";
	
	@HAPAttribute
	public static final String FINALELEMENT = "finalElement";
	
	public HAPDomainValueStructure valueStructureDomain;
	
	//ref to brick
	public HAPIdBrickInBundle brickId;
	
	public String valuePortSide;
	
	//value port id
	public HAPIdValuePortInBrick valuePortId;
	
	//resolved structure runtime id
	public String structureId;

	public String fullPath;

	//resolved root name in structure
	public String rootName;
	
	public String elementPath;
	
	//solving result through original structure 
	public HAPResultDesendantResolve elementInfoOriginal;
	
	//solving result through solid structure
	public HAPResultDesendantResolve elementInfoSolid;
	
	//final element, solid (maybe logic element which embeded in real element)
	public HAPElementStructure finalElement;
	
	public HAPResultReferenceResolve() {
		
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.brickId!=null) {
			jsonMap.put(BRICKID, this.brickId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUEPORTSIDE, this.valuePortSide);
		jsonMap.put(VALUEPORTID, this.valuePortId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(FULLPATH, this.fullPath);
		jsonMap.put(ROOTNAME, this.rootName);
		jsonMap.put(ELEMENTPATH, this.elementPath);
		
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
