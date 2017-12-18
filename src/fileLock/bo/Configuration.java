package fileLock.bo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fileLock.config.FileMapping;
import fileLock.config.Utils;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by lbin on 8/2/2016.
 */
public class Configuration {
    public ConfigurationBean m_cfgBean;

    private Configuration(){ }

    private static Configuration m_instance;
    public static void initSettings(){
        try{
            // init config.json file
            String path = Utils.getDataFolderPath();
            String configPath = Paths.get(path, Utils.ConfigFileName).toString();
            if(!Utils.isPathExists(path)){
                Utils.ensurePathExists(path, true);
                Utils.ensurePathExists(configPath, false);
                Utils.writeFile(configPath, Utils.ConfigureFileTemplate);

//                Gson gson = new Gson();
//                java.lang.reflect.Type type = new TypeToken<ConfigurationBean>() {}.getType();
//                m_instance = new Configuration();
//                m_instance.m_cfgBean = gson.fromJson(Utils.ConfigureFileTemplate, type);

                //init codelines folder
                String clFolderPath = Utils.getCodeLineFolder();
                Utils.ensurePathExists(clFolderPath, true);
                String clConfigPath = Paths.get(clFolderPath, Utils.CodeLineEntriesFileName).toString();
                Utils.ensurePathExists(clConfigPath, false);
                Utils.writeFile(clConfigPath, Utils.CodeLineEntryFileTemplate);
            }

            String str = Utils.readFile(configPath);
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<ConfigurationBean>() {}.getType();
            m_instance = new Configuration();
            m_instance.m_cfgBean = gson.fromJson(str, type);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static Configuration getInstance(){
        if (m_instance == null){
            try{
                String path = Utils.getDataFolderPath();
                String configPath = Paths.get(path, Utils.ConfigFileName).toString();
                Configuration.EnsureConfigurationFileExists(configPath);
                File file = new File(configPath);
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String temp, objStr = "";
                while ((temp = br.readLine()) != null) {
                    objStr += temp;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<ConfigurationBean>() {}.getType();
                m_instance = new Configuration();
                m_instance.m_cfgBean = gson.fromJson(objStr, type);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return m_instance;
    }

    public CodeLineBean getCodeLineBean(String path){
        CodeLineBean ret = null;
        Configuration inst = Configuration.getInstance();
        for(int i = 0; i < inst.m_cfgBean.codeLine.size(); i++){
            CodeLineBean temp = inst.m_cfgBean.codeLine.get(i);
            if (temp.proPath.equals(path)){
                ret = temp;
                break;
            }
        }

        return ret;
    }
    public CodeLineBean getCodeLineBean(String path, String time){
        CodeLineBean ret = null;
        Configuration inst = Configuration.getInstance();
        for(int i = 0; i < inst.m_cfgBean.codeLine.size(); i++){
            CodeLineBean temp = inst.m_cfgBean.codeLine.get(i);
            if (temp.proPath.equals(path) && temp.createDate.equals(time) ){
                ret = temp;
                break;
            }
        }

        return ret;
    }

    public static void EnsureConfigurationFileExists(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            System.out.print("@@ EnsureConfigurationFileExists: file is not exists, " + filePath);
            try{
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file,false);
                out.write(Utils.ConfigureFileTemplate.getBytes());
                out.close();
                System.out.print("@@ EnsureConfigurationFileExists: file created");
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            System.out.print("@@ EnsureConfigurationFileExists: file is exists, " + filePath);
        }

    }

    public boolean addCodeLine(CodeLineBean line){
        boolean ret;
        Configuration inst = Configuration.getInstance();
        inst.m_cfgBean.codeLine.add(line);
        ret = save();
        return  ret;
    }

    public CodeLineBean createNewCodeLine(String path, String time){
        CodeLineBean ret = null;
        int maxNo = 0;
        Configuration inst = Configuration.getInstance();
        Iterator<CodeLineBean> iter = inst.m_cfgBean.codeLine.iterator();
        while (iter.hasNext()){
            CodeLineBean config = iter.next();
            if(config.codeLineNo > maxNo)
                maxNo = config.codeLineNo;
        }
        int newCodeLineNo = maxNo + 1;
        String codeLinePath = Paths.get(Utils.getDataFolderPath(), String.valueOf(newCodeLineNo)).toString();
        File dir = new File(codeLinePath);
        dir.mkdir();
        File dir2 = new File(Paths.get(codeLinePath, Utils.Backup_File).toString());
        dir2.mkdir();

        ret = new CodeLineBean();
        ret.codeLineNo = newCodeLineNo;
        ret.proPath = path;
        ret.createDate = time;
        ret.repoPath = codeLinePath;
        String mapFile = path + "\\" + FileMapping.FL_FileMappingPath;
        File temp = new File(mapFile);
        ret.isUnderSvn = temp.exists();
        inst.m_cfgBean.codeLine.add(ret);

        createDefaultCL(ret);

        save();
        return ret;
    }

    public boolean createDefaultCL(CodeLineBean lineCfg){
        boolean ret = true;

        String clFilePath = Paths.get(lineCfg.repoPath, ChangeList.Default_CL_No + Utils.JSON_Suffix).toString();
        try{
            File file = new File(clFilePath);
            if (!file.exists()){
                file.createNewFile();

                String txt = String.format(Utils.CL_Template, ChangeList.Default_CL_No,
                        Calendar.getInstance().getTimeInMillis(), lineCfg.codeLineNo, "Default", 0);
                Utils.writeFile(clFilePath, txt);
            }
        }catch (IOException e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public boolean save(){
        boolean ret = true;
        Gson gson = new Gson();
        String json = gson.toJson(m_cfgBean);
        String configPath = Utils.getDataFolderPath();
        File file = new File(Paths.get(configPath, Utils.ConfigFileName).toString());
        try{
            FileOutputStream out = new FileOutputStream(file,false);
            out.write(json.getBytes());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public int getNextCLNo(){
        int ret =  m_cfgBean.nextCLNo;
        m_cfgBean.nextCLNo++;
        save();
        return ret;
    }

    public CompAppBean getDefaultCompApp(){
        CompAppBean ret = null;
        for (CompAppBean app : m_cfgBean.compApp){
            if (app.isdft == 1){
                ret = app;
                break;
            }
        }

        return ret;
    }
}
