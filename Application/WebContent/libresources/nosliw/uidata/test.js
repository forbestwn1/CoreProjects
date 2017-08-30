	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){

		  var node_wrapperFactory = nosliw.getNodeData("uidata.wrapper.wrapperFactory");
		  var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
		  var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
		  var node_createEventObject = nosliw.getNodeData("common.event.createEventObject");
		
			 var buildWrapperTree = function(treeDefinition){
	  			 var loc_eventObject = node_createEventObject();
				 var loc_wrappers = {};
				 _.each(treeDefinition, function(nodeDef, index){
					 var name = nodeDef[0];
					 var data_parentwrapper = nodeDef[1];
					 var path = nodeDef[2];
					 var wrapper;
					 if(nodeDef[3]!=undefined){
						 //data base
						  wrapper = node_wrapperFactory.createWrapper(data_parentwrapper, path);
					 }
					 else{
						 //relative
						  wrapper = node_wrapperFactory.createWrapper(loc_wrappers[data_parentwrapper], path);
					 }
					 //listen to event from wrapper
					 wrapper.registerDataOperationListener(loc_eventObject, function(event, path, operationValue, requestInfo){
						  nosliw.logging.debug("Event : ", event, path, JSON.stringify(operationValue));
					 }, this);
					 loc_wrappers[name] = wrapper;
					 nosliw.logging.debug("Wrapper created: ", name, JSON.stringify(wrapper.getValue()));
				 });
				 
				 var out = {
					getWrapper : function(name){
						return loc_wrappers[name];
					},
					
					dataOperate : function(operationDef){
						var wrapper = loc_wrappers[operationDef[0]];
						var operation = operationDef[1];
						var opValue = operationDef[2];
					    wrapper.requestDataOperation(new node_ServiceInfo(operation, opValue));
					},

					dataOperates : function(operationsDef){
						for(var i in operationsDef){
							this.dataOperate(operationsDef[i]);
						}
					},
				 };
				 
				 return out;
			 };
			  
		
		  
		  
		  var objectData = {
				 string : "string value",
				 int : 12345,
				 boolean : true,
				 object : {
					 string : "2 string value",
					 int : 12345,
					 boolean : true,
					 object : {
						 
					 },
					 array : [
						 {
							 string : "4 string value",
							 int : 12345,
							 boolean : true,
						 }, 
						 "3 string value",
						 12345,
						 true,
						 ["2 1", "2 2", "2 3", "2 4"]
					],
				 },
				 array : ["1", "2", "3", "4"],
				 
		  };
		  
/*		  
		  var rootWrapper = node_wrapperFactory.createWrapper(objectData, "object");
		  nosliw.logging.debug("root wrapper value", JSON.stringify(rootWrapper.getValue()));

		  var leafWrapper1 = node_wrapperFactory.createWrapper(rootWrapper, "string");
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper1.getValue()));
		  
		  var leafWrapper2 = node_wrapperFactory.createWrapper(rootWrapper1, "array.0");
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper2.getValue()));
		  
		  var leafWrapper3 = node_wrapperFactory.createWrapper(leafWrapper2, "");
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper3.getValue()));
		  
		  var leafWrapper4 = node_wrapperFactory.createWrapper(leafWrapper3, "string");
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper4.getValue()));
		  

		  rootWrapper.requestDataOperation(new node_ServiceInfo(node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}));
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper1.getValue()));
*/

		 var treeDefinition = [
			 ["root1", objectData, "object", true],
			 ["leaf1", "root1", "string"],
			 ["leaf2", "root1", "array.0"],
			 ["leaf3", "leaf2", ""],
			 ["leaf4", "leaf3", "string"],
		 ];
		 
		 var wrappersTree = buildWrapperTree(treeDefinition);

		 wrappersTree.dataOperate(["leaf1", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}]);
		 
		 
		 
	});
