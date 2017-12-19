package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;
import fileLock.config.CodeLineManager;
import fileLock.config.CurrentAction;
import fileLock.config.Utils;

import java.io.File;

/**
 * Created by lbin on 7/26/2016.
 */
public class MergeChangesToSVNSource extends AnAction {
    public MergeChangesToSVNSource(){
        super("MergeChangesToSVNSource");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        CurrentAction.setActionEvent(event);
        String path = virFile.getPath();
        ChangeList cl = ChangeList.findChangeList(path);
        if (cl == null)
            return;
        Utils.MergeChangesToSvnSource(path);
    }

    @Override
    public void update(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (event == null || virFile == null) {
            return;
        }
        CurrentAction.setActionEvent(event);
        event.getPresentation().setEnabled(CodeLineManager.getCurrentCodeLine().getIsUnderSvn());
    }
}
