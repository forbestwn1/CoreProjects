
nosliw.runtime.getResourceService().importResource({"id":{"id":"loop",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"test",
"context":{"inherit":true,
"public":{"<%=&(element)&%>":{"children":{},
"type":"relative",
"path":"<%=&(data)&%>.element"
}
}
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
    var loc_env = env;
    var loc_dataVariable = env.createVariable("internal_data");
    var loc_eleContextEleName = attributes.element;
    var loc_eleNameContextEleName = attributes.elename;
    var loc_childResourceViews = {};
    var loc_getElementContextVariable = function (key) {
        var out = node_createContextVariable(loc_dataContextEleName);
        out.path = node_namingConvensionUtility.cascadePath(out.path, key + "");
        return out;
    };
    var loc_updateView = function (requestInfo) {
        var request = loc_dataVariable.getWrapper().getHandleEachElementRequest(function (element) {
            return node_createServiceRequestInfoSimple({}, function () {
                loc_addEle(element, requestInfo);
            });
        });
        node_requestServiceProcessor.processRequest(request, false);
    };
    var loc_addEle = function (element, requestInfo) {
        var eleContext = loc_env.createExtendedContext([loc_env.createContextElementInfoFromContext(loc_eleContextEleName, "internal_data", element.key), loc_env.createContextElementInfo(loc_eleNameContextEleName, element.key)], requestInfo);
        var resourceView = loc_env.createUIResourceViewWithId(loc_env.getId() + "." + element.key, eleContext, requestInfo);
        resourceView.insertAfter(loc_env.getStartElement());
        loc_childResourceViews[element.key] = resourceView;
    };
    var loc_out = {prv_deleteEle: function (key, requestInfo) {
        alert("ffff");
        var view = loc_childResourceViews[key];
        view.detachViews();
        delete loc_childResourceViews[key];
    }, ovr_postInit: function (requestInfo) {
        loc_updateView();
        var that = this;
        loc_dataVariable.registerDataChangeEventListener(undefined, function (event, dataOperation, requestInfo) {
            if (event == "EVENT_WRAPPER_ADDELEMENT") {
                loc_addEle({key: dataOperation.index, value: dataOperation.value});
            }
            if (event == "WRAPPER_EVENT_DESTROY") {
                that.prv_deleteEle(loc_getElementContextVariable(dataOperation.index));
            }
        }, this);
    }, ovr_preInit: function (requestInfo) {
    }, ovr_initViews: function (startEle, endEle, requestInfo) {
        loc_startEle = startEle;
        loc_endEle = endEle;
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

