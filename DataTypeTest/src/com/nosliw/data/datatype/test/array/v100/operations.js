//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.array");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['process'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var valueOut = [];
			var expressionData = parms.getParm("expression");
			_.each(this.value, function(eleData, i){
				
				var gatewayParms = {
					"expression" : this.value,
					"variablesValue" : {}
				};
				gatewayParms.variablesValue[parms.getParm("elementVariableName")] = eleData;
				var eleResult =  context.getResourceDataByName("myGateWay").command("executeExpression", gatewayParms);
				valueOUt.push(eleResult);
			});

			return {
				dataTypeId : "test.array;1.0.0",
				value : valueOut,
			};
		},

		requires:{
			"jsGateway" : { 
				myGateWay: "discovery",
			}
		},
};

dataTypeDefition.operations['new'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			return {
				dataTypeId : "test.array;1.0.0",
				value : [],
			};
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
