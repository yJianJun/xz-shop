package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.SystemTimeConfig;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;

public interface SystemTimeConfigService extends IService<SystemTimeConfig> {

    /**
     * 修改配置
     * @param updateVO
     * @return
     */
    String updateConfig(SystemTimeConfigVO updateVO);

    /**
     * 获取商城配置项
     * @return
     */
    SystemTimeConfigVO getSystemTimeConfig();

}
