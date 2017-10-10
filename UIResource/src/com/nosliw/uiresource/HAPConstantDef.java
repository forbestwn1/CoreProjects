package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.runtime.HAPRuntime;

@HAPEntityWithAttribute
public class HAPConstantDef extends HAPSerializableImp{
	@HAPAttribute
	public static String LITERATE = "literate";

	@HAPAttribute
	public static String PROCESSED = "processed";
	
	@HAPAttribute
	public static String VALUE = "value";
	
	private Object m_definitionObj;
	
	private Object m_value;
	
	private boolean m_isProcessed = false;
	
	public HAPConstantDef(Object defObj){
		this.m_definitionObj = defObj;
	}

	/**
	 * get data after processing the constant
	 * @return
	 */
	public Object getValue(){
		return this.m_value;
	}
	
	public boolean processed(){  return this.m_isProcessed; }
	
	/**
	 * process to get data of constant
	 */
	public void process(
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		if(!this.processed()){
			Object data = this.processJsonNode(m_definitionObj, constantDefs, idGenerator, expressionMan, runtime);
			if(data==null)	this.m_value = this.m_definitionObj;
			else		this.m_value = data;
			this.m_isProcessed = true;
		}
	}
	
	/**
	 * Process any json node
	 * @param node
	 * @param constantDefs
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 * @return
	 */
	private Object processJsonNode(
			Object node,
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		Object out = null;
		try{
			if(node instanceof JSONObject){
				JSONObject jsonObj = (JSONObject)node;
				Iterator<String> keys = jsonObj.keys();
				Map<String, Object> leafDatas = new LinkedHashMap<String, Object>();
				while(keys.hasNext()){
					String key = keys.next();
					Object childNode = jsonObj.get(key);
					Object data = this.processJsonNode(childNode, constantDefs, idGenerator, expressionMan, runtime);
					if(data!=null)   leafDatas.put(key, data);
				}
				for(String key : leafDatas.keySet()){
					jsonObj.remove(key);
					jsonObj.put(key, leafDatas.get(key));
				}
			}
			else if(node instanceof JSONArray){
				JSONArray jsonArray = (JSONArray)node;
				for(int i=0; i<jsonArray.length(); i++){
					Object childNode = jsonArray.get(i);
					Object data = this.processJsonNode(childNode, constantDefs, idGenerator, expressionMan, runtime);
					if(data!=null)   jsonArray.put(i, data);
				}
			}
			else{
				out = this.processLeaf(node, constantDefs, idGenerator, expressionMan, runtime);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Process leaf node only, as script expression can only defined in leaf node 
	 * @param leafData
	 * @param constantDefs
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 * @return
	 */
	private Object processLeaf(
			Object leafData,
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		
		StringBuffer valueStr = new StringBuffer();
		Object valueObj = null;
		List<Object> segments = HAPUIResourceParserUtility.parseUIExpression(leafData.toString(), idGenerator, expressionMan);
		for(Object segment : segments){
			if(segment instanceof HAPScriptExpression){
				HAPScriptExpression sciptExpression = (HAPScriptExpression)segment;
				//find all required constants names in script expressions
				Set<String> expConstantNames = new HashSet<String>();
				Set<String> scriptConstantNames = new HashSet<String>();
				
				List<Object> uiExpEles = sciptExpression.getElements();
				for(Object uiExpEle : uiExpEles){
					if(uiExpEle instanceof HAPExpressionDefinition){
						HAPExpressionDefinition expDef = (HAPExpressionDefinition)uiExpEle;
						HAPOperand expOperand = expDef.getOperand();
						expConstantNames.addAll(HAPExpressionUtility.discoveryUnsolvedConstants(expOperand));
					}
					else if(uiExpEle instanceof HAPScriptExpressionScriptSegment){
						HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)uiExpEle;
						scriptConstantNames.addAll(scriptSegment.getConstants());
					}
				}
				
				//build expression parms
				Map<String, HAPData> expParms = new LinkedHashMap<String, HAPData>();
				for(String constantName : expConstantNames){
					HAPConstantDef constantDef = constantDefs.get(constantName);
					constantDef.process(constantDefs, idGenerator, expressionMan, runtime);
					HAPData parm = constantDef.getDataValue();
					expParms.put(constantName, parm);
				}
				
				//build script constants parm
				Map<String, Object> scriptConstants = new LinkedHashMap<String, Object>();
				for(String scriptConstantName : scriptConstantNames){
					HAPConstantDef constantDef = constantDefs.get(scriptConstantName);
					constantDef.process(constantDefs, idGenerator, expressionMan, runtime);
					Object constant = constantDef.getValue();
					scriptConstants.put(scriptConstantName, constant);
				}
				
				//process script scriptExpression
				Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
				for(Object ele : sciptExpression.getElements()){
					if(ele instanceof HAPExpressionDefinition){
						HAPExpressionDefinition expDef = (HAPExpressionDefinition)ele;
						HAPExpression expression = expressionMan.processExpression(expDef.getName(), expDef, expParms, null);
						expressions.put(expDef.getName(), expression);
					}
				}
				
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(sciptExpression, expressions, null, scriptConstants);
				HAPServiceData serviceData = runtime.executeTaskSync(task);
				
				if(segments.size()>1){
					//if have more than one segment, use the string value of result of script expression
					valueStr.append(serviceData.getData().toString());
				}
				else{
					//if have only one script expression, then use its object value
					valueObj = serviceData.getData();
				}
			}
			else{
				valueStr.append(segment.toString());
			}
		}
		
		if(valueObj!=null){
			//if only have one js expression
			return valueObj;
		}
		else if(segments.size()==1){
			//if no js expression, use the original one
			return leafData;
		}
		else{
			return valueStr.toString();
		}
	}
	
	
	private HAPData getDataValue(){
		return new HAPDataWrapper(this.m_value.toString());
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(LITERATE, this.m_definitionObj.toString());
		jsonMap.put(PROCESSED, this.m_isProcessed+"");
		typeJsonMap.put(PROCESSED, Boolean.class);
		jsonMap.put(VALUE, this.m_value.toString());
		typeJsonMap.put(VALUE, this.m_value.getClass());
	}
}
