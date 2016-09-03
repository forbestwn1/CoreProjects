package com.nosliw.common.interpolate;

public abstract class HAPInterpolateProcessor {

	private String m_startToken;
	private String m_endToken;
	
	public HAPInterpolateProcessor(String startToken, String endToken){
		this.m_startToken = startToken;
		this.m_endToken = endToken;
	}
	
	protected abstract String processIterpolate(String iterpolate, Object object);

	/*
	 * find all element bounded by start and end tokens, and process it by processor
	 */
	public HAPInterpolateOutput processExpression(String text, Object obj){
		HAPInterpolateOutput out = new HAPInterpolateOutput();

		int startTokenLen = m_startToken.length();
		int endTokenLen = m_endToken.length();
		
		String output = text;
		int start = 0;
		int end = -1;
		start = output.indexOf(m_startToken, start);
		while(start!=-1){
			end = output.indexOf(m_endToken, start);
			if(end==-1)  break;
			String expression = output.substring(start+startTokenLen, end);
			String varValue = this.processIterpolate(expression, obj);
			if(varValue==null){
				//unsolved element
				varValue = output.substring(start, end+endTokenLen);
				out.addUnsolved(expression);
			}
			output = output.substring(0, start) + varValue + output.substring(end+endTokenLen);
			start = output.indexOf(m_startToken, start+varValue.length()-1);
		}
		out.setOutput(output);
		return out;
	}
	
}
