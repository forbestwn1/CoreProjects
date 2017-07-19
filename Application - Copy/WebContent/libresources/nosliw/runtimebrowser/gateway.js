var gateway = {
		
		requestLoadLibraryResources : function(resourceIds, callBackFunction){
			var data = {
				command : "loadLibraryResources",
				data : resourceIds
			};
			
			$.ajax({
				url : "gateway",
				type : "POST",
				dataType: "json",
				data : data,
				async : true,
				success : function(serviceData, status){
					var result = serviceData.data;
					var fileNumber = result.length();
					var count = 0;
			    	for(var i in result){
						var url = result[i];
						var script = document.createElement('script');
						script.setAttribute('src', url);
						script.setAttribute('type', 'text/javascript');

//						script.onload = callBackFunction;
						script.onreadystatechange = function(){
							count++;
							if(count==fileNumber){
								callBackFunction.invoke();
							}
						};
						document.getElementsByTagName("head")[0].appendChild(script);
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
