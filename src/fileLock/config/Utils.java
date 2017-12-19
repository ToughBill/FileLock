package fileLock.config;

import com.google.gson.Gson;
import fileLock.bo.ChangeList;
import fileLock.bo.CompAppBean;
import fileLock.bo.Configuration;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lbin on 7/29/2016.
 */
public class Utils {
    public static final String DataFolderName = "FileLock_Data";
    public static final String ShelvedCLFolderName = "shelvedCL";
    public static final String CodeLineFolderName = "codelines";
    public static final String ConfigFileName = "config.json";
    public static final String CodeLineEntriesFileName = "codelines.json";
    public static final String CodeLineBeanFileName = "codeline.json";
    public static final String ChangeListBeanFileName = "changelist.json";
    public static final String JSON_Suffix = ".json";
    public static final String BackupFilesFolder = "backupfiles";
    public static final String RevisionsFolder = "revisions";
    public static final String ChangeListsFolder = "changelists";
    public static final String ConfigureFileTemplate = "{\"compApp\":[]}";
    public static final String CodeLineEntryFileTemplate = "{\"codeLineEntries\":[], \"nextCodeLineNo\":1}";
    public static final String CodeLineFileTemplate = "{\"id\":%d,\"createDate\":%d,\"desc\":\"%s\"}";
    public static final String CL_Template = "{\"clNo\":%d,\"createDate\":%d,\"codeLine\":%d,\"files\":[],\"desc\":\"%s\"}";

    public static String getDataFolderPath(){
        String userFolder = System.getProperty("user.home");
        String path = Paths.get(userFolder, DataFolderName).toString();
//        File folder = new File(path);
//        if (!folder.isDirectory()){
//            folder.mkdir();
//        }

        return path;
    }
    public static String getCodeLineFolder(){
        String dtFolder = getDataFolderPath();
        return Paths.get(dtFolder, CodeLineFolderName).toString();
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

    public static boolean copyFile(String source, String target, boolean setTargetReadOnly){
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

            if (setTargetReadOnly){
                File file = new File(target);
                file.setReadOnly();
            }

        }catch (IOException e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public static boolean isPathExists(String path){
        File file = new File(path);
        return file.exists();
    }
    public static void ensurePathExists(String path, boolean isDir){
        File file = new File(path);
        if(!file.exists()){
            System.out.print("@@ EnsurePathExists: path is not exists, " + path + "\n");
            try{
                if(isDir){
                    file.mkdir();
                } else {
                    file.createNewFile();
                }

//                FileOutputStream out = new FileOutputStream(file,false);
//                out.write(Utils.ConfigureFileTemplate.getBytes());
//                out.close();
                System.out.print("@@ EnsurePathExists: path created\n");
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            System.out.print("@@ EnsurePathExists: path is exists, " + path + "\n");
        }
    }

    public static String exeCmd(String commandStr) {
        String ret = null;
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            ret = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    public static void DiffFile(String path){
        String fileName = Paths.get(path).getFileName().toString();
        String baseFilePath;
//        if(CodeLineManager.getCurrentCodeLine().getIsUnderSvn()){
//            baseFilePath = CodeLineManager.getCurrentCodeLine().getFileMap().getSourcePath(path);
//        } else{
            ChangeList cl = ChangeList.findChangeList(path);
            if (cl == null)
                return;
            String targetFileName = String.valueOf(cl.getCLNo()) + "_" + fileName;
            baseFilePath = Paths.get(CodeLineManager.getCurrentCodeLine().getRepoPath(), Utils.BackupFilesFolder, targetFileName).toString();
        //}

        CompAppBean appPath = Configuration.getInstance().getDefaultCompApp();
        startCompare(appPath.path, baseFilePath, path);
    }

    public static void MergeChangesToSvnSource(String path){
        if(!CodeLineManager.getCurrentCodeLine().getIsUnderSvn()){
            return;
        }

        String baseFilePath = CodeLineManager.getCurrentCodeLine().getFileMap().getSourcePath(path);
        CompAppBean appPath = Configuration.getInstance().getDefaultCompApp();
        startCompare(appPath.path, baseFilePath, path);
    }

    private static void startCompare(String appPath, String file1, String file2){
        try{
            Runtime run = Runtime.getRuntime();
            String args = appPath + " \"" + file1 + "\" \"" + file2 + "\" /lefttitle=\"base\" /righttitle=\"workspace\"";
            Process p = run.exec(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void ShowInExplorer(String path){
        try{
            Runtime run = Runtime.getRuntime();
            String args = "explorer.exe /select,\"" + path +"\"";
            Process p = run.exec(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getCurrentTimeString(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }

    public static String getExtensionPath(){
        String ret = CurrentAction.getProjectPath();
        if(ret != null){

        }

        return ret;
    }
}
