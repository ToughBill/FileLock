package fileLock.config;

import com.intellij.openapi.project.ProjectManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by lbin on 9/26/2016.
 */
public class FileMapping {
    public static final String FL_FileMappingPath = "FL_FileMapping";

    private String m_projectPath;
    private Map<String, List> m_map;
    private String m_svnTrunkPath;
    public FileMapping(String projectPath_){
        m_projectPath = projectPath_;
        m_map = new HashMap<String, List>();
        initMap();
    }

    private void initMap(){
        //File file = new File(m_mappingFile);
        BufferedReader reader = null;
        try{
            //String projectPath = ProjectManager.getInstance().getOpenProjects()[0].getBasePath();
            //String projectPath = CurrentAction.getProjectPath();
            String mapFilePath = m_projectPath + "\\" + FileMapping.FL_FileMappingPath;
            File mapFile = new File(mapFilePath);
            if(!mapFile.exists()){
                return;
            }
            System.out.print("initMap: mapFilePath = " + mapFilePath);
            reader = new BufferedReader(new FileReader(mapFilePath));
            String temp = null;
            while ((temp = reader.readLine()) != null){
                int idx = temp.indexOf('>');
                if (idx >= 0){
                    String str1 = temp.substring(0, idx);
                    String str2 = temp.substring(idx + 1, temp.length());
                    System.out.print("initMap: str1 = " + str1);
                    System.out.print("initMap: str2 = " + str2);
                    File tempObj = new File(str1);
                    if (m_map.containsKey(str2)){
                        m_map.get(str2).add(new PathInfo(str1, tempObj.isFile()));
                    }
                    else {
                        List<PathInfo> list = new ArrayList<>();
                        list.add(new PathInfo(str1, tempObj.isFile()));
                        m_map.put(str2, list);
                    }
                }
                else {
                    idx = temp.indexOf('<');
                    if (idx >= 0){
                        String str1 = temp.substring(0, idx);
                        String str2 = temp.substring(idx + 1, temp.length());
                        if (str1.equals("SVN_Path")){
                            m_svnTrunkPath = str2;
                        }
                    }
                }
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e1){}
            }
        }

    }

    public String getSourcePath(String filePath_){
        String ret = null;
        File file = new File(filePath_);
        String fileName = file.getName();
        String folder = file.getParent();
//        while (!m_map.containsKey(folder)){
//            File tempObj = new File(folder);
//            folder = tempObj.getParent();
//        }
        Set<String> keys = m_map.keySet();
        List<PathInfo> list;
        File tempObj;
        String iterFolder = folder;
        boolean isFound = false;
        String[] arrKeys = keys.toArray(new String[0]);
        for(int j = arrKeys.length - 1; j >= 0; j--){
            isFound = false;
            iterFolder = folder;
            if(!iterFolder.contains(arrKeys[j])){
                continue;
            }
//            if (iterFolder==null)
//                continue;

            list = m_map.get(arrKeys[j]);
            for (int i = list.size() - 1; i >= 0 ; i--){
                PathInfo pi = list.get(i);
                if (pi.IsFile){
                    if (pi.Name.equals(fileName)){
                        ret = pi.Path;
                        isFound = true;
                        break;
                    }
                }
                else{
                    String tempStr = pi.Path + "\\" + filePath_.substring(arrKeys[j].length() + 1);
                    tempObj = new File(tempStr);
                    if (tempObj.exists() && tempObj.isFile()){
                        isFound = true;
                        ret = tempStr;
                        break;
                    }
                }
            }
            if (isFound){
                ret = ret.replace('/','\\');
                break;
            }
        }

        return ret;
    }

    public String getSVNTrunkPath(){
        return m_svnTrunkPath;
    }

    private static FileMapping m_inst;
    public static FileMapping getInstance(){
        if(m_inst == null){
            m_inst = new FileMapping(FileMapping.FL_FileMappingPath);
        }

        return m_inst;
    }
}

class PathInfo{
    public String Path;
    public String Name;
    public boolean IsFile;

    public PathInfo(String path_, boolean isFile_){
        Path = path_;
        IsFile = isFile_;
        File temp = new File(path_);
        Name = temp.getName();
    }
}