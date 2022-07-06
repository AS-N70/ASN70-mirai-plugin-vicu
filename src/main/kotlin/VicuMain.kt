package org.ASN70.vicu

import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONUtil
import kotlinx.serialization.decodeFromString
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registerCommand
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.registeredCommands
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregisterCommand
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import org.ASN70.vicu.Utils.ValueMaps.UP_MAP
import org.ASN70.vicu.Utils.ValueMaps.UsedFiles
import org.ASN70.vicu.Utils.vicuAPI


import org.ASN70.vicu.jsonConfig.CustomJson
import org.ASN70.vicu.jsonConfig.UP_LIST
import java.io.File

/**
 * 使用 kotlin 版请把
 * `src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin`
 * 文件内容改成 `org.example.mirai.plugin.VicuMain` 也就是当前主类全类名
 *
 * 使用 kotlin 可以把 java 源集删除不会对项目有影响
 *
 * 在 `settings.gradle.kts` 里改构建的插件名称、依赖库和插件版本
 *
 * 在该示例下的 [JvmPluginDescription] 修改插件名称，id和版本，etc
 *
 * 可以使用 `src/test/kotlin/RunMirai.kt` 在 ide 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

object VicuMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.ASN70.vicu",
        name = "B站查评论插件",
        version = "0.1.0"
    ) {
        author("ASN70")
        info(
            """
            B站查评论插件
            网站：vicu.online
        """.trimIndent()
        )
    }
) {
    override fun onEnable() {
        UsedFiles.forEach {
            if(!it.value.exists()) it.value.createNewFile()
        }
        File(dataFolder,"comment").mkdir()
        registerCommand(Command.icuComment)
        val ups: UP_LIST = CustomJson.decodeFromString(JSONUtil.toJsonPrettyStr(HttpUtil.get(vicuAPI.ups)))
        ups.data.forEach { up ->
            UP_MAP[up.uname] = up.mid.toString()
        }
    }
    override fun onDisable() {
        for(command in registeredCommands) unregisterCommand(command)

    }
    private fun testJsonPath() {
        val testObj = JSONUtil.parseObj(HttpUtil.get(vicuAPI.latestReply))
        logger.info(testObj.getByPath("data.item[0].uname") as String)
    }
}
