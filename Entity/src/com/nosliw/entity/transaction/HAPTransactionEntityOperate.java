package com.nosliw.entity.transaction;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.dataaccess.HAPDataContext;

public class HAPTransactionEntityOperate extends HAPTransaction{

	public HAPTransactionEntityOperate(HAPConfigure configure, HAPEntityDataAccess access, HAPDataContext dataContext) {
		super(configure, access, dataContext);
		this.setOperationScope(HAPConstant.ENTITYOPERATION_SCOPE_ENTITY);
	}

}
