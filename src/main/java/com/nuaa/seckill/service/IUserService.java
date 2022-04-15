package com.nuaa.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.vo.LoginVo;
import com.nuaa.seckill.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-04-14
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo vo);
}
