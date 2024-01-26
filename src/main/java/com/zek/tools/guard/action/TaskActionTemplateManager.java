package com.zek.tools.guard.action;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.zek.tools.guard.GuardApplication;
import com.zek.tools.guard.task.TaskTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chenp
 * @date 2024/1/20
 */
@Slf4j
public class TaskActionTemplateManager {

    // 单实例
    private static TaskActionTemplateManager instance = null;

    private Map<String, TaskActionTemplate> taskActionTemplateHashMap = new HashMap<>();

    /*

     */
    public static TaskActionTemplateManager getInstance() {
        if (instance == null) {
            instance = new TaskActionTemplateManager();
        }
        return instance;
    }

    /**
     * 私有构造方法，禁止外部实例化
     */
    private TaskActionTemplateManager() {
        this.scanTaskTemplates();
    }

    // 扫描指定包及其子包中指定父类的任务模板
    private void scanTaskTemplates() {
        // 使用反射工具类扫描指定包及其子包中指定父类的任务模板类
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(GuardApplication.class.getPackageName(), TaskActionTemplate.class);
        // 遍历任务模板类集合
        for (Class<?> clazz : classes) {
            // 实例化任务模板对象
            TaskActionTemplate taskTemplate = (TaskActionTemplate) ReflectUtil.newInstance(clazz);
            // 将任务模板对象添加到任务模板Map中，以任务模板名称作为键
            taskActionTemplateHashMap.put(taskTemplate.name(), taskTemplate);
        }
    }


    /**
     * 根据任务模板名称获取对应的任务模板
     * @param name 任务模板名称
     * @return 任务模板对象
     */
    public TaskActionTemplate getTaskTemplate(String name) {
        return taskActionTemplateHashMap.get(name);
    }

}
