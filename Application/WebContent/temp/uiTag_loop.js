
nosliw.runtime.getResourceService().importResource({"id":{"id":"loop",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"test",
"context":{"inherit":true,
"public":{}
},
"attributes":{},
"script":
function (context, parentResourceView, uiTagResource, attributes, env) {
    var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
    var node_DataOperationService = nosliw.getNodeData("uidata.dataoperation.DataOperationService");
    var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var node_createUIResourceViewFactory = nosliw.getNodeData("uiresource.createUIResourceViewFactory");
    var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
    var node_createContext = nosliw.getNodeData("uidata.context.createContext");
    var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
    var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var loc_env = env;
    var loc_dataVariable = env.createVariable("internal_data");
    var loc_eleContextEleName = attributes.element;
    var loc_eleNameContextEleName = attributes.elename;
    var loc_childResourceViews = [];
    var loc_childVaraibles = [];
    var loc_id = 0;
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
        var index = 0;
        loc_env.executeGetHandleEachElementRequest("internal_data", "", function (container, eleVarWrapper, indexVarWrapper) {
            loc_dataVariable = container;
            return node_createServiceRequestInfoSimple({}, function () {
                loc_addEle(eleVarWrapper, indexVarWrapper, index, requestInfo);
                index++;
            });
        });
    };
    var loc_addEle = function (eleVar, indexVar, index, requestInfo) {
        var eleContext = loc_env.createExtendedContext([loc_env.createContextElementInfo(loc_eleContextEleName, eleVar)], requestInfo);
        var index = loc_childResourceViews.length;
        var resourceView = loc_env.createUIResourceViewWithId(loc_env.getId() + "." + loc_generateId(), eleContext, requestInfo);
        if (index == 0) {
            resourceView.insertAfter(loc_env.getStartElement());
        } else {
            resourceView.insertAfter(loc_childResourceViews[index - 1].getEndElement());
        }
        loc_childResourceViews.splice(index, 0, resourceView);
        loc_childVaraibles.splice(index, 0, eleVar);
        eleVar.registerDataOperationEventListener(undefined, function (event, dataOperation, requestInfo) {
            window.alert("element:" + event);
            if (event == "EVENT_WRAPPER_DELETE") {
                resourceView.detachViews();
                resourceView.destroy();
            }
        }, this);
    };
    var loc_out = {prv_deleteEle: function (key, requestInfo) {
        var view = loc_childResourceViews[key];
        view.detachViews();
        view.destroy();
        loc_childResourceViews.splice(key, 1);
    }, ovr_postInit: function (requestInfo) {
        loc_updateView();
        var that = this;
        loc_dataVariable.registerDataOperationEventListener(undefined, function (event, eventData, requestInfo) {
            if (event == "EVENT_WRAPPER_NEWELEMENT") {
                loc_addEle(eventData.getElement(), eventData.indexVarWrapper, 0);
            }
            if (event == "WRAPPER_EVENT_DESTROY") {
                that.prv_deleteEle(loc_getElementContextVariable(eventData.index));
            }
        }, this);
    }, ovr_preInit: function (requestInfo) {
    }, ovr_initViews: function (startEle, endEle, requestInfo) {
        loc_startEle = startEle;
        loc_endEle = endEle;
    }, ovr_destroy: function () {
        loc_dataVariable.release();
        _.each(loc_childResourceViews, function (resourceView, id) {
            resourceView.destroy();
        });
        _.each(loc_childVaraibles, function (variable, path) {
            variable.release();
        });
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

