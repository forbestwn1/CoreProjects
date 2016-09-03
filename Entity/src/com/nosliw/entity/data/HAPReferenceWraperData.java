package com.nosliw.entity.data;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPReferenceWraperData extends HAPDataImp {
	private int m_refType;
	
	private HAPEntityID m_ID;
	private String m_path;
	
	HAPReferenceWraperData(HAPEntityID ID, HAPDataType dataType){
		super(dataType);
		this.m_refType = HAPReference.REFERENCE_ID;
		this.m_ID = ID;
	}

	HAPReferenceWraperData(String path, HAPDataType dataType){
		super(dataType);
		this.m_refType = HAPReference.REFERENCE_PATH;
		this.m_path = path;
	}

	public int getReferenceType(){
		return this.m_refType;
	}
	
	public String getReferencePath(){
		return this.m_path;
	}
	
	public HAPEntityID getReferenceID(){
		return this.m_ID;
	}
	
	@Override
	public HAPData cloneData() {
		HAPData out = null;
		switch(this.m_refType){
		case HAPReference.REFERENCE_ID:
			out = new HAPReferenceWraperData(this.m_ID, this.getDataType());
			break;
		case HAPReference.REFERENCE_PATH:
			out = new HAPReferenceWraperData(this.m_path, this.getDataType());
			break;
		}
		return out;
	}

	@Override
	public String toDataStringValue(String format) {
		String out = null;
		if(format.equals(HAPConstant.SERIALIZATION_JSON)){
			if(this.m_ID!=null){
				out = this.m_ID.toStringValue(format);
			}
			else if(this.m_path!=null){
				out = this.m_path;
			}
		}
		return out;
	}

	@Override
	public boolean isEmpty() {		
		return m_ID==null && HAPBasicUtility.isStringEmpty(this.m_path);
	}
}
