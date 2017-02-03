package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeVersionImp extends HAPStringableValueEntity implements HAPDataTypeVersion{

	@HAPAttribute
	public static String NAME = "name";
	
	public String getName(){  return this.getAtomicAncestorValueString(HAPDataTypeVersionImp.NAME);	}
	public void setName(String name){ this.buildObjectByLiterate(name); }
	
	@Override
	public String getMajor(){  return this.getAtomicAncestorValueString(HAPDataTypeVersion.MAJOR);	}
	@Override
	public String getMinor(){ return this.getAtomicAncestorValueString(HAPDataTypeVersion.MINOR);	}
	@Override
	public String getRevision(){  return this.getAtomicAncestorValueString(HAPDataTypeVersion.REVISION);	}

	@Override
	protected void buildObjectByLiterate(String literateValue){	
		this.updateAtomicChild(HAPDataTypeVersionImp.NAME, literateValue, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);

		String[] segs = HAPNamingConversionUtility.parsePaths(literateValue);
		if(segs.length>=3) this.updateAtomicChild(HAPDataTypeVersion.REVISION, segs[2], HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);   
		if(segs.length>=2) this.updateAtomicChild(HAPDataTypeVersion.MINOR, segs[1], HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);   
		if(segs.length>=1) this.updateAtomicChild(HAPDataTypeVersion.MAJOR, segs[0], HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);   
	}

	@Override
	protected String buildLiterate(){
		String out = this.getName();
		if(HAPBasicUtility.isStringEmpty(out)){
			out = HAPNamingConversionUtility.cascadePath(String.valueOf(this.getMajor()), String.valueOf(this.getMinor()));
			if(HAPBasicUtility.isStringNotEmpty(this.getMinor())){
				out = HAPNamingConversionUtility.cascadePath(out, this.getRevision());
			}
			this.updateAtomicChild(HAPDataTypeVersionImp.NAME, out, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);
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
