//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.expression");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['outputCriteria'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting [outputCriteria]  ----------------");
			
			var varCriterias = {};
			_.each(parms.getParm("parms").value, function(varCriteriaData, varName){
				varCriterias[varName] = varCriteriaData.value;
			});
			
			var gatewayParms = {
				"expression" : this.value,
				"variablesCriteria" : varCriterias
			};
			var criteriaStr =  context.getResourceDataByName("myGateWay").command("getOutputCriteria", gatewayParms);
			return {
				dataTypeId : "test.datatypecriteria;1.0.0",
				value : criteriaStr,
			};
		},

		requires:{
			"jsGateway" : { 
				"myGateWay": "discovery",
			}
		},
};

dataTypeDefition.operations['outputCriteriaFromParmData'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting [outputCriteria]  ----------------");
			
			var varCriterias = {};
			_.each(parms.getParm("parms").value, function(parm, varName){
				var parmCriteria = context.operation("test.parm;1.0.0", "getCriteria", [{name:"base", value:parm}]);
				varCriterias[varName] = parmCriteria.value;
			});
			
			var gatewayParms = {
				"expression" : this.value,
				"variablesCriteria" : varCriterias
			};
			var criteriaStr =  context.getResourceDataByName("myGateWay").command("getOutputCriteria", gatewayParms);
			return {
				dataTypeId : "test.datatypecriteria;1.0.0",
				value : criteriaStr,
			};
		},

		requires:{
			"operation" : { 
				op1: "test.parm;1.0.0;getCriteria",
			},
			"jsGateway" : { 
				"myGateWay": "discovery",
			}
		},
};


dataTypeDefition.operations['execute'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting [executeExpression]  ----------------");
			
			var varValues = {};
			_.each(parms.getParm("parms").value, function(varValue, varName){
				varValues[varName] = varValue;
			});
			
			var gatewayParms = {
				"expression" : this.value,
				"variablesValue" : varValues
			};
			var resultData =  context.getResourceDataByName("myGateWay").command("executeExpression", gatewayParms);
			return resultData;
		},

		requires:{
			"jsGateway" : { 
				"myGateWay": "discovery",
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
