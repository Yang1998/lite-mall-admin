package com.yyt.axios.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.bytecode.annotation.Annotation;
import org.apache.ibatis.javassist.bytecode.annotation.StringMemberValue;

@Slf4j
public class AnnotationUtils<annoClass> {
    private static AnnotationUtils instance;

    public static AnnotationUtils getInstance() {
        if (instance == null) {
            synchronized (AnnotationUtils.class) {
                if (instance == null) {
                    instance = new AnnotationUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 修改注解上的属性值
     *
     * @param className  当前类名
     * @param methodName 当前方法名
     * @param annoName   方法上的注解名
     * @param fieldName  注解中的属性名
     * @param fieldValue 注解中的属性值
     * @throws NotFoundException
     */
    public void setAnnotatioinFieldStringValue(String className, String methodName, String annoName, String fieldName, String fieldValue) throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ct = classPool.get(className);
        CtMethod ctMethod = ct.getDeclaredMethod(methodName);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        ConstPool constPool = methodInfo.getConstPool();
        AnnotationsAttribute attr = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.visibleTag);
        Annotation annotation = attr.getAnnotation(annoName);
        if (annotation != null) {
            annotation.addMemberValue(fieldName, new StringMemberValue(fieldValue, constPool));
            attr.setAnnotation(annotation);
            methodInfo.addAttribute(attr);
        }
    }

    /**
     * 获得注解上的属性值
     * @param className  当前类名
     * @param methodName 当前方法名
     * @param annoName 方法上的注解名
     * @param fieldName 注解中的属性名
     * @return 注解的值（String）
     * @throws NotFoundException
     */
    public String setAnnotatioinFieldStringValue(String className, String methodName, String annoName, String fieldName) throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.getCtClass(className);
        CtMethod declaredMethod = ctClass.getDeclaredMethod(methodName);
        MethodInfo methodInfo = declaredMethod.getMethodInfo();
        AnnotationsAttribute attribute = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.visibleTag);
        if(attribute != null) {
            Annotation annotation = attribute.getAnnotation(annoName);
            if(annotation != null) {
                return ((StringMemberValue)annotation.getMemberValue(fieldName)).getValue();
            }
        }
        return null;
    }
}
