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
	var node_basicUtility;
	var node_createHandleEachElementProcessor;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUICustomerTagTest = function(envObj){
	var loc_envObj = envObj;

	var loc_containerrView;
	var loc_attributesView;
	var loc_referenceCustomEventViews=[];

    var loc_inputVariableInfos = {};

    var loc_referencedCustomTags;

	var loc_embededs = {};

    var loc_processReferencedCustomerTag = function(){
		var query = {
			elements : []
		};
		var queryStr = loc_envObj.getAttributeValue("reference_tag");
		if(queryStr!=undefined){
			var parmSegs = node_namingConvensionUtility.parseLevel1(queryStr);
			_.each(parmSegs, function(parmSeg){
    			var segs = node_namingConvensionUtility.parsePart(parmSeg);
				query.elements.push({
					key : segs[0],
					value : segs[1]
				});
			});
    		loc_referencedCustomTags = loc_envObj.queryCustomTagInstance(query);
    		_.each(loc_referencedCustomTags, function(customTag, i){
				customTag.registerEventListener(undefined, function(event, eventData){
					loc_referenceCustomEventViews[i].val(node_basicUtility.stringify({
						event : event,
						eventData : eventData
					}));
				});
			});
    		
		}
	};

    var loc_presentArrayRequest = function(arrayVariable, wrapperView, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);

		loc_handleEachElementProcessor = node_createHandleEachElementProcessor(arrayVariable, ""); 
		
		out.addRequest(loc_handleEachElementProcessor.getLoopRequest({
			success : function(requestInfo, eles){
				var addEleRequest = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
				_.each(eles, function(ele, index){
					var variationPoints = {
						afterValueContext: function(complexEntityDef, valuePortContainerId, bundleCore, coreConfigure){
							var valuePortContainer = bundleCore.getValuePortDomain().getValuePortContainer(valuePortContainerId);
							var valueStructureRuntimeId = valuePortContainer.getValueStructureRuntimeIdByName("embeded_part1");
							var valueStructure = valuePortContainer.getValueStructure(valueStructureRuntimeId);
							valueStructure.addVariable(ele.elementVar, loc_envObj.getAttributeValue("arrayelement"));
							valueStructure.addVariable(ele.indexVar, loc_envObj.getAttributeValue("arrayindex"));
						}
					}
					addEleRequest.addRequest(loc_envObj.getCreateDefaultUIContentWithInitRequest(variationPoints, wrapperView, {
						success: function(request, uiConentNode){
//							loc_elements.push(uiConentNode.getChildValue().getCoreEntity());
						}
					}));
				});
				addEleRequest.setParmData("processMode", "promiseBased");
				return addEleRequest;
			}
		}));
		
		return out;
	};


    var loc_isValidVariableAttribute = function(attrName){
		var out = false;
		var attrDef = loc_envObj.getAttributeDefinition(attrName);
		var attrType = attrDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITIONATTRIBUTE_TYPE];
		if(attrType==node_COMMONCONSTANT.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE){
    		if(loc_envObj.getAttributeValue(attrName)!=undefined){
				out = true;
			}
		}
		return out;
	};

	var loc_getUpdateAttributeVariableViewRequest = function(attrName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_envObj.getDataOperationRequestGet(loc_inputVariableInfos[attrName].variable, "", {
			success : function(requestInfo, data){
				loc_inputVariableInfos[attrName].view.val(node_basicUtility.stringify(data.value));
			}
		}));
		return out;
	};

    var loc_initEmbededs = function(){
		var entityEnvInterface = loc_envObj.getEntityEnvInterface();
		var valuePortContainer = entityEnvInterface[node_CONSTANT.INTERFACE_ENTITY].getInternalValuePortContainer();
		
		var valueStructures = valuePortContainer.getValueStructures();
		_.each(valueStructures, function(byGroupType){
			_.each(byGroupType, function(byGroupId){
				_.each(byGroupId, function(vsWrapper, vsId){
					if(vsWrapper.isSolid()){
						var vsInfo = vsWrapper.getRuntimeInfo();
						var vsName = vsInfo[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
						if(vsName!=undefined&&vsName.startsWith("embeded_")){
    						var embeded = {
	    						name : vsName
		    				};
			    			loc_embededs[vsId] = embeded;
				    	}
					}
				});
			});
		});
		
	};

	var loc_initViews = function(handlers, request){
		loc_containerrView = $('<div/>');

        //reference customer tag
        if(loc_referencedCustomTags!=undefined){
    		var referenceCustomsWrapperView = $('<div/>');
    		loc_containerrView.append(referenceCustomsWrapperView);
    		
    		_.each(loc_referencedCustomTags, function(ref, i){
        		var referenceCustomWrapperView = $('<div/>');
    			referenceCustomWrapperView.append($('<br>Custom Tag UIId: ' + ref.getUIId() + ' <br>'));
        	    var referenceCustomEventView = $('<textarea rows="6" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
     			referenceCustomWrapperView.append(referenceCustomEventView);
     			loc_referenceCustomEventViews.push(referenceCustomEventView);
     			
     			referenceCustomsWrapperView.append(referenceCustomWrapperView);
			});
		}

        //triggue event 
		var triggueEventWrapperView = $('<div/>');
	    var triggueEventButtonView = $('<button type="button">'+"TriggueEvent"+'</button>');	
		triggueEventButtonView.bind('click', function(){
			var eventData = {
				"eventData": {
    				"dataTypeId": "test.string;1.0.0",
	    			"value": "event data string"
				}
			};
			loc_envObj.trigueEvent("event1", eventData);
		});
		triggueEventWrapperView.append(triggueEventButtonView);
		loc_containerrView.append(triggueEventWrapperView);
		
		//attribute view
		var attributesWrapperView = $('<div/>');
		attributesWrapperView.append($('<br>Attributes: <br>'));
    	loc_attributesView = $('<textarea rows="6" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
		attributesWrapperView.append(loc_attributesView);
		loc_containerrView.append(attributesWrapperView);

        //input variables view
		var variablesWrapperView = $('<div/>');
		variablesWrapperView.append($('<br>Variables: <br>'));
		_.each(loc_inputVariableInfos, function(varInfo, varName){
    		var varWrapperView = $('<div/>');
			varWrapperView.append($('<br>'+varName+':<br>'));
        	varInfo.view = $('<textarea rows="6" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
        	
			varInfo.view.bind('change', function(){
    			var currentData = node_basicUtility.toObject(loc_inputVariableInfos[varName].view.val());
    			
				loc_envObj.executeBatchDataOperationRequest([
					loc_envObj.getDataOperationSet(loc_inputVariableInfos[varName].variable, "", currentData)
				], {
					success : function(requet){
						var kkkk = 5555;
					},
					error : function(request, serviceData){
						loc_envObj.trigueEvent(serviceData.code, serviceData.data);
					}
				});
			});
        	
			varWrapperView.append(varInfo.view);
			variablesWrapperView.append(varWrapperView);
		});
		loc_containerrView.append(variablesWrapperView);

		//embeded view
		var embededsWrapperView = $('<div/>');
		embededsWrapperView.append($('<br>Embededs: <br>'));
		
	    var showEmbededButtonView = $('<button type="button">'+"Show Emdeded"+'</button>');	
		embededsWrapperView.append(showEmbededButtonView);
		showEmbededButtonView.bind('click', function(){
			var requestInfo = loc_envObj.getCreateDefaultUIContentWithInitRequest(undefined, embededsWrapperView, {
				success: function(request, uiConentNode){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo);
		});
		

	    var arrayButtonView = $('<button type="button">'+"Show Array"+'</button>');	
		embededsWrapperView.append(arrayButtonView);
		arrayButtonView.bind('click', function(){
			var requestInfo = loc_presentArrayRequest(loc_inputVariableInfos["data_array"].variable, embededsWrapperView, {
				success: function(request, uiConentNode){
				}
			});
			node_requestServiceProcessor.processRequest(requestInfo);
		});
		

/*		
		_.each(loc_embededs, function(embededInfo){
    		var embedWrapperView = $('<div/>');
			embedWrapperView.append($('<br>'+embededInfo.name+':<br>'));
    		var embedView = $('<div/>');
			embedWrapperView.append(embedView);
		    var buttonView = $('<button type="button">'+embededInfo.name+'</button>');	
			embedWrapperView.append(buttonView);
			buttonView.bind('click', function(){
				var variationPoints = {
					afterValueContext: function(complexEntityDef, valuePortContainerId, bundleCore, coreConfigure){
						var valuePortContainer = bundleCore.getValuePortDomain().getValuePortContainer(valueContextId);
						var valueStructureRuntimeId = valuePortContainer.getValueStructureRuntimeIdByName("nosliw_internal");
						var valueStructure = valuePortContainer.getValueStructure(valueStructureRuntimeId);
						valueStructure.addVariable(loc_envObj.getAttributeValue("element"), ele.elementVar);
						valueStructure.addVariable(loc_envObj.getAttributeValue("index"), ele.indexVar);
					}
				}
				addEleRequest.addRequest(loc_envObj.getCreateDefaultUIContentRequest(variationPoints, {
					success: function(request, uiConentNode){
						loc_elements.push(uiConentNode.getChildValue().getCoreEntity());
					}
				}));
			});
			embededsWrapperView.append(embedWrapperView);
		});
*/		
		loc_containerrView.append(embededsWrapperView);

		
		return loc_containerrView;
	};
	
	var loc_updateAttributeView = function(){
		loc_attributesView.val(node_basicUtility.stringify(loc_envObj.getAttributeValues()));
	};
	
	var loc_out = {
		
		created : function(){
			_.each(loc_envObj.getAllAttributeNames(), function(attrName, i){
				if(loc_isValidVariableAttribute(attrName)){
					loc_inputVariableInfos[attrName] = {};
				}
			});
			
			loc_initEmbededs();
		},
		
		updateAttributes : function(attributes, request){
//            loc_attributeValues = _.extend(loc_attributeValues, attributes);
            loc_updateAttributeView();
		},
		
		initViews : function(handlers, request){
            loc_processReferencedCustomerTag();
			var out = loc_initViews(handlers, request);
			loc_updateAttributeView();
			return out;
		},
		postInit : function(request){
			var out = node_createServiceRequestInfoSequence(undefined, undefined, request);
			_.each(loc_inputVariableInfos, function(varInfo, varName){
				var dataVariable = loc_envObj.createVariableByName(varName);
				varInfo.variable = dataVariable; 
				out.addRequest(loc_getUpdateAttributeVariableViewRequest(varName));
			});	
            
			return out;		
		},
		destroy : function(request){
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
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.orderedcontainer.createHandleEachElementProcessor", function(){node_createHandleEachElementProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUICustomerTagTest", node_createUICustomerTagTest); 

})(packageObj);
