package fileLock.bo;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import fileLock.config.*;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lbin on 8/1/2016.
 */
public class CodeLine {
    private CodeLineBean m_codeLineBean;
    private FileMapping m_fileMap;

    public int getCodeLineNo(){
        return m_codeLineBean.codeLineNo;
    }
    public String getRepoPath(){
        return m_codeLineBean.repoPath;
    }
    public String getProjectPath(){
        return  m_codeLineBean.proPath;
    }
    public boolean getIsUnderSvn(){ return m_codeLineBean.isUnderSvn; }

    public CodeLine(int codeLineNo){
        String clsFolder = Utils.getCodeLineFolder();
        String clJsonPath = Paths.get(clsFolder, String.valueOf(codeLineNo), Utils.CodeLineBeanFileName).toString();
        try{
            File file = new File(clJsonPath);
            if(file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String temp, objStr = "";
                while ((temp = br.readLine()) != null) {
                    objStr += temp;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CodeLineBean>() {}.getType();
                m_codeLineBean = gson.fromJson(objStr, type);
                initFileMap();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public CodeLine(CodeLineBean codeLineBean){
        m_codeLineBean = codeLineBean;
        initFileMap();
    }
    public void initFileMap(){
        if(m_codeLineBean.isUnderSvn){
            m_fileMap = new FileMapping(CurrentAction.getProjectPath());
        }
    }

    public String toString(){
        return m_codeLineBean.proPath + "   " + m_codeLineBean.createDate;
    }

    public FileMapping getFileMap(){
        return m_fileMap;
    }

    public List<ChangeList> getAllChangeList(){
        List<ChangeList>  ret = null;

        String repoPath = getRepoPath();
        try{
            File dir = new File(repoPath);
            FileTypeFilter filter = new FileTypeFilter(Utils.JSON_Suffix);
            String[] files = dir.list(filter);
            ret = new ArrayList<>();
            for (String str : files) {
                ChangeList cl = new ChangeList();
                cl.getByKey(Integer.valueOf(getFileNameWithoutExtension(str)));
                ret.add(cl);
            }

        }catch (Exception e){
            e.printStackTrace();
            ret = null;
        }

        return ret;
    }

    private String getFileNameWithoutExtension(String fileName){
        String temp = Paths.get(fileName).getFileName().toString();
        return temp.substring(0, temp.length() - Utils.JSON_Suffix.length());
    }

    private static Map<String,CodeLine> m_lstCodeLines;
    public static CodeLine getCurrentCodeLine(){
        CodeLine ret = null;
        if(m_lstCodeLines == null){
            m_lstCodeLines = new HashMap<>();
        }
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
        CodeLineBean clbean;
        int maxNo = 0;
//        Configuration inst = Configuration.getInstance();
//        Iterator<CodeLineBean> iter = inst.m_cfgBean.codeLine.iterator();
//        while (iter.hasNext()){
//            CodeLineBean config = iter.next();
//            if(config.codeLineNo > maxNo)
//                maxNo = config.codeLineNo;
//        }
        int newCodeLineNo = CodeLineManager.getNextCodeLineNo();
        String codeLinePath = Paths.get(Utils.getDataFolderPath(), String.valueOf(newCodeLineNo)).toString();
        File dir = new File(codeLinePath);
        dir.mkdir();
        File dir2 = new File(Paths.get(codeLinePath, Utils.Backup_File).toString());
        dir2.mkdir();

        clbean = new CodeLineBean();
        clbean.codeLineNo = newCodeLineNo;
        clbean.proPath = projectPath;
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        clbean.createDate = sdf.format(dt);
        clbean.repoPath = codeLinePath;
        String mapFile = projectPath + "\\" + FileMapping.FL_FileMappingPath;
        File temp = new File(mapFile);
        clbean.isUnderSvn = temp.exists();
        //inst.addCodeLine(clbean);
        CodeLineManager.addCodeLineEntry(projectPath, newCodeLineNo);

        createDefaultCL(clbean);
        setProjectFilesToReadonly(projectPath);

        CodeLine codeLine = new CodeLine(clbean);
        m_lstCodeLines.put(projectPath, codeLine);

        return codeLine;
    }
    public static boolean createDefaultCL(CodeLineBean lineCfg){
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
        } catch (IOException e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
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

class FileTypeFilter implements FilenameFilter{
    private String m_type;
    public FileTypeFilter(String type){
        m_type = type;
    }
    public boolean accept(File fl, String path) {
        File file = new File(path);
        String filename = file.getName();
        return filename.indexOf(m_type)!=-1;
    }
}