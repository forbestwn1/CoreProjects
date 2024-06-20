//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createServiceRequestInfoSimple;
	var node_expressionUtility;
	var node_makeObjectWithApplicationInterface;
    var node_createTaskContainerInterface;
	var node_createTaskInterface;
	var node_createValuePortValueFlat;
	var node_interactiveUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataExpressionElementInLibrary = function(expressionData){
	
	var loc_expressionData = expressionData;
	
	var loc_requestValuePort = node_createValuePortValueFlat();
	var loc_resultValuePort = node_createValuePortValueFlat();
	
	var loc_init = function(){
		var parmsValue = {};
		var interactive = loc_expressionData[node_COMMONATRIBUTECONSTANT.WITHINTERACTIVE_INTERACTIVE];
		var parmDefs = interactive[node_COMMONATRIBUTECONSTANT.INTERACTIVE_REQUEST][node_COMMONATRIBUTECONSTANT.INTERACTIVEREQUEST_PARM];
		_.each(parmDefs, function(parmDef, i){
			var defaultValue = parmDef[node_COMMONATRIBUTECONSTANT.REQUESTPARMININTERACTIVE_DEFAULTVALUE];
			if(defaultValue!=undefined){
				parmsValue[parmDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]] = defaultValue;
			}
		});
		loc_requestValuePort.init(node_interactiveUtility.getInteractiveRequestInitValue(loc_expressionData));
	};
	
	var loc_getExecuteRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		var expressionItem = loc_expressionData[node_COMMONATRIBUTECONSTANT.DATAEXPRESSIONELEMENTINLIBRARY_EXPRESSION];
		var variablesContainer = loc_expressionData[node_COMMONATRIBUTECONSTANT.DATAEXPRESSIONUNIT_VARIABLEINFOS];
		var withValuePortInterface = {
			getValuePort : function(valuePortGroup, valuePortName){
				return loc_getValuePort(valuePortGroup, valuePortName);
			}
		};
		out.addRequest(node_expressionUtility.getExecuteDataExpressionRequest(expressionItem, variablesContainer, withValuePortInterface, undefined, undefined, {
			success : function(request, result){
				loc_resultValuePort.setValue("result", result);
				return result;
			}
		}));
		return out;
	};
	
	var loc_getValuePort = function(valuePortGroup, valuePortName){
		if(valuePortName==node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST){
			return loc_requestValuePort;
		}
		else{
			return loc_resultValuePort;
		}
	};
	
	var loc_out = {
		
		getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_init();
			}));
			return out;
		},
		
		getExecuteRequest : function(handlers, request){
			return loc_getExecuteRequest(handlers, request);
		},

		getExternalValuePort : function(valuePortGroup, valuePortName){
			return loc_getValuePort(valuePortGroup, valuePortName);
		},
		
		getInternalValuePort : function(valuePortGroup, valuePortName){
			return loc_getValuePort(valuePortGroup, valuePortName);
		},
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskContainerInterface", function(){	node_createTaskContainerInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("task.createTaskInterface", function(){	node_createTaskInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("valueport.createValuePortValueFlat", function(){	node_createValuePortValueFlat = this.getData();	});
nosliw.registerSetNodeDataEvent("task.interactiveUtility", function(){	node_interactiveUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createDataExpressionElementInLibrary", node_createDataExpressionElementInLibrary); 

})(packageObj);
