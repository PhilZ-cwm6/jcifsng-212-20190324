package com.test.jcifs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;











import jcifsng212.CIFSContext;
import jcifsng212.config.PropertyConfiguration;
import jcifsng212.context.BaseContext;
import jcifsng212.smb.NtlmPasswordAuthentication;
import jcifsng212.smb.SmbException;
import jcifsng212.smb.SmbFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestJcifsNg {
	@SuppressWarnings("deprecation")
	private static Logger log = LoggerFactory.getLogger(TestJcifsNg.class);
	public static void main( String argv[] ) throws Exception {
//		log.setLogOption(true, true, true, true, true);
		
 
		String a = "テスト";
		
//		log.trace(a); // 出力なし
//        log.debug(a); //2016/07/21 23:19:45.191 [main] DEBUG  テスト
//        log.info(a); //2016/07/21 23:19:45.192 [main] INFO   テスト
//        log.warn(a); //2016/07/21 23:19:45.192 [main] WARN   テスト
//        log.error(a); //2016/07/21 23:19:45.192 [main] ERROR  テスト
		
		Properties prop=new Properties();
		
		prop.setProperty("jcifs.smb.client.ipcSigningEnforced", "false");
		prop.setProperty("jcifs.smb.client.forceExtendedSecurity","false");
		
		prop.setProperty("jcifs.smb.client.minVersion", "SMB1");
		prop.setProperty("jcifs.smb.client.maxVersion", "SMB300");

        BaseContext bc = new BaseContext(new PropertyConfiguration(prop));
        NtlmPasswordAuthentication creds = new NtlmPasswordAuthentication(bc, "","android","test");
        CIFSContext ct = bc.withCredentials(creds);

        SmbFile out_file = new SmbFile("smb://192.168.200.132/android/test.docx", ct);

        SmbFile sl = new SmbFile("smb://192.168.200.132/", ct);

        File in_file=new File("e:\\test.docx");
		
        try {
            String[] shares=sl.list();
            for(String item:shares) System.out.println( item );
            
//            String[] files=fl.list();
//            for(String item:files) System.out.println( item );

//            long b_time=System.currentTimeMillis();
//        	InputStream is=new FileInputStream(in_file);
//        	OutputStream os=out_file.getOutputStream();
//        	byte[] buff=new byte[1024*1024*8];
//        	int rc=is.read(buff, 0, buff.length);
//        	while(rc>0) {
//        		os.write(buff,0,rc);
//        		rc=is.read(buff, 0, buff.length);
//        	}
//        	os.flush();
//        	os.close();
//        	is.close();

//	      	SmbFile new_file=new SmbFile("smb://192.168.200.210/sda1/test2.xls", ct);
//	      	new_file.setLastModified(in_file.lastModified());

//        	System.out.println("copy ended, elapsed time="+(System.currentTimeMillis()-b_time)+", size="+out_file.length());
        } catch(SmbException e) {
        	System.out.println( e.getMessage()+", "+String.format("0x%08x", e.getNtStatus() ));
        	e.printStackTrace();
        }

        out_file.close();
    }
}
