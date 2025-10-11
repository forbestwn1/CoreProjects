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
//*******************************************   Start Node Definition  ************************************** 	

var node_createUICustomerTagTest = function(envObj){
	var loc_envObj = envObj;

	var loc_containerrView;
	var loc_attributesView;

    var loc_inputVariableInfos = {};

	var loc_embededs = {};

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
				]);
			});
        	
			varWrapperView.append(varInfo.view);
			variablesWrapperView.append(varWrapperView);
		});
		loc_containerrView.append(variablesWrapperView);

		//embeded view
		var embededsWrapperView = $('<div/>');
		embededsWrapperView.append($('<br>Embededs: <br>'));
		
	    var buttonView = $('<button type="button">'+"Show Emdeded"+'</button>');	
		embededsWrapperView.append(buttonView);
		buttonView.bind('click', function(){
			var requestInfo = loc_envObj.getCreateDefaultUIContentWithInitRequest(undefined, embededsWrapperView, {
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

//Register Node by Name
packageObj.createChildNode("createUICustomerTagTest", node_createUICustomerTagTest); 

})(packageObj);
