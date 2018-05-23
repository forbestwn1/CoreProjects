
nosliw.runtime.getResourceService().importResource({"id":{"id":"map",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"map",
"context":{"inherit":true,
"public":{}
},
"attributes":{},
"script":
function (env) {
    var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var loc_env = env;
    var loc_containerVariable;
    var loc_childResourceViews = [];
    var loc_childVaraibles = [];
    var loc_markers = [];
    var loc_id = 0;
    var loc_map;
    var loc_infowindow;
    var loc_currentChildView;
    var loc_generateId = function () {
        loc_id++;
        return loc_id + "";
    };
    var loc_getElementContextVariable = function (key) {
        var out = node_createContextVariable(loc_dataContextEleName);
        out.path = node_namingConvensionUtility.cascadePath(out.path, key + "");
        return out;
    };
    var loc_updateView = function (requestInfo) {
        loc_containerVariable = undefined;
        var index = 0;
        loc_env.executeGetHandleEachElementRequest("internal_data", "", function (containerVar, eleVar, indexVar) {
            if (loc_containerVariable == undefined) {
                loc_containerVariable = containerVar;
                loc_containerVariable.registerDataOperationEventListener(undefined, function (event, eventData, requestInfo) {
                    if (event == "EVENT_WRAPPER_SET") {
                        loc_out.destroy();
                        loc_updateView();
                    }
                    if (event == "EVENT_WRAPPER_NEWELEMENT") {
                        loc_addEle(eventData.getElement(), eventData.getIndex(), 0);
                    }
                    if (event == "WRAPPER_EVENT_DESTROY") {
                        loc_out.prv_deleteEle(loc_getElementContextVariable(eventData.index));
                    }
                }, this);
            }
            loc_addEle(eleVar, indexVar, index);
            index++;
        });
    };
    var loc_addEle = function (eleVar, indexVar, index, requestInfo) {
        var eleContext = loc_env.createExtendedContext([loc_env.createContextElementInfo(loc_env.getAttributeValue("element"), eleVar)], requestInfo);
        var resourceView = loc_env.createUIResourceViewWithId(loc_env.getId() + "." + loc_generateId(), eleContext, requestInfo);
        loc_env.executeDataOperationRequestGet(eleVar, "geo", {success: function (request, data) {
            var marker = new google.maps.Marker({position: {lat: data.value.value.latitude, lng: data.value.value.longitude}, map: loc_map});
            loc_markers.splice(index, 0, marker);
            marker.addListener("click", function () {
                if (loc_currentChildView != undefined) {
                    loc_currentChildView.detachViews();
                }
                loc_currentChildView = loc_childResourceViews[index];
                loc_currentChildView.appendTo(loc_infowindow.getContent());
                loc_infowindow.open(loc_map, marker);
            });
        }});
        loc_childResourceViews.splice(index, 0, resourceView);
        loc_childVaraibles.splice(index, 0, eleVar);
        eleVar.registerDataOperationEventListener(undefined, function (event, dataOperation, requestInfo) {
            if (event == "EVENT_WRAPPER_DELETE") {
                loc_out.prv_deleteEle(index);
            }
        }, this);
    };
    function initMap() {
        var uluru = {lat: 43.751319, lng: -79.407853};
        loc_map = new google.maps.Map(loc_view.get(0), {zoom: 11, center: uluru});
        loc_infowindow = new google.maps.InfoWindow({content: $("<div style=\"height:100px;width:200px;\">").get(0)});
    }
    var loc_out = {prv_deleteEle: function (index, requestInfo) {
        var view = loc_childResourceViews[index];
        view.detachViews();
        view.destroy();
        var marker = loc_markers[index];
        marker.setMap(null);
        loc_childResourceViews.splice(index, 1);
        loc_childVaraibles.splice(index, 1);
        loc_markders.splice(index, 1);
    }, postInit: function (requestInfo) {
        initMap();
        loc_updateView();
    }, initViews: function (requestInfo) {
        loc_view = $("<div id=\"map\" style=\"height:600px;width:100%;\"></div>");
        return loc_view;
    }, destroy: function () {
        loc_containerVariable.release();
        _.each(loc_childResourceViews, function (resourceView, id) {
            resourceView.destroy();
        });
        _.each(loc_childVaraibles, function (variable, path) {
            variable.release();
        });
        _.each(loc_markers, function (marker, index) {
            marker.setMap(null);
        });
        loc_currentChildView = undefined;
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

