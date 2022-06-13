package WOWSCollection.Commands;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class wows_check extends PureCommand{

    public wows_check(String name) {
        super(name);
    }

    public static Long time = 0L;

    @Override
    public String info() {
        return super.info() + " -> 打开一个幸运的WOWS超级补给箱。";
    }

    @Override
    public void onCommand(GroupMessageEvent event) throws Exception {
        //是否存在此用户
        DatabaseManage f = new DatabaseManage();
        if(f.check(event.getSender().getId())){
            menu(event);
        }else{
            event.getSubject().sendMessage("请先注册喵~发送“注册”即可");
        }
    }

    //获取全部船只列表
    public void menu(GroupMessageEvent event) throws Exception {
        String filePath = "./data/wows_box/ships_all";
        List<File> list = getFileList(filePath);
        if(list.size()==0){
            System.out.println("error");
        }
        mergeImage(list,  2, "./data/wows_box/c.jpg",event);
    }

    /**
     * 图鉴图拼接生成
     * @param type      1 横向拼接， 2 纵向拼接
     * （注意：必须两张图片长宽一致）
     */
    public static void mergeImage(List<File> list, int type, String targetFile,GroupMessageEvent event) throws Exception {
        File top = new File("./data/wows_box/top.png");
        list.add(0,top.getAbsoluteFile());
        System.out.println(top.getAbsoluteFile());

        DatabaseManage f = new DatabaseManage();
        int num=0;

        List<String> list_num = f.list_get(event);//创建取结果的列表

        //list图片拼接
        int len = list.size();
        BufferedImage[] images = new BufferedImage[len];
        for(int i=0;i<list.size();i++){
            /*System.out.println(list_num.get(0));
            System.out.println(list.get(i).getName());*/
            if(list_num.contains(list.get(i).getName().substring(0,list.get(i).getName().length()-4))){

                images[i]=binaryImage(ImageIO.read(list.get(i)));
            }else{
                num++;
                images[i]=ImageIO.read(list.get(i));
            }
        }
        int[][] ImageArrays = new int[len][];

        for (int i = 0; i < len; i++) {
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {// 纵向
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return;
        }
        if (type == 2 && newHeight < 1) {
            return;
        }
        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            //输出想要的图片
            ImageIO.write(ImageNew,"jpg",new File(targetFile));
            //发送
            MessageChain msg = new MessageChainBuilder()
                    .append(new QuoteReply(event.getSource()))
                    .append(new At(event.getSender().getId()))
                    .append("收集进度:"+(num-1)+"/"+(list.size()-1))
                    .append(Contact.uploadImage(event.getGroup(), new File(targetFile)))
                    .build();
            event.getSubject().sendMessage(msg);
            System.out.println("拼接图片成功");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //从文件获取全部船只列表
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
                    //String strFileName = files[i].getAbsolutePath();
                    //System.out.println("---" + strFileName);
                    fileList.add(files[i]);
                } else {
                    continue;
                }
            }
        }
        return fileList;
    }
    public static BufferedImage binaryImage(BufferedImage image) throws Exception{

        int width = image.getWidth();
        int height = image.getHeight();

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int p = image.getRGB(i, j);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int avg = (r + g + b) / 3;

                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                image.setRGB(i, j, p);
            }
        }
        //输出图片格式可随便定义 如: jpg\png\bmp等
        return image;
    }
}
