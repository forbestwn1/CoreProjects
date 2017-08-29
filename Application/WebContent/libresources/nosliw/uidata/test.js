	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){

		  var node_wrapperFactory = nosliw.getNodeData("uidata.wrapper.wrapperFactory");
		
		  var objectData = {
				 string : "string value",
				 int : 12345,
				 boolean : true,
				 object : {
					 string : "string value",
					 int : 12345,
					 boolean : true,
					 object : {
						 
					 },
					 array : [
						 {
							 string : "string value",
							 int : 12345,
							 boolean : true,
						 }, 
						 "string value",
						 12345,
						 true,
						 ["1", "2", "3", "4"]
					],
				 },
				 array : ["1", "2", "3", "4"],
				 
		  };
		  
		  var rootWrapper = node_wrapperFactory.createWrapper(objectData, "");
		  var leafWrapper = node_wrapperFactory.createWrapper(rootWrapper, "int");
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper.getValue()));
		  var leafWrapper = node_wrapperFactory.createWrapper(rootWrapper, "object.int");
		  nosliw.logging.debug("leaf wrapper value", JSON.stringify(leafWrapper.getValue()));
		  
	});
