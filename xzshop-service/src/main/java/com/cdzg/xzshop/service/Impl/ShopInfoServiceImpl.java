package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.mapper.ReturnGoodsInfoMapper;
import com.cdzg.xzshop.vo.admin.*;
import com.cdzg.xzshop.utils.PageUtil;
import com.cdzg.xzshop.vo.common.PageResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
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

        if (flag){
            updateStatusAndGmtPutOnTheShelfByIdIn(flag,LocalDateTime.now(),list);
        }else {
            updateStatusAndGmtPutOnTheShelfByIdIn(flag,LocalDateTime.parse("1000-01-01T00:00:00"),list);
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

        }else {
            if (receiveMoney!=2){
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }

        WeChatReceiveVo wxPayVo = addVo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)){

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
        }else {
            if (receiveMoney != 1){
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

        Integer receiveMoney = vo.getReceiveMoney();
        AliPayReceiveVo aliPayVo = vo.getAliPayVo();

        if (Objects.nonNull(aliPayVo)){

            boolean flag = receiveMoney == 3 || (receiveMoney == 1); // 全部 支付宝 | 微信
            if (Objects.nonNull(alipay)){

                alipay.setAppid(aliPayVo.getAppId());
                alipay.setPrivateKey(aliPayVo.getPrivateKey());
                alipay.setStatus(flag);
                alipay.setPublicKey(alipay.getPublicKey());
                alipay.setSigntype(aliPayVo.getSigntype());
                receivePaymentInfoMapper.insertOrUpdate(alipay);
            }else {
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
        }else {
            if (2 != receiveMoney){
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }

        WeChatReceiveVo wxPayVo = vo.getWxPayVo();
        if (Objects.nonNull(wxPayVo)){

            boolean flag = receiveMoney == 3 || (receiveMoney == 2); // 全部 微信 | 支付宝
            if (Objects.nonNull(wechat)){

                wechat.setAppid(wxPayVo.getAppId());
                wechat.setKeyPath(wxPayVo.getKeyPath());
                wechat.setMchid(wxPayVo.getMchId());
                wechat.setStatus(flag);
                wechat.setPrivateKey(wxPayVo.getPrivateKey());

                receivePaymentInfoMapper.insertOrUpdate(wechat);
            }else {
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
        }else {
            if (1 != receiveMoney){
                throw new BaseException(ResultCode.PARAMETER_ERROR);
            }
        }
        ReturnGoodsInfo returnGoodsInfo = returnGoodsInfoMapper.findOneByShopId(shopInfo.getId());
        ReturnGoodsInfoVo returnfoVo = vo.getReturnfoVo();

        if (Objects.nonNull(returnGoodsInfo)){

            returnGoodsInfo.setPhone(returnfoVo.getPhone());
            returnGoodsInfo.setAddress(returnfoVo.getAddress());
            returnGoodsInfo.setRecipient(returnfoVo.getRecipient());
            returnGoodsInfo.setPrecautions(returnfoVo.getPrecautions());
            returnGoodsInfoMapper.insertOrUpdate(returnGoodsInfo);
        }
    }

	@Override
	public int updateStatusAndGmtPutOnTheShelfByIdIn(Boolean updatedStatus,LocalDateTime updatedGmtPutOnTheShelf,Collection<Long> idCollection){
		 return shopInfoMapper.updateStatusAndGmtPutOnTheShelfByIdIn(updatedStatus,updatedGmtPutOnTheShelf,idCollection);
	}



}





