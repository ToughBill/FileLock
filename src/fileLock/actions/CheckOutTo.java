package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fileLock.ui.*;

/**
 * Created by lbin on 7/29/2016.
 */
public class CheckOutTo extends AnAction {
    public CheckOutTo(){
        super("CheckOutTo");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        CheckOutToForm chk = new CheckOutToForm();
        chk.setVisible(true);
    }
}
