//get/create package
var packageObj = library.getChildPackage("test");    

(function(packageObj){
//get used node
var node_wrapperFactory;
var node_createContextElementInfo;
var node_createContext;
var node_createValueStructureVariableInfo;
var node_ServiceInfo;
var node_CONSTANT;
var node_createEventObject;
var node_createWrapperVariable;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_buildContext = function(contextsDefinition){
	
	var loc_contexts = {};
	var loc_eventObject = node_createEventObject();

	_.each(contextsDefinition, function(contextDefintion){
		var contextName = contextDefintion[0];
		var elementInfosArray = [];
		for(var i=1; i<contextDefintion.length; i++){
			var contextElementInfo;
			var contextEleDefinition = contextDefintion[i];
			var contextElementName = contextEleDefinition[0];
			if(contextEleDefinition[3]!=undefined){
				contextElementInfo = node_createContextElementInfo(contextElementName, contextEleDefinition[1], contextEleDefinition[2]);
			}
			else{
				contextElementInfo = node_createContextElementInfo(contextElementName, loc_contexts[contextEleDefinition[1]], contextEleDefinition[2]);
			}
			elementInfosArray.push(contextElementInfo);
		}
		var context = node_createContext(elementInfosArray);
		loc_contexts[contextName] = context;
		
		context.registerContextListener(loc_eventObject, function(event, context, requestInfo){
			  nosliw.logging.debug("Context Event : ", contextName, event);
		}, this);
	});
	
	var loc_out = {
		
		getContext : function(name){  return loc_contexts[name];  },
		
		
		
	};
	
	return loc_out;
};

var node_buildVariableTree = function(variablesDefinition, contexts){
	 var loc_contexts = contexts; 
	 var loc_variables = {};
	 var loc_eventObject = node_createEventObject();
	 _.each(variablesDefinition, function(variableDef, index){
		 var variableName = variableDef[0];
		 var contextName = variableDef[1];
		 var contextEleName = variableDef[2];
		 var path = variableDef[3];
		 
		 var context = loc_contexts.getContext(contextName);
		 var contextVariable = node_createValueStructureVariableInfo(contextEleName, path);
		 var variable = context.createVariable(contextVariable);
		 loc_variables[variableName] = variable;
		 
		 //listen to event from variable
		 variable.registerDataChangeEventListener(loc_eventObject, function(event, path, operationValue, requestInfo){
			  nosliw.logging.debug("Variable Event Data Operation : ", name, event, path, JSON.stringify(operationValue));
		 }, name);

		 variable.registerLifecycleEventListener(loc_eventObject, function(event, data, requestInfo){
			  nosliw.logging.debug("Variable Event Lifecycle : ", name, event, JSON.stringify(data));
		 }, name);
		 
		 nosliw.logging.debug("Variable created: ", name, JSON.stringify(variable.getValue()));
	 });
	 
	 var out = {
			
		getContext : function(name){  return loc_contexts[name];  },
		
		getValue : function(name){
			return this.getVariable(name).getValue();
		},
	 
		getVariable : function(name){
			return loc_variables[name];
		},
		
		getWrapper : function(name){
			this.getVariable(name).getWrapper();
		},
		
		dataOperate : function(operationDef){
			var variable = loc_variables[operationDef[0]];
			var operation = operationDef[1];
			var opValue = operationDef[2];
		    variable.requestDataOperation(new node_ServiceInfo(operation, opValue));
		},

		dataOperates : function(operationsDef){
			for(var i in operationsDef){
				this.dataOperate(operationsDef[i]);
			}
		},
		
		printVariable : function(name){
			 nosliw.logging.debug("Variable value: ", name, JSON.stringify(this.getValue(name)));
		}
	 };
	 
	 return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("variable.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.createWrapperVariable", function(){node_createWrapperVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildVariableTree", node_buildVariableTree); 
packageObj.createChildNode("buildContext", node_buildContext); 

})(packageObj);
