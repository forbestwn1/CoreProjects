package com.nosliw.common.keyvalue;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * provide basic function getting string value by name
 */

public class HAPKeyValue {

	private Map<String, String> m_values = null;
	
	public HAPKeyValue()
	{
		m_values = new LinkedHashMap<String, String>();
	}
	
	protected void setValue(String attribute, String value)
	{
		m_values.put(attribute, value);
	}
	
	protected String getValue(String attribute)
	{
		return (String)m_values.get(attribute);
	} 
	
	protected String[] getAllAttributes()
	{
		return this.m_values.keySet().toArray(new String[0]);
	}
	
	protected void cloneTo(HAPKeyValue entity)
	{
		entity.m_values.clear();
		entity.m_values.putAll(this.m_values);
	}
	
	@Override
	public String toString()
	{
		StringBuffer out = new StringBuffer();
		
		out.append("\n");
		out.append(this.getClass().getName());
		
		Iterator<String> iter = m_values.keySet().iterator();
		while(iter.hasNext())
		{
			Object key = iter.next();
			out.append("     ");
			out.append(key);
			out.append(" : ");
			out.append(m_values.get(key));
			out.append("\n");
		}
		
		out.append("\n");
		
		return out.toString();
	}

	
}
