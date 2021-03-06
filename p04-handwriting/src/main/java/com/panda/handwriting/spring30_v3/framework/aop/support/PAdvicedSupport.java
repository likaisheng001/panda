package com.panda.handwriting.spring30_v3.framework.aop.support;/**
 * Created by My on 2020-04-13.
 */

import com.panda.handwriting.spring30_v3.framework.aop.aspect.PAdvice;
import com.panda.handwriting.spring30_v3.framework.aop.aspect.PAfterReturningAdviceInterceptor;
import com.panda.handwriting.spring30_v3.framework.aop.aspect.PAspectJAfterThrowingAdvice;
import com.panda.handwriting.spring30_v3.framework.aop.aspect.PMethodBeforeAdviceInterceptor;
import com.panda.handwriting.spring30_v3.framework.aop.config.PAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Likaisheng
 * @Description: 解析AOP配置的工具类。
 *               这个类的命名有些奇怪，这个类明明是解析AOP配置的工具类，可是却叫PAdvicedSupport，为什么这样命名呢？
 *               我猜，是因为这个工具类解析出来的配置都是给Advice用的。所谓的AOP配置，也就是通知的配置。
 * @Date: Created in 15:34:12 2020-04-13
 * @Modified By:
 */
public class PAdvicedSupport {
    private PAopConfig config;
    private Object target;
    private Class targetClass;

    private Map<Method, List<Object>> methodCache;
    private Pattern pointCutClassPattern;

    public PAdvicedSupport(PAopConfig config) {
        this.config = config;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws NoSuchMethodException {

        // 从缓存中获取
        List<Object> cached = this.methodCache.get(method);
        // 缓存未命中，则进行下一步处理
        if (cached == null) {
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            cached = this.methodCache.get(m);
            // 存入缓存
            this.methodCache.put(m,cached);
        }
        return cached;
    }

    //解析配置文件的
    private void parse(){
        //把Spring的Excpress变成Java能够识别的正则表达式
        String pointCut = config.getPointCut()
                .replaceAll("\\.","\\\\.")
                .replaceAll("\\\\.\\*",".*")
                .replaceAll("\\(","\\\\(")
                .replaceAll("\\)","\\\\)");

        //三段
        //第一段：方法的修饰符和返回值
        //第二段：类名
        //第三段：方法的名称和形参列表

        //保存专门匹配Class的正则
        String pointCutForClassRegex = pointCut.substring(0,pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, List<Object>>();

        //保存专门匹配方法的正则
        Pattern pointCutPattern = Pattern.compile(pointCut);
        try {
            Class aspectClass = Class.forName(this.config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<String, Method>();
            for (Method method : aspectClass.getMethods()){
                aspectMethods.put(method.getName(),method);
            }

            //以上都是初始化工作，准备阶段
            //从这里开始封装PAdvice
            for (Method method : this.targetClass.getMethods()){
                String methodString = method.toString();
                if (methodString.contains("throws")){
                    methodString = methodString.substring(0,methodString.lastIndexOf("throws")).trim();

                }
                Matcher matcher = pointCutPattern.matcher(methodString);
                if (matcher.matches()){
                    LinkedList<Object> advices = new LinkedList<Object>();
                    if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore()))){
                        advices.add(new PMethodBeforeAdviceInterceptor(aspectClass.newInstance(),aspectMethods.get(config.getAspectBefore())));
                    }

                    if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter()))){
                        advices.add(new PAfterReturningAdviceInterceptor(aspectClass.newInstance(),aspectMethods.get(config.getAspectAfter())));
                    }

                    if (!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow()))){
                        PAspectJAfterThrowingAdvice advice = new PAspectJAfterThrowingAdvice(aspectClass.newInstance(), aspectMethods.get(config.getAspectAfterThrow()));
                        advice.setThrowName(config.getAspectAfterThrowingName());
                        advices.add(advice);
                    }
                    //目标代理类的业务方法和Advices建立一个一对多的关联关系，以便在Proxy类中获得
                    methodCache.put(method,advices);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*//根据一个目标代理类的方法，获取其对应的通知
    public Map<String, PAdvice> getAdvices(Method method, Object o) throws Exception {
        //享元设计模式的应用
        Map<String, PAdvice> cache = methodCache.get(method);
        if (null == cache){
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            cache = methodCache.get(m);
            this.methodCache.put(m,cache);
        }
        return cache;
    }*/

    //给ApplicationContext IOC中的对象初始化时调用，决定要不要生成代理类的逻辑
    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class getTargetClass() {
        return this.targetClass;
    }

    public Object getTarget() {
        return this.target;
    }
}
