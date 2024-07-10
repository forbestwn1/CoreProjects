package com.nosliw.data.core.datasource.imp.secondhand;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.datasource.HAPDefinition;
import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.datasource.HAPDefinitionOutput;
import com.nosliw.data.core.datasource.HAPDefinitionParm;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceUtility;
import com.nosliw.data.core.runtime.js.resource.HAPResourceIdJSGateway;

public class HAPOperandDataSource{
/*
	extends HAPOperandImp{

	@HAPAttribute
	public static final String DATASOURCE_NAME = "dataSource_name";

	@HAPAttribute
	public static final String DATASOURCE_DEFINITION = "dataSource_definition";
	
	@HAPAttribute
	public static final String DATASOURCE_CONSTANT = "dataSource_constant";

	@HAPAttribute
	public static final String DATASOURCE_VARCONFIGURE = "dataSource_varConfigure";
	
	@HAPAttribute
	public static final String DATASOURCE_VARMAPPING = "dataSource_varMapping";
	
	@HAPAttribute
	public static final String DATASOURCE_PARMMATCHERS = "dataSource_parmMatchers";
	
	private String m_dataSourceName;
	
	private HAPDefinition m_dataSourceDefinition;
	
	private Map<String, HAPData> m_constants;
	
	private Map<String, String> m_varConfigure;
	
	private Map<String, String> m_varMapping;
	
	//matchers required by data source parm
	private Map<String, HAPMatchers> m_parmsMatchers;
	
	public HAPOperandDataSource(String dataSourceName, HAPDefinition dataSourceDefinition, Map<String, HAPData> constants, Map<String, String> varConfigure){
		super(HAPConstant.EXPRESSION_OPERAND_DATASOURCE);
		this.m_dataSourceName = dataSourceName;
		this.m_dataSourceDefinition = dataSourceDefinition;
		this.m_constants = constants;
		this.m_varConfigure = varConfigure;
		this.m_varMapping = new LinkedHashMap<String, String>();
		for(String name : varConfigure.keySet()){
			this.m_varMapping.put(varConfigure.get(name), name);
		}
	}

	@Override
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		for(String parm : this.m_parmsMatchers.keySet()){
			out.addAll(HAPResourceUtility.getConverterResourceIdFromRelationship(this.m_parmsMatchers.get(parm).discoverRelationships()));
		}
		return out;	
	}
	
	@Override
	public List<HAPResourceId> getResources() {
		List<HAPResourceId> out = super.getResources();
		out.add(new HAPResourceIdJSGateway(HAPDataSourceManager.GATEWAY_DATASOURCE));
		return out;
	}
	
	@Override
	public HAPMatchers discover(Map<String, HAPVariableDefinition> variablesInfo, HAPVariableDefinition expectCriteria,
			HAPProcessExpressionDefinitionContext context, HAPDataTypeHelper dataTypeHelper) {
		m_parmsMatchers = new LinkedHashMap<String, HAPMatchers>();
		
		Map<String, HAPDefinitionParm> dataSourceParms = this.m_dataSourceDefinition.getParms();
		for(String dataSourceParmName : dataSourceParms.keySet()){
			HAPDefinitionParm dataSourceParm = dataSourceParms.get(dataSourceParmName);
			if(this.m_constants.get(dataSourceParmName)!=null){
				//constant
				
			}
			else{
				String mappedParmName = this.m_varConfigure.get(dataSourceParmName);
				if(mappedParmName!=null){
					//mapped to different variable name
					HAPVariableDefinition variableInfo = variablesInfo.get(mappedParmName);
					this.m_parmsMatchers.put(dataSourceParmName, this.isMatchable(variableInfo.getCriteria(), dataSourceParm.getVaraibleInfo(), context, dataTypeHelper));
				}
				else{
					HAPVariableDefinition variableInfo = variablesInfo.get(dataSourceParmName);
					if(variableInfo==null){
						//add new variable
						HAPVariableDefinition newVariableInfo = new HAPVariableDefinition();
						newVariableInfo.setCriteria(dataSourceParm.getVaraibleInfo());
						newVariableInfo.setStatus(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN);
						variablesInfo.put(dataSourceParmName, newVariableInfo);
					}
					else{
						this.m_parmsMatchers.put(dataSourceParmName, this.isMatchable(variableInfo.getCriteria(), dataSourceParm.getVaraibleInfo(), context, dataTypeHelper));
					}
				}
			}
		}
		
		HAPDefinitionOutput dataSourceOutput = this.m_dataSourceDefinition.getOutput();
		//set output criteria
		this.setOutputCriteria(dataSourceOutput.getCriteria());
		//cal converter
		return this.isMatchable(dataSourceOutput.getCriteria(), expectCriteria, context, dataTypeHelper);
	}

	@Override
	public HAPOperand cloneOperand() {
		HAPOperand out = new HAPOperandDataSource(m_dataSourceName, this.m_dataSourceDefinition, m_constants, m_varConfigure);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATASOURCE_NAME, this.m_dataSourceName);
		jsonMap.put(DATASOURCE_DEFINITION, HAPManagerSerialize.getInstance().toStringValue(this.m_dataSourceDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(DATASOURCE_CONSTANT, HAPManagerSerialize.getInstance().toStringValue(this.m_constants, HAPSerializationFormat.JSON));
		jsonMap.put(DATASOURCE_VARMAPPING, HAPManagerSerialize.getInstance().toStringValue(this.m_varMapping, HAPSerializationFormat.JSON));
		jsonMap.put(DATASOURCE_VARCONFIGURE, HAPManagerSerialize.getInstance().toStringValue(this.m_varMapping, HAPSerializationFormat.JSON));
		jsonMap.put(DATASOURCE_PARMMATCHERS, HAPManagerSerialize.getInstance().toStringValue(this.m_parmsMatchers, HAPSerializationFormat.JSON));
		
	}
*/	
}
