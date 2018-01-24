{
        "mappings": {
            "dataset": {
                "properties": {
                    "FDPurl": {
                        "type": "string",
                        "analyzer": "whitespace"
                    },
                    "catalogTitle": {
                        "type": "string",
                        "analyzer": "english"
                    },
                    "catalogURL": {
                        "type": "string",
                        "analyzer": "whitespace"
                    },
                    "datasetURL": {
                        "type": "string",
                        "analyzer": "whitespace"
                    },
                    "description": {
                        "type": "string",
                        "analyzer": "english"
                    },
                    "distribution": {
                        "properties": {
                            "accessURL": {
                                "type": "string",
                                "analyzer": "whitespace"
                            },
                            "distributionURI": {
                                "type": "string"
                            },
                            "download": {
                                "type": "string",
                                "analyzer": "whitespace"
                            },
                            "downloadHumanReadableSize": {
                                "type": "string"
                            },
                            "downloadSize": {
                                "type": "integer"
                            },
                            "format": {
                                "type": "string"
                            },
                            "title": {
                                "type": "string",
                                "analyzer": "english"
                            },
                            "licenseAcronym": {
                                "type": "string",
                                "fields": {
                                    "raw": {
                                      "type": "string",
                                        "index": "not_analyzed"
                                    }
                                }
                            },
                            "licenseURI": {
                                "type": "string",
                                "analyzer": "whitespace",
                                "fields": {
                                    "raw": {
                                        "type":"string",
                                        "index": "not_analyzed"
                                    }
                                }
                            }
                        }
                    },
                    "keyword": {
                        "type": "string"
                    },
                    "landingPage": {
                        "type": "string",
                        "analyzer": "whitespace"
                    },
                    "readabilityMetrics": {
                        "properties": {
                            "ARI": {
                                "type": "double"
                            },
                            "ColemanLiau": {
                                "type": "double"
                            },
                            "Complex": {
                                "type": "long"
                            },
                            "FleschKincaidGradeLevel": {
                                "type": "double"
                            },
                            "FleschReadingEase": {
                                "type": "double"
                            },
                            "GunningFog": {
                                "type": "double"
                            },
                            "SMOG": {
                                "type": "double"
                            },
                            "SMOGIndex": {
                                "type": "double"
                            }
                        }
                    },
                    "repositoryCountry": {
                        "type": "string",
                        "analyzer": "whitespace"
                    },
                    "repositoryLocation": {
                        "type": "geo_point"
                    },
                    "repositoryTitle": {
                        "type": "string",
                        "analyzer": "english",
                        "fields": {
                            "raw": {
                              "type":"string",
                                "index": "not_analyzed"
                            }
                        }
                    },
                    "size": {
                        "type": "long"
                    },
                    "suggest": {
                        "type": "completion",
                        "analyzer": "english",
                        "payloads": true,
                        "preserve_separators": true,
                        "preserve_position_increments": true,
                        "max_input_length": 50
                    },
                    "taxonomyList": {
                        "type": "string"
                    },
                    "theme": {
                        "properties": {
                            "uri": {
                                "type": "string",
                                "analyzer": "whitespace"
                            },
                            "label": {
                                "type": "string",
                                "analyzer":"whitespace"
                            }
                        }
                    },
                    "title": {
                        "type": "string",
                        "analyzer": "english"
                    },
                    "lastUpdated": {
                      "type" : "date",
                      "format" : "epoch_second"
                    }
                }
            }
        }
}

