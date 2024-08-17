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
	var node_createHandleEachElementProcessor;
	var node_requestServiceProcessor;
	var node_complexEntityUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createTagUITest = function(varName, variable, dataType, dataChangeHandler, envObj){
	var loc_envObj = envObj;

	var loc_varName = varName;
	var loc_variable = variable;
	var loc_dataType = dataType;
	
	var loc_wrapperView;
	var loc_dataControlView;
	
	var loc_dataChangeHandler = dataChangeHandler;

	//array handler
	var loc_handleEachElementProcessor;

	var loc_initArrayElementProcessor = function(requestInfo){
		loc_handleEachElementProcessor = node_createHandleEachElementProcessor(loc_variable, ""); 
		loc_handleEachElementProcessor.registerEventListener(undefined, function(event, eventData, requestInfo){
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
	};

	var loc_getUpdateArrayViewRequest = function(handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);

		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
/*
			for(var i in loc_childVaraibles){
				loc_out.prv_deleteEle(0, requestInfo);
			}
*/			
		}));

		out.addRequest(loc_handleEachElementProcessor.getLoopRequest({
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
					var variationPoints = {
						afterValueContext: function(complexEntityDef, valueContextId, bundleCore, coreConfigure){
							var loc_valueContext = bundleCore.getVariableDomain().getValueContext(valueContextId);
							loc_valueContext.populateVariable(loc_envObj.getAttributeValue("arrayelement"), ele.elementVar.getVariable());
							loc_valueContext.populateVariable(loc_envObj.getAttributeValue("arrayindex"), ele.indexVar.getVariable());
						}
					}
	
					elesRequest.addRequest(loc_envObj.getCreateDefaultUIContentRequest(variationPoints, {
						success: function(request, uiConentNode){
							return node_complexEntityUtility.getInitBrickRequest(uiConentNode.getChildValue().getCoreEntity(), loc_dataControlView);
						}
					}));
				});
				return elesRequest;
			}
		}));
		
		return out;
	};
	
	/**
	*  eleVar : variable for element
	*  indexVar : index variable for index of element
	*  path : element's path from parent
	**/
	var loc_getAddEleRequest = function(eleVar, indexVar, index, handlers, requestInfo){
		var variables = {};
		variables[loc_getElementVariableName()] = eleVar;
		variables[loc_getIndexVariableName()] = indexVar;
		
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
		out.addRequest(loc_coreObj.getCreateElementViewRequest(loc_env.getId()+"."+loc_generateId(), index, variables, {
			success : function(requestInfo, view){
				loc_childVaraibles.splice(index, 0, variables);
			}
		}));
		return out;
	};

	var loc_createDataControlView = function(){
		loc_wrapperView = $("<div/>");
		if(loc_dataType=="string"){
			loc_dataControlView = $('<input type="text" style="display:inline;background:#e6dedc"/>');
			loc_dataControlView.bind('change', function(){
				loc_dataChangeHandler(loc_varName, loc_getViewData());
			});
		}
		else if(loc_dataType=="array"){
			loc_dataControlView = $('<div/>');
			loc_initArrayElementProcessor();
		}
		loc_wrapperView.append(loc_dataControlView);
	};
	
	var loc_getViewData = function(){
		if(loc_dataType=="string"){
			var value = loc_dataControlView.val();
			if(value==undefined || value=="")  return;
			return {
				dataTypeId: "test.string;1.0.0",
				value: value
			};
		}
	};

	var loc_updateView = function(data, request){
		if(loc_dataType=="string"){
			if(data==undefined || data.value==undefined)  loc_dataControlView.val("");
			else loc_dataControlView.val(data.value);
		}
		else if(loc_dataType=="array"){
			node_requestServiceProcessor.processRequest(loc_getUpdateArrayViewRequest(undefined, request));
		}
	};

	var loc_getCreateArrayElementViewRequest = function(id, index, variables, handlers, requestInfo){
		var extendeContextInfo = [];
		_.each(variables, function(variable, name){
			extendeContextInfo.push(loc_base.getEnv().createContextElementInfo(name, variable));
		});
		var eleContext = loc_base.getEnv().createExtendedContext(extendeContextInfo, requestInfo);

		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
		out.addRequest(loc_base.getEnv().getCreateUIViewWithIdRequest(id, eleContext, {
			success : function(requestInfo, resourceView){
				if(index==0)	resourceView.appendTo(loc_view);
				else	resourceView.insertAfter(loc_childViews[index-1].getViews());
				loc_childViews.splice(index, 0, resourceView);
				return resourceView;
			}
		}));
		return out;
	};


	var loc_out = {

		initViews : function(handlers, request){
			loc_createDataControlView();
			return loc_wrapperView;
		},

		updateView : function(data, request){
			loc_updateView(data, request);
		}

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
nosliw.registerSetNodeDataEvent("variable.orderedcontainer.createHandleEachElementProcessor", function(){node_createHandleEachElementProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTagUITest", node_createTagUITest); 

})(packageObj);
