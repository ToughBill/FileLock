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
        return  m_codeLineBean.projPath;
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
        return m_codeLineBean.projPath + "   " + m_codeLineBean.createDate;
    }

    public FileMapping getFileMap(){
        return m_fileMap;
    }

    public List<ChangeList> getAllChangeList(){
        List<ChangeList>  ret = null;

        String repoPath = getRepoPath();
        try{
            File dir = new File(Paths.get(repoPath, Utils.ChangeListsFolder).toString());
            FolderFilter filter = new FolderFilter();
            String[] files = dir.list(filter);
            ret = new ArrayList<>();
            for (String str : files) {
                ChangeList cl = new ChangeList();
                cl.getByKey(Integer.valueOf(str));
                ret.add(cl);
            }

        }catch (Exception e){
            e.printStackTrace();
            ret = null;
        }

        return ret;
    }

    public int getNextChangeListNo(){
        return m_codeLineBean.nextCLNo;
    }

    public boolean updateNextChangeListNo(){
        m_codeLineBean.nextCLNo++;
        return save();
    }

    public boolean save(){
        boolean ret = true;

        String codeLineFilePath = getCodeLineFilePath();
        String str = Utils.objectToString(m_codeLineBean);
        ret = Utils.writeFile(codeLineFilePath, str);

        return ret;
    }

    private String getCodeLineFilePath(){
        return Paths.get(Utils.getCodeLineFolder(), String.valueOf(m_codeLineBean.codeLineNo), Utils.CodeLineBeanFileName).toString();
    }

    private String getFileNameWithoutExtension(String fileName){
        String temp = Paths.get(fileName).getFileName().toString();
        return temp.substring(0, temp.length() - Utils.JSON_Suffix.length());
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

class FolderFilter implements FilenameFilter{
    public boolean accept(File fl, String path) {
        //File file = new File(path);
        return fl.isDirectory();
    }
}