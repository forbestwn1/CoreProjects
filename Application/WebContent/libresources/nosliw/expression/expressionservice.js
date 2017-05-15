//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var makeObjectWithLifecycleNode = packageObj.require("common.lifecycle.makeObjectWithLifecycle");
	var createIdServiceNode = packageObj.require("common.idservice.createIdService");
	var createLoggingServiceNode = packageObj.require("common.loggingservice.createLoggingService");
	var createResourceServiceNode = packageObj.require("common.resourceservice.createResoruceService");
	var createExpressionServiceNode = packageObj.require("common.resourceservice.createExpressionService");
//*******************************************   Start Node Definition  ************************************** 	

	
var loc_executeExpression = function(expression, variables, handlers){

	var executeExpressionRequest = createServiceRequestInfoSequence();
	
	//convert variables
	var variablesData = varsData;
	var convertVarsRequest = loc_buildConvertVarsTask(expression.variables, epression.varConverters, {
		success : function(varsData, requestInfo){
			variablesData = varsData;
		}
	});
	executeExpressionRequest.addRequet(convertVarsRequest);
	
	//execute operand
	var executeOperandRequest = loc_buildExecuteOperand(expression.operand, variablesData, {
		success : function(operandResult, requestInfo){
			
		}
	});
	executeExpressionRequest.addRequet(executeOperandRequest);
	
	//execute task
	nosliw.getRequestServiceManager().processRequest(executeExpressionRequest, false);
};
	
//convert 
var loc_buildConvertVarsTask = function(varsData, varsConverter, handlers){
	//convert variables
	var contertedVars = {};
	var varConvertRequest = createServiceRequestInfoSequenceSet();
	var varConverters = expression.varConverters;
	_.each(varsData, function(varData, varName, list){
		var request = loc_buildConvertTask(varData, varsConverter[varName], {
			success : function(convertedVarData, requestInfo){
				contertedVars[varName] = convertedVarData;
			}
		});
		varConvertRequest.add(request);
	}, this);
	
};

//convert individual var
var loc_buildConvertTask = function(data, varConverter, handlers){
	
};


var loc_buildExecuteOperand = function(operand, variables, handlers){
	
};

var loc_buildExecuteOperation = function(dataTypeId, operation, requestInfo){

};	




	
	
	
var loc_buildService = function(expression, variables){
	
};	
	
var loc_buildExpressionExecuteRequest = function(expression, variables, handlers){
	createServiceRequestInfoSequence(loc_buildService(service, variables), handlers, requester_parent)	
		
};
		

	
	
/**
 * 
 */
var createExpressionService = function(){
		
	var loc_out = {
			
		execute : function(expression, variables, requestInfo){
			var operand = expression[NOSLIWATCOMMONTRIBUTECONSTANT.EXPRESSION_OPERAND];
			var variablesInfo = expression[NOSLIWATCOMMONTRIBUTECONSTANT.EXPRESSION_VARIABLES];
			
			
		},
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createExpressionService", createExpressionService); 

})(packageObj);


