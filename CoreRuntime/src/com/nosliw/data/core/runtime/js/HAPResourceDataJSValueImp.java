package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPResourceDataJSValueImp extends HAPSerializableImp implements HAPResourceDataJSValue{

	@Override
	public String toString() {   return this.getValue();  }
}
