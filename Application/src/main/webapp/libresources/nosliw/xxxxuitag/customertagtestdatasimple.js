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
	var node_requestServiceProcessor;
	var node_complexEntityUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUICustomerTagTestDataSimple = function(envObj){
	var loc_envObj = envObj;

	var loc_inputDataInfo = {};
	var loc_wrapperView;

	var loc_initViews = function(handlers, request){
		loc_wrapperView = $('<div/>');
		_.each(loc_inputDataInfo, function(dataInfo, varName){
			loc_wrapperView.append($('<br/>'));
			loc_createDataControlView(varName);
			loc_wrapperView.append(dataInfo.wrapperView);
		});
		return loc_wrapperView;
	};
	
	var loc_createDataControlView = function(varName){
		var dataInfo = loc_inputDataInfo[varName];
		
		var wrapperView = $("<div/>");
		wrapperView.append($("<br>Variable Name: "+varName+"</br>"));
		wrapperView.append($("<br>"+"Variable Id:"+dataInfo.variable.getVariable().prv_id+"</br>"));
		if(dataInfo.dataType=="string"){
			var dataControlView = $('<input type="text" style="display:inline;background:#e6dedc"/>');
			dataInfo.view = dataControlView;
			dataControlView.bind('change', function(){
				var viewData;
				var value = dataControlView.val();
				if(value==undefined || value==""){}
				else{
					viewData = {
						dataTypeId: "test.string;1.0.0",
						value: value
					};
				}
				loc_onDataChange(varName, viewData);
			});
		}
		wrapperView.append(dataControlView);
		dataInfo.wrapperView = wrapperView;
	};


	var loc_onDataChange = function(varName, data){
		var dataInfo = loc_inputDataInfo[varName];
		
		var currentData = dataInfo.currentData;
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
		
		dataInfo.currentData = currentData;
		
		loc_envObj.executeBatchDataOperationRequest([
			loc_envObj.getDataOperationSet(dataInfo.variable, "", currentData)
		]);
		loc_envObj.trigueEvent("valueChanged", {
			varName : varName,
			data : currentData
		});
	};

	var loc_getUpdateViewsRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		_.each(loc_inputDataInfo, function(dataInfo, varName){
			out.addRequest(loc_getUpdateVariableViewRequest(varName));
		});
		return out;
	};

	var loc_getUpdateVariableViewRequest = function(varName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var dataInfo = loc_inputDataInfo[varName];
		out.addRequest(loc_envObj.getDataOperationRequestGet(dataInfo.variable, "", {
			success : function(requestInfo, data){
				if(dataInfo.dataType=="string"){
					if(data==undefined || data.value==undefined)  dataInfo.view.val("");
					else dataInfo.view.val(data.value.value);
				}
			}
		}));
		return out;
	};

	var loc_out = {
		
		created : function(){
		},
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			//create variables for each internal 
			_.each(loc_envObj.getAttributes(), function(attr, attrName){
				if(loc_envObj.getAttributeValue(attrName)!=undefined){
					var dataAttrPrefix = "data_";
					if(attrName.startsWith(dataAttrPrefix)){
						var varName = "internal_"+attrName;
	
						var coreAttrName = attrName.substring(dataAttrPrefix.length);
						var dataType = coreAttrName;
						var index = coreAttrName.indexOf("_");
						if(index!=-1){
							dataType = coreAttrName.substring(index+1);
						}
	
						var dataVariable = loc_envObj.createVariableByName(varName);
						
						loc_inputDataInfo[varName] = {
							variable : dataVariable,
							variableName : varName,
							dataType : dataType,
						};
					}
				}
			});
			return out;
		},
		initViews : function(handlers, request){
			return loc_initViews(handlers, request);
		},
		postInit : function(request){
			return loc_getUpdateViewsRequest(undefined, request);
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
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUICustomerTagTestDataSimple", node_createUICustomerTagTestDataSimple); 

})(packageObj);
