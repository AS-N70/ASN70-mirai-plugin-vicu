package org.ASN70.vicu
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comments(
    @SerialName("databack") val dataBack: List<DataBack>
)
@Serializable
data class DataBack(
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: List<CommentInfo>
)
@Serializable
data class CommentInfo(
    @SerialName("dynamic_url") val dynamic_url: String,
    @SerialName("up_uid") val up_uid: Long,
    @SerialName("up_uname") val up_name: String,
    @SerialName("replies") val replies: List<Replies>,

)
@Serializable
data class Replies(
    @SerialName("content") val content: String,
    @SerialName("ctime") val ctime: Long,
    @SerialName("like_count") val like_count: Int
)