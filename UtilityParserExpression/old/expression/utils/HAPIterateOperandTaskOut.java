package com.nosliw.data.expression.utils;

import com.nosliw.data.core.expression.HAPOperand;

/*
 * operand iterate task method output
 */
public class HAPIterateOperandTaskOut{
	//operand object looks like after processing
	public HAPOperand outOperand;
	//the input data for child operand
	public Object childData;
	//whether to iterate child operand
	public boolean toChild = true;
}

