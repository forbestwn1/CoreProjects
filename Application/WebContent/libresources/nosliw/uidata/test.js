	nosliw.registerNodeEvent("runtime1", "active", function(eventName, nodeName){

		var node_buildVariableTree = nosliw.getNodeData("uidata.test.buildVariableTree");
		var node_buildContext = nosliw.getNodeData("uidata.test.buildContext");
		var node_createContextVariablesGroup = nosliw.getNodeData("uidata.context.createContextVariablesGroup");
		var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
		
		  var objectData1 = {
				 string : "string value",
				 int : 12345,
				 boolean : true,
				 object : {
					 string : "2 string value",
					 int : 12345,
					 boolean : true,
					 object : {
						 string : "6 string value",
						 int : 12345,
						 boolean : true,
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

		  
		  var objectData2 = {
			   child : {
				     string : "string value",
					 int : 12345,
					 boolean : true,
					 object : {
						 string : "2 string value new wrapper",
						 int : 12345,
						 boolean : true,
						 object : {
							 string : "6 string value",
							 int : 12345,
							 boolean : true,
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
			   }
			};

			 var contextDefinition = [
				 ["context1", ["root1", objectData1, "object", true]],
				 ["context2", ["root2", "hello world", "", true]],
				 ["context3", ["root3", "context1", "root1.object"]],
			];
		  
		 var contexts = node_buildContext(contextDefinition);
			
		 
		 var variablesDefinition = [
			 ["leaf1", "context1", "root1", "string"],
			 ["leaf2", "context1", "root1", "array.0"],
			 ["leaf5", "context1", "root1", "object"],
			 ["leaf7", "context1", "root1", "object.string"],
			 ["leaf8", "context1", "root1", "array"],
			 ["leaf9", "context1", "root1", "object"],
			 ["leaf10", "context1", "root1", "array.0.string"],
		];
		 
		 var variablesTree = node_buildVariableTree(variablesDefinition, contexts);
		 variablesTree.printVariable("leaf1");
		 variablesTree.printVariable("leaf2");
		 variablesTree.printVariable("leaf5");
		 variablesTree.printVariable("leaf7");
		 variablesTree.printVariable("leaf8");
		 variablesTree.printVariable("leaf9");

		 var contextVars = [];
		 contextVars.push(node_createContextVariable("root1", "array"));
		 node_createContextVariablesGroup(contexts.getContext("context1"), contextVars, function(event){
			  nosliw.logging.debug("Variable Group Event : ");
		 });
		 
		 
		 variablesTree.dataOperate(["leaf8", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"0.string", data:"new data"}]);
		 variablesTree.printVariable("leaf2");
		 variablesTree.printVariable("leaf8");
		 variablesTree.printVariable("leaf10");
		 
		 
//		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf6");
//		 variablesTree.printVariable("leaf7");
//		 
//		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data11111"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf6");
//		 variablesTree.printVariable("leaf7");
//
//
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"array", data:"new data", index: 7}]);
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"array", data:"new data"}]);
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"object", data:"new data", index: "new"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf9");
//
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT, {path:"object", index: "int"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf9");
//
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"object", data:"new data", index: "new"}]);
//
//		 
//		 variablesTree.dataOperate(["leaf21", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"", data:"new data11111"}]);
//		 variablesTree.printVariable("root2");
//		 variablesTree.printVariable("leaf21");
		 
//		 var newWrapper = variablesTree.getVariable("leaf31").getWrapper();
//		 variablesTree.getVariable("root1").setWrapper(newWrapper);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf1");
//
//		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf6");
//		 variablesTree.printVariable("leaf7");
		 
	});
