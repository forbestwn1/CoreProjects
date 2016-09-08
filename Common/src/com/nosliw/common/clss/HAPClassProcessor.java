package com.nosliw.common.clss;

import com.nosliw.common.utils.HAPSystemUtility;

public abstract class HAPClassProcessor {

	public void process(Class cls, Object data){
		try {
			if(HAPSystemUtility.isHAPClass(cls)){
				if(this.isValid(cls)){
					this.processClass(cls, data);
					this.process(cls.getSuperclass(), data);
					for(Class inf : cls.getInterfaces()){
						this.process(inf, data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	abstract protected void processClass(Class cls, Object data);
	
	abstract protected boolean isValid(Class cls);
}
