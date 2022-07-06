package org.ASN70.vicu.Utils

import org.ASN70.vicu.VicuMain
import java.io.File

object ValueMaps {
    /*/** 用户UID字典*/
var USER_UID_MAP = mapOf<String, String>(
    "uid" to ""
)*/

    /** UP主UID map*/
    val UP_MAP = mutableMapOf<String,String>()

    /**用于存储每个UP主评论区评论内容的Map*/
    var commentMap = mutableMapOf<String,MutableList<String>>(
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
    )

    /** 用到的文件目录*/
    val UsedFiles = mapOf(
        "json" to File(VicuMain.dataFolder, "comment.json"),
        "userInfo" to File(VicuMain.dataFolder,"userInfo.json"),
        "comment" to File(VicuMain.dataFolder,"comment.jpg"),
        "ups" to File(VicuMain.dataFolder,"ups.json"),
        "latest" to File(VicuMain.dataFolder,"latest.png")
    )

/*/**供HttpGet使用的参数map*/
val UP_UID_PARAM_MAP = mapOf(
    "向晚大魔王" to ("up_uid" to "672346917"),
    "贝拉kira" to ("up_uid" to "672353429"),
    "珈乐Carol" to ("up_uid" to "351609538"),
    "嘉然今天吃什么" to ("up_uid" to "672328094"),
    "乃琳Queen" to ("up_uid" to "672342685"),
    "A-SOUL_Official" to ("up_uid" to "703007996"),
    "虞莫MOMO" to ("up_uid" to "1811071010"),
    "柚恩不加糖" to ("up_uid" to "1795147802"),
    "露早GOGO" to ("up_uid" to "1669777785"),
    "莞儿睡不醒" to ("up_uid" to "1875044092"),
    "米诺高分少女" to ("up_uid" to "1778026586"),
    "EOE组合" to ("up_uid" to "2018113152"),
    "星瞳_Official" to ("up_uid" to "401315430"),
    "量子少年-慕宇" to ("up_uid" to "1230039261"),
    "量子少年-祥太" to ("up_uid" to "1461176910"),
    "量子少年-泽一" to ("up_uid" to "1535525542"),
    "量子少年-楚枫" to ("up_uid" to "1757836012"),
    "量子少年Official" to ("up_uid" to "1895683714"),
    "七海Nana7mi" to ("up_uid" to "434334701"),
    "阿梓从小就很可爱" to ("up_uid" to "7706705"),
    "泠鸢yousa" to ("up_uid" to "282994"),
    "永雏塔菲" to ("up_uid" to "1265680561"),
    "東雪蓮Official" to ("up_uid" to "1437582453"),
)*/
}