var gateway = {
		
		requestLoadLibraryResources : function(resourceIds, callBackFunction){
			
			$.ajax({
				url : "loadLibraryResources",
				type : "POST",
				dataType: "json",
				data : resourceIds,
				async : true,
				success : function(result, status){
					var fileNumber = 0;
					for (var resourceId in result) {
					    if (result.hasOwnProperty(resourceId)) {
					    	fileNumber = fileNumber + result[resourceId].length;
					    }
					}
					
					var count = 0;
					for (var resourceId in result) {
					    if (result.hasOwnProperty(resourceId)) {
					    	var files = result[resourceId];
					    	for(var i in files){
								var url = files[i];
								var script = document.createElement('script');
								script.setAttribute('src', url);
								script.setAttribute('type', 'text/javascript');

//								script.onload = callBackFunction;
								script.onreadystatechange = function(){
									count++;
									if(count==fileNumber){
										callBackFunction.invoke();
									}
								};
								document.getElementsByTagName("head")[0].appendChild(script);
					    	}
					    }
					}
				},
				error: function(obj, textStatus, errorThrown){
				},
			});
		},

		/**
		 * Callback method used to request to discover resources into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered resource info)
		 */
		requestDiscoverResources : function(objResourceIds, callBackFunction){
			
		},
		
		/**
		 * Callback method used to request to discover resources and load into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered and loaded resource info)
		 */
		requestDiscoverAndLoadResources : function(objResourceIds, callBackFunction){
			
		},
		
		/**
		 * Callback method used to request to load resources into runtime env
		 * @param objResourcesInfo: a list of resource info 
		 * @param callBackFunction (nothing)
		 */
		requestLoadResources : function(resourcesInfo, callBackFunction){
			var scriptType = requestInfo.getParmData('type');
			var scriptInfo = encodeURI(requestInfo.getParmData('info'));

			var url = "http://localhost:8080/Application/loadResource?resources="+resources;

			  var script = document.createElement('script');
			  script.setAttribute('src', url);
			  script.setAttribute('type', 'text/javascript');

			  script.onload = callBackFunction;
			  script.onreadystatechange = callBackFunction;
			  document.getElementsByTagName("head")[0].appendChild(script);
		},
		
};
