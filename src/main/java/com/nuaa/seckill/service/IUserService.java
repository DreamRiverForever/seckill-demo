package com.nuaa.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.vo.LoginVo;
import com.nuaa.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-04-14
 */
public interface IUserService extends IService<User> {

    /**
     * 登录功能
     * @param vo
     * @param request
     * @param response
     * @return
     */
    RespBean doLogin(LoginVo vo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
