package org.ASN70.vicu.Utils

import cn.hutool.core.io.FileUtil
import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import kotlinx.coroutines.*
import org.ASN70.vicu.Utils.ValueMaps.UsedFiles
import org.ASN70.vicu.VicuMain.dataFolder
import org.ASN70.vicu.VicuMain.logger
import java.io.File

/**把请求返回的数据写入JSON*/
object ToJSON{
    /** 页码 */
    var PAGE_NUM = 1
    @OptIn(DelicateCoroutinesApi::class)
    fun comments(URL: String) {
        if (!UsedFiles["json"]!!.exists()) UsedFiles["json"]!!.createNewFile()
        else UsedFiles["json"]!!.writeText("")
        val commentArray = JSONUtil.createArray()
        loop@ while (true) {

            if(JSONUtil.parseObj(HttpUtil.get("$URL&pn=$PAGE_NUM"))["msg"] == "success") {
                commentArray.add(
                    JSONUtil.parseObj(
                        JSONUtil.toJsonPrettyStr(HttpUtil.get("$URL&pn=$PAGE_NUM"))
                    )
                )
                File(dataFolder,"jsonTest.json").deleteOnExit()
                File(dataFolder,"jsonTest.json").writeText(commentArray.toStringPretty())
                PAGE_NUM++
            }
            else {
                PAGE_NUM = 1
                break@loop
            }
        }
        val commentObj = JSONUtil.createObj().set("databack",commentArray)
        UsedFiles["json"]!!.writeText(commentObj.toStringPretty())
    }
    fun userInfo(uid: String) {
        if (!UsedFiles["userInfo"]!!.exists()) UsedFiles["json"]!!.createNewFile()
        else UsedFiles["userInfo"]!!.writeText(JSONUtil.toJsonPrettyStr(HttpUtil.get("${vicuAPI.user}?uid=$uid")))
    }
}




