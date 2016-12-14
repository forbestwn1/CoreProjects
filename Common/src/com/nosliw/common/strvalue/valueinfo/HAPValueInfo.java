package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntityBasic;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String TYPE = "type";

	public static final String SQLINFOS = "sqlInfos";

	public static final String SQLINFO = "sqlInfos";
	
	abstract public HAPValueInfo clone();
	
	abstract public HAPStringableValue buildDefault();
	
	public HAPValueInfo(){
		this.updateComplexChild(SQLINFOS, HAPConstant.STRINGALBE_VALUEINFO_LIST);
	}
	
	public HAPValueInfo getElement(String name){
		return null;
	}
	
	public HAPStringableValueList<HAPSqlInfo> getSqlInfos(){		return this.getListAncestorByPath(SQLINFOS);	}
	public void addSqlInfo(HAPSqlInfo sqlInfo){		this.getSqlInfos().addChild(sqlInfo);	}
	
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
