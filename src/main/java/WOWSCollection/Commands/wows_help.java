package WOWSCollection.Commands;

import WOWSCollection.Resources.Resources;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class wows_help extends PureCommand{

    public wows_help(String name) {
        super(name);
    }

    public static Long time = 0L;

    @Override
    public String info() {
        return super.info() + " -> 打开一个幸运的WOWS超级补给箱。";
    }

    @Override
    public void onCommand(GroupMessageEvent event) throws Exception {
        MessageChain msg = new MessageChainBuilder()
                .append("---wows收集图鉴---\n")
                .append("1.注册--发送\"注册\"\n")
                .append("2.开箱--发送\"开箱\"\n")
                .append("3.每天三箱哦~\n")
                .append("4.图鉴--发送\"图鉴\"\n")
                .append(Resources.help_message)
                .build();
        event.getSubject().sendMessage(msg);
    }
}
