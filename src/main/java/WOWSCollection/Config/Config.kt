package WOWSCollection.Config

import WOWSCollection.Resources.Resources
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("${Resources.NAME}Config") {
    //var imageAPI: String by value(Resources.IMG_API_URL)
    var commandPrefix: String by value(Resources.COMMAND_PREFIX)

    var imageStorage: String by value(Resources.IMG_STORAGE_PATH)

    var MySQLurl: String by value(Resources.MySQLurl)

    var MyDQLuser: String by value(Resources.MySQLuser)

    var MyDQLpassword: String by value(Resources.MySQLpassword)

    var help_message: String by value(Resources.help_message)

    var open_num: Int by value(default = 3)
}