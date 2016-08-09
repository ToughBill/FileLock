package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;
import fileLock.bo.Configuration;
import fileLock.config.Utils;

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

        String fileName = Paths.get(path).getFileName().toString();
        String targetFileName = String.valueOf(cl.getCLNo()) + "_" + fileName;
        String targetFilePath = Paths.get(CodeLine.getCurrentCodeLine().getRepoPath(), Utils.Backup_File, targetFileName).toString();
        //Configuration.getInstance().m_cfgBean.compApp
    }
}
