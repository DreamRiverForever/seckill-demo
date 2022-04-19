package com.nuaa.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuaa.seckill.exception.GlobalException;
import com.nuaa.seckill.mapper.UserMapper;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.IUserService;
import com.nuaa.seckill.utils.CookieUtil;
import com.nuaa.seckill.utils.MD5Util;
import com.nuaa.seckill.utils.UUIDUtil;
import com.nuaa.seckill.vo.LoginVo;
import com.nuaa.seckill.vo.RespBean;
import com.nuaa.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVo vo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = vo.getMobile();
        String password = vo.getPassword();
        User user = userMapper.selectById(mobile);
        if (null == user)
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        if (!MD5Util.fromPassToDBPass(password, user.getSalt()).equals(user.getPassword()))
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        // 生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user" + ticket, user);
        // request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success();
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (userTicket == null)
            return null;
        User user = (User) redisTemplate.opsForValue().get("user" + userTicket);
        if (user != null){
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}
