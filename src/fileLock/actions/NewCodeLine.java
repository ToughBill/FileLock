package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import fileLock.bo.CodeLine;
import fileLock.bo.CodeLineBean;
import fileLock.config.CurrentAction;

public class NewCodeLine extends AnAction {
    public  NewCodeLine(){
        super("NewCodeLine");
    }

    @Override
    public void actionPerformed(AnActionEvent event){
        CurrentAction.setActionEvent(event);
        Project project = event.getData(CommonDataKeys.PROJECT);
        String projPath = project.getBasePath();
        CodeLine.createCodeLine(projPath);
    }
}
