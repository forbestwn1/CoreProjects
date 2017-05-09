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

var loc_buildService = function(expression, variables){
	
}	
	
var loc_buildExpressionExecuteRequest = function(expression, variables, handlers){
	createServiceRequestInfoSequence(loc_buildService(service, variables), handlers, requester_parent)	
		
},
		

	
	
var loc_executeOperation = function(dataTypeId, operation, requestInfo){
}	

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


