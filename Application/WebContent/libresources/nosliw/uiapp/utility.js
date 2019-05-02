//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_createServiceRequestInfoSequence;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){

	var loc_appDataPrefex = "applicationData_";

	var loc_out = {
		
		getApplicationDataName : function(moduleDef){
			var dataDependency = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_DATADEPENDENCY];
			for(var i in dataDependency){
				var dataName = dataDependency[i];
				if(dataName.startsWith(loc_appDataPrefex)){
					return dataName.substring(loc_appDataPrefex.length);
				}
			}
		},
			
		getApplicationDataIOName : function(appDataName){
			return loc_appDataPrefex+appDataName;
		},
		
		buildModuleExternalIO : function(appIOContext, applicationData, appDataService){
			var externalDataIO = node_createIODataSet();
			externalDataIO.setData(undefined, appIOContext.generateDataEle());
			
			_.each(applicationData, function(appDataInfo, index){
				var dataIOName = loc_out.getApplicationDataIOName(appDataInfo.dataName);
				externalDataIO.setData(dataIOName, node_createDynamicData(
					function(handlers, request){
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getGetAppDataByIdRequest(appDataInfo.dataName, appDataInfo.dataId, {
							success : function(request, dataInfo){
								return dataInfo.data;
							}
						}));
						return out;
					},
					function(value, handlers, request){
						return appDataService.getUpdateAppDataRequest(appDataInfo.dataName, appDataInfo.dataId, value, handlers, request);
					}
				));
			});
			return externalDataIO;
		},
		
		buildModuleInputMapping : function(externalDataIO, moduleDef){
			var inputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
			var out = {};
			_.each(inputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(externalDataIO, mapping);
			});
			return out;

		},
		
		buildModuleOutputMapping : function(externalDataIO, moduleRuntime, moduleDef){
			var outputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
			var out = {};
			_.each(outputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(moduleRuntime.getModule().getIOContext(), mapping, externalDataIO);
			});
			return out;
		},

	};
	
	return loc_out;
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
