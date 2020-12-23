package cn.yiidii.openapi.unicommeituan.service.impl;

import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.entity.uincommeituan.ChinaUnicomInfo;
import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomInfoFrom;
import cn.yiidii.openapi.unicommeituan.mapper.ChinaUnicomInfoMapper;
import cn.yiidii.openapi.unicommeituan.service.ChinaUnicomInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChinaUnicomInfoServiceImpl implements ChinaUnicomInfoService {

    private final ChinaUnicomInfoMapper chinaUnicomInfoMapper;

    @Override
    public List<ChinaUnicomInfo> getAllChinaUnicom() {
        return chinaUnicomInfoMapper.selectList(new QueryWrapper<ChinaUnicomInfo>());
    }

    @Override
    public ChinaUnicomInfo getChinaUnicomById(Integer id) throws ServiceException {
        ChinaUnicomInfo chinaUnicomInfo = chinaUnicomInfoMapper.selectById(id);
        if (Objects.isNull(chinaUnicomInfo)) {
            throw new ServiceException("账号不存在");
        }
        return chinaUnicomInfo;
    }

    @Override
    public ChinaUnicomInfo getChinaUnicomByPhoneNum(String phoneNum) throws ServiceException {
        return chinaUnicomInfoMapper.selectOne(new QueryWrapper<ChinaUnicomInfo>().eq("phone_num", phoneNum));
    }

    @Override
    public int addChinaUnicomInfo(ChinaUnicomInfoFrom chinaUnicomInfoFrom) throws ServiceException {
        ChinaUnicomInfo chinaUnicomInfo = transForm2ChinaUnicomInfo(chinaUnicomInfoFrom);
        String phoneNum = chinaUnicomInfo.getPhoneNum();
        String cookie = chinaUnicomInfo.getCookie();
        ChinaUnicomInfo exist = chinaUnicomInfoMapper.selectOne(new QueryWrapper<ChinaUnicomInfo>().eq("phone_num", phoneNum));
        if (Objects.nonNull(exist)) {
            throw new ServiceException("手机号已存在!");
        }
        if (!StringUtils.contains(cookie, phoneNum)) {
            throw new ServiceException("Cookie格式不正确!");
        }
        return chinaUnicomInfoMapper.insert(chinaUnicomInfo);
    }

    @Override
    public int updateChinaUnicomInfo(ChinaUnicomInfoFrom chinaUnicomInfoFrom) throws ServiceException {
        ChinaUnicomInfo chinaUnicomInfo = transForm2ChinaUnicomInfo(chinaUnicomInfoFrom);
        return chinaUnicomInfoMapper.update(chinaUnicomInfo, new UpdateWrapper<ChinaUnicomInfo>().eq("id", chinaUnicomInfo.getId()));
    }

    @Override
    public int delChinaUnicomInfoById(Integer id) throws ServiceException {
        return chinaUnicomInfoMapper.delete(new UpdateWrapper<ChinaUnicomInfo>().eq("id", id));
    }

    @Override
    public int delChinaUnicomInfoByPhoneNum(String phoneNum) throws ServiceException {
        ChinaUnicomInfo chinaUnicomInfo = getChinaUnicomByPhoneNum(phoneNum);
        if(Objects.isNull(chinaUnicomInfo)){
            throw new ServiceException("手机号暂未收录");
        }
        return chinaUnicomInfoMapper.delete(new UpdateWrapper<ChinaUnicomInfo>().eq("phone_num", phoneNum));
    }

    private ChinaUnicomInfo transForm2ChinaUnicomInfo(ChinaUnicomInfoFrom form) {
        ChinaUnicomInfo chinaUnicomInfo = new ChinaUnicomInfo();
        BeanUtils.copyProperties(form, chinaUnicomInfo);
        return chinaUnicomInfo;
    }

}
