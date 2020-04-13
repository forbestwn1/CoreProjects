package com.nosliw.common.info;

public class HAPEntityInfoWritableImp extends HAPEntityInfoImp implements HAPEntityInfoWritable{

	public HAPEntityInfoWritableImp() {	}
	
	public HAPEntityInfoWritableImp(String name, String description) {
		super(name, description);
	}
	
	@Override
	public HAPEntityInfoImp cloneEntityInfo() {
		HAPEntityInfoWritableImp out = new HAPEntityInfoWritableImp();
		this.cloneToEntityInfo(out);
		return out;
	}

}
