//get/create package
var packageObj = library.getChildPackage("test");    

(function(packageObj){
//get used node
var node_wrapperFactory;
var node_ServiceInfo;
var node_CONSTANT;
var node_createEventObject;
var node_createWrapperVariable;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_buildVariableTree = function(treeDefinition){
	 var loc_eventObject = node_createEventObject();
	 var loc_variables = {};
	 _.each(treeDefinition, function(nodeDef, index){
		 var name = nodeDef[0];
		 var data_parent = nodeDef[1];
		 var path = nodeDef[2];
		 var variable;
		 if(nodeDef[3]!=undefined){
			 //data base
			  var wrapper = node_wrapperFactory.createWrapper(data_parent, path);
			  variable = node_createWrapperVariable(wrapper);
		 }
		 else{
			 //relative
			  variable = node_createWrapperVariable(loc_variables[data_parent], path);
		 }

		 //listen to event from variable
		 variable.registerDataChangeEventListener(loc_eventObject, function(event, path, operationValue, requestInfo){
			  nosliw.logging.debug("Event Data Operation : ", name, event, path, JSON.stringify(operationValue));
		 }, name);

		 variable.registerLifecycleEventListener(loc_eventObject, function(event, data, requestInfo){
			  nosliw.logging.debug("Event Lifecycle : ", name, event, JSON.stringify(data));
		 }, name);
		 
		 loc_variables[name] = variable;
		 nosliw.logging.debug("Variable created: ", name, JSON.stringify(variable.getValue()));
	 });
	 
	 var out = {
			 
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
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createWrapperVariable", function(){node_createWrapperVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildVariableTree", node_buildVariableTree); 

})(packageObj);
