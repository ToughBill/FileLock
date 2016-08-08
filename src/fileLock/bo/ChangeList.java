package fileLock.bo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fileLock.config.Utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by lbin on 7/29/2016.
 */
public class ChangeList {
    private ChangeListBean m_clBean;

    public static final int Default_CL_No = -10;

    public ChangeList(){
    }

    public String toString(){
        return String.format("ID: %d, Description: %s", m_clBean.clNo, m_clBean.desc);
    }

    public String getCLDesc(){
        return m_clBean.desc;
    }
    public List<String> getFiles(){
        return m_clBean.files;
    }

    public boolean save(){
        boolean ret = true;

        String clFilePath = getCLPath();
        String str = Utils.objectToString(m_clBean);
        ret = Utils.writeFile(clFilePath, str);

        return ret;
    }

    public String getCLPath(){
        return getCLPath(m_clBean.clNo);
    }
    public String getCLPath(int clNo){
        CodeLine codeLine = CodeLine.getCurrentCodeLine();
        return Paths.get(codeLine.getRepoPath(), String.valueOf(clNo) + Utils.JSON_Suffix).toString();
    }

    public boolean getByKey(int clNo){
        boolean ret = true;
        String path = getCLPath(clNo);
        try{
            String str = Utils.readFile(path);
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<ChangeListBean>() {}.getType();
            m_clBean = gson.fromJson(str, type);
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public static int generateCLNo(){
        int ret = -1;
        String datFol = Utils.getDataFolderPath();
        File folder = new File(datFol);
        String[] files = folder.list();
        if (files.length <= 0){

        }

        return ret;
    }

    public boolean checkoutFile(String file){
        boolean ret = true;

        m_clBean.files.add(file);
        //save();
        return ret;
    }

    public static boolean CheckoutToDefaultCL(String file){
        boolean ret = true;

        ChangeList cl = new ChangeList();
        cl.getByKey(ChangeList.Default_CL_No);
        if(cl.checkoutFile(file)){
            ret = cl.save();
        }else {
            ret = false;
        }

        return ret;
    }
}
