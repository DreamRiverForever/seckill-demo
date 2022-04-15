package com.nuaa.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuaa.seckill.exception.GlobalException;
import com.nuaa.seckill.mapper.UserMapper;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.IUserService;
import com.nuaa.seckill.utils.MD5Util;
import com.nuaa.seckill.utils.ValidatorUtil;
import com.nuaa.seckill.vo.LoginVo;
import com.nuaa.seckill.vo.RespBean;
import com.nuaa.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-04-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public RespBean doLogin(LoginVo vo) {
        String mobile = vo.getMobile();
        String password = vo.getPassword();
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password))
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        if (!ValidatorUtil.isMobile(mobile))
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        User user = userMapper.selectById(mobile);
        if (null == user)
            // return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        if (!MD5Util.fromPassToDBPass(password, user.getSalt()).equals(user.getPassword()))
            // return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        return RespBean.success();
    }
}
