package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;
import fileLock.config.Utils;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by lbin on 7/26/2016.
 */
public class Revert extends AnAction {
    public Revert(){
        super("Revert");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        Document doc = editor.getDocument();
        if (FileDocumentManager.getInstance().isDocumentUnsaved(doc)){
            String str = "File has been changed, continue to revert will lose the change. Do you want to continue?";
            int ret = Messages.showDialog(str, "File Lock", new String[]{"Yes","No"}, -1, null);
            System.out.print("ret: ");
            System.out.print(ret);
            System.out.println();
            if (ret == 1)
                return;
        }

        String path = virFile.getPath();
        ChangeList cl = ChangeList.findChangeList(path);
        if (cl == null)
            return;

//        File file = new File(path);
//        String fileName = file.getName();
//        String backupFolder = Paths.get(CodeLine.getCurrentCodeLine().getRepoPath(), Utils.Backup_File).toString();
//        String backupFilePath = Paths.get(backupFolder, String.valueOf(cl.getCLNo()) + "_" + fileName).toString();
//        if(Utils.copyFile(backupFilePath, path, true)){
            //file.setReadOnly();
            cl.revertFile(path);
            cl.save();
//            File backupFile = new File(backupFilePath);
//            backupFile.delete();

            virFile.refresh(false,false);
        //}
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
