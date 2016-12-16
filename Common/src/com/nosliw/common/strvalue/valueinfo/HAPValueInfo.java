package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntityBasic;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String TYPE = "type";

	public static final String DBCOLUMNINFOS = "dbColumnInfos";

	public static final String DBCOLUMNLINFO = "dbColumnInfo";
	
	abstract public HAPValueInfo clone();
	
	abstract public HAPStringableValue buildDefault();
	
	public HAPValueInfo(){
		this.updateComplexChild(DBCOLUMNINFOS, HAPConstant.STRINGALBE_VALUEINFO_LIST);
	}
	
	public HAPValueInfo getElement(String name){
		return null;
	}
	
	public HAPStringableValueList<HAPDBColumnInfo> getDBColumnInfos(){		return this.getListAncestorByPath(DBCOLUMNINFOS);	}
	public void addDbColumnInfo(HAPDBColumnInfo columnInfo){		this.getDBColumnInfos().addChild(columnInfo);	}
	
	public String getValueInfoType(){	return this.getAtomicAncestorValueString(HAPValueInfo.TYPE);	}

	public HAPValueInfo getSolidValueInfo(){		return this;	}
	public String getSolidValueInfoType(){		return this.getValueInfoType();	}
	
	@Override
	public void init(){
		super.init();
	}

	protected HAPValueInfoManager getValueInfoManager(){ return HAPValueInfoManager.getInstance(); }
	
	protected void cloneFrom(HAPValueInfo valueInfo){
		super.cloneFrom(valueInfo);
	}
}
