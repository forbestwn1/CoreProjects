function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_complexEntityUtility = nosliw.getNodeData("complexentity.complexEntityUtility");
	var node_taskExecuteUtility = nosliw.getNodeData("task.taskExecuteUtility");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_getWithValuePortInterface = nosliw.getNodeData("valueport.getWithValuePortInterface");
	var node_getEntityObjectInterface = nosliw.getNodeData("complexentity.getEntityObjectInterface");
	var node_utilityNamedVariable = nosliw.getNodeData("valueport.utilityNamedVariable");
	var node_makeObjectWithApplicationInterface = nosliw.getNodeData("component.makeObjectWithApplicationInterface");
	var node_interactiveUtility = nosliw.getNodeData("task.interactiveUtility");
	var node_createTaskCore = nosliw.getNodeData("task.createTaskCore");
	var node_dataUtility = nosliw.getNodeData("common.utility.dataUtility");
	var node_TaskResult = nosliw.getNodeData("task.TaskResult");
	var node_ruleUtility = nosliw.getNodeData("rule.ruleUtility");

    var loc_complexEntityDef;
    
	var loc_envInterface = {};
	
	var loc_taskCore;
	
	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return loc_taskCore;
		},
	};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_complexEntityDef = complexEntityDef;
		loc_taskCore = node_createTaskCore(loc_out, loc_out);
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
		},

		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var valuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
			var withValuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT];

			var dataVariable = valuePortContainer.getVariableByName(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST, node_COMMONCONSTANT.NAME_ROOT_DATA);
			out.addRequest(dataVariable.getGetValueRequest({
				success : function(request, dataValue){
					var value = dataValue==undefined? undefined : dataValue.value;
					
            		var dataRuleDef = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTASKTASKSCRIPT_EXTRA);
					var enumDataSet = dataRuleDef[node_COMMONATRIBUTECONSTANT.DATARULEENUM_DATASET];
					
					if(enumDataSet==undefined){
						//for enum code, try get enum data set
					}

					for(var i in enumDataSet){
						if(node_dataUtility.isDataEqual(enumDataSet[i], value)==true){
    						return node_ruleUtility.createRuleValidationSuccessResult(); 
						}
					}

    				return node_ruleUtility.createRuleValidationFailResult({
						"dataTypeId": "test.string;1.0.0",
						"value": "value is not valid"
					}); 
				}
			}));

			return out;
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
	return loc_out;
}
