package org.ASN70.vicu

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestReplies(
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: LatestData
)

@Serializable
data class LatestData(
    @SerialName("online") val online: Int,
    @SerialName("total") val total: Long,
    @SerialName("newest_replies") val newest: List<Newest>
)

@Serializable
data class Newest(
    @SerialName("dynamic_url") val dynamic_url: String,
    @SerialName("up_uid") val up_uid: Long,
    @SerialName("up_uname") val up_uname: String,
    @SerialName("mid") val mid: String,
    @SerialName("uname") val uname: String,
    @SerialName("content") val content: String,
    @SerialName("ctime") val ctime: Long
)