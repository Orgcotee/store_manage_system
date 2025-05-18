package com.example.backend.aspect;

import com.example.backend.anno.PreAuthorize;
import com.example.backend.common.JwtTokenUtil;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthorizeAspect {

    private final UserMapper mapper;

//    private final HttpServletRequest request;
    //配置切入点
    @Pointcut("@annotation(com.example.backend.anno.PreAuthorize)")
    public void authorizePointCut(){

    }
    //对后端接口进行鉴权

    /**
     * 1.查询用户所有接口权限列表
     * 2.获取当前接口所需要的访问标识符
     * 3.比较
     * @return
     */
    @Around("authorizePointCut()")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable{
        //1
        // 替换直接注入的 request
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        Integer userId = JwtTokenUtil.getIdFromToken(request.getHeader("token"));

        List<String> list = mapper.selectPermissionCodeById(userId);
        //2,反射获取当前接口的权限标识符
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        String permission = annotation.value();

        boolean result = list.contains(permission);
        //没有权限return null;
        if (!result){
            throw new ServiceException(ServiceCode.ERR_NO_AUTHORIZED,"用户没有权限");
        }
        //有权限继续执行
        return pjp.proceed();
    }
}
