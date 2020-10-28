package com.xxx.interceptor;

import com.xxx.util.RedisUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;

/**
 * @description:redis缓存过滤器
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/28 3:22
 */
public class MethodCacheInterceptor implements MethodInterceptor {

    private RedisUtil redisUtil;
    private List<String> targetNamesList; // 禁用缓存的类名列表
    private List<String> methodNamesList; // 禁用缓存的方法列表
    private String defaultCacheExpireTime; // 缓存默认的过期时间

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
//        Object value = null;
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        if(!isAddCache(targetName, methodName)){
            //跳过缓存返回结果
            return invocation.proceed();
        }
        Object[] arguments = invocation.getArguments();
        String key = getCacheKey(targetName, methodName, arguments);
        try{
            if(redisUtil.exists(key)){//如果缓存中已经存在，直接从缓存中读取并返回
                return redisUtil.get(key);
            }
            //缓存中不存在，执行方法并将结果存入缓存，同时返回结果
            Object value = invocation.proceed();
            if(value != null){
                final String tkey = key;
                final Object tvalue = value;
                new Thread(() -> redisUtil.set(key, value, Long.parseLong(defaultCacheExpireTime))).start();
                redisUtil.set(key, value);
            }
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAddCache(String targetName, String methodName){
        if(targetNamesList.contains(targetName) || methodNamesList.contains(methodName) || targetName.contains("$$EnhancerBySpringCGLIB$$")){
            return false;
        }
        return true;
    }

    private String getCacheKey(String targetName, String methodName, Object[] arguments){
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append("_").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append("_").append(arguments[i]);
            }
        }
        return sb.toString();
    }
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void setTargetNamesList(List<String> targetNamesList) {
        this.targetNamesList = targetNamesList;
    }

    public void setMethodNamesList(List<String> methodNamesList) {
        this.methodNamesList = methodNamesList;
    }

    public void setDefaultCacheExpireTime(String defaultCacheExpireTime) {
        this.defaultCacheExpireTime = defaultCacheExpireTime;
    }
}
