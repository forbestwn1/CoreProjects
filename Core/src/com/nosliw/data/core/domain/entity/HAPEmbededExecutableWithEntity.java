package com.nosliw.data.core.domain.entity;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPExecutable;

@HAPEntityWithAttribute
public class HAPEmbededExecutableWithEntity extends HAPEmbededExecutable{

	public HAPEmbededExecutableWithEntity() {}
	
	public HAPEmbededExecutableWithEntity(HAPExecutable executable, String entityType, boolean isComplex) {
		super(executable, entityType, isComplex);
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EMBEDED_TYPE_EXECUTABLE_ENTITY;  }

	public HAPExecutable getEntity() {	return (HAPExecutable)this.getValue();	}
	
	public void setEntity(HAPExecutable executable) {  this.setEntity(executable);  }
	
	@Override
	public HAPEmbededExecutableWithEntity cloneEmbeded() {
		HAPEmbededExecutableWithEntity out = new HAPEmbededExecutableWithEntity();
		this.cloneToEmbeded(out);
		return out;
	}
}
