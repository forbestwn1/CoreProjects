	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){

		var node_buildVariableTree = nosliw.getNodeData("uidata.test.buildVariableTree");
		
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
		  
		 var treeDefinition = [
			 ["root1", objectData1, "object", true],
			 ["leaf1", "root1", "string"],
			 ["leaf2", "root1", "array.0"],
			 ["leaf3", "leaf2", ""],
			 ["leaf4", "leaf3", "string"],
			 ["leaf5", "root1", "object"],
			 ["leaf6", "leaf5", "string"],
			 ["leaf7", "root1", "object.string"],
			 ["leaf8", "root1", "array"],
			 ["leaf9", "root1", "object"],

			 ["root2", "hello world", "", true],
			 ["leaf21", "root2", ""],

			 ["root3", objectData2, "", true],
			 ["leaf31", "root3", "child.object"],
		];
		 
		 var variablesTree = node_buildVariableTree(treeDefinition);

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
		 
		 var newWrapper = variablesTree.getVariable("leaf31").getWrapper();
		 variablesTree.getVariable("root1").setWrapper(newWrapper);
		 variablesTree.printVariable("root1");
		 variablesTree.printVariable("leaf1");

		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}]);
		 variablesTree.printVariable("root1");
		 variablesTree.printVariable("leaf6");
		 variablesTree.printVariable("leaf7");
		 
	});
