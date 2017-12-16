package fileLock.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

public class CodeLineManager {
    private CodeLineConfigurationBean m_configBean;
    private CodeLineManager(){
        initBean();
    }
    private void initBean(){
        try{
            String path = Utils.getCodeLineFolder();
            File file = new File(path);
            if(file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String temp, objStr = "";
                while ((temp = br.readLine()) != null) {
                    objStr += temp;
                }
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CodeLineConfigurationBean>() {}.getType();
                m_configBean = gson.fromJson(objStr, type);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int _getNextCodeLineNo(){
        return m_configBean.nextCodeLineNo;
    }
    public CodeLineEntry _getCodeLineEntry(String projPath){
        CodeLineEntry entry = null;
        for(int i = 0; i < m_configBean.codeLineEntries.size(); i++){
            CodeLineEntry temp =m_configBean.codeLineEntries.get(i);
            if(temp.projPath.equals(projPath)){
                entry = temp;
                break;
            }
        }
        return entry;
    }
    public boolean _addCodeLineEntry(String projPath, int codeLineNo){
        CodeLineEntry entry = new CodeLineEntry();
        entry.projPath = projPath;
        entry.codelineNo = codeLineNo;
        m_configBean.codeLineEntries.add(entry);
        m_configBean.nextCodeLineNo++;
        return save();
    }
    public boolean save(){
        boolean ret = true;
        Gson gson = new Gson();
        String json = gson.toJson(m_configBean);
        String configPath = Utils.getCodeLineFolder();
        File file = new File(Paths.get(configPath, Utils.CodeLineEntriesFileName).toString());
        try{
            FileOutputStream out = new FileOutputStream(file,false);
            out.write(json.getBytes());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    private static CodeLineManager m_manager;
    private static CodeLineManager getCodeLineManager(){
        if(m_manager == null){
            m_manager = new CodeLineManager();
        }
        return m_manager;
    }

    public static List<CodeLineEntry> getAllCodeLineEntries(){
        return getCodeLineManager().m_configBean.codeLineEntries;
    }
    public static CodeLineEntry getCodeLineEntry(String projPath){
        return getCodeLineManager()._getCodeLineEntry(projPath);
    }
    public static int getNextCodeLineNo(){
        return getCodeLineManager()._getNextCodeLineNo();
    }
    public static boolean addCodeLineEntry(String projPath, int codeLineNo){
        return getCodeLineManager()._addCodeLineEntry(projPath, codeLineNo);
    }
}

