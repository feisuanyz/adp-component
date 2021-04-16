/* 
 * All rights Reserved, Designed By www.feisuanyz.com
 * @title     ZipUtility.java   
 * @package   com.feisuanyz.flow.component.zip.utils
 * @version   V1.0 
 * @copyright 2020 www.feisuanyz.com Inc. All rights reserved.
 * 注意：本内容仅限于前海飞算云智软件科技（深圳）有限公司内部传阅，除非适用法律要求或书面同意，禁止外泄以及用于其他的商业目的
 */
package com.feisuanyz.component.zip.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

/**   
 * <p>
 *    压缩、解压文件工具类
 * </p>
 * @author 飞算云智
 * @date 2020-06-19 18:13
 */
public final class ZipUtility {
	
	private ZipUtility() {
	}
	
	/** 
	 * <p>
	 * 	 压缩文件
	 * </p>
	 * @param srcDirName 要压缩的目录路径
	 * @param destZipName 压缩后的文件全路径
	 * @param isDel 压缩后是否删除 srcPathName
	 * @return Boolean    返回类型
	 */
	public static Boolean zip(String srcDirName, String destZipName, Boolean isDel) {
		File srcdir = new File(srcDirName);
		if (!srcdir.exists()) {
			throw new IllegalArgumentException(String.format("【zip file %s not found.】", srcDirName));
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(new File(destZipName));
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		if (srcdir.isFile()) {
			fileSet.setFile(srcdir);
		} else {
			fileSet.setDir(srcdir);
		}
		zip.addFileset(fileSet);
		zip.execute();
		if (isDel) {
			Delete delete = new Delete();
			delete.setProject(prj);
			// 可同时将子目录及所有文件删除
			delete.setDir(new File(srcDirName)); 
			delete.execute();
		}
		return Boolean.TRUE;
	}

	/** 
	 * <p>
	 * 	 解压缩文件
	 * </p>
	 * @param sourceZipName 要解压的压缩文件全路径
	 * @param destDirName 压缩后的目录全路径
	 * @return Boolean    返回类型
	 */
	public static Boolean unZip(String sourceZipName, String destDirName) {
		Project prj = new Project();
		Expand expand = new Expand();
		expand.setProject(prj);
		expand.setSrc(new File(sourceZipName));
		// 是否覆盖
		expand.setOverwrite(true);
		File f = new File(destDirName);
		if (!f.exists()) {
			f.mkdir();
		}
		expand.setDest(f);
		expand.execute();
		return Boolean.TRUE;
	}

	/** 
	 * <p>
	 * 	 删除zip文件,可同时将子目录及所有文件删除
	 * </p>
	 * @param fileFullPath
	 * @return boolean    返回类型
	 */
	public static boolean deleteZip(String fileFullPath) {
		boolean sign = false;
		File ff = new File(fileFullPath);
		Delete delete = new Delete();
		if (ff.isFile()) {
			delete.setFile(ff);
		} else if (ff.isDirectory()) {
			delete.setDir(ff);
		}
		delete.execute();
		sign = true;
		return sign;
	}
	
	/** 
	  * 解压tar.gz 文件 
	  * @param file 要解压的tar.gz文件对象 
	  * @param outputDir 要解压到某个指定的目录下 
	  * @return void
	  * @throws IOException 
	  */
	public static void unTarGz(File file, String outputDir) throws IOException {
		try (TarInputStream tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))), 1024 * 2)) {
			// 创建输出目录
			createDirectory(outputDir, null);
			TarEntry entry = null;
			while ((entry = tarIn.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					// 是目录
					entry.getName();
					// 创建空目录
					createDirectory(outputDir, entry.getName());
				} else {
					// 是文件
					File tmpFile = new File(outputDir + "/" + entry.getName());
					// 创建输出目录
					createDirectory(tmpFile.getParent() + "/", null);
					writeToFile(tarIn, tmpFile);
				}
			}
		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常", ex);
		}
	}
	
	/** 
	 * <p>
	 * 	 写数据到文件
	 * </p>
	 * @param tarIn
	 * @param tmpFile
	 * @return void 
	 * @throws IOException 
	 */
	private static void writeToFile(TarInputStream tarIn, File tmpFile) throws IOException {
		try (OutputStream out = new FileOutputStream(tmpFile)) {
			int length = 0;
			byte[] b = new byte[2048];
			while ((length = tarIn.read(b)) != -1) {
				out.write(b, 0, length);
			}
		} catch (IOException ex) {
			throw ex;
		}
	}

	/** 
	 * 构建目录 
	 * @param outputDir 
	 * @param subDir 
	 * @return void
	 */
	private static void createDirectory(String outputDir, String subDir) {
		File file = new File(outputDir);
		// 子目录不为空
		if (!(subDir == null || subDir.trim().equals(""))) {
			file = new File(outputDir + "/" + subDir);
		}
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.mkdirs();
		}
	}

}
