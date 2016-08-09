package fileLock.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.ProjectManager;

import java.io.*;
import java.nio.file.Paths;

/**
 * Created by lbin on 7/29/2016.
 */
public class Utils {
    public static final String DataFolderName = "FileLock_Data";
    public static final String ShelvedCLFolderName = "shelvedCL";
    public static final String ConfigFileName = "config.json";
    public static final String JSON_Suffix = ".json";
    public static final String Backup_File = "backup_file";
    public static final String CL_Template = "{\"clNo\":%d,\"createDate\":%d,\"codeLine\":%d,\"files\":[],\"desc\":\"%s\",\"seelvedCL\":%d}";

    public static String getDataFolderPath(){
        String userFolder = System.getProperty("user.home");
        String path = Paths.get(userFolder, DataFolderName).toString();
        File folder = new File(path);
        if (!folder.isDirectory()){
            folder.mkdir();
        }

        return path;
    }

    public static String readFile(String filePath) throws Exception{
        String ret = "";
        File file = new File(filePath);
        System.out.print("file: " + filePath);
        if (!file.exists()){
            throw new Exception("File not found!");
        }
        try{
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String temp, objStr = "";
            while ((temp = br.readLine()) != null) {
                objStr += temp;
            }

            ret = objStr;
        }catch (Exception e){
            e.printStackTrace();
        }

        return ret;
    }

    public static boolean writeFile(String filePath, String content){
        boolean ret = true;

        try{
            File file = new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file,false);
            out.write(content.getBytes());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public static String objectToString(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static boolean copyFile(String source, String target){
        boolean ret = true;

        try{
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(target);
            byte[] bytes = new byte[1024];
            int temp = 0;
            while ((temp = fis.read(bytes)) != -1){
                fos.write(bytes, 0, temp);
            }
            fos.flush();
            fis.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }
}
