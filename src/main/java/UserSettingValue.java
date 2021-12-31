import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.ArrayList;

public class UserSettingValue implements Serializable {
    public Boolean isPre = true;
    public ArrayList<String> preStringList = new ArrayList();

    private UserSettingValue() {
    }

    private volatile static UserSettingValue instance;
    public static final String savePath = System.getProperty("user.home") + "/.idCompletion.json";

    public static UserSettingValue getInstance() {
        if (instance == null) {
            synchronized (UserSettingValue.class) {
                if (instance == null) {
                    instance = new UserSettingValue();
                    try {
                        String jsonString = readToString(savePath);
                        if (jsonString != null) {
                            DataBean data = JSON.parseObject(jsonString, DataBean.class);
                            instance.isPre = data.getPre();
                            instance.preStringList = new ArrayList(data.getPreStringList());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return instance;
    }

    /**
     * 读取文件内容
     */
    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写入文件
     */
    public static void saveStringToFile(String fileName, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(fileName));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
