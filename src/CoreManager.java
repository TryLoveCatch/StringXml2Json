import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class CoreManager {

    private String mSrcPath;
    private String mDstPath;
    private Callback mCallback;
    
    public void setCallback(Callback pCallback){
    	this.mCallback = pCallback;
    }
    
    public void startWork(String pSrcPath, String pDstPath){
        
        mSrcPath = pSrcPath;
        mDstPath = pDstPath;
        
        File tFile = new File(mSrcPath);
        
        if(!tFile.exists() && !tFile.isFile()) return;
        if(tFile.isFile()){
            create(tFile);
            return;
        }
    }
    
    private void create(File pFile){
        XmlParse tXmlParse = new XmlParse();
        if(!pFile.exists()) return;
        
        FileInputStream tFis = null;;
        try {
            tFis = new FileInputStream(pFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            tFis = null;
        }
        if(tFis == null) return;
        
        List<InfoDimen> tArrSrc = tXmlParse.parseXml(tFis);
        if(tArrSrc!=null && tArrSrc.size() > 0){
            String tDstName = pFile.getName() + ".json";
            File tFileDst = new File(mDstPath, tDstName);
            try {
//            	tFileDst.deleteOnExit();
            	tFileDst.createNewFile();
                tXmlParse.createJson(tArrSrc, tFileDst.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if(mCallback!=null){
        	mCallback.onComplete();
        }
    }
    
    public interface Callback{
    	public void onComplete();
    }
}
