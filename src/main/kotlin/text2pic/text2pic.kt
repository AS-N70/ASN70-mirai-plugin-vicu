package org.ASN70.vicu.text2pic


import cn.hutool.core.date.DateUtil
import cn.hutool.extra.emoji.EmojiUtil
import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import kotlinx.serialization.decodeFromString
import org.ASN70.vicu.Comments
import org.ASN70.vicu.LatestReplies
import org.ASN70.vicu.UserInfo
import org.ASN70.vicu.Utils.ValueMaps.UsedFiles
import org.ASN70.vicu.Utils.ValueMaps.commentMap
import org.ASN70.vicu.Utils.vicuAPI
import org.ASN70.vicu.VicuMain.dataFolder
import org.ASN70.vicu.VicuMain.logger
import org.ASN70.vicu.jsonConfig.CustomJson
import java.awt.Color
import java.awt.Font
import java.io.File

object text2pic {

    fun reply(comment: Comments, userInfo: UserInfo) {
        commentListConstructor(comment)
        var tmpImg: IGraphics2D?
        val tmpJPG = File(dataFolder, "tmp.jpg")
        tmpImg = IGraphics2D(
            width = 1080,
            height = 1920,
        ).drawWord(
            //被查询用户的B站名字，y=100,等线,50号字
            content = userInfo.data.uname,
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 50,
            font = Font("等线", Font.BOLD, 50),
            line = 40,
            Color.BLACK
        ).drawWord(
            //被查询用户的B站UID，y=150,等线,50号字,黑色
            content = "UID:${userInfo.data.mid}",
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 150,
            font = Font("等线", Font.BOLD, 50),
            line = 0,
            Color.BLACK
        ).drawWord(
            //被查询用户的总评论数，y=220,黑体,40号字,rgb=100,100,100
            content = "评论数：${userInfo.data.total}",
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 220,
            font = Font("黑体", Font.BOLD, 40),
            line = 0,
            color = Color(100, 100, 100)
        ).drawWord(
            //被查询用户的被回复数，y=220,黑体,40号字,rgb=100,100,100
            content = "被回复数：${userInfo.data.reply_count}",
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 260,
            font = Font("黑体", Font.BOLD, 40),
            line = 0,
            color = Color(100, 100, 100)
        ).drawWord(
            //被查询用户的被赞数，y=220,黑体,40号字,rgb=100,100,100
            content = "被赞数：${userInfo.data.like_count}",
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 300,
            font = Font("黑体", Font.BOLD, 40),
            line = 0,
            color = Color(100, 100, 100)
        ).drawWord(
            //被查询用户的首次评论时间，y=220,黑体,40号字,rgb=100,100,100
            content = "首次评论：${DateUtil.date(userInfo.data.first_reply_time * 1000)}",
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 340,
            font = Font("黑体", Font.BOLD, 40),
            line = 0,
            color = Color(100, 100, 100)
        ).drawWord(
            //被查询用户的最后评论时间，y=220,黑体,40号字,rgb=100,100,100
            content = "最后评论：${DateUtil.date(userInfo.data.last_reply_time * 1000)}",
            interval = 0,
            oneLineWidth = 960,
            x = 50,
            y = 380,
            font = Font("黑体", Font.BOLD, 40),
            line = 0,
            color = Color(100, 100, 100)
        )
        var X = 50
        var Y = 430
        var YY = 430
        var w = 1080
        var h = 1920
        //var picID = 1
        commentMap.forEach { member ->
            if (member.value.isNotEmpty()) {
                Y += 10
                YY += 10
                if (Y > 10000) {
                    w += 1080
                    //tmpImg.flush(File(dataFolder,"/comment/$picID.png"))
                    try {
                        tmpImg = IGraphics2D(w, 10800).drawImage(0, 0, tmpImg!!.bufferedImage)
                    } catch (e: OutOfMemoryError) {
                        logger.warning(e)
                        tmpImg!!.flush(tmpJPG)
                        tmpImg!!.bufferedImage.graphics.dispose()
                        tmpImg = null
                        System.gc()
                        tmpImg = IGraphics2D(w, 10800).drawImage(0, 0, tmpJPG)
                    }
                    X += 1000
                    Y = 100
                }
                else if (YY > 1800) {
                    YY -= 1920
                    h += 1920
                    if(w <= 1080) {
                        try {
                            tmpImg = IGraphics2D(1080, h).drawImage(0, 0, tmpImg!!.bufferedImage)
                        } catch (e: OutOfMemoryError) {
                            logger.warning(e)
                            tmpImg!!.flush(tmpJPG)
                            tmpImg!!.bufferedImage.graphics.dispose()
                            tmpImg = null
                            System.gc()
                            tmpImg = IGraphics2D(1080, h).drawImage(0, 0, tmpJPG)
                        }
                    }
                }
                tmpImg!!.drawWord(
                    content = member.key,
                    interval = 0,
                    oneLineWidth = 960,
                    x = X,
                    y = Y,
                    font = Font("方正特粗光辉简体", Font.BOLD, 30),
                    line = 0,
                    color = Color.decode("#576690")
                )
                Y += 40
                YY += 40
                try {
                    member.value.forEach { reply ->
                        if (Y > 10000) {
                            w += 1080
                            //tmpImg.flush(File(dataFolder,"/comment/$picID.png"))
                            try {
                                tmpImg = IGraphics2D(w, 10800).drawImage(0, 0, tmpImg!!.bufferedImage)
                            } catch (e: OutOfMemoryError) {
                                logger.warning(e)
                                tmpImg!!.flush(tmpJPG)
                                tmpImg!!.bufferedImage.graphics.dispose()
                                tmpImg = null
                                System.gc()
                                tmpImg = IGraphics2D(w, 10800).drawImage(0, 0, tmpJPG)
                            }
                            X += 1000
                            Y = 100
                        }
                        else if (YY > 1800) {
                            YY -= 1920
                            h += 1920
                            if(w <= 1080) {
                                try {
                                    tmpImg = IGraphics2D(1080, h).drawImage(0, 0, tmpImg!!.bufferedImage)
                                } catch (e: OutOfMemoryError) {
                                    logger.warning(e)
                                    tmpImg!!.flush(tmpJPG)
                                    tmpImg!!.bufferedImage.graphics.dispose()
                                    tmpImg = null
                                    System.gc()
                                    tmpImg = IGraphics2D(1080, h).drawImage(0, 0, tmpJPG)
                                }
                            }
                        }
                        tmpImg!!.drawWord(
                            content = reply,
                            interval = 30,
                            oneLineWidth = 960,
                            x = X,
                            y = Y,
                            font = Font("微软雅黑", Font.PLAIN, 25),
                            line = 5,
                            color = Color.BLACK
                        )
                        Y += 30 * tmpImg!!.getMessageRowsNumber(reply, 960, Font("微软雅黑", Font.PLAIN, 25))
                        YY += 30 * tmpImg!!.getMessageRowsNumber(reply, 960, Font("微软雅黑", Font.PLAIN, 25))

                    }
                } catch (e: Exception) {
                        tmpImg!!.flush(File(dataFolder, "Exception.png"))
                }
            }
        }
        tmpImg!!.flush(UsedFiles["comment"])
        commentMap.keys.forEach {
            commentMap[it] = mutableListOf()
        }
    }

    fun latest50() {
        var tmpImg: IGraphics2D?
        tmpImg = IGraphics2D(
            width = 1080,
            height = 10800
        )
        var X = 50
        var Y = 100
        var w = 1080
        val tmpLatest = File(dataFolder,"tmpLatest.jpg")
        val latestReplies: LatestReplies = CustomJson.decodeFromString((JSONUtil.toJsonPrettyStr(HttpUtil.get(vicuAPI.latestReply))))
        latestReplies.data.newest.forEach {
            if (Y > 10000) {
                w += 1080
                X += 1080
                Y = 100
                try { tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpImg!!.bufferedImage) }
                catch (e: OutOfMemoryError) {
                    tmpImg!!.flush(tmpLatest)
                    tmpImg!!.bufferedImage.graphics.dispose()
                    tmpImg = null
                    System.gc()
                    tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpLatest)
                }
            }
            tmpImg!!.drawWord(
                content = "${it.uname}(${it.mid}) - ${it.content}",
                oneLineWidth = 960,
                interval = 0,
                x = X,
                y = Y,
                font = Font("等线", Font.BOLD,30),
                line = 0,
                color = Color.BLACK
            )
            Y += 35 * (tmpImg!!.getMessageRowsNumber("${it.uname}(${it.mid}) - ${it.content}", oneLineWidth = 960, Font("等线", Font.BOLD,30)) + 1)
            if (Y > 10000) {
                w += 1080
                X += 1080
                Y = 100
                try { tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpImg!!.bufferedImage) }
                catch (e: OutOfMemoryError) {
                    tmpImg!!.flush(tmpLatest)
                    tmpImg!!.bufferedImage.graphics.dispose()
                    tmpImg = null
                    System.gc()
                    tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpLatest)
                }
            }
            tmpImg!!.drawWord(
                content = "评论时间：${DateUtil.date(it.ctime * 1000)}",
                oneLineWidth = 960,
                interval = 0,
                x = X,
                y = Y,
                font = Font("等线", Font.BOLD,30),
                line = 0,
                color = Color.BLACK
            )
            Y += 35 * (tmpImg!!.getMessageRowsNumber("评论时间：${DateUtil.date(it.ctime * 1000)}",oneLineWidth = 960, Font("等线", Font.BOLD,30)) + 1)
            if (Y > 10000) {
                w += 1080
                X += 1080
                try { tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpImg!!.bufferedImage) }
                catch (e: OutOfMemoryError) {
                    tmpImg!!.flush(tmpLatest)
                    tmpImg!!.bufferedImage.graphics.dispose()
                    tmpImg = null
                    System.gc()
                    tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpLatest)
                }
            }
            tmpImg!!.drawWord(
                content = "UP主：${it.up_uname}(${it.up_uid})",
                oneLineWidth = 960,
                interval = 0,
                x = 50,
                y = Y,
                font = Font("等线", Font.BOLD,30),
                line = 0,
                color = Color.BLACK
            )
            Y += 35 * (tmpImg!!.getMessageRowsNumber("UP主：${it.up_uname}(${it.up_uid})",oneLineWidth = 960, Font("等线", Font.BOLD,30)) + 1)
            if (Y > 10000) {
                w += 1080
                X += 1080
                try { tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpImg!!.bufferedImage) }
                catch (e: OutOfMemoryError) {
                    tmpImg!!.flush(tmpLatest)
                    tmpImg!!.bufferedImage.graphics.dispose()
                    tmpImg = null
                    System.gc()
                    tmpImg = IGraphics2D(w,10800).drawImage(0,0,tmpLatest)
                }
            }
            tmpImg!!.drawWord(
                content = "原动态链接：${it.dynamic_url}",
                oneLineWidth = 960,
                interval = 0,
                x = X,
                y = Y,
                font = Font("等线", Font.BOLD,30),
                line = 10,
                color = Color.BLACK
            )
            Y += 35 * (tmpImg!!.getMessageRowsNumber("原动态链接：${it.dynamic_url}", oneLineWidth = 960, Font("等线", Font.BOLD,30)) + 1) + 30
        }
        tmpImg!!.flush(UsedFiles["latest"])

    }

    private fun commentListConstructor(comment: Comments) {
        /*val commentMap = mutableMapOf<String,MutableList<String>>(
            "向晚大魔王" to mutableListOf(),
            "贝拉kira" to mutableListOf(),
            "珈乐Carol" to mutableListOf(),
            "嘉然今天吃什么" to mutableListOf(),
            "乃琳Queen" to mutableListOf(),
            "A-SOUL_Official" to mutableListOf(),
            "虞莫MOMO" to mutableListOf(),
            "柚恩不加糖" to mutableListOf(),
            "露早GOGO" to mutableListOf(),
            "莞儿睡不醒" to mutableListOf(),
            "米诺高分少女" to mutableListOf(),
            "EOE组合" to mutableListOf(),
            "星瞳_Official" to mutableListOf(),
            "量子少年-慕宇" to mutableListOf(),
            "量子少年-祥太" to mutableListOf(),
            "量子少年-泽一" to mutableListOf(),
            "量子少年-楚枫" to mutableListOf(),
            "量子少年Official" to mutableListOf(),
            "七海Nana7mi" to mutableListOf(),
            "阿梓从小就很可爱" to mutableListOf(),
            "泠鸢yousa" to mutableListOf(),
            "永雏塔菲" to mutableListOf(),
            "東雪蓮Official" to mutableListOf()
        )*/
        comment.dataBack.forEach { it ->
            it.data.forEach { dataItem ->
                dataItem.replies.forEach { reply ->
                    commentMap[dataItem.up_name]!!.add("${DateUtil.date(reply.ctime * 1000)} - ${EmojiUtil.toHtml(reply.content)}(赞:${reply.like_count})")
                }
            }
        }
    }
}