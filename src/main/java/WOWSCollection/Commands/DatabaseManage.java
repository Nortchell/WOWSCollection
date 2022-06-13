package WOWSCollection.Commands;

import WOWSCollection.Resources.Resources;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class DatabaseManage {
    //定义连接数据库所需要的对象
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection ct = null;

    public void init(){
        //加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //得到连接
            ct = DriverManager.getConnection(Resources.MySQLurl,Resources.MySQLuser,Resources.MySQLpassword);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseManage(){
        this.init();
    }

    public boolean check(Long ID){
        try {
            ps = ct.prepareStatement("select * from list where ID='"+ID+"'");
            rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public boolean check_limit(GroupMessageEvent event,Long ID){
        try {
            // 普通的时间转换
            String string = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            ps = ct.prepareStatement("select 次数,日期 from list where ID='"+ID+"'");
            rs = ps.executeQuery();
            if(rs.next()){
                System.out.println(string);
                System.out.println(rs.getString(2));
                if(string.equals(rs.getString(2))){
                    if(rs.getInt(1)>=Resources.open_num){
                        event.getSubject().sendMessage("今天三箱已经开完了哦，大佬明天再来吧。");
                        return false;
                    }else {
                        String add = "update list set 次数=" + (rs.getInt(1) + 1) + " where ID='" + ID + "'";
                        ps = ct.prepareStatement(add);
                        ps.executeUpdate();
                        return true;
                    }
                }else{
                    String add = "update list set 日期='"+string+"' where ID='"+ID+"'";
                    ps = ct.prepareStatement(add);
                    ps.executeUpdate();
                    add = "update list set 次数="+1+" where ID='"+ID+"'";
                    ps = ct.prepareStatement(add);
                    ps.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //注册用户信息
    public boolean Register(Long ID){
        boolean b = true;
        try {
            ps = ct.prepareStatement("select * from list where ID='"+ID+"'");
            rs = ps.executeQuery();

            if(rs.next()){
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            ps = ct.prepareStatement("insert into list(ID) values(?)");
            ps.setString(1, String.valueOf(ID));
            if(ps.executeUpdate()!=1)  // 执行sql语句
            {
                b=false;
            }
        } catch (SQLException e) {
            b = false;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    //取得用户信息
    public boolean Is_get(GroupMessageEvent event, String get, Long ID, boolean flag){
        try {
            ps = ct.prepareStatement("select `"+get+"` from list where ID='"+event.getSender().getId()+"'");
            rs = ps.executeQuery();
            rs.next();
            if(rs.getBoolean(1)){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //更新用户数据
    public boolean Update_user(GroupMessageEvent event,String get, Long ID, boolean flag){
        boolean b = true;
        if(flag) {
            try {
                String add = "update list set `"+get+"`="+true+" where ID='"+ID+"'";
                ps = ct.prepareStatement(add);
                if (ps.executeUpdate() != 1)  // 执行sql语句
                {
                    b = false;
                }
            } catch (SQLException e) {
                b = false;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }/*else{
            try {
                Pattern ptn = Pattern.compile("([\\u4e00-\\u9fa5])?：([\\d+])(.*)");
                Matcher matcher = ptn.matcher(get);
                String name = matcher.group(1);
                String num =matcher.group(2);
                String add = "update list set `"+name+"`="+num+" where ID='"+String.valueOf(ID)+"'";
                ps = ct.prepareStatement(add);
                if (ps.executeUpdate() != 1)  // 执行sql语句
                {
                    b = false;
                }
            } catch (SQLException e) {
                b = false;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
        return b;
    }

    //重复船只，重新抽取
    public void ship_reget(GroupMessageEvent event) throws SQLException {
        ps = ct.prepareStatement("select * from list where ID='" + event.getSender().getId() + "'");
        ResultSet rs = ps.executeQuery();//得到结果集
        ResultSetMetaData data = rs.getMetaData();
        int colCount = data.getColumnCount();
        List<String> list = new ArrayList<String>();//创建取结果的列表，之所以使用列表，不用数组，因为现在还不知道结果有多少，不能确定数组长度，所有先用list接收，然后转为数组
        while (rs.next()) {//如果有数据，取第一列添加如list
            for (int i = 1; i < colCount + 1; i++) {
                if (!rs.getBoolean(i)) {
                    list.add(data.getColumnName(i));
                }
            }
        }
        //更新数据
        if (list.size() != 0) {
            Random r = new Random();
            int n5 = r.nextInt(list.size());
            try {
                String add = "update list set `" + list.get(n5) + "`=" + true + " where ID='" + event.getSender().getId() + "'";
                ps = ct.prepareStatement(add);
                if (ps.executeUpdate() != 1)  // 执行sql语句
                {
                    System.out.println("ok");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //发送信息
            MessageChain msg = new MessageChainBuilder()
                    .append(new QuoteReply(event.getSource()))
                    .append(new At(event.getSender().getId()))
                    .append(Contact.uploadImage(event.getGroup(), new File("./data/wows_box/ships_all/" + list.get(n5) + ".png")))
                    .build();
            event.getSubject().sendMessage(msg);
        } else {
            event.getSubject().sendMessage("您已经完成全收集了哦！所以只能达布隆*5000啦！");
        }
    }

    //获取此用户的未获得船只列表
    public List<String> list_get(GroupMessageEvent event) throws SQLException {
        ps = ct.prepareStatement("select * from list where ID='" + event.getSender().getId() + "'");
        ResultSet rs = ps.executeQuery();//得到结果集
        ResultSetMetaData data = rs.getMetaData();
        int colCount = data.getColumnCount();
        List<String> list = new ArrayList<String>();//创建取结果的列表
        while (rs.next()) {//如果有数据，取第一列添加如list
            for (int i = 1; i < colCount + 1; i++) {
                try {
                    if (!rs.getBoolean(i)) {//如果用户没有拥有此船，则添加到列表
                        list.add(data.getColumnName(i));
                    }
                }catch(Exception e){
                    continue;
                }
            }
        }
        return list;
    }
}
