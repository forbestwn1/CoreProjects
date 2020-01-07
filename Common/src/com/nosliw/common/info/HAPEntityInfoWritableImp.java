package com.nosliw.common.info;

public class HAPEntityInfoWritableImp extends HAPEntityInfoImp implements HAPEntityInfoWritable{

	public HAPEntityInfoWritableImp() {	}
	
	public HAPEntityInfoWritableImp(String name, String description) {
		super(name, description);
	}
	
	@Override
	public void setInfo(HAPInfo info) {
		if(info!=null)		this.m_info = info.cloneInfo();  
	}
	
	@Override
	public void setName(String name) {  this.m_name = name;    }

	@Override
	public void setDescription(String description) {   this.m_description = description;   }

	@Override
	public HAPEntityInfoImp clone() {
		HAPEntityInfoWritableImp out = new HAPEntityInfoWritableImp();
		this.cloneToEntityInfo(out);
		return out;
	}

}
