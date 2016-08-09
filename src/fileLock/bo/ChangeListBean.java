package fileLock.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbin on 8/2/2016.
 */
public class ChangeListBean {
    public int clNo;
    public long createDate;
    public int codeLine;
    public List<String> files;
    public String desc;
    public int shelvedCL;

    public ChangeListBean(){
        desc = "";
        files = new ArrayList<String>();
    }
}
