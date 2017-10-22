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
			_.each(parms.getParm("parms"), function(varName, varCriteriaData){
				varCriterias[varName] = varCriteriaData.value;
			});
			
			var gatewayParms = {
				expression : this.value,
				variablesCriteria : varCriterias
			};
			return  context.getResourceDataByName("myGateWay").command("getOutputCriteria", gatewayParms);
		},

		requires:{
			"jsGateway" : { 
				myGateWay: "discovery",
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
