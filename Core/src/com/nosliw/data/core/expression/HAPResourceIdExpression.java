package com.nosliw.data.core.expression;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdExpression  extends HAPResourceIdSimple{

	private HAPExpressionId m_expressionId; 
	
	public HAPResourceIdExpression(){    super(HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION);     }

	public HAPResourceIdExpression(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdExpression(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdExpression(HAPExpressionId expressionId){
		this();
		init(null, null);
		this.m_expressionId = expressionId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(expressionId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_expressionId = new HAPExpressionId(id);
	}

	public HAPExpressionId getExpressionId(){  return this.m_expressionId;	}
	
	public HAPResourceIdExpressionSuite getExpressionSuiteResourceId() {
		HAPExpressionId expressionId = this.getExpressionId();
		HAPResourceIdExpressionSuite out = new HAPResourceIdExpressionSuite(expressionId.getSuiteId(), this.getSupplement());
		return out;
	}
	
	@Override
	public HAPResourceIdExpression clone(){
		HAPResourceIdExpression out = new HAPResourceIdExpression();
		out.cloneFrom(this);
		return out;
	}
}
