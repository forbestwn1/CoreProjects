//get/create package
var packageObj = library.getChildPackage("test");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_createTagUITest;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUICustomerTagTest = function(envObj){
	var loc_envObj = envObj;

	var loc_dataVariables = {};
	var loc_dataUIs = {};
	var loc_dataCurrent = {};
	
	var loc_wrapperView;
	var loc_stringInputView;
	var loc_contentView;
	

	var loc_updateViews = function(varName, request){
		_.each(loc_dataVariables, function(dataVar, varName){
			loc_updateVariableView(varName, request);
		});
	};
	
	var loc_updateVariableView = function(varName, request){
		loc_envObj.executeDataOperationRequestGet(loc_dataVariables[varName], "", {
			success : function(requestInfo, data){
				loc_dataUIs[varName].updateView(data.value);
			}
		}, request);
	};

	var loc_onDataChange = function(varName, data){
		var currentData = loc_dataCurrent[varName];
		if(data==undefined){
			currentData = data;
		}
		else{
			if(currentData==undefined){
				currentData = data;
			}
			else{
				currentData[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID] = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID]; 
				currentData[node_COMMONATRIBUTECONSTANT.DATA_VALUE] = data[node_COMMONATRIBUTECONSTANT.DATA_VALUE]; 
			}
		}
		
		loc_dataCurrent[varName] = currentData;
		
		loc_envObj.executeBatchDataOperationRequest([
			loc_envObj.getDataOperationSet(loc_dataVariables[varName], "", currentData)
		]);
		loc_envObj.trigueEvent("valueChanged", loc_currentData);
	};

	var loc_initViews = function(handlers, request){
		loc_wrapperView = $('<div/>');
		_.each(loc_dataUIs, function(dataUI, varName){
			loc_wrapperView.append($('<br/>'));
			loc_wrapperView.append(dataUI.initViews());
		});
		return loc_wrapperView;
	};
	
	var loc_out = {
		
		created : function(){
		},
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			//create variables for each internal 
			_.each(loc_envObj.getAllAttributeNames(), function(attrName){
				var dataAttrPrefix = "data_";
				if(attrName.startsWith(dataAttrPrefix)){
					var varName = "internal_"+attrName;

					var coreAttrName = attrName.substring(dataAttrPrefix.length);
					var dataType = coreAttrName;
					var index = coreAttrName.indexOf("_");
					if(index!=-1){
						dataType = coreAttrName.substring(index+1);
					}

					loc_dataUIs[varName] = node_createTagUITest(varName, dataType, function(varName, data){
						loc_onDataChange(varName, data);
					});
					
					var dataVariable = loc_envObj.createVariableByName(varName);
					loc_dataVariables[varName] = dataVariable; 
				}
			});
			return out;
		},
		initViews : function(handlers, request){
			return loc_initViews(handlers, request);
		},
		postInit : function(request){
			loc_updateViews(request);
		},
		destroy : function(request){
			loc_dataVariable.release();	
			loc_coreObj.destroy();
		},
	};
	
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.test.createTagUITest", function(){node_createTagUITest = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createUICustomerTagTest", node_createUICustomerTagTest); 

})(packageObj);
