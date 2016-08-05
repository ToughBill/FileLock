package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;

import java.io.File;

/**
 * Created by lbin on 7/25/2016.
 */
public class CheckOut extends AnAction {
    public CheckOut(){
        super("Check out");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        VirtualFile virFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virFile == null) {
            return;
        }
        String path = virFile.getPath();
        File file = new File(path);
        if (!file.canWrite()){
            file.setWritable(true);
            virFile.refresh(false,false);
            if(!ChangeList.CheckoutToDefaultCL(path)){
                file.setWritable(false);
                virFile.refresh(false,false);
            }
        }


//        Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
//        Document doc = editor.getDocument();
//        //doc.setReadOnly(false);
//
//        FileDocumentManager.getInstance().reloadFromDisk(doc);


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
        String str = ("update method: " + (file.canWrite() ? "can write" : "cannot write"));
        System.out.print(str);
        System.out.println();
    }
}
