import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileController {
	String srcVersion;
	String dstVersion;
	
	
	public FileController() {
		srcVersion = "";
		dstVersion = "";
	}
	
	public FileController(String srcVersion, String dstVersion) {
		this.srcVersion = srcVersion;
		this.dstVersion = dstVersion;
	}
	
	public void copyDir(String srcPath) {
		//System.out.println(srcPath);
		
		String dstPath = srcPath.replaceFirst(srcVersion, dstVersion);
		if(srcVersion.equals("jp_1_0") || srcVersion.equals("jp_1_1")) {
			srcPath = srcPath.replaceFirst("res", "res\\\\extend\\\\res");
		}
		if(dstVersion.equals("jp_1_0") || dstVersion.equals("jp_1_1")) {
			dstPath = dstPath.replaceFirst("res", "res\\\\extend\\\\res");
		}
				
		//System.out.println(srcPath + "  " + dstPath);
		File srcFile = new File(srcPath);
		File dstFile = new File(dstPath);
		if(!srcFile.isDirectory()) {
			srcPath = srcPath.replaceFirst("res\\\\extend\\\\res", "res");
			copyFile(srcPath);
			return;
		}
		
		if(!dstFile.exists() && srcFile.isDirectory()) {
			//System.err.println(srcFile);
			dstFile.mkdir();
		}
		String[] fileList = srcFile.list();
		if(fileList == null || fileList.length == 0) {
			return;
		}
		for(String tempFilePath : fileList) {
			//System.out.println(tempFilePath);
			if(new File(srcPath + File.separator + tempFilePath).isDirectory()) {
				//System.out.println(srcPath + File.separator + tempFile + " is a dictionary");
				srcPath = srcPath.replaceFirst("res\\\\extend\\\\res", "res");
				copyDir(tempFilePath);
			} else {
				//System.out.println(srcPath + File.separator + tempFile + " is not a dictionary");
				srcPath = srcPath.replaceFirst("res\\\\extend\\\\res", "res");
				copyFile(srcPath + File.separator + tempFilePath);
			}
		}
	}
	
	public void copyFile(String srcPath) {
		
		//System.out.println(srcPath);
		
		String dstPath = srcPath.replaceFirst(srcVersion, dstVersion);
		if(srcVersion.equals("jp_1_0") || srcVersion.equals("jp_1_1")) {
			if(!srcPath.contains("05_state")) {
				srcPath = srcPath.replaceFirst("res", "res\\\\extend\\\\res");
			}
		}
		if(dstVersion.equals("jp_1_0") || dstVersion.equals("jp_1_1")) {
			if(!dstPath.contains("05_state")) {
				dstPath = dstPath.replaceFirst("res", "res\\\\extend\\\\res");
			}
		}
						
		//System.out.println(srcPath + "  " + dstPath);
		
		File dstFile = new File(dstPath);
		if(dstFile.exists()) {
			return;
		}
		try {
			FileInputStream inFile = new FileInputStream(srcPath);
			FileOutputStream outFile = new FileOutputStream(dstPath);
			
			byte datas[] = new byte[1024*8];
			int len = 0;
			while((len = inFile.read(datas)) != 1) {
				outFile.write(datas, 0, len);
			}
			inFile.close();
			outFile.close();
			
			System.out.println(dstPath + ": copy done!");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
