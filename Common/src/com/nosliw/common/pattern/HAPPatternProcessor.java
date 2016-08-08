package com.nosliw.common.pattern;

public interface HAPPatternProcessor {

	public String getName();
	
	/*
	 * the method to parse text to object
	 * 		text: text to parse
	 * 		data: sometime, parsing need more information
	 */
	public Object parse(String text, Object data);

	/*
	 * compose text from object
	 * 		obj: object to compose text
	 * 		data: extra data
	 */
	public String compose(Object obj, Object data);
}
