//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefinition = nosliw.getDataTypeDefinition("test.options");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefinition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefinition.operations['all'] = {
		operation : function(parms, context){
			var valueOut = [];
			var gatewayParms = {
				"id" : parms.getParm("optionsId").value,
			};
			var out = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest("options", "getValues", gatewayParms, {
//			var out =  context.getResourceDataByName("myGateWay").request("getValues", gatewayParms, {
				success : function(requestInfo, optionsValues){
					_.each(optionsValues, function(value, i){
						valueOut.push({
							dataTypeId : "test.options;1.0.0",
							value : value,
						});
					});
					return {
						dataTypeId : "test.array;1.0.0",
						value : valueOut,
					};
				}
			});
			return out;
		},

		requires:{
		},
};


dataTypeDefinition.convert = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(data, toDataType, reverse, context){
			if(reverse){
				//from string
				return {
					dataTypeId : "test.options;1.0.0",
					value : {
						value : data.value
					}
				};
			}
			else{
				//to string
				return {
					dataTypeId : "test.string;1.0.0",
					value : data.value.value
				};
			}
		} 
};

nosliw.addDataTypeDefinition(dataTypeDefinition);
