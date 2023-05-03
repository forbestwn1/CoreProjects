{
	"info": {
		"name": "test data expression",
		"description": ""
	},
	"entity": [
		{
			"info": {
				"name" : "test_none_dataexpressiongroup",
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
			},
			"resourceId": "dataexpressiongroup|#test"
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"parent_tree_public": {
							"definition": {
								"child" : {
									"a" : {
										"child" : {
											"aa" : {"criteria":"test.string;1.0.0"},
											"ab" : {"criteria":"test.url;1.0.0"},
											"ac" : {}
										}
									}
								}
							},
							"defaultValue": {
								"a": {
									"aa" : {
										"dataTypeId": "test.string;1.0.0",
										"value": "default value of parent_tree_public.a.aa"
									},
									"ab" : {
										"dataTypeId": "test.url;1.0.0",
										"value": "default value of parent_tree_public.a.ab"
									},
									"ac" : "default value of parent_tree_public.a.ac"
								}
							}
						}
					}
				}
			]
		}
	]
}
