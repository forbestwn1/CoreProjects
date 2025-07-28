//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.datatypecriteria");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['getChild'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var gatewayParms = {
				"criteria" : this.value,
				"childName" : parms.getParm("childName").value
			};
			var criteriaStr = context.getResourceDataByName("myGateWay").command("getChildCriteria", gatewayParms).data;
			return {
				dataTypeId : "test.datatypecriteria;1.0.0",
				value : criteriaStr,
			};
		},

		requires:{
			"jsGateway" : { 
				myGateWay: "criteria",
			}
		},
};

dataTypeDefition.operations['getChildren'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var gatewayParms = {
				"criteria" : this.value
			};
			var childrenCriterias = context.getResourceDataByName("myGateWay").command("getChildrenCriteria", gatewayParms);
			var out = {
				dataTypeId : "test.map;1.0.0",
				value : {},
			};
			_.each(childrenCriterias, function(childCriteria, name){
				out.value[name] = {
					dataTypeId : "test.datatypecriteria;1.0.0",
					value : childCriteria,
				};
			});
			return out;
		},

		requires:{
			"jsGateway" : { 
				myGateWay: "criteria",
			}
		},
};


dataTypeDefition.operations['addChild'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var gatewayParms = {
				"criteria" : this.value,
				"childName" : parms.getParm("childName").value,
				"child" : parms.getParm("child").value
			};
			var criteriaStr = context.getResourceDataByName("myGateWay").command("addChildCriteria", gatewayParms);
			return {
				dataTypeId : "test.datatypecriteria;1.0.0",
				value : criteriaStr,
			};
		},

		requires:{
			"jsGateway" : { 
				myGateWay: "criteria",
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
