package com.nosliw.data.core.domain.entity.expression.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.expression.data1.HAPIdExpressionSuite;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPSupplementResourceId;

public class HAPResourceIdExpressionSuite  extends HAPResourceIdSimple{

	private HAPIdExpressionSuite m_expressionSuiteId; 
	
	public HAPResourceIdExpressionSuite(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE);  }

	public HAPResourceIdExpressionSuite(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdExpressionSuite(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdExpressionSuite(HAPIdExpressionSuite expressionSuiteId){
		this();
		init(null, null);
		this.m_expressionSuiteId = expressionSuiteId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(expressionSuiteId, HAPSerializationFormat.LITERATE); 
	}

	public HAPResourceIdExpressionSuite(String id, HAPSupplementResourceId supplement){
		this();
		init(id, supplement);
	}
	
	@Override
	public void setId(String id){
		super.setId(id);
		this.m_expressionSuiteId = new HAPIdExpressionSuite(id);
	}

	public HAPIdExpressionSuite getExpressionSuiteId(){  return this.m_expressionSuiteId;	}
	
	@Override
	public HAPResourceIdExpressionSuite clone(){
		HAPResourceIdExpressionSuite out = new HAPResourceIdExpressionSuite();
		out.cloneFrom(this);
		return out;
	}
}
