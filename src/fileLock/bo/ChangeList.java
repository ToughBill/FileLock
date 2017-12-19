package fileLock.bo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fileLock.config.CodeLineManager;
import fileLock.config.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lbin on 7/29/2016.
 */
public class ChangeList {
    private ChangeListBean m_clBean;

    public static final int Default_CL_No = -10;

    public ChangeList(){
    }

    public boolean initNew(){
        m_clBean = new ChangeListBean();
        m_clBean.clNo = Configuration.getInstance().getNextCLNo();

        return true;
    }

    public String toString(){
        return String.format("ID: %d, Description: %s", m_clBean.clNo, m_clBean.desc);
    }

    public String getCLDesc(){
        return m_clBean.desc;
    }
    public void setCLDesc(String desc){
        m_clBean.desc = desc;
    }
    public void setDate(long date_){
        m_clBean.createDate = date_;
    }
    public void setCodeLine(int codeLine_){
        m_clBean.codeLine = codeLine_;
    }
    public List<String> getFiles(){
        return m_clBean.files;
    }
    public int getCLNo(){
        return m_clBean.clNo;
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
        CodeLine codeLine = CodeLineManager.getCurrentCodeLine();
        return Paths.get(codeLine.getRepoPath(), Utils.ChangeListsFolder, String.valueOf(clNo), Utils.ChangeListBeanFileName).toString();
    }
    public long getTime(){
        return m_clBean.createDate;
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
        if(m_clBean.files.contains(file)){
            return  ret;
        }
        m_clBean.files.add(file);
        //if(!CodeLineManager.getCurrentCodeLine().getIsUnderSvn()){
            String backupFolder = Paths.get(CodeLineManager.getCurrentCodeLine().getRepoPath(), Utils.ChangeListsFolder, String.valueOf(m_clBean.clNo), Utils.BackupFilesFolder).toString();
            ret = Utils.copyFile(file, Paths.get(backupFolder,
                String.valueOf(m_clBean.clNo) + "_" + Paths.get(file).getFileName().toString()).toString(),
                true);
            //save();
        //}

        return ret;
    }

    public boolean addFile(ChangeList oriCL, String filePath){
        boolean ret = true;
        if(!oriCL.getFiles().contains(filePath)){
            return ret;
        }

        String backupFolder = Paths.get(CodeLineManager.getCurrentCodeLine().getRepoPath(), Utils.BackupFilesFolder).toString();
        String oriBackFile = Paths.get(backupFolder,
                String.valueOf(oriCL.getCLNo()) + "_" + Paths.get(filePath).getFileName().toString()).toString();
        String newBackFile = Paths.get(backupFolder,
                String.valueOf(m_clBean.clNo) + "_" + Paths.get(filePath).getFileName().toString()).toString();
        Utils.copyFile(oriBackFile, newBackFile, true);
        oriCL.getFiles().remove(filePath);
        oriCL.save();
        m_clBean.files.add(filePath);
        save();

        return ret;
    }

    public boolean revertFile(String file){
        boolean ret = true;

        if(m_clBean.files.contains(file)){
            m_clBean.files.remove(file);
            String oriFilePath = "";
            if(CodeLineManager.getCurrentCodeLine().getIsUnderSvn()){
                oriFilePath = CodeLineManager.getCurrentCodeLine().getFileMap().getSourcePath(file);
            } else {
                File temp = new File(file);
                String fileName = temp.getName();
                String backupFolder = Paths.get(CodeLineManager.getCurrentCodeLine().getRepoPath(), Utils.BackupFilesFolder).toString();
                oriFilePath = Paths.get(backupFolder, String.valueOf(m_clBean.clNo) + "_" + fileName).toString();
            }
            if(Utils.copyFile(oriFilePath, file, true)){
                //if(!CodeLineManager.getCurrentCodeLine().getIsUnderSvn()){
                    File backupFile = new File(oriFilePath);
                    backupFile.delete();
                //}
            }
        }

        return ret;
    }

    public static boolean createDefaultCL(CodeLineBean lineCfg){
        boolean ret = true;

        String clPath = Paths.get(lineCfg.repoPath, Utils.ChangeListsFolder, String.valueOf(ChangeList.Default_CL_No)).toString();
        try{
            File file = new File(clPath);
            if (!file.exists()){
                // create changelist(%d) folder
                file.mkdir();

                // create changelist.json file
                String clFilePath = Paths.get(clPath, Utils.ChangeListBeanFileName).toString();
                File temp = new File(clFilePath);
                temp.createNewFile();
                String txt = String.format(Utils.CL_Template, ChangeList.Default_CL_No,
                        Calendar.getInstance().getTimeInMillis(), lineCfg.codeLineNo, "default");
                Utils.writeFile(clFilePath, txt);

                // create backupFile folder
                temp = new File(Paths.get(clPath, Utils.BackupFilesFolder).toString());
                temp.mkdir();

                // create revisions folder
                temp = new File(Paths.get(clPath, Utils.RevisionsFolder).toString());
                temp.mkdir();
            }
        } catch (IOException e){
            e.printStackTrace();
            ret = false;
        }

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

    public static ChangeList findChangeList(String filePath){
        ChangeList ret = null;
        List<ChangeList> cls = CodeLineManager.getCurrentCodeLine().getAllChangeList();
        boolean found = false;
        for(ChangeList cl : cls){
            List<String> files = cl.getFiles();
            for(String str : files){
                if (str.equals(filePath)){
                    found = true;
                    ret = cl;
                    break;
                }
            }
            if (found)
                break;
        }

        return  ret;
    }
}
