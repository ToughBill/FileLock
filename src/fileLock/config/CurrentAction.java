package fileLock.config;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by lbin on 10/25/2016.
 */
public class CurrentAction {

    private CurrentAction(AnActionEvent actEv){}

    private static AnActionEvent m_actEv;
    private static TypedActionHandler m_oriTypedHandler;
    private static CurrentAction m_inst;
    public static CurrentAction getInstance(){
        return m_inst;
    }
    public static void setActionEvent(AnActionEvent actEv){
        m_actEv = actEv;
    }
    public static void setOriginalTypedHandler(TypedActionHandler handler){
        m_oriTypedHandler = handler;
    }
    public static TypedActionHandler getOriginalTypedHandler(){
        return m_oriTypedHandler;
    }

    public static String getProjectPath(){
        if(m_actEv == null) {
            System.out.print("action is null!");
            return null;
        }
        Project project = m_actEv.getData(CommonDataKeys.PROJECT);
        return project.getBasePath();
    }
}
