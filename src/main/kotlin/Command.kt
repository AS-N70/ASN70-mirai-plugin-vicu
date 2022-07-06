package org.ASN70.vicu

import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.nextEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import org.ASN70.vicu.Utils.ToJSON
import org.ASN70.vicu.Utils.ValueMaps.UP_MAP
import org.ASN70.vicu.Utils.ValueMaps.UsedFiles
import org.ASN70.vicu.Utils.ValueMaps.commentMap
import org.ASN70.vicu.Utils.vicuAPI
import org.ASN70.vicu.VicuMain.dataFolder
import org.ASN70.vicu.VicuMain.logger
import org.ASN70.vicu.jsonConfig.CustomJson
import org.ASN70.vicu.jsonConfig.UP_LIST
import org.ASN70.vicu.text2pic.text2pic
import java.io.File

class Command {
    object icuComment: RawCommand(
        owner = VicuMain,
        primaryName = "查评论",
        usage = """
            #查评论 [UID] [UP主]（非必选）
            #查评论 最新
            #查评论 UP主列表
        """.trimIndent(),
        prefixOptional = true
    ) {
        /**根据参数构造URL*/
        private fun urlConstructor(USER_UID: String, UP: String): String {
            return if (UP_MAP.containsKey(UP)) "${vicuAPI.reply}?uid=$USER_UID&up_uid=${UP_MAP[UP]}"
            else if (UP_MAP.containsValue(UP)) "${vicuAPI.reply}?uid=$USER_UID&up_uid=$UP"
            else "${vicuAPI.reply}?uid=$USER_UID"
        }
        override suspend fun CommandSender.onCommand(args: MessageChain) {
            val comment: Comments
            val userInfo: UserInfo
            when(args.size) {
                0 -> {
                    try {
                        sendMessage("请输入要查询用户的UID]\n（输入[取消]来取消")
                        val msg = withTimeout(30000L) {
                            subject!!.globalEventChannel().nextEvent<MessageEvent>(EventPriority.MONITOR) { it.sender == user }
                        }
                        if(msg.message.content == "取消") {
                            sendMessage("已取消")
                        } else {
                            val uid = msg.message.content
                            try {
                                ToJSON.comments((urlConstructor(uid,"")))
                                ToJSON.userInfo(uid)
                                comment = CustomJson.decodeFromString(UsedFiles["json"]!!.readText())
                                userInfo = CustomJson.decodeFromString(UsedFiles["userInfo"]!!.readText())
                                text2pic.reply(comment,userInfo)
                                sendMessage(UsedFiles["comment"]!!.uploadAsImage(subject!!))
                                /*val picTree = File(dataFolder,"comment")!!.walk()
                                picTree.filter {
                                    it.isFile
                                    it.extension == "png"
                                }.forEach {
                                    sendMessage(it.uploadAsImage(subject!!))
                                }*/

                            } catch (e: Exception) {
                                logger.warning(e)
                                sendMessage("出错惹(> <)\n${e.localizedMessage}")
                            }
                        }
                    }
                    catch (e: TimeoutCancellationException) {
                        sendMessage("超时未输入UID，已取消！")
                    }
                }
                1 -> {
                    val uid = args[0].content
                    try {
                        if (listOf("最新","最新评论").contains(uid)) {
                            text2pic.latest50()
                            sendMessage(UsedFiles["latest"]!!.uploadAsImage(subject!!))
                        }
                        else if(listOf("UP主列表","up主列表","UP主","up主").contains(uid)) {
                            var upList = "$uid:\n"
                            val ups: UP_LIST = CustomJson.decodeFromString(JSONUtil.toJsonPrettyStr(HttpUtil.get(vicuAPI.ups)))
                            ups.data.forEach { up ->
                                upList += "${up.uname}(${up.uname})\n"
                                UP_MAP[up.uname] = up.mid.toString()
                            }
                            sendMessage(upList)
                        }
                        else {
                            ToJSON.comments((urlConstructor(uid,"")))
                            ToJSON.userInfo(uid)
                            comment = CustomJson.decodeFromString(UsedFiles["json"]!!.readText())
                            userInfo = CustomJson.decodeFromString(UsedFiles["userInfo"]!!.readText())
                            text2pic.reply(comment,userInfo)
                            sendMessage(UsedFiles["comment"]!!.uploadAsImage(subject!!))
                            /*val picTree = File(dataFolder,"comment")!!.walk()
                            picTree.filter {
                                it.isFile
                                it.extension == "png"
                            }.forEach {
                                sendMessage(it.uploadAsImage(subject!!))
                            }*/
                        }
                    } catch (e: Exception) {
                        sendMessage("出错惹(> <)\n${e.localizedMessage}")
                    }
                }
                2 -> {
                    val uid = args[0].content
                    val up = args[1].content
                    try {
                        if (UP_MAP.containsValue(up) || UP_MAP.containsValue(up)) {
                            ToJSON.comments((urlConstructor(uid,up)))
                            ToJSON.userInfo(uid)
                            comment = CustomJson.decodeFromString(UsedFiles["json"]!!.readText())
                            userInfo = CustomJson.decodeFromString(UsedFiles["userInfo"]!!.readText())
                            text2pic.reply(comment,userInfo)
                            sendMessage(UsedFiles["comment"]!!.uploadAsImage(subject!!))
                            /*val picTree = File(dataFolder,"comment").walk()
                            picTree.filter {
                                it.isFile
                                it.extension == "png"
                            }.forEach {
                                sendMessage(it.uploadAsImage(subject!!))
                                it.delete()
                            }*/
                        } else {
                            ToJSON.comments((urlConstructor(uid,up)))
                            ToJSON.userInfo(uid)
                            comment = CustomJson.decodeFromString(UsedFiles["json"]!!.readText())
                            userInfo = CustomJson.decodeFromString(UsedFiles["userInfo"]!!.readText())
                            text2pic.reply(comment,userInfo)
                            sendMessage("未收录该UP！默认返回所有评论")
                            text2pic.reply(comment,userInfo)
                            sendMessage(UsedFiles["comment"]!!.uploadAsImage(subject!!))
                            /*val picTree = File(dataFolder,"comment").walk()
                            picTree.filter {
                                it.isFile
                                it.extension == "png"
                            }.forEach {
                                sendMessage(it.uploadAsImage(subject!!))
                            }*/
                        }
                    } catch (e:Exception){
                        logger.warning(e)
                        sendMessage("出错惹(> <)\n${e.localizedMessage}")
                    }

                }
                else -> sendMessage("输入格式有误！")
            }
        }
    }
}