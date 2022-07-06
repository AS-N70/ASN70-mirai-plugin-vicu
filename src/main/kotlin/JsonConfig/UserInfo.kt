package org.ASN70.vicu

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: InfoData
)
@Serializable
data class InfoData(
    @SerialName("uname") val uname: String,
    @SerialName("mid") val mid: Long,
    @SerialName("total") val total: Int,
    @SerialName("like_count") val like_count: Int,
    @SerialName("reply_count") val reply_count: Int,
    @SerialName("first_reply_time") val first_reply_time: Long,
    @SerialName("last_reply_time") val last_reply_time: Long,
    @SerialName("items") val items: List<Items>
)
@Serializable
data class Items(
    @SerialName("mid") val mid: Long,
    @SerialName("uname") val uname: String,
    @SerialName("total") val total: Int
)
