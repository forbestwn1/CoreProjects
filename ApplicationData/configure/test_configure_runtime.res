{
	"nosliw_decoration" :[
		{
			"nosliw_info" : {
				"name" : "firstDec",
				"type" : "decoration_script",
				"id": "decoration1",
				"description": "this is for testing decoration"
			},
			"nosliw_core" : {
				"nosliw_debug" : "true",
				"configureDec1" : "configureDec1Value",
				"configureDec2" : ["configureDec21Value", "configureDec22Value", "configureDec23Value"]
			}
		}
	],
	"nosliw_core" : {
		"a-test_complex_script" : {
			"nosliw_core" : {
				"a_configure" : {
					"aa" : "aa value",
					"ab" : "ab value"
				}
			}
		},
		"b-test_complex_1" : {
			"nosliw_core" : {
				"ba-test_complex_script" : {
					"nosliw_core" : {
						"baa" : "baa value",
						"bab" : "bab value"
					}
				}
			}
		},
		"c-test_complex_1" : {
			"nosliw_core" : {
				"ca-test_complex_script" : {
					"nosliw_core" : {
						"caa" : "caa value",
						"cab" : "cab value"
					}
				}
			}
		},
		"nosliw_debug_package" : "true",
		"nosliw_debug" : "true",
		"configure2" : ["configure21Value", "configure22Value", "configure23Value"],
		"test_none_dataexpressiongroup" : {
			"nosliw_debug_package" : "true",
			"nosliw_debug" : "true",
			"nosliw_decoration" : {
				
			
			}
		}
	}
}
