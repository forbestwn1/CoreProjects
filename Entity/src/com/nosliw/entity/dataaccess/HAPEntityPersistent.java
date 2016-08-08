package com.nosliw.entity.dataaccess;

import com.nosliw.common.configure.HAPConfigure;

public abstract class HAPEntityPersistent extends HAPEntityDataAccessImp{

	public HAPEntityPersistent(HAPConfigure configure) {
		super(configure, null, null);
	}

}
