package com.cdzg.xzshop.service.Impl;

import java.math.BigInteger;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.mapper.GoodsSpuMapper;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.mapper.ReturnGoodsInfoMapper;
import com.cdzg.xzshop.service.ReceivePaymentInfoService;
import com.cdzg.xzshop.service.ReturnGoodsInfoService;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.admin.ReturnGoodsInfoVo;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.time.LocalDateTime;

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
    private ReturnGoodsInfoMapper returnGoodsInfoMapper;

    @Autowired
    ReceivePaymentInfoService receivePaymentInfoService;

    @Autowired
    ReturnGoodsInfoService returnGoodsInfoService;

    @Resource
    GoodsSpuMapper spuMapper;

    @Resource
    private ReceivePaymentInfoMapper receivePaymentInfoMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insert(ShopInfo record) {
        return shopInfoMapper.insert(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrUpdate(ShopInfo record) {
        return shopInfoMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertOrUpdateSelective(ShopInfo record) {
        return shopInfoMapper.insertOrUpdateSelective(record);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateBatch(List<ShopInfo> list) {
        return shopInfoMapper.updateBatch(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateBatchSelective(List<ShopInfo> list) {
        return shopInfoMapper.updateBatchSelective(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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

        if (flag) {
            updateStatusAndGmtPutOnTheShelfByIdIn(flag, LocalDateTime.now(), list);
        } else {
            updateStatusAndGmtPutOnTheShelfByIdIn(flag, LocalDateTime.parse("1000-01-01T00:00:00"), list);
            List<Long> ids = spuMapper.findIdByShopIdIn(list);
            spuMapper.updateStatusAndGmtPutOnTheShelfByIdIn(flag, LocalDateTime.parse("1000-01-01T00:00:00"), ids);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(ShopInfoAddVo addVo, String adminUser) {

        ShopInfo shopInfo;

        shopInfo = findOneByShopUnion(addVo.getUnion());
        if (Objects.nonNull(shopInfo)) {
            throw new BaseException("该工会下已存在店铺,不能重复添加");
        }
        shopInfo = ShopInfo.builder()
                .shopName(addVo.getShopName())
                .createUser(adminUser)
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
        if (Objects.nonNull(aliPayVo)) {

            boolean flag = receiveMoney == 3 || (receiveMoney == 1); // 全部 支付宝 | 微信
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

        } else {
            if (receiveMoney != 2) {
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }

        WeChatReceiveVo wxPayVo = addVo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)) {

            boolean flag = receiveMoney == 3 || (receiveMoney == 2); // 全部 微信 | 支付宝
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
        } else {
            if (receiveMoney != 1) {
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }

        ReturnGoodsInfoVo returnfoVo = addVo.getReturnfoVo();
        ReturnGoodsInfo returnGoodsInfo = ReturnGoodsInfo.builder()
                .address(returnfoVo.getAddress())
                .shopId(shopInfo.getId())
                .phone(returnfoVo.getPhone())
                .precautions(returnfoVo.getPrecautions())
                .recipient(returnfoVo.getRecipient())
                .build();
        returnGoodsInfoMapper.insert(returnGoodsInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(ShopInfoUpdateVO vo) {

        ShopInfo shopInfo = getById(vo.getId());
        if (Objects.nonNull(shopInfo)) {

            shopInfo.setShopName(vo.getShopName());
            shopInfo.setContactPerson(vo.getPerson());
            shopInfo.setDepartment(vo.getDepartment());
            shopInfo.setFare(vo.getFare());
            shopInfo.setLogo(vo.getLogoUrl());
            shopInfo.setGmtUpdate(LocalDateTime.now());
            shopInfo.setShopUnion(vo.getUnion());
            shopInfo.setPhone(vo.getContact());

            saveOrUpdate(shopInfo);
        } else {
            throw new BaseException(ResultCode.DATA_ERROR);
        }

        ReceivePaymentInfo alipay = receivePaymentInfoMapper.findOneByShopIdAndType(shopInfo.getId(), ReceivePaymentType.Alipay);
        ReceivePaymentInfo wechat = receivePaymentInfoMapper.findOneByShopIdAndType(shopInfo.getId(), ReceivePaymentType.Wechat);

        Integer receiveMoney = vo.getReceiveMoney();
        AliPayReceiveVo aliPayVo = vo.getAliPayVo();

        if (Objects.nonNull(aliPayVo)) {

            boolean flag = receiveMoney == 3 || (receiveMoney == 1); // 全部 支付宝 | 微信
            if (Objects.nonNull(alipay)) {

                alipay.setAppid(aliPayVo.getAppId());
                alipay.setPrivateKey(aliPayVo.getPrivateKey());
                alipay.setStatus(flag);
                alipay.setPublicKey(alipay.getPublicKey());
                alipay.setSigntype(aliPayVo.getSigntype());
                receivePaymentInfoMapper.insertOrUpdate(alipay);
            } else {
                ReceivePaymentInfo aliInfo = ReceivePaymentInfo.builder()
                        .appid(aliPayVo.getAppId())
                        .shopId(shopInfo.getId())
                        .status(flag)
                        .keyPath("")
                        .mchid("")
                        .privateKey(aliPayVo.getPrivateKey())
                        .publicKey(aliPayVo.getPublicKey())
                        .signtype(aliPayVo.getSigntype())
                        .type(ReceivePaymentType.Alipay)
                        .build();
                receivePaymentInfoMapper.insert(aliInfo);
            }
        } else {
            if (2 != receiveMoney) {
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }

        WeChatReceiveVo wxPayVo = vo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)) {

            boolean flag = receiveMoney == 3 || (receiveMoney == 2); // 全部 微信 | 支付宝
            if (Objects.nonNull(wechat)) {

                wechat.setAppid(wxPayVo.getAppId());
                wechat.setKeyPath(wxPayVo.getKeyPath());
                wechat.setMchid(wxPayVo.getMchId());
                wechat.setStatus(flag);
                wechat.setPrivateKey(wxPayVo.getPrivateKey());

                receivePaymentInfoMapper.insertOrUpdate(wechat);
            } else {
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
            }
        } else {
            if (1 != receiveMoney) {
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoMapper.findOneByShopId(shopInfo.getId());
        ReturnGoodsInfoVo returnfoVo = vo.getReturnfoVo();

        if (Objects.nonNull(returnGoodsInfo)) {

            returnGoodsInfo.setPhone(returnfoVo.getPhone());
            returnGoodsInfo.setAddress(returnfoVo.getAddress());
            returnGoodsInfo.setRecipient(returnfoVo.getRecipient());
            returnGoodsInfo.setPrecautions(returnfoVo.getPrecautions());
            returnGoodsInfoMapper.insertOrUpdate(returnGoodsInfo);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus, LocalDateTime updatedGmtPutOnTheShelf, Collection<Long> idCollection) {
        return shopInfoMapper.updateStatusAndGmtPutOnTheShelfByIdIn(updatedStatus, updatedGmtPutOnTheShelf, idCollection);
    }

    @Override
    public ShopInfo findOneByShopUnion(String shopUnion) {
        return shopInfoMapper.findOneByShopUnion(shopUnion);
    }

    @Override
    public ShopInfoUpdateVO get(Long id) {
        ShopInfo shopInfo = getById(id);
        List<ReceivePaymentInfo> paymentInfos = receivePaymentInfoService.findAllByShopId(id);
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoService.findOneByShopId(id);

        if (Objects.nonNull(shopInfo) && Objects.nonNull(returnGoodsInfo) && CollectionUtils.isNotEmpty(paymentInfos)) {

            ShopInfoUpdateVO updateVO = ShopInfoUpdateVO.builder().build();
            BeanUtils.copyProperties(shopInfo, updateVO);
            updateVO.setUnion(shopInfo.getShopUnion());
            updateVO.setLogoUrl(shopInfo.getLogo());
            updateVO.setPerson(shopInfo.getContactPerson());
            updateVO.setContact(shopInfo.getPhone());
            updateVO.setReceiveMoney(setReceiveMoney(paymentInfos));
            setReceiveVo(paymentInfos, updateVO);
            ReturnGoodsInfoVo infoVo = new ReturnGoodsInfoVo();
            BeanUtils.copyProperties(returnGoodsInfo, infoVo);
            updateVO.setReturnfoVo(infoVo);
            return updateVO;
        }
        throw new BaseException(ResultCode.DATA_ERROR);
    }

    @Override
    public ShopInfoUpdateVO get(BigInteger organizationId) {

        ShopInfo shopInfo = findOneByShopUnion(organizationId + "");

        if (Objects.isNull(shopInfo)) {
            throw new BaseException(ResultCode.DATA_ERROR.getCode(), "你所在的工会没有创建店铺！");
        }

        List<ReceivePaymentInfo> paymentInfos = receivePaymentInfoService.findAllByShopId(shopInfo.getId());
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoService.findOneByShopId(shopInfo.getId());

        ShopInfoUpdateVO updateVO = ShopInfoUpdateVO.builder().build();
        BeanUtils.copyProperties(shopInfo, updateVO);
        updateVO.setUnion(shopInfo.getShopUnion());
        updateVO.setLogoUrl(shopInfo.getLogo());
        updateVO.setPerson(shopInfo.getContactPerson());
        updateVO.setContact(shopInfo.getPhone());

        if (Objects.nonNull(returnGoodsInfo) && CollectionUtils.isNotEmpty(paymentInfos)) {

            updateVO.setReceiveMoney(setReceiveMoney(paymentInfos));
            setReceiveVo(paymentInfos, updateVO);
            ReturnGoodsInfoVo infoVo = new ReturnGoodsInfoVo();
            BeanUtils.copyProperties(returnGoodsInfo, infoVo);
            updateVO.setReturnfoVo(infoVo);
            return updateVO;
        }
        throw new BaseException(ResultCode.DATA_ERROR);
    }

    private void setReceiveVo(List<ReceivePaymentInfo> paymentInfos, ShopInfoUpdateVO updateVO) {

        for (ReceivePaymentInfo paymentInfo : paymentInfos) {

            if (paymentInfo.getType() == ReceivePaymentType.Alipay) {

                AliPayReceiveVo aliPayReceiveVo = new AliPayReceiveVo();
                aliPayReceiveVo.setAppId(paymentInfo.getAppid());
                BeanUtils.copyProperties(paymentInfo, aliPayReceiveVo);
                updateVO.setAliPayVo(aliPayReceiveVo);
            }

            if (paymentInfo.getType() == ReceivePaymentType.Wechat) {

                WeChatReceiveVo chatReceiveVo = new WeChatReceiveVo();
                chatReceiveVo.setAppId(paymentInfo.getAppid());
                chatReceiveVo.setMchId(paymentInfo.getMchid());
                BeanUtils.copyProperties(paymentInfo, chatReceiveVo);
                updateVO.setWxPayVo(chatReceiveVo);
            }
        }
    }

    private int setReceiveMoney(List<ReceivePaymentInfo> paymentInfos) {

        List<Integer> flag = new ArrayList<>();
        for (ReceivePaymentInfo paymentInfo : paymentInfos) {
            if (paymentInfo.getStatus()) {

                if (ReceivePaymentType.Alipay == paymentInfo.getType()) {
                    flag.add(1);
                }
                if (ReceivePaymentType.Wechat == paymentInfo.getType()) {
                    flag.add(2);
                }
            }
        }

        if (flag.size() == 2) {
            return 3;
        } else if (flag.size() == 0) {
            return 0;
        } else {
            return flag.get(0);
        }
    }


}







