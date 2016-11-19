package com.nosliw.data.datatype.importer;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeVersionImp extends HAPStringableValueEntity implements HAPDataTypeVersion{
	
	@Override
	public String getName(){  return this.getAtomicAncestorValueString(HAPDataTypeVersion.NAME);	}
	@Override
	public Integer getMajor(){  return this.getAtomicAncestorValueInteger(HAPDataTypeVersion.MAJOR);	}
	@Override
	public Integer getMinor(){ return this.getAtomicAncestorValueInteger(HAPDataTypeVersion.MINOR);	}
	@Override
	public String getRevision(){  return this.getAtomicAncestorValueString(HAPDataTypeVersion.REVISION);	}

	@Override
	protected void buildObjectByLiterate(String literateValue){	
		this.updateAtomicChild(HAPDataTypeVersion.NAME, literateValue, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);

		String[] segs = HAPNamingConversionUtility.parsePathSegs(literateValue);
		if(segs.length>=3) this.updateAtomicChild(HAPDataTypeVersion.REVISION, segs[2], HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);   
		if(segs.length>=2) this.updateAtomicChild(HAPDataTypeVersion.MINOR, segs[1], HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);   
		if(segs.length>=1) this.updateAtomicChild(HAPDataTypeVersion.MAJOR, segs[0], HAPConstant.STRINGABLE_ATOMICVALUETYPE_INTEGER, null);   
	}

	@Override
	protected String buildLiterate(){
		String out = "";
		out = HAPNamingConversionUtility.buildPath(String.valueOf(this.getMajor()), String.valueOf(this.getMinor()));
		out = HAPNamingConversionUtility.buildPath(out, this.getRevision());
		return out; 
	}
	
	@Override
	public HAPDataTypeVersion cloneVersion(){
		HAPDataTypeVersionImp out = new HAPDataTypeVersionImp();
		out.cloneFrom(this);
		return out;
	}
}
