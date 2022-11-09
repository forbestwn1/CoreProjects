package com.nosliw.common.interpolate;

import java.util.Map;

import com.nosliw.common.utils.HAPUtilityBasic;

/*
 * process method call or attribute
 */
public abstract class HAPPatternProcessorInterpolationDynamic extends HAPPatternProcessorInterpolation{

	public HAPPatternProcessorInterpolationDynamic(String startToken, String endToken){
		super(new HAPInterpolateProcessor(startToken, endToken){
			@Override
			public String processIterpolate(String expression, Object object) {
				if(HAPUtilityBasic.isStringEmpty(expression) || object==null)  return null;
				Object out = null;
				int index = expression.indexOf(".");
				if(index!=-1){
					String exp1 = expression.substring(0, index);
					String exp2 = expression.substring(index+1);
					if(object instanceof Map){
						out = this.processIterpolate(exp2, ((Map)object).get(exp1));
					}
					else{
						out = this.processIterpolate(exp2, this.actionCall(exp1, object));
					}
				}
				else{
					out = this.actionCall(expression, object);
				}
				if(out==null)  return null;
				else return out.toString();
			}
			
			private Object actionCall(String action, Object actionObj){
				String out = null;
				try{
					int index = action.lastIndexOf("()");
					if(index!=-1){
						//method call
						String methodName = action.substring(0, index);
						out = (String)actionObj.getClass().getMethod(methodName).invoke(actionObj);
					}
					else{
						//attribute
						String attrName = action;
						out = (String)actionObj.getClass().getField(attrName).get(actionObj);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return out;
			}
			
		});
	}
}
