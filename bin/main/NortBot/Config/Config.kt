package NortBot.Config

import NortBot.Resources.Resources
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("${Resources.NAME}Config") {
    //var imageAPI: String by value(Resources.IMG_API_URL)
    var commandPrefix: String by value(Resources.COMMAND_PREFIX)

    var imageStorage: String by value(Resources.IMG_STORAGE_PATH)

    var imageAPIList: List<String> by value(arrayListOf(Resources.IMG_API_URL))

    var imageAPIList_2: List<String> by value(arrayListOf(Resources.IMG_API_URL_2))

    var imageAPIList_3: List<String> by value(arrayListOf(Resources.IMG_API_URL_3))

    // Command Switch Config
    var ping: Boolean by value(true)
    //var getRandImage: Boolean by value(true)
    var ybb: Boolean by value(true)

    // Customized Setting for Command
    var getRandImageCD: Int by value(0)
}