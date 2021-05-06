package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.vo.admin.AliPayReceiveVo;
import com.cdzg.xzshop.vo.admin.ShopInfoAddVo;
import com.cdzg.xzshop.vo.admin.ShopInfoUpdateVO;
import com.cdzg.xzshop.vo.admin.WeChatReceiveVo;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.mapper.ShopInfoMapper;
import com.cdzg.xzshop.service.ShopInfoService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements ShopInfoService {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Resource
    private ReceivePaymentInfoMapper receivePaymentInfoMapper;

    @Override
    public int insert(ShopInfo record) {
        return shopInfoMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(ShopInfo record) {
        return shopInfoMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(ShopInfo record) {
        return shopInfoMapper.insertOrUpdateSelective(record);
    }


    @Override
    public int updateBatch(List<ShopInfo> list) {
        return shopInfoMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<ShopInfo> list) {
        return shopInfoMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<ShopInfo> list) {
        return shopInfoMapper.batchInsert(list);
    }

    @Override
    public PageResultVO<ShopInfo> page(int page, int pageSize, String likeShopName, Boolean status, Date minGmtPutOnTheShelf, Date maxGmtPutOnTheShelf) {
        PageHelper.startPage(page, pageSize);
        return PageUtil.transform(new PageInfo(shopInfoMapper.findAllByShopNameLikeAndStatusAndGmtPutOnTheShelfBetweenEqual(likeShopName, status, minGmtPutOnTheShelf, maxGmtPutOnTheShelf)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchPutOnDown(List<Long> list, Boolean flag) {

        QueryWrapper<ShopInfo> queryWrapper = new QueryWrapper<>();
        for (int i = 0; i < list.size(); i++) {

            Long id = list.get(i);
            ShopInfo shopInfo = baseMapper.selectOne(queryWrapper.eq("id",id));
            if (Objects.nonNull(shopInfo)){
                shopInfo.setStatus(flag);

                if (flag){
                    shopInfo.setGmtPutOnTheShelf(LocalDateTime.now());
                }
                updateById(shopInfo);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(ShopInfoAddVo addVo) {

        ShopInfo shopInfo = ShopInfo.builder()
                .shopName(addVo.getShopName())
                .createUser("")   //yjjtodo 需改
                .contactPerson(addVo.getPerson())
                .department(addVo.getDepartment())
                .fare(addVo.getFare())
                .gmtCreate(LocalDateTime.now())
                .logo(addVo.getLogoUrl())
                .status(false)
                .shopUnion(addVo.getUnion())
                .phone(addVo.getContact())
                .build();

        save(shopInfo);

        Integer receiveMoney = addVo.getReceiveMoney();

        AliPayReceiveVo aliPayVo = addVo.getAliPayVo();
        if (Objects.nonNull(aliPayVo)){

            boolean flag = receiveMoney == null || (receiveMoney == 0); // 全部 支付宝 | 微信
            ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                    .appid(aliPayVo.getAppId())
                    .shopId(shopInfo.getId())
                    .keyPath("")
                    .mchid("")
                    .status(flag)
                    .privateKey(aliPayVo.getPrivateKey())
                    .publicKey(aliPayVo.getPublicKey())
                    .signtype(aliPayVo.getSigntype())
                    .type(ReceivePaymentType.Alipay)
                    .build();
            receivePaymentInfoMapper.insert(aliInfo);

        }else {
            if (Objects.nonNull(receiveMoney)&& !receiveMoney.equals(1)){
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }

        WeChatReceiveVo wxPayVo = addVo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)){

            boolean flag = receiveMoney == null || (receiveMoney == 1); // 全部 微信 | 支付宝
            ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                    .appid(wxPayVo.getAppId())
                    .signtype("")
                    .status(flag)
                    .shopId(shopInfo.getId())
                    .keyPath(wxPayVo.getKeyPath())
                    .mchid(wxPayVo.getMchId())
                    .privateKey(wxPayVo.getPrivateKey())
                    .publicKey("")
                    .type(ReceivePaymentType.Wechat)
                    .build();

            receivePaymentInfoMapper.insert(aliInfo);
        }else {
            if (Objects.nonNull(receiveMoney)&& !receiveMoney.equals(0)){
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(ShopInfoUpdateVO vo) {

        ShopInfo shopInfo  = getById(vo.getId());
        if (Objects.nonNull(shopInfo)){

            shopInfo.setShopName(vo.getShopName());
            shopInfo.setContactPerson(vo.getPerson());
            shopInfo.setDepartment(vo.getDepartment());
            shopInfo.setFare(vo.getFare());
            shopInfo.setLogo(vo.getLogoUrl());
            shopInfo.setGmtUpdate(LocalDateTime.now());
            shopInfo.setShopUnion(vo.getUnion());
            shopInfo.setPhone(vo.getContact());

            saveOrUpdate(shopInfo);
        }else {
            throw new BaseException(ResultCode.DATA_ERROR);
        }

        ReceivePaymentInfo alipay = receivePaymentInfoMapper.findOneByShopIdAndType(shopInfo.getId(), ReceivePaymentType.Alipay);
        ReceivePaymentInfo wechat = receivePaymentInfoMapper.findOneByShopIdAndType(shopInfo.getId(), ReceivePaymentType.Wechat);

        AliPayReceiveVo aliPayVo = vo.getAliPayVo();

        if (Objects.nonNull(aliPayVo)){
            if (Objects.nonNull(alipay)){

                alipay.setAppid(aliPayVo.getAppId());
                alipay.setPrivateKey(aliPayVo.getPrivateKey());
                alipay.setPublicKey(alipay.getPublicKey());
                alipay.setSigntype(aliPayVo.getSigntype());
                receivePaymentInfoMapper.insertOrUpdate(alipay);
            }else {
                ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                        .appid(aliPayVo.getAppId())
                        .shopId(shopInfo.getId())
                        .keyPath("")
                        .mchid("")
                        .privateKey(aliPayVo.getPrivateKey())
                        .publicKey(aliPayVo.getPublicKey())
                        .signtype(aliPayVo.getSigntype())
                        .type(ReceivePaymentType.Alipay)
                        .build();
                receivePaymentInfoMapper.insert(aliInfo);
            }
        }

        WeChatReceiveVo wxPayVo = vo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)){
            if (Objects.nonNull(wechat)){

                wechat.setAppid(wxPayVo.getAppId());
                wechat.setKeyPath(wxPayVo.getKeyPath());
                wechat.setMchid(wxPayVo.getMchId());
                wechat.setPrivateKey(wxPayVo.getPrivateKey());

                receivePaymentInfoMapper.insertOrUpdate(wechat);
            }else {

                ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                        .appid(wxPayVo.getAppId())
                        .signtype("")
                        .shopId(shopInfo.getId())
                        .keyPath(wxPayVo.getKeyPath())
                        .mchid(wxPayVo.getMchId())
                        .privateKey(wxPayVo.getPrivateKey())
                        .publicKey("")
                        .type(ReceivePaymentType.Wechat)
                        .build();
                receivePaymentInfoMapper.insert(aliInfo);
            }
        }
    }
}





