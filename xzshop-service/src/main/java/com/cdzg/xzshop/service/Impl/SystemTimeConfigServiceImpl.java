package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.universal.vo.response.user.UserLoginResponse;
import com.cdzg.xzshop.domain.SystemTimeConfig;
import com.cdzg.xzshop.filter.auth.LoginSessionUtils;
import com.cdzg.xzshop.mapper.SystemTimeConfigMapper;
import com.cdzg.xzshop.service.SystemTimeConfigService;
import com.cdzg.xzshop.vo.admin.SystemTimeConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SystemTimeConfigServiceImpl extends ServiceImpl<SystemTimeConfigMapper, SystemTimeConfig> implements SystemTimeConfigService {


    @Override
    public String updateConfig(SystemTimeConfigVO updateVO) {
        SystemTimeConfig config = new SystemTimeConfig();
        BeanUtils.copyProperties(updateVO, config);
        UserLoginResponse adminUser = LoginSessionUtils.getAdminUser();
        config.setUpdateBy(adminUser.getUserId());
        config.setUpdateTime(LocalDateTime.now());
        this.update(config, new QueryWrapper<>());
        return null;
    }

    @Override
    public SystemTimeConfigVO getSystemTimeConfig() {
        SystemTimeConfig one = this.getOne(new QueryWrapper<>());
        SystemTimeConfigVO vo = new SystemTimeConfigVO();
        BeanUtils.copyProperties(one, vo);
        return vo;
    }

}
