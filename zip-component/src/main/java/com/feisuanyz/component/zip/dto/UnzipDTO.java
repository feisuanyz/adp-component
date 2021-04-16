/* 
 * All rights Reserved, Designed By www.feisuanyz.com
 * @title     UnzipDTO.java   
 * @package   com.feisuanyz.flow.component.zip.dto
 * @version   V1.0 
 * @copyright 2020 www.feisuanyz.com Inc. All rights reserved.
 * 注意：本内容仅限于前海飞算云智软件科技（深圳）有限公司内部传阅，除非适用法律要求或书面同意，禁止外泄以及用于其他的商业目的
 */
package com.feisuanyz.component.zip.dto;

import java.io.Serializable;

import com.feisuanyz.flow.component.bean.DownloadFile;

import lombok.Data;

/**   
 * <p>
 *   解压DTO
 * </p>
 * @author 飞算云智
 * @date 2020-06-11 21:14
 */
@Data
public class UnzipDTO implements Serializable {
	private static final long serialVersionUID = -7012539095171979077L;
	
	/**
	 * 下载的文件内容对象 
	 */
	private DownloadFile downloadFile;
	/**
	 * 解压的目录片段
	 */
	private String unzipDirSnippet;

}
