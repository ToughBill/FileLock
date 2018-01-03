package fileLock.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import fileLock.bo.Configuration;
import fileLock.ui.SettingsForm;

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
