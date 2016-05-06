import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;


public class XmlParse {

    
    public List<InfoDimen> parseXml(InputStream is){
        List<InfoDimen> mSrcList = null;
        XmlPullParser tXmlParser = new KXmlParser();
        try {
            tXmlParser.setInput(is, "utf-8");
            int tEventType = tXmlParser.getEventType();
            InfoDimen tInfo = null;
            while(tEventType!=XmlPullParser.END_DOCUMENT){
                
                String tTagName = tXmlParser.getName();
                
                switch(tEventType){
                case XmlPullParser.START_DOCUMENT:
                    mSrcList = new ArrayList<InfoDimen>();
                    break;
                case XmlPullParser.START_TAG:
                    if("string".equals(tTagName)){
                        tInfo = new InfoDimen();
                        tInfo.name = tXmlParser.getAttributeValue(null, "name");
                        tInfo.value = tXmlParser.nextText();
                        mSrcList.add(tInfo);
                    }else if("string-array".equals(tTagName) || "plurals".equals(tTagName)){
                    	tInfo = new InfoDimen();
                        tInfo.name = tXmlParser.getAttributeValue(null, "name");
                        tInfo.values = new ArrayList<>();
                        if("plurals".equals(tTagName)){
                        	tInfo.isPlurals = true;
                        }
                    }else if("item".equals(tTagName)){
                    	if(tInfo!=null && tInfo.values!=null){
                    		tInfo.values.add(tXmlParser.nextText());
                    	}
                    }
                    break;
                case XmlPullParser.END_TAG:
//                    if("dimen".equals(tTagName)){
//                        mSrcList.add(tInfo);
//                    }
                	if("string-array".equals(tTagName) || "plurals".equals(tTagName)){
                		if(tInfo!=null && tInfo.values!=null){
                			mSrcList.add(tInfo);
                		}
                    }
                    break;
                }
                
                tEventType = tXmlParser.next();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        	try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
		}
        return mSrcList;
    }
    
    public void createJson(List<InfoDimen> pData, String pPath){
    	File tFile = new File(pPath);
        FileWriter tFos = null;
        try {
            tFos = new FileWriter(tFile);
            StringBuffer tSb = new StringBuffer();
            int i = 0;
            for(InfoDimen tInfo : pData){
            	tSb = new StringBuffer();
            	tSb.append("\"");
            	tSb.append(tInfo.name);
            	tSb.append("\"");
            	tSb.append(": ");
            	
            	
            	if(tInfo.values==null){
            		createString(tInfo, tSb);
            	}else if(tInfo.isPlurals){
            		createStringPlurals(tInfo, tSb);
            	}else{
            		createStringArray(tInfo, tSb);
            	}
            	if(i < pData.size() - 1){
            		tSb.append(",");
            	}
            	
            	tSb.append("\r\n");
            	tFos.write(tSb.toString());
            	tFos.flush();
            	i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }finally {
        	try {
                tFos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
		}
    }
    
    private void createString(InfoDimen tInfo, StringBuffer tSb){
    	tSb.append("\"");
		tSb.append(tInfo.value);
		tSb.append("\"");
    }
    
    private void createStringArray(InfoDimen tInfo, StringBuffer tSb){
    	tSb.append("[");
		tSb.append("\r\n");
		tSb.append("\t");
		for(int i=0;i<tInfo.values.size();i++){
			if(i > 0){
				tSb.append(",");
				tSb.append("\r\n");
				tSb.append("\t");
			}
			tSb.append("\"");
    		tSb.append(tInfo.values.get(i));
    		tSb.append("\"");
		}
		tSb.append("\r\n");
		tSb.append("]");
    }
    
    private void createStringPlurals(InfoDimen tInfo, StringBuffer tSb){
    	tSb.append("{");
		tSb.append("\r\n");
		tSb.append("\t");
		for(int i=0;i<tInfo.values.size();i++){
			
			if(i > 0){
				tSb.append(",");
				tSb.append("\r\n");
				tSb.append("\t");
			}
			tSb.append("\"");
			tSb.append(i);
			tSb.append("\"");
			tSb.append(": ");
			
			tSb.append("\"");
    		tSb.append(tInfo.values.get(i));
    		tSb.append("\"");
		}
		tSb.append("\r\n");
		tSb.append("}");
    }
    
    public void createXml(List<InfoDimen> pData, String pPath){
        XmlSerializer tXmlSerializer = new KXmlSerializer();
        File tFile = new File(pPath);
        FileOutputStream tFos = null;
        try {
            tFos = new FileOutputStream(tFile);
            tXmlSerializer.setOutput(tFos, "utf-8");
            //缩进  
            tXmlSerializer.setFeature(  
                    "http://xmlpull.org/v1/doc/features.html#indent-output",  
                    true);
            tXmlSerializer.startDocument("UTF-8", true);
            tXmlSerializer.startTag(null, "resources");
            for(InfoDimen tInfo : pData){
                tXmlSerializer.startTag(null, "dimen");
                tXmlSerializer.attribute(null, "name", tInfo.name);
                tXmlSerializer.text(tInfo.value);
                tXmlSerializer.endTag(null, "dimen");
            }
            tXmlSerializer.endTag(null, "resources");
            tXmlSerializer.endDocument();
            tXmlSerializer.flush();
            tFos.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                tFos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
    }
}
