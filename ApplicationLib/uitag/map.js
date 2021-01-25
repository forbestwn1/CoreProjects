{
	name : "map",
	type : "data",
	base : "arrayData",
	description : "",
	attributes : [
		{
			name : "data",
		},
		{
			name : "element",
			defaultValue : "element", 
		},
		{
			name : "index",
			defaultValue : "index", 
		}	
	],
	context: {
		group : {
			private : {
				element : {
					"internal_data": {
						definition : {
							path : "<%=&(nosliwattribute_data)&%>",
							definition : {
								criteria : "test.array;1.0.0"
							}
						},
					}
				},
			},
			protected : {
				element : {
					"<%=&(nosliwattribute_element)&%>" : {
						definition : {
							path : "<%=&(nosliwattribute_data)&%>.element",
						},
						info:{
							instantiate : "manual"
						}
					},
					"<%=&(nosliwattribute_index)&%>" : {
						definition : {
							criteria : "test.integer;1.0.0",
						},
						info:{
							instantiate : "manual"
						}
					}		
				},
			},
		},
		info : {
			inherit : "true",
		}
	},
	event : [
		
	],
	script : function(base){

		var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
		var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
		var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
		var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
		var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");

		var loc_base = base;
		var loc_contentView;

		var loc_map;
		var loc_infowindow;
		var loc_elements = [];

		var loc_getBuildContentViewRequest = function(handlers, request){

			var extendeContextInfo = [];
			extendeContextInfo.push(loc_base.getEnv().createContextElementInfo(loc_base.getElementVariableName(), undefined));
			extendeContextInfo.push(loc_base.getEnv().createContextElementInfo(loc_base.getIndexVariableName(), undefined));

			var eleContext = loc_base.getEnv().createExtendedContext(extendeContextInfo, request);

			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_base.getEnv().getCreateUIViewWithIdRequest("", eleContext, {
				success : function(requestInfo, resourceView){
					loc_contentView = resourceView;
					loc_contentView.appendTo(loc_infowindow.getContent());
					return resourceView;
				}
			}));
			return out;
		};
		

		/**
		*  eleVar : variable for element
		*  indexVar : index variable for index of element
		*  path : element's path from parent
		**/
		var loc_getCreateElementViewRequest = function(id, index, variables, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_base.getEnv().getDataOperationRequestGet(variables[loc_base.getElementVariableName()], undefined, {
				success : function(request, data){
					var kkkk = 5555;
				}
			}));
			out.addRequest(loc_base.getEnv().getDataOperationRequestGet(variables[loc_base.getElementVariableName()], "geo", {
				success : function(request, data){
					var marker = new google.maps.Marker({
						position: {lat: data.value.value.latitude, lng: data.value.value.longitude},
						map: loc_map
					});

					var ele = {};
					ele.marker = marker;
					ele.vars = variables;
					ele.elementVar = variables[loc_base.getElementVariableName()];
					ele.indexVar = variables[loc_base.getIndexVariableName()];
					
					loc_elements.splice(index, 0, ele);

					marker.addListener('click', function(request) {
						var showConentRequest = node_createServiceRequestInfoSequence(undefined, {
							success : function(request){
								loc_infowindow.open(loc_map, marker);
							}
						}, request);
						
						var getDataRequest = node_createServiceRequestInfoSet(undefined, {
							success : function(request, resultSet){
								return loc_contentView.getUpdateContextRequest(resultSet.getResults());
							}
						});
						
						getDataRequest.addRequest(loc_base.getElementVariableName(), loc_base.getEnv().getDataOperationRequestGet(ele.vars[loc_base.getElementVariableName()]));
						getDataRequest.addRequest(loc_base.getIndexVariableName(), loc_base.getEnv().getDataOperationRequestGet(ele.vars[loc_base.getIndexVariableName()]));
						
						showConentRequest.addRequest(getDataRequest);
						node_requestServiceProcessor.processRequest(showConentRequest);
					});
					
				}
			}));
			return out;
		};

		var loc_initMap = function() {
			var uluru = {lat: 43.751319, lng: -79.407853};
			loc_map = new google.maps.Map(loc_view.get(0), {
			  zoom: 11,
			  center: uluru
			});
			
			loc_infowindow = new google.maps.InfoWindow({
				content: $('<div style="height:100px;width:200px;">').get(0)
			});
	    };
	

		var loc_out = 
		{
			initViews : function(handlers, requestInfo){
				var mode = loc_base.getEnv().getMode();
				if(mode==node_CONSTANT.TAG_RUNTIME_MODE_PAGE){
					loc_view = $('<div id="map" style="height:600px;width:100%;"></div>');	
				}
				else if(mode==node_CONSTANT.TAG_RUNTIME_MODE_DEMO){
					loc_view = $('<div id="map" style="height:100px;width:200px;"></div>');	
				}
				loc_initMap();
				var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
				out.addRequest(loc_getBuildContentViewRequest({
					success : function(request, view){
						return loc_view;;
					}
				}));
				
				return out;
			},
	
			getCreateElementViewRequest : function(id, index, variables, handlers, requestInfo){
				return loc_getCreateElementViewRequest(id, index, variables, handlers, requestInfo);
			},

			postInit : function(requestInfo){
//				loc_updateView();
			},

			deleteElement : function(index, requestInfo){
				var ele = loc_elements[index];
				ele.marker.setMap(null);
				loc_elements.splice(index, 1);
			}, 
			
			getDataForDemo : function(){
				return {
					dataTypeId:"test.array;1.0.0",
					value:[
						{
							dataTypeId:"test.map;1.0.0",
							value:{
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value : {
										latitude : 43.7541051,
										longitude : -79.3089712,
									}
								}
							}, 
						},
						{
							dataTypeId:"test.map;1.0.0",
							value:{
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value : {
										latitude : 43.7541051,
										longitude : -79.3089712,
									}
								}
							}, 
						},
					], 
				};
			},


			destroy : function(request){
			},
			
		};
		return loc_out;
	}
}
