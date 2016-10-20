package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;
import fileLock.bo.CompAppBean;
import fileLock.bo.Configuration;
import fileLock.config.FileMapping;
import fileLock.config.Utils;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by lbin on 7/26/2016.
 */
public class Diff extends AnAction {
    public  Diff(){
        super("Diff");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        String path = virFile.getPath();
        ChangeList cl = ChangeList.findChangeList(path);
        if (cl == null)
            return;
        Utils.DiffFile(path);
//        String fileName = Paths.get(path).getFileName().toString();
//        String targetFilePath;
//        if(CodeLine.getCurrentCodeLine().getIsUnderSvn()){
//            targetFilePath = FileMapping.getInstance().getSourcePath(fileName);
//        } else{
//            String targetFileName = String.valueOf(cl.getCLNo()) + "_" + fileName;
//            targetFilePath = Paths.get(CodeLine.getCurrentCodeLine().getRepoPath(), Utils.Backup_File, targetFileName).toString();
//        }
//
//        CompAppBean appPath = Configuration.getInstance().getDefaultCompApp();
//        startCompare(appPath.path, path, targetFilePath);
    }

    private void startCompare(String appPath, String file1, String file2){
        try{
            Runtime run = Runtime.getRuntime();
            String args = appPath + " \"" + file1 + "\" \"" + file2 + "\" /lefttitle=\"base\" /righttitle=\"workspace\"";
            Process p = run.exec(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        String path = virFile.getPath();
        File file = new File(path);
        event.getPresentation().setEnabled(file.canWrite());
    }
}
