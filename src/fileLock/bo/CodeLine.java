package fileLock.bo;

import com.intellij.openapi.diff.impl.incrementalMerge.Change;
import com.intellij.openapi.project.ProjectManager;
import fileLock.config.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    public CodeLine(CodeLineBean codeLineBean){
        m_codeLineBean = codeLineBean;
    }

    public List<ChangeList> getAllChangeList(){
        List<ChangeList>  ret = null;

        String repoPath = getRepoPath();
        try{
            File dir = new File(repoPath);
            String[] files = dir.list();
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

    private static CodeLine m_curCodeLine;
    public static CodeLine getCurrentCodeLine(){
        if(m_curCodeLine == null){
            String projectPath = ProjectManager.getInstance().getOpenProjects()[0].getBasePath();
            String folderCreateTime = getFolderCreatTime(projectPath);
            CodeLineBean codeLineBean = Configuration.getInstance().getCodeLineBean(projectPath, folderCreateTime);
            if (codeLineBean == null){
                codeLineBean = Configuration.getInstance().createNewCodeLine(projectPath, folderCreateTime);
            }
            m_curCodeLine = new CodeLine(codeLineBean);
        }

        return m_curCodeLine;
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
