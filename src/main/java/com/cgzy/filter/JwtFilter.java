package com.cgzy.filter;

import cn.hutool.json.JSONUtil;
import com.cgzy.common.lang.result;
import com.cgzy.entity.t_Admin;
import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Teacher;
import com.cgzy.service.AdminService;
import com.cgzy.service.StudentService;
import com.cgzy.service.TeacherService;
import com.cgzy.shiro.JwtToken;
import com.cgzy.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 用jwtFilter将jwt封装成jwtToken(主要)
 * */
@Component
public class JwtFilter extends AuthenticatingFilter {


    @Autowired
    private StudentService studentService;

    /**
     * createToken：实现登录，我们需要生成我们自定义支持的JwtToken
    * */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        System.out.println("进入了createTOken");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //从Header里面将jwt获取出来并封装成token形式
        String jwt = request.getHeader("authorization");
        System.out.println(jwt);
        if(StringUtils.isEmpty(jwt)) {
            onAccessDenied(servletRequest,servletResponse);
        }
        return new JwtToken(jwt);
    }
/**
 * onAccessDenied：拦截校验，
 * 当头部没有Authorization时候，我们直接通过，不需要自动登录；
 * 当带有的时候，首先我们校验jwt的有效性，
 * 没问题我们就直接执行executeLogin方法实现自动登录
 * */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        System.out.println("进入了Denied");
        System.out.println("request"+servletRequest.toString());
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("request"+request.getHeader("authorization"));

        String requestURI = request.getRequestURI().toString();
        System.out.println("请求里面的URL"+requestURI);


        String jwt = request.getHeader("authorization");
        if(StringUtils.isEmpty(jwt)) {
            System.out.println("没有jwt，返回true");
            return true;    //没有jwt就不需要交给shiro进行登录处理    后面只需要交给注解进行拦截就可以了
        } else {
            AntPathMatcher antPathMatcher = new AntPathMatcher(File.separator);
            if (antPathMatcher.match("/cgzy/user/**",requestURI)){
                System.out.println("是管理员进入的页面，验证管理员的身份");
//            校验jwt
                System.out.println("登录的jwt"+jwt);
//            通过jwt获取用户名
                String admin_num = JwtUtil.getUsername(jwt);
//              通过jwt获取用户的类型
                String userType = JwtUtil.getUserType(jwt);
                System.out.println("前端传来的jwt中用户账号"+admin_num);
                System.out.println("前端传来的jwt中用户类型"+userType);
                /**
                 * 因为spring加载bean在拦截器之后，所以在这里注入是无法获得，会报空指针异常
                 * 所以需要用WebApplicationContext来获取service类对象
                 */
                WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                AdminService bean = wac.getBean(AdminService.class);
                System.out.println("从WebApplicationContext里去获取的service"+bean);
                t_Admin admin = bean.getAdminInfoById(admin_num);
                if (StringUtils.isEmpty(admin)){
                    System.out.println("admin为空");
                    throw new Exception("用户为空");
                }
                String pw = admin.getAdmin_pw();
                System.out.println(jwt);
                System.out.println(admin_num);
                System.out.println(pw);
                System.out.println(userType);
                boolean verify = JwtUtil.verify(jwt, admin_num, pw,userType);
                System.out.println(verify);
                if(verify == false) {
                    throw new ExpiredCredentialsException("token已失效，请重新登录");
                }
            }else if (antPathMatcher.match("/cgzy/stu/**",requestURI)){
                System.out.println("是学生进入的页面，验证学生的身份");
//              校验jwt
                System.out.println("登录的jwt"+jwt);
//              通过jwt获取用户名和密码
                String user_num = JwtUtil.getUsername(jwt);
//              通过jwt获取用户的类型
                String userType = JwtUtil.getUserType(jwt);
                System.out.println("前端传来的jwt中用户账号"+user_num);

                /**
                 * 因为spring加载bean在拦截器之后，所以在这里注入是无法获得，会报空指针异常
                 * 所以需要用WebApplicationContext来获取service类对象
                 */
                WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                StudentService bean = wac.getBean(StudentService.class);
                System.out.println("从WebApplicationContext里去获取的service"+bean);
                t_Student student = bean.getStudentInfoById(user_num);
                if (StringUtils.isEmpty(student)){
                    System.out.println("student为空");
                    throw new Exception("用户为空");
                }
                String pw = student.getStudent_pw();
                boolean verify = JwtUtil.verify(jwt, user_num, pw,userType);
                if(verify == false) {
                    throw new ExpiredCredentialsException("token已失效，请重新登录");
                }
            }else if (antPathMatcher.match("/cgzy/tea/**",requestURI)) {
                System.out.println("是老师进入的页面，验证老师的身份");
//              校验jwt
                System.out.println("登录的jwt" + jwt);
//              通过jwt获取用户名和密码
                String user_num = JwtUtil.getUsername(jwt);
//              通过jwt获取用户的类型
                String userType = JwtUtil.getUserType(jwt);
                System.out.println("前端传来的jwt中用户账号" + user_num);

                /**
                 * 因为spring加载bean在拦截器之后，所以在这里注入是无法获得，会报空指针异常
                 * 所以需要用WebApplicationContext来获取service类对象
                 */
                WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                TeacherService bean = wac.getBean(TeacherService.class);


                System.out.println("从WebApplicationContext里去获取的service" + bean);
                t_Teacher teacher = bean.selectTeacherInfo(user_num);
                if (StringUtils.isEmpty(teacher)) {
                    System.out.println("teacher账号为空");
                    throw new Exception("用户为空");
                }
                String teacher_password = teacher.getPassword();
                boolean verify = JwtUtil.verify(jwt, user_num, teacher_password, userType);
                if (verify == false) {
                    throw new ExpiredCredentialsException("token已失效，请重新登录");
                }
            }else{
                throw new Exception("地址错误");
            }
        }
        // 执行登录    拿到token信息  然后交给realm处理
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     *  onLoginFailure：登录异常时候进入的方法，我们直接把异常信息封装然后抛出
     * */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();
        result result1 = result.Loginfail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result1);       //hutool工具

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }
/**
 * 跨域处理
 * preHandle：拦截器的前置拦截，
 * 因为我们是前后端分析项目，项目中除了需要跨域全局配置之外，
 * 我们再拦截器中也需要提供跨域支持。
 * 这样，拦截器才不会在进入Controller之前就被限制了
 * */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
