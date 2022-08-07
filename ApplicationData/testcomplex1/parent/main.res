{
	"extra": {
		"name": "parent complex entity",
		"description": "parent complex entity"
	},
	"entity": {
		"ignore_variable" : "parent_public",
		"simplized1_none_testsimple1": {
			"attribute2_none_testsimple1": {
			
			}
		},
		"entity1_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
				"attribute2_none_testsimple1": {
				
				}
			}
		},
		"entity2_none_testcomplex1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
			}
		},
		"entity3_none_testcomplex1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
				"attribute2_none_testcomplex1": {
				
				}
			}
		},
		"valueStructureInheritModeNone_none_testcomplex1": {
			"resourceId": "testcomplex1|#valuestructure_inherit_mode",
			"extra": {
				"status": "disabled"
			},
			"parent": {
				"valuestructure": {
					"inherit": {
						"mode" : "none"
					}
				}
			}
		},
		"valueStructureInheritModeDefinition_none_testcomplex1": {
			"resourceId": "testcomplex1|#valuestructure_inherit_mode",
			"extra": {
				"status": "disabled"
			},
			"parent": {
				"valuestructure": {
					"inherit": {
						"mode" : "definition"
					}
				}
			}
		},
		"valueStructureInheritModeRuntime_none_testcomplex1": {
			"resourceId": "testcomplex1|#valuestructure_inherit_mode",
			"extra": {
				"status": "disabled"
			},
			"parent": {
				"valuestructure": {
					"inherit": {
						"mode" : "runtime"
					}
				}
			}
		},
		"valueStructureInheritModeReference_none_testcomplex1": {
			"resourceId": "testcomplex1|#valuestructure_inherit_mode",
			"extra": {
				"status": "disabled"
			},
			"parent": {
				"valuestructure": {
					"inherit": {
						"mode" : "reference"
					}
				}
			}
		},
		"valueStructureInheritInfoExclude_none_testcomplex1": {
			"resourceId": "testcomplex1|#valuestructure_inherit_mode",
			"extra": {
				"status": "disabled"
			},
			"parent": {
				"valuestructure": {
					"inherit": {
						"mode" : "runtime",
						"excludedInfo" : ["excluded1", "excluded2"]
					}
				}
			}
		},
		"valueStructureRelative_none_testcomplex1": {
			"resourceId": "testcomplex1|#valuestructure_relative",
			"extra": {
				"status": "disabled"
			},
			"parent": {
				"valuestructure": {
					"inherit": {
						"mode" : "none"
					}
				}
			}
		},
		"localresource1_none_testcomplex1": {
			"resourceId": "testcomplex1|#localchild",
			"extra": {
				"status": "disabled"
			}
		},
		"globalresource1_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"resourceId": "testsimple1|*test1"
		},
		"globalresource2_none_testcomplex1": {
			"extra": {
				"status": "disabled1"
			},
			"resourceId": "testcomplex1|*child",
			"parent": {
				"attachment": {
					"mode": "none"
				}
			}
		},
		"reference1_none_testsimple1": { 
			"extra": {
				"status": "disabled"
			},
			"reference": "samename"
		},
		"reference2_none_testcomplex1": { 
			"extra": {
				"status": "disabled"
			},
			"reference": "data"
		},
		"normal1_set_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity": {
						"attribute2_none_testsimple1": {
						
						}
					}					
				}
			]
		},
		"normal1_list_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity": {
						"attribute2_none_testsimple1": {
						
						}
					}					
				}
			]
		},
		"attachment": {
			"extra": {
				"status": "disabled1",
				"name": "parent attachment" 
			},
			"entity": {
				"valuestructure" : [
					{
						"name" : "valuestructure1",
						"entity": {
							"fromAttachment1": {
								"definition": {
									"criteria": "test.string;1.0.0"
								},
								"defaultValue": {
									"dataTypeId": "test.string;1.0.0",
									"value": "Default value for forlistservice_1_ex_parm1"
								}
							},
							"fromAttachment2": {
								"definition": {
									"criteria": "test.float;1.0.0"
								}
							}
						}
					}
				],
				"testsimple1": [
					{
						"name": "samename",
						"entity": {
							"parent_none_testsimple1": {
								"entity":{
								}
							}
						}
					},
					{
						"name": "inparentonly",
						"entity": {
							"parent_none_testsimple1": {
								"entity":{
								}
							}
						}
					}
				],
				"testcomplex1": [
				
				]
			}
		},
		"valueStructure": {
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"parent_public": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "protected",
					"valueStructure" : {
						"parent_protected": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "internal",
					"valueStructure" : {
						"parent_internal": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "private",
					"valueStructure" : {
						"parent_private": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType": "public",
					"groupName" : "fromResource",
					"valueStructure" : {
						"resourceId" : "valuestructure|*test1"
					}
				},
				{
					"groupType": "public",
					"groupName" : "fromAttachment",
					"valueStructure" : {
						"reference": "valuestructure1;valuestructure"
					}
				}
			]
		}
	}
}
