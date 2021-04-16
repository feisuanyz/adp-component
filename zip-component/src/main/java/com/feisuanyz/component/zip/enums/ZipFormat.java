/* 
 * All rights Reserved, Designed By www.feisuanyz.com
 * @title     ZipFormat.java   
 * @package   com.feisuanyz.flow.component.zip.enums
 * @version   V1.0 
 * @copyright 2020 www.feisuanyz.com Inc. All rights reserved.
 * 注意：本内容仅限于前海飞算云智软件科技（深圳）有限公司内部传阅，除非适用法律要求或书面同意，禁止外泄以及用于其他的商业目的
 */
package com.feisuanyz.component.zip.enums;

import lombok.Getter;

/**   
 * <p>
 *   压缩文件格式
 * </p>
 * @author 飞算云智
 * @date 2020-06-19 18:28
 */
@Getter
public enum ZipFormat {
	/**
	 * 压缩文件格式
	 */
	ZIP(".zip", "ZIP压缩文件"),
	RAR(".rar", "RAR压缩文件"),
	TAR_GZ(".tar.gz", "TAR.GZ压缩文件")
	;
	
	private String ext;
    private String desc;
    
	private ZipFormat(String ext, String desc) {
		this.ext = ext;
		this.desc = desc;
	}
	
	/** 
	 * <p>
	 * 	 通过扩展名查询枚举值
	 * </p>
	 * @param ext
	 * @return ZipFormat 
	 */
	public static ZipFormat parse(String ext) {
        for (ZipFormat zf : values()) {
            if (zf.ext.equals(ext)) {
                return zf;
            }
        }
        return null;
    }
    
}
