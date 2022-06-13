package WOWSCollection;

import WOWSCollection.Commands.*;
import WOWSCollection.Config.Config;
import WOWSCollection.Resources.Resources;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.IOException;
import java.sql.SQLException;

public final class Plugin extends JavaPlugin {
    public CommandsManager commandsManager;
    public static final Plugin INSTANCE = new Plugin();


    private Plugin() {
        super(new JvmPluginDescriptionBuilder(Resources.ID, Resources.VERSION)
                .name(Resources.NAME)
                .info(Resources.DESCRIPTION)
                .author(Resources.AUTHOR)
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin: " + Resources.NAME + " loading...");


        //读取配置文件
        reloadPluginConfig(Config.INSTANCE);

        // 创建自建指令管理系统
        commandsManager = new CommandsManager(getLogger(), Resources.COMMAND_PREFIX);

        // 注册自建指令
        commandsManager.registerCommand(new wows_box("开箱"));
        commandsManager.registerCommand(new wows_resigter("注册"));
        commandsManager.registerCommand(new wows_check("图鉴"));
        commandsManager.registerCommand(new wows_help("手册"));

        // 注册Console命令


        // 注入自建的指令处理系统
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            try {
                commandsManager.handle(event);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
            getLogger().info("Plugin: " + Resources.NAME + " loaded!");

        }
    static Long timestamp = Long.valueOf(111);
}

