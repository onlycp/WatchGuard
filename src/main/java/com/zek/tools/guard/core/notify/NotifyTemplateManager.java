package com.zek.tools.guard.core.notify;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.zek.tools.guard.GuardApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chenp
 * @date 2024/1/20
 */
@Slf4j
public class NotifyTemplateManager {

    // 单实例
    private static NotifyTemplateManager instance = null;

    private Map<String, NotifyTemplate> templateMap = new HashMap<>();

    /*

     */
    public static NotifyTemplateManager getInstance() {
        if (instance == null) {
            instance = new NotifyTemplateManager();
        }
        return instance;
    }

    /**
     * 私有构造方法，禁止外部实例化
     */
    private NotifyTemplateManager() {
        this.scanTemplates();
    }

    // 扫描指定包及其子包中指定父类的任务模板
    private void scanTemplates() {
        // 使用反射工具类扫描指定包及其子包中指定父类的任务模板类
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(GuardApplication.class.getPackageName(), NotifyTemplate.class);
        // 遍历任务模板类集合
        for (Class<?> clazz : classes) {
            // 实例化任务模板对象
            NotifyTemplate notifyTemplate = (NotifyTemplate) ReflectUtil.newInstance(clazz);
            // 将任务模板对象添加到任务模板Map中，以任务模板名称作为键
            templateMap.put(notifyTemplate.name(), notifyTemplate);
        }
    }


    /**
     * 根据任务模板名称获取对应的任务模板
     * @param name 任务模板名称
     * @return 任务模板对象
     */
    public NotifyTemplate get(String name) {
        return templateMap.get(name);
    }

}
