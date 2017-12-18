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
import fileLock.bo.Configuration;
import fileLock.config.CurrentAction;
import fileLock.ui.SettingsForm;

import java.io.File;

public class Settings extends AnAction {
    public Settings(){
        super("Settings");
    }
    static {
        Configuration.initSettings();
    }
    @Override
    public void actionPerformed(AnActionEvent event){
        SettingsForm settingsFrm = new SettingsForm();
        settingsFrm.setVisible(true);
    }


}
