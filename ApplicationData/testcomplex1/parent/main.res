{
	"extra": {
		"name": "parent complex entity",
		"description": "parent complex entity"
	},
	"entity": {
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
				"attribute2_none_testcomplex1": {
				
				}
			}
		},
		"localresource1_none_testcomplex1": {
			"resourceId": "testcomplex1|#localchild",
			"extra": {
				"status": "disabled1"
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
						"parent1": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						},
						"parent2" : {
							"definition": {
								"criteria" : "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 5
							}
						},
						"parent3" : {
							"definition": {
								"criteria": "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 7
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
