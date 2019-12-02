nosliw.utility = function(){
	
	var loc_requestLoadFileResources = function(api, command, parms, processResult, callBackFunction){
		$.ajax({
			url : loc_out.getServerBase()+api,
			type : "POST",
			dataType: "json",
			data : {
				command : command,
				parms : JSON.stringify(parms
						
				)
			},
			async : true,
			success : function(serviceData, status){
				var result = processResult(serviceData);
				
				var fileNumber = result.length;
				var count = 0;
				
				var loadScriptInOrder1 = function(){
					var url = result[count];
					
					jQuery.getScript(loc_out.getServerBase()+url, function(data, textStatus, jqxhr){
						callBack();
					});
				};
				
				var loadScriptInOrder = function(){
					var url = result[count];
					
					var scriptEle = document.createElement('script');
					scriptEle.setAttribute('src', loc_out.getServerBase()+url);
					scriptEle.setAttribute('defer', "defer");
					scriptEle.setAttribute('type', 'text/javascript');

					scriptEle.onload = callBack;
					document.getElementsByTagName("head")[0].appendChild(scriptEle);
				};
				
				var callBack = function(){
					count++;
					if(count>=fileNumber){
						callBackFunction.call();
					}
					else{
						loadScriptInOrder();
					}
				};
				
				loadScriptInOrder();
			},
			error: function(obj, textStatus, errorThrown){
			},
		});
	};
	
	var loc_out = {
		getServerBase : function(){
			return nosliw.getConfigureValue("serverBase", "");
		},	
			
		buildNosliwUrl : function(url){
			return loc_out.getServerBase()+url;
		},
		
		//load librarys by name
		requestLoadLibraryResources : function(libNames, version, callBackFunction){
			var resourceIds = [];
			for(var i in libNames){
				resourceIds.push({
					"id" : libNames[i],
					"type" : "jslibrary"
				});
			}
			loc_requestLoadFileResources(
				"loadlib", 
				"requestLoadLibraryResources", 
				{
					"resourceIds" : resourceIds,
					"version" : version
				}, 
				function(serviceData){return serviceData.data.data;}, 
				callBackFunction);
		},
		
		requestLoadFileResources : function(fileFolders, callBackFunction){
			loc_requestLoadFileResources(
				"loadfile", 
				"requestLoadFileResources", 
				{
					"fileFolders" : fileFolders
				}, 
				function(serviceData){return serviceData.data;}, 
				callBackFunction);
		}
	};
	return loc_out;
}();
