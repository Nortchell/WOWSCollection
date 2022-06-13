package WOWSCollection.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;

public class wows_resigter extends PureCommand {

    public wows_resigter(String name) {
        super(name);
    }

    public static Long time = 0L;

    @Override
    public String info() {
        return super.info() + " -> 打开一个幸运的WOWS超级补给箱。";
    }

    @Override
    public void onCommand(GroupMessageEvent event) {
        resister(event);
    }

    public void resister(GroupMessageEvent event){
        DatabaseManage data = new DatabaseManage();
        System.out.println("连接数据库成功！");
        boolean flag = data.Register(event.getSender().getId());
        if(flag){
            event.getSubject().sendMessage("注册成功了诶");
        }else {
            event.getSubject().sendMessage("注册失败了捏");
        }
    }
}
