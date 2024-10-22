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

var node_createUICustomerTagTestDataCollection = function(envObj){
	var loc_envObj = envObj;

	var loc_inputDataInfo = {};
	var loc_wrapperView;

	var loc_initArrayElementProcessor = function(varName, requestInfo){
		var dataInfo = loc_inputDataInfo[varName];
		var handleEachElementProcessor = node_createHandleEachElementProcessor(dataInfo.variable, ""); 
		handleEachElementProcessor.registerEventListener(undefined, function(event, eventData, requestInfo){
			if(event=="EACHELEMENTCONTAINER_EVENT_RESET"){
				node_requestServiceProcessor.processRequest(loc_getUpdateViewRequest(undefined, requestInfo));
			}
			else if(event=="EACHELEMENTCONTAINER_EVENT_NEWELEMENT"){
				var req = node_createServiceRequestInfoSequence(undefined, {}, requestInfo);
				req.addRequest(eventData.indexVar.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
					success : function(request, data){
						return loc_getAddEleRequest(eventData.elementVar, eventData.indexVar, data.value.getValue());
					}
				}));
				node_requestServiceProcessor.processRequest(req);
			}
			else if(event=="EACHELEMENTCONTAINER_EVENT_DELETEELEMENT"){
				eventData.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
					success : function(request, data){
						loc_out.prv_deleteEle(node_dataUtility.getValueOfData(data), request);
					}
				}, requestInfo);
			}
		});
		loc_inputDataInfo[varName].handleEachElementProcessor = handleEachElementProcessor;
	};


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
		wrapperView.append($("<br>"+"Variable Id:"+dataInfo.variable.prv_id+"</br>"));
		if(dataInfo.dataType=="array"){
			var dataControlView = $('<div/>');
			loc_initArrayElementProcessor(varName);
		}
		wrapperView.append(dataControlView);
		dataInfo.wrapperView = wrapperView;
		dataInfo.view = dataControlView;
	};


	var loc_getUpdateArrayViewRequest = function(varName, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);

		var dataInfo = loc_inputDataInfo[varName];

		out.addRequest(dataInfo.handleEachElementProcessor.getLoopRequest({
			success : function(requestInfo, eles){
/*
				var addEleRequest = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
				_.each(eles, function(ele, index){
					addEleRequest.addRequest(loc_getAddEleRequest(ele.elementVar, ele.indexVar, index));
				});
				addEleRequest.setParmData("processMode", "promiseBased");
				return addEleRequest;
*/

				var elesRequest = node_createServiceRequestInfoSequence(undefined);
				_.each(eles, function(ele, index){
					var elementVar = ele.elementVar.getVariable();
					var indexVar = ele.indexVar.getVariable();
					
					var variationPoints = {
						afterValueContext: function(complexEntityDef, valueContextId, bundleCore, coreConfigure){
							var valuePortContainer = bundleCore.getVariableDomain().getValuePortContainer(valueContextId);
							valuePortContainer.populateVariable(loc_envObj.getAttributeValue("arrayelement"), elementVar);
							valuePortContainer.populateVariable(loc_envObj.getAttributeValue("arrayindex"), indexVar);
						}
					}
	
					elesRequest.addRequest(loc_envObj.getCreateDefaultUIContentRequest(variationPoints, {
						success: function(request, uiConentNode){
							dataInfo.view.append($("<br>"+"Elment Variable Id:"+elementVar.prv_id+"</br>"));
							dataInfo.view.append($("<br>"+"Index Variable Id:"+indexVar.prv_id+"</br>"));
	
							return node_complexEntityUtility.getInitBrickRequest(uiConentNode.getChildValue().getCoreEntity(), dataInfo.view);
						}
					}));
				});
				elesRequest.setParmData("processMode", "promiseBased");
				return elesRequest;
			}
		}));
		
		return out;
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
				if(dataInfo.dataType=="array"){
					return loc_getUpdateArrayViewRequest(varName);
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
packageObj.createChildNode("createUICustomerTagTestDataCollection", node_createUICustomerTagTestDataCollection); 

})(packageObj);
