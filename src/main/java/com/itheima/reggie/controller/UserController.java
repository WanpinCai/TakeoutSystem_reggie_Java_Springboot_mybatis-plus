package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.Service.UserService;
import com.itheima.reggie.common.Result;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.utils.TwilioSMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.hasText(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4).toString();
            log.info("code={}",code);

            //调用Twilio提供的短信服务api完成发送短信
//            TwilioSMSUtils.sendMessage("+15739952917", user.getPhone(), code);

            //需要将生成的验证码保存到Session
            session.setAttribute(phone,code);

            return Result.success("手机短信验证码发送成功");
        }
        return Result.error("短信发送失败");
    }

    /**
     * 移动端登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
        //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
        //判断当前的手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);

            }
            session.setAttribute("user",user.getId());
            return Result.success(user);
        }

        return Result.error("登录失败");
    }
}
