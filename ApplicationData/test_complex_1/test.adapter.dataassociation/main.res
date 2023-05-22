{
	"info": {
		"name": "test data association",
		"description": ""
	},
	"entity": [
		{
			"embeded": {
				"info": {
					"name" : "adapter-none-test_complex_script",
					"status": "disabled1"
				},
				"resourceId": "test_complex_script|#child",
				"parent": {
					"valuestructure":{
						"inherit":{
							"mode" : "none"
						}
					}
				}
			},
			"adapter":[
				{
					"name" : "parent.child.data.value",
					"status": "disabled1",
					"entityType" : "dataAssociation",
					"entity" : {
						"mapping" : {
							"child_tree_public" : {
								"definition" : {
									"child" : {
										"a" : {
											"child" : {
												"ac" : {
													"mapping" : {
														"elementPath": "parent_tree_public.a.ac"
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
				{
					"name" : "parent.child.data.data",
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
				{
					"name" : "parent.child.data.matcher",
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
														"elementPath": "parent_tree_public.a.ab"
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
				{
					"name" : "parent.child.data.constant",
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
													"value" : {
														"dataTypeId": "test.url;1.0.0",
														"value": "constant value"
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
				{
					"name" : "parent.child.node",
					"status": "disabled",
					"entityType" : "dataAssociation",
					"entity" : {
						"mapping" : {
							"child_tree_public" : {
								"definition" : {
									"child" : {
										"a" : {
											"mapping" : {
												"elementPath": "parent_tree_public.a"
											}
										}
									}
								}
							}
						}
					}
				},
				{
					"name" : "child.parent.data",
					"status": "disabled",
					"entityType" : "dataAssociation",
					"entity" : {
						"direction": "upStream",
						"mapping" : {
							"parent_tree_public" : {
								"definition" : {
									"child" : {
										"a" : {
											"child" : {
												"aa" : {
													"mapping" : {
														"elementPath": "child_tree_public.a.aa"
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
				{
					"name" : "child.parent.data.matcher",
					"status": "disabled",
					"entityType" : "dataAssociation",
					"entity" : {
						"direction": "upStream",
						"mapping" : {
							"parent_tree_public" : {
								"definition" : {
									"child" : {
										"a" : {
											"child" : {
												"aa" : {
													"mapping" : {
														"elementPath": "child_tree_public.a.ab"
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
				{
					"name" : "child.parent.node",
					"status": "disabled",
					"entityType" : "dataAssociation",
					"entity" : {
						"direction": "upStream",
						"mapping" : {
							"parent_tree_public" : {
								"definition" : {
									"child" : {
										"a" : {
											"mapping" : {
												"elementPath": "child_tree_public.a"
											}
										}
									}
								}
							}
						}
					}
				}
			]
		},
		{
			"info": {
				"name" : "debug_parent_valuestructure-none-test_complex_script",
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
			},			
			"entity":{
				"scriptName": "complexscript_test_value",
				"parm" : {
					"variable" : []
				}
			}
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
