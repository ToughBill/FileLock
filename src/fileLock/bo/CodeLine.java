package fileLock.bo;

import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.diff.impl.incrementalMerge.Change;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.*;
import fileLock.config.CurrentAction;
import fileLock.config.Utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by lbin on 8/1/2016.
 */
public class CodeLine {
    private CodeLineBean m_codeLineBean;

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

    public CodeLine(CodeLineBean codeLineBean){
        m_codeLineBean = codeLineBean;
    }

    public String toString(){
        return m_codeLineBean.proPath + "   " + m_codeLineBean.createDate;
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
        CodeLine ret;
        if(m_lstCodeLines == null){
            m_lstCodeLines = new HashMap<>();
        }
        String projectPath = CurrentAction.getProjectPath();
        if(m_lstCodeLines.keySet().contains(projectPath)){
            ret = m_lstCodeLines.get(projectPath);
        } else {
            String folderCreateTime = getFolderCreatTime(projectPath);
            CodeLineBean codeLineBean = Configuration.getInstance().getCodeLineBean(projectPath, folderCreateTime);
            if (codeLineBean == null){
                codeLineBean = Configuration.getInstance().createNewCodeLine(projectPath, folderCreateTime);
            }
            ret = new CodeLine(codeLineBean);
            m_lstCodeLines.put(projectPath, ret);
        }
//        if(m_curCodeLine == null){
//            String projectPath = ProjectManager.getInstance().getOpenProjects()[0].getBasePath();
//            String folderCreateTime = getFolderCreatTime(projectPath);
//            CodeLineBean codeLineBean = Configuration.getInstance().getCodeLineBean(projectPath, folderCreateTime);
//            if (codeLineBean == null){
//                codeLineBean = Configuration.getInstance().createNewCodeLine(projectPath, folderCreateTime);
//            }
//            m_curCodeLine = new CodeLine(codeLineBean);
//        }

        return ret;
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