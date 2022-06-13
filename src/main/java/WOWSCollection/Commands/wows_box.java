package WOWSCollection.Commands;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class wows_box extends PureCommand {

    public wows_box(String name) {
        super(name);
    }

    public static Long time = 0L;

    @Override
    public String info() {
        return super.info() + " -> 打开一个幸运的WOWS超级补给箱。";
    }

    @Override
    public void onCommand(GroupMessageEvent event) throws SQLException {
        DatabaseManage f = new DatabaseManage();
        if(f.check(event.getSender().getId())){
            if(f.check_limit(event,event.getSender().getId())){
                douwnload_local(event);
            }
        }else{
            event.getSubject().sendMessage("请先注册喵~发送“注册”即可");
        }

    }

    public void douwnload_local(GroupMessageEvent event) throws SQLException {
        System.out.println("正在准备箱子中");
        boolean flag;
        String file = "";
        Random r = new Random();
            int n5 = r.nextInt(10000);
            file = "";
            flag = false;
            System.out.print("本次结果为" + n5);
            if (n5 < 1000) { //55个数字的区间，55%的几率
                file = "./data/wows_box/ship/0.05";
                flag = true;
            } else if (n5 < 2000) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/ship/0.2";
                flag = true;
            } else if (n5 < 5000) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/ship/1.25";
                flag = true;
            } else if (n5 < 5500) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/0.1";
            } else if (n5 < 6000) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/0.2";
            } else if (n5 < 6500) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/0.4";
            } else if (n5 < 7000) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/0.55";
            } else if (n5 < 7500) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/1";
            } else if (n5 < 8000) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/1.5";
            } else if (n5 < 8450) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/2";
            } else if (n5 < 8750) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/3.45";
            } else if (n5 < 9000) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/5";
            } else if (n5 < 9500) {//[55,95)，40个数字的区间，40%的几率
                file = "./data/wows_box/others/8.5";
            } else {
                file = "./data/wows_box/others/10";
            }
            //这下面的是按官方的爆率
            /*if(n5 < 5){ //55个数字的区间，55%的几率
                file="./data/wows_box/ship/0.05";
                flag=true;
            }else if(n5 < 25){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/ship/0.2";
                flag=true;
            }else if(n5 < 150){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/ship/1.25";
                flag=true;
            }else if(n5 < 250){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/0.1";
            }else if(n5 < 270){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/0.2";
            }else if(n5 < 590){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/0.4";
            }else if(n5 < 2405){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/0.55";
            }else if(n5 < 2505){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/1";
            }else if(n5 < 3705){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/1.5";
            }else if(n5 < 6305){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/2";
            }else if(n5 < 6650){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/3.45";
            }else if(n5 < 7150){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/5";
            }else if(n5 < 8000){//[55,95)，40个数字的区间，40%的几率
                file="./data/wows_box/others/8.5";
            }else{
                file="./data/wows_box/others/10";
            }*/
        System.out.println(file);
        douwnload_local_2(event,file,flag);
    }

    public void douwnload_local_2 (GroupMessageEvent event, String file, boolean flag) throws SQLException {

        List<File> list = getFileList(file);
        if(list.size()==0){
            System.out.println("error");
        }
        Random r = new Random();
        int n5 = r.nextInt(list.size());
        DatabaseManage f = new DatabaseManage();
        if(f.Is_get(event,list.get(n5).getName().substring(0,list.get(n5).getName().length()-4),event.getSender().getId(),flag)){
            f.ship_reget(event);
        }else {

            f.Update_user(event, list.get(n5).getName().substring(0, list.get(n5).getName().length() - 4), event.getSender().getId(), flag);
            if (flag) {
                MessageChain msg = new MessageChainBuilder()
                        .append(new QuoteReply(event.getSource()))
                        .append(new At(event.getSender().getId()))
                        .append(Contact.uploadImage(event.getGroup(), new File(String.valueOf(list.get(n5)))))
                        .build();
                event.getSubject().sendMessage(msg);
            } else {
                MessageChain msg = new MessageChainBuilder()
                        .append(new QuoteReply(event.getSource()))
                        .append(new At(event.getSender().getId()))
                        .append(Contact.uploadImage(event.getGroup(), new File(String.valueOf(list.get(n5)))))
                        .append(list.get(n5).getName().substring(0, list.get(n5).getName().length() - 4))
                        .build();
                event.getSubject().sendMessage(msg);
            }
        }
    }

    public static List<File> getFileList(String path) {
        List<File> fileList = new ArrayList<>();
        File dir = new File(path);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    // 获取文件绝对路径
                    getFileList(files[i].getAbsolutePath());
                    // 判断文件名是否以.png结尾
                } else if (fileName.endsWith(".png")) {
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println("---" + strFileName);
                    fileList.add(files[i]);
                } else {
                    continue;
                }
            }
        }
        return fileList;
    }

}

