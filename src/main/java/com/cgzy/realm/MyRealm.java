package com.cgzy.realm;


import com.cgzy.entity.t_Admin;
import com.cgzy.entity.t_Student;
import com.cgzy.entity.t_Teacher;
import com.cgzy.service.AdminService;
import com.cgzy.service.StudentService;
import com.cgzy.service.TeacherService;
import com.cgzy.shiro.JwtToken;
import com.cgzy.utils.JwtUtil;
import com.cgzy.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class MyRealm extends AuthorizingRealm {

    @Resource
    private StudentService studentServcie;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AdminService adminService;
    @Resource
    private TeacherService teacherService;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
//        System.out.println("===============Shiro权限认证开始============ [ roles、permissions]==========");
//        System.out.println("权限的principal"+principal);
//
//        String student_num = null;
//        if (principal != null) {
//            //这里为什么可以强转成LoginUser呢？
//            //因为在登录验证时，我们new SimpleAuthenticationInfo传入的第一个参数就是t_Student
//             t_student primaryPrincipal = (t_student) principal.getPrimaryPrincipal();
//            System.out.println(primaryPrincipal);
//            student_num = primaryPrincipal.getStudent_num();
//        }
//
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        System.out.println(student_num);
////        从数据库获取角色信息和权限添加给当前subject
//        Set<String> userRole = roleServcie.RoleByStudentId(student_num);
//        List<String> userPermission = permissionDao.Role_PermissionById(student_num);
//
//        info.setRoles(userRole);
//        info.addStringPermissions(userPermission);
//        System.out.println(userRole);
//        System.out.println(userPermission);
//        System.out.println(info);
//
//        // 设置用户拥有的角色集合，比如“admin,test”
//        //sysUserService.getUserRolesSet(username);从数据库中获取到用户所拥有的角色信息
////        Set<String> roleSet = new HashSet<>();
////        roleSet.add("admin");//模拟
////        roleSet.add("visit");
////        info.setRoles(roleSet);
//
////        数据库获取角色
//
//        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
//        //sysUserService.getUserPermissionsSet(username);从数据库获取到用户的权限信息
////        Set<String> permissionSet = new HashSet<>();
////        permissionSet.add("sys:role:add");
////        permissionSet.add("sys:user:add");
////        info.addStringPermissions(permissionSet);
////        log.info("===============Shiro权限认证成功==============");
//        return info;
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //这里getPrincipal和getCredentials是一样的
        //因为我们在
        // JwtToken jwtToken = new JwtToken(token);
        // getSubject(request, response).login(jwtToken);
        // 提交的是JwtToken，jwtToken中Principal和Credentials都是token
        String credentials = (String) token.getPrincipal();

        System.out.println("进入自定义的realm，token"+credentials);

        if (credentials == null) {
            throw new AuthenticationException("token为空!");
        }
        // 校验token有效性
//        根据token取出类型进行强转
//        t_Student student=null;
//        t_Admin admin=null;

//        String userType = JwtUtil.getUserType(credentials);
//        if (userType=="A"){
//            Object loginUser = this.checkUserTokenIsEffect(credentials);
//        }else if (userType == "S"){
//            Object loginUser = this.checkUserTokenIsEffect(credentials);
//        }
        Object loginUser = this.checkUserTokenIsEffect(credentials);
        //return SimpleAuthenticationInfo之后会进入到方法 assertCredentialsMatch(token, info);
        //该方法比较info中的Credential和token中的Credential，具体表现在
        //Object tokenHashedCredentials = hashProvidedCredentials(token, info);取出token中的Credential
        //Object accountCredentials = getCredentials(info);取出authenticationInfo中的Credential
        //equals(tokenHashedCredentials, accountCredentials);
        return new SimpleAuthenticationInfo(loginUser, credentials, getName());
    }

    /**
     * 校验token的有效性
     *
     * @param token
     */
    public Object checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        System.out.println(token);
        String userNum = JwtUtil.getUsername(token);
        String userType = JwtUtil.getUserType(token);
        System.out.println("doget:userNum"+userNum);
        System.out.println("doget:usertype"+userType);

        if (userNum == null) {
            throw new AuthenticationException("token非法无效!");
        }

        // 查询用户信息
        System.out.println("———校验token是否有效————checkUserTokenIsEffect——————— "+ token);
        //TODO 查询数据库，根据username得到loginUser
        if (userType.equals("A")){
            System.out.println("myrealm判断admin类型");
            t_Admin admin = adminService.getAdminInfoById(userNum);
            if (admin == null) {
                throw new AuthenticationException("用户不存在!");
            }
//            // 判断用户状态
//            if (admin.getStatus() != 1) {
//                throw new AuthenticationException("账号已被锁定,请联系管理员!");
//            }
            // 校验token是否超时失效 & 或者账号密码是否错误
            if (!jwtTokenRefresh(token, userNum, admin.getAdmin_pw(),userType)) {
                throw new AuthenticationException("Token失效，请重新登录!");
            }
            return admin;
        }else if (userType.equals("S")){
            System.out.println("myrealm判断student类型");
            t_Student student = studentServcie.getStudentInfoById(userNum);
            System.out.println(student);
            if (student == null) {
                throw new AuthenticationException("用户不存在!");
            }
            // 判断用户状态
            if (student.getStatu() != 1) {
                throw new AuthenticationException("账号已被锁定,请联系管理员!");
            }
            // 校验token是否超时失效 & 或者账号密码是否错误
            if (!jwtTokenRefresh(token, userNum, student.getStudent_pw(),userType)) {
                throw new AuthenticationException("Token失效，请重新登录!");
            }
            return student;
        }else if(userType.equals("T")){
            System.out.println("myrealm判断teacher类型");
            t_Teacher teacher = teacherService.selectTeacherInfo(userNum);
            if (teacher == null) {
                throw new AuthenticationException("用户不存在!");
            }
            System.out.println("1231");
            // 校验token是否超时失效 & 或者账号密码是否错误
            if (!jwtTokenRefresh(token, userNum, teacher.getPassword(),userType)) {
                throw new AuthenticationException("Token失效，请重新登录!");
            }
            System.out.println(teacher);
            return teacher;
        }
        return null;
    }

    /**
     * TODO:
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     *       用户过期时间 = Jwt有效时间 * 2。
     * @param token
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord,String userType){
//        String cacheToken = String.valueOf(redisUtil.get(token));
        String cacheToken = String.valueOf(redisUtil.get(userName));
        if (StringUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord,userType)) {
                String newAuthorization = JwtUtil.sign(userName, passWord,userType);
                // 设置超时时间
//                redisUtil.set(token, newAuthorization);
                redisUtil.set(userName, newAuthorization);
                redisUtil.expire(userName, JwtUtil.EXPIRE_TIME *2 / 1000);
//                redisUtil.expire(token, JwtUtil.EXPIRE_TIME *2 / 1000);
                System.out.println("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— "+ token);
            }
            return true;
        }
        return false;
    }


}
