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
					"expression" : expressionData.value,
					"variablesValue" : {}
				};
				gatewayParms.variablesValue[parms.getParm("elementVariableName").value] = eleData;
				var eleResult =  context.getResourceDataByName("myGateWay").command("executeExpression", gatewayParms);
				valueOut.push(eleResult);
			}, this);

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

dataTypeDefition.operations['getChildrenNames'] = {
		operation : function(parms, context){
			var names = [];
			_.each(this.value, function(arrayValue, index){
				names.push({
					dataTypeId : "test.string;1.0.0",
					value : index+""
				});
			});
			
			return {
				dataTypeId : "test.array;1.0.0",
				value : names
			};
		},
};

dataTypeDefition.operations['getChildData'] = {
		operation : function(parms, context){
			var name = parms.getParm("name").value;
			return this.value[parseInt(name)];
		}
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
