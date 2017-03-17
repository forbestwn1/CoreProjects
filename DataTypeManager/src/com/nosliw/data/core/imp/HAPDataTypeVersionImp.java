package com.nosliw.data.core.imp;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeVersion;

public class HAPDataTypeVersionImp extends HAPStringableValueEntity implements HAPDataTypeVersion{

	@HAPAttribute
	public static String NAME = "name";
	
	public HAPDataTypeVersionImp(){
		this.updateAtomicChildStrValue(MAJOR, "0", HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);
		this.updateAtomicChildStrValue(MINOR, "0", HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);
	}
	
	public HAPDataTypeVersionImp(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public String getName(){  return this.getAtomicAncestorValueString(HAPDataTypeVersionImp.NAME);	}
	public void setName(String name){ this.buildObjectByLiterate(name); }
	
	@Override
	public String getMajor(){  return this.getAtomicAncestorValueString(HAPDataTypeVersion.MAJOR);	}
	@Override
	public String getMinor(){ return this.getAtomicAncestorValueString(HAPDataTypeVersion.MINOR);	}
	@Override
	public String getRevision(){  return this.getAtomicAncestorValueString(HAPDataTypeVersion.REVISION);	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		this.updateAtomicChildStrValue(HAPDataTypeVersionImp.NAME, literateValue, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);

		String[] segs = HAPNamingConversionUtility.parsePaths(literateValue);
		if(segs.length>=3) this.updateAtomicChildStrValue(HAPDataTypeVersion.REVISION, segs[2], HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);   
		if(segs.length>=2) this.updateAtomicChildStrValue(HAPDataTypeVersion.MINOR, segs[1], HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);   
		if(segs.length>=1) this.updateAtomicChildStrValue(HAPDataTypeVersion.MAJOR, segs[0], HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);
		return true;
	}

	@Override
	protected String buildLiterate(){
		String out = this.getName();
		if(HAPBasicUtility.isStringEmpty(out)){
			out = HAPNamingConversionUtility.cascadeComponentPath(new String[]{String.valueOf(this.getMajor()), String.valueOf(this.getMinor()), this.getRevision()});
			this.updateAtomicChildStrValue(HAPDataTypeVersionImp.NAME, out, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);
		}
		
		return out; 
	}
	
	@Override
	public HAPDataTypeVersion cloneVersion(){
		HAPDataTypeVersionImp out = new HAPDataTypeVersionImp();
		out.cloneFrom(this);
		return out;
	}
}
