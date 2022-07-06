package org.ASN70.vicu.jsonConfig

import kotlinx.serialization.json.Json

/** 序列化JSON配置*/
val CustomJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
}