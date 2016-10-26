package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fileLock.config.CurrentAction;
import fileLock.ui.ViewAllChangeListForm;

/**
 * Created by lbin on 8/4/2016.
 */
public class ViewAllChangeList extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event){
        CurrentAction.setActionEvent(event);
        ViewAllChangeListForm frm = new ViewAllChangeListForm();
        frm.setFromAction(event);
        frm.setVisible(true);
    }
}
