package cn.yiidii.openapi.xjj.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.yiidii.openapi.entity.xjj.Cdk;
import cn.yiidii.openapi.entity.xjj.CdkPromote;
import cn.yiidii.openapi.xjj.mapper.CdkMapper;
import cn.yiidii.openapi.xjj.mapper.CdkPromoteMapper;
import cn.yiidii.openapi.xjj.service.ICdkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CdkServiceImpl implements ICdkService {

    /**
     * cdk 数据库操作注入
     */
    @Autowired
    private CdkMapper cdkMapper;

    /**
     * cdk邀请 数据库操作注入
     */
    @Autowired
    private CdkPromoteMapper cdkPromoteMapper;

    @Override
    public Cdk getCdkById(String id) {
        return cdkMapper.selectById(id);
    }

    @Override
    public Cdk getCdkByCdk(String cdkStr) {
        return cdkMapper.selectOne(new QueryWrapper<Cdk>().eq("cdk", cdkStr));
    }

    @Override
    public String addCdk(Date expireTime) {
        String cdkStr = this.generateUniqueCdkStr();
        Cdk cdk = Cdk.builder().cdk(cdkStr).expireTime(expireTime).createTime(new Date()).build();
        cdkMapper.insert(cdk);
        return cdkStr;
    }

    @Override
    public int updateCdk(Cdk cdk) {
        return cdkMapper.update(cdk, new UpdateWrapper<Cdk>().eq("cdk", cdk.getCdk()));
    }

    @Override
    public void add10Day4Promote(Cdk cdk) {
        CdkPromote cdkPromoteInfo = cdkPromoteMapper.selectOne(new QueryWrapper<CdkPromote>().eq("cdk", cdk.getCdk()));
        if (Objects.isNull(cdkPromoteInfo) || cdkPromoteInfo.getState()) {
            return;
        }
        String cdkPromoteStr = cdkPromoteInfo.getPromoteCdk();
        Cdk promoteCdk = cdkMapper.selectOne(new QueryWrapper<Cdk>().eq("cdk", cdkPromoteStr));
        if (Objects.isNull(promoteCdk)) {
            return;
        }
        Date expireTime = promoteCdk.getExpireTime();
        expireTime = DateUtil.offsetDay(expireTime, 10);
        promoteCdk.setExpireTime(expireTime);
        this.updateCdk(promoteCdk);
        cdkPromoteInfo.setState(true);
        cdkPromoteMapper.update(cdkPromoteInfo, new UpdateWrapper<CdkPromote>().eq("cdk", cdk.getCdk()));
        log.info("[{}]通过邀请[{}]获得3天奖励!", cdkPromoteInfo.getPromoteCdk(), cdkPromoteInfo.getCdk());
    }

    @Override
    public String generateUniqueCdkStr() {
        String cdkStr = "";
        while (true) {
            String datePrefix = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_FORMAT);
            String uuidHashSuffix = String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode()));
            cdkStr = datePrefix + uuidHashSuffix;
            long count = cdkMapper.selectCount(new QueryWrapper<Cdk>().eq("cdk", cdkStr));
            if (count <= 0) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1L);
            } catch (InterruptedException e) {
            }
        }
        return cdkStr;
    }

    @Async("scheduleTaskExecutor")
//    @Scheduled(fixedRate = 1000 * 3) // 3s debug
    @Scheduled(fixedRate = 1000 * 60 * 60) // 1h
    public void timerClearRedundantCdk() {
        try {
            List<Cdk> redundantCdkList = cdkMapper.selectRedundantCdk();
            List<String> delCdkList = redundantCdkList.stream().map(Cdk::getCdk).collect(Collectors.toList());
            if (delCdkList.size() == 0) {
                return;
            }
            // 删除cdk
            int delCdkRow = cdkMapper.delete(new QueryWrapper<Cdk>().in("cdk", delCdkList));
            // 删除cdkPromote
            int delCdkPromoteRow = cdkPromoteMapper.delete(new QueryWrapper<CdkPromote>().in("cdk", delCdkList));
            log.info("timer Clear {} RedundantCdk and {} cdkPromote . ", delCdkRow, delCdkPromoteRow);
        } catch (Exception e) {
            log.info("{}", e);
        }
    }
}
