package fileLock.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;
import fileLock.bo.CodeLineBean;
//import io.grpc.Status;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

public class CodeLineManager {
    private CodeLineConfigurationBean m_configBean;
    private CodeLineManager(){
        initBean();
    }
    private void initBean(){
        try{
            String path = Utils.getCodeLineFolder();
            File file = new File(Paths.get(path, Utils.CodeLineEntriesFileName).toString());
            if(file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String temp, objStr = "";
                while ((temp = br.readLine()) != null) {
                    objStr += temp;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CodeLineConfigurationBean>() {}.getType();
                m_configBean = gson.fromJson(objStr, type);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int _getNextCodeLineNo(){
        return m_configBean.nextCodeLineNo;
    }
    public CodeLineEntry _getCodeLineEntry(String projPath){
        CodeLineEntry entry = null;
        for(int i = 0; i < m_configBean.codeLineEntries.size(); i++){
            CodeLineEntry temp =m_configBean.codeLineEntries.get(i);
            if(temp.projPath.equals(projPath)){
                entry = temp;
                break;
            }
        }
        return entry;
    }
    public boolean _addCodeLineEntry(String projPath, int codeLineNo){
        CodeLineEntry entry = new CodeLineEntry();
        entry.projPath = projPath;
        entry.codelineNo = codeLineNo;
        m_configBean.codeLineEntries.add(entry);
        m_configBean.nextCodeLineNo++;
        return save();
    }
    public boolean save(){
        boolean ret = true;
        Gson gson = new Gson();
        String json = gson.toJson(m_configBean);
        String configPath = Utils.getCodeLineFolder();
        File file = new File(Paths.get(configPath, Utils.CodeLineEntriesFileName).toString());
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

    private static CodeLineManager m_manager;
    private static CodeLineManager getCodeLineManager(){
        if(m_manager == null){
            m_manager = new CodeLineManager();
        }
        return m_manager;
    }



    public static CodeLineEntry getCodeLineEntry(String projPath){
        return getCodeLineManager()._getCodeLineEntry(projPath);
    }
    public static int getNextCodeLineNo(){
        return getCodeLineManager()._getNextCodeLineNo();
    }
    public static boolean addCodeLineEntry(String projPath, int codeLineNo){
        return getCodeLineManager()._addCodeLineEntry(projPath, codeLineNo);
    }

    private static Map<String,CodeLine> m_lstCodeLines = new HashMap<>();
    public static CodeLine getCurrentCodeLine(){
        CodeLine ret = null;
        String projectPath = CurrentAction.getProjectPath();
        if(m_lstCodeLines.keySet().contains(projectPath)){
            ret = m_lstCodeLines.get(projectPath);
        } else {
//            CodeLineBean codeLineBean = Configuration.getInstance().getCodeLineBean(projectPath);
//
//            if (codeLineBean == null){
//                ret = createCodeLine(projectPath);
//            } else {
//                ret = new CodeLine(codeLineBean);
//            }

            CodeLineEntry entry = CodeLineManager.getCodeLineEntry(projectPath);
            if(entry == null){
                ret = createCodeLine(projectPath);
            } else {
                ret = new CodeLine(entry.codelineNo);
            }
        }

        return ret;
    }
    public static CodeLine createCodeLine(String projectPath){
        CodeLineBean clbean = null;
        try {
            // create changelists folder
            int newCodeLineNo = CodeLineManager.getNextCodeLineNo();
            String codeLinePath = Utils.encodePath(Paths.get(Utils.getCodeLineFolder(), String.valueOf(newCodeLineNo)).toString());
            File dir = new File(codeLinePath);
            dir.mkdir();
            File dir2 = new File(Paths.get(codeLinePath, Utils.ChangeListsFolder).toString());
            dir2.mkdir();

            long curTimestamp = Calendar.getInstance().getTimeInMillis();
            clbean = new CodeLineBean();
            clbean.codeLineNo = newCodeLineNo;
            clbean.proPath = projectPath;
            clbean.createDate = curTimestamp;
            clbean.repoPath = codeLinePath;
            String mapFile = projectPath + "\\" + FileMapping.FL_FileMappingPath;
            File temp = new File(mapFile);
            clbean.isUnderSvn = temp.exists();

            // init codeline.json
            String configFilePath = Paths.get(codeLinePath, Utils.CodeLineBeanFileName).toString();
            File codelineCongifFile = new File(configFilePath);
            codelineCongifFile.createNewFile();
            String strTemplate = String.format(Utils.CodeLineFileTemplate, newCodeLineNo, curTimestamp, projectPath, codeLinePath, 1, false, "default");
            Utils.writeFile(configFilePath, strTemplate);

            // add new codeline entry to codelines.json and update nextCodeLineNo
            CodeLineManager.addCodeLineEntry(projectPath, newCodeLineNo);

            // create default changelist
            ChangeList.createDefaultCL(clbean);
            setProjectFilesToReadonly(projectPath);
        } catch (IOException e){
            e.printStackTrace();
        }

        CodeLine codeLine = new CodeLine(clbean);
        m_lstCodeLines.put(projectPath, codeLine);

        return codeLine;
    }

    private static void setProjectFilesToReadonly(String projectPath){
        try{
            Process p = Runtime.getRuntime().exec("cmd /C cd \"" + projectPath + "\" && attrib +R * /S /D");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    private static String getFolderCreatTime(String folderPath){
        String result = null;
        try {
            String folderName = Paths.get(folderPath).getFileName().toString();
            Process p = Runtime.getRuntime().exec("cmd /C dir \"" + Paths.get(folderPath).getParent().toString() + "\" /tc");
            System.out.print("cmd /C dir \"" + folderPath + "\" /tc");
            System.out.println();
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String getTime = null, temp;
            while ((temp = br.readLine()) != null) {
                String[] str = temp.split(" ");
                for (int i = str.length - 1; i >= 0; i--) {
                    if (str[i].equals(folderName)) {
                        getTime = str[0] + " " + str[2];
                    }
                }
            }
            result = getTime;
        } catch (java.io.IOException exc) {
            exc.printStackTrace();
        }

        return result;
    }

    private static String getFileCreateTime(String filePath){
        String result = null;
        try{
            BasicFileAttributes attr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
            result = attr.creationTime().toString();
        }catch (IOException e){

        }

        return result;
    }
}

