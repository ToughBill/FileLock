package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.CodeLine;
import fileLock.config.CurrentAction;
import fileLock.config.FileMapping;
import fileLock.config.Utils;

/**
 * Created by lbin on 9/26/2016.
 */
public class OpenSourcePath extends AnAction {
    public OpenSourcePath(){
        super("OpenSourcePath");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        CurrentAction.setActionEvent(event);
        String path = virFile.getPath();
        String sourcePath = CodeLine.getCurrentCodeLine().getFileMap().getSourcePath(path);
        if (sourcePath != null){
            Utils.ShowInExplorer(sourcePath);
        }
    }
}
