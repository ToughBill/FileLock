package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;
import fileLock.config.CurrentAction;
import fileLock.config.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by lbin on 7/26/2016.
 */
public class SetProjectFilesToReadOnly extends AnAction {
    public SetProjectFilesToReadOnly(){
        super("SetProjectFilesToReadOnly");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        CurrentAction.setActionEvent(event);
        //String path = virFile.getPath();
        try{
            String projectPath = CurrentAction.getProjectPath();
            Process p = Runtime.getRuntime().exec("cmd /C cd \"" + projectPath + "\" && attrib +R * /S /D");

        }
        catch (IOException ex){
            ex.printStackTrace();
        }

    }

//    @Override
//    public void update(AnActionEvent event){
//        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
//        if (virFile == null) {
//            return;
//        }
//        String path = virFile.getPath();
//        File file = new File(path);
//        event.getPresentation().setEnabled(file.canWrite());
//    }
}
