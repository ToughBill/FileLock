package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.ui.*;

import java.io.File;

/**
 * Created by lbin on 7/29/2016.
 */
public class CheckOutTo extends AnAction {
    public CheckOutTo(){
        super("CheckOutTo");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        //String path = virFile.getPath();
        CheckOutToForm chk = new CheckOutToForm(virFile);
        chk.setVisible(true);
    }

    @Override
    public void update(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        String path = virFile.getPath();
        File file = new File(path);
        event.getPresentation().setEnabled(!file.canWrite());
    }
}
