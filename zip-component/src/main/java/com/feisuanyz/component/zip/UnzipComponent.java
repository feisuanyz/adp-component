/* 
 * All rights Reserved, Designed By www.feisuanyz.com
 * @title     UnzipComponent.java   
 * @package   com.feisuanyz.flow.component.zip
 * @version   V1.0 
 * @copyright 2020 www.feisuanyz.com Inc. All rights reserved.
 * 注意：本内容仅限于前海飞算云智软件科技（深圳）有限公司内部传阅，除非适用法律要求或书面同意，禁止外泄以及用于其他的商业目的
 */
package com.feisuanyz.component.zip;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import com.feisuanyz.component.zip.dto.UnzipDTO;
import com.feisuanyz.component.zip.utils.ZipUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.feisuanyz.flow.component.FlowComponent;
import com.feisuanyz.flow.component.bean.DownloadFile;
import com.feisuanyz.flow.component.bean.UnzipResult;
import com.feisuanyz.flow.component.exception.FlowComponentException;
import com.feisuanyz.flow.component.parameter.FlowComponentParameter;

import static com.feisuanyz.component.zip.enums.ZipFormat.ZIP;
import static com.feisuanyz.component.zip.enums.ZipFormat.RAR;
import static com.feisuanyz.component.zip.enums.ZipFormat.TAR_GZ;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**   
 * <p>
 *   解压组件
 * </p>
 * @author 飞算云智
 * @date 2020-06-11 21:26
 */
@Slf4j
public class UnzipComponent implements FlowComponent {
	
	/**
	 * 解压根目录路径
	 */
	private static final String UNZIP_ROOT = FileUtils.getUserDirectoryPath() + File.separator + "unzip_component_temp_dir";

	public static final String SYMBOL_SLASH = "/";

	@Override
	public void execute(FlowComponentParameter parameter) throws FlowComponentException {
		UnzipDTO unzip = parameter.getInputParameter("unzipDTO", UnzipDTO.class);
		UnzipResult ur = unzip(parameter, unzip);
		parameter.setOutputParameter("unzipExecuteResult", ur);
	}
	
	/** 
	 * <p>
	 * 	 解压处理过程
	 * </p>
	 * @param parameter
	 * @param unzip
	 * @return UnzipResult 
	 */
	private UnzipResult unzip(FlowComponentParameter parameter, UnzipDTO unzip) {
		String path = UNZIP_ROOT;
		String unzipDirSnippet = unzip.getUnzipDirSnippet();
		if (StringUtils.isNotBlank(unzipDirSnippet)) {
			if (unzipDirSnippet.startsWith(SYMBOL_SLASH)) {
				unzipDirSnippet = unzipDirSnippet.substring(1);
			}
			path = path + File.separator + unzipDirSnippet;
		}
		File parent = new File(path, UUID.randomUUID().toString());
		parent.mkdirs();
		
		DownloadFile downloadFile = unzip.getDownloadFile();
		String originFileName = downloadFile.getOriginFileName();
		File zipFile = new File(parent, originFileName);
		try {
			// 将流写入到文件中
			FileUtils.copyInputStreamToFile(downloadFile.getIn(), zipFile);
			// 解压目录
			String ext = extName(originFileName);
			String subDir = originFileName.replace(ext, "");
			File unzipDir = new File(parent, subDir);
			unzipDir.mkdirs();
			// 解压
			doUnzip(zipFile, unzipDir, originFileName);
            // 获取解压目录中的所有文件
            Collection<File> files = FileUtils.listFiles(unzipDir, null, true);
            
            UnzipResult ur = new UnzipResult();
            ur.setUnzipDir(unzipDir);
            ur.setFiles(Lists.newArrayList(files));

            
            return ur;
		} catch (IOException e) {
			throw new FlowComponentException("执行解压操作时出现异常！", e);
		}
	}
	
	/** 
	 * <p>
	 * 	 获取压缩文件扩展名
	 * </p>
	 * @param originFileName
	 * @return String 
	 */
	private String extName(String originFileName) {
		String ext = null;
		if (originFileName.endsWith(ZIP.getExt())) {
			ext = ZIP.getExt();
		} else if (originFileName.endsWith(RAR.getExt())) {
			ext = RAR.getExt();
		} else if (originFileName.endsWith(TAR_GZ.getExt())) {
			ext = TAR_GZ.getExt();
		} else {
			throw new FlowComponentException(String.format("不支持的解压格式！originFileName=%s", originFileName));
		}
		return ext;
	}
	
	/** 
	 * <p>
	 * 	 执行解压操作
	 * </p>
	 * @param zipFile
	 * @param unzipDir 
	 * @param originFileName
	 * @return void 
	 * @throws IOException 
	 */
	private void doUnzip(File zipFile, File unzipDir, String originFileName) throws IOException {
		if (originFileName.endsWith(ZIP.getExt()) || originFileName.endsWith(RAR.getExt())) {
			// .zip或.rar文件
			ZipUtility.unZip(zipFile.getAbsolutePath(), unzipDir.getAbsolutePath());
		} else if (originFileName.endsWith(TAR_GZ.getExt())) {
			// .tar.gz文件
			ZipUtility.unTarGz(zipFile, unzipDir.getAbsolutePath());
		} else {
			throw new FlowComponentException(String.format("不支持的解压格式！zipFile=%s", zipFile.getAbsolutePath()));
		}
	}

}
