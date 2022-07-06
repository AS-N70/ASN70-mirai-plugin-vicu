package org.ASN70.vicu.jsonConfig

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UP_LIST(
    @SerialName("data") val data: List<UP_INFO>
)

@Serializable
data class UP_INFO(
    @SerialName("uname") val uname: String,
    @SerialName("mid") val mid: Long,
    @SerialName("group") val group: String,
)