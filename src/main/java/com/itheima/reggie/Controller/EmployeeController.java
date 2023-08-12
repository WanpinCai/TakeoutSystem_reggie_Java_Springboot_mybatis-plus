package com.itheima.reggie.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.itheima.reggie.Service.EmployeeService;
import com.itheima.reggie.common.Result;
import com.itheima.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //1，将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("登录");

        //2，提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3，如果没有查询到则返回失败结果
        if(emp == null){
            return Result.error("登录失败");
        }
        //4，密码比对，错的话返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return Result.error("登录失败");
        }
        //5,查看员工状态，如果为已禁用，返回禁用结果
        if(emp.getStatus() == 0){
            return Result.error("账号已禁用");
        }
        //6,登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //清理Session中当前账号的
        request.getSession().removeAttribute("employee");
        return Result.success("logout success");
    }

    @PostMapping
    public Result<String> save(@RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee.toString());
        return null;
    }


}

