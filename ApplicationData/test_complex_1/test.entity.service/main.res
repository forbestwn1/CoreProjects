{
	"info": {
		"name": "test data association",
		"description": ""
	},
	"entity": [
		{
			"embeded": {
				"info": {
					"name" : "adapter-none-serviceprovider",
					"status": "disabled1"
				},
				"entity" : {
					"serviceKey" : {
						"id" : "TestTemplateService3"
					}
				}
			},
			"adapter":[
				{
					"name" : "parent.child.data",
					"status": "disabled",
					"entityType" : "dataAssociation",
					"entity" : {
						"mapping" : {
							"child_tree_public" : {
								"definition" : {
									"child" : {
										"a" : {
											"child" : {
												"aa" : {
													"mapping" : {
														"elementPath": "parent_tree_public.a.aa"
													}
												}
											}
										}
									}
								}
							}
						}
					}
				},
			]
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
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
