package com.zek.tools.guard.core.instance;

import cn.hutool.core.util.NumberUtil;
import com.zek.tools.guard.core.AppCommandLineRunner;
import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.action.TaskActionManager;
import com.zek.tools.guard.core.config.NotifyConfig;
import com.zek.tools.guard.core.config.TaskConfig;
import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.notify.NotifyTemplate;
import com.zek.tools.guard.core.notify.NotifyTemplateManager;
import com.zek.tools.guard.core.task.ScheduledFutureHolder;
import com.zek.tools.guard.core.task.TaskTemplate;
import com.zek.tools.guard.core.task.TaskTemplateManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author chenp
 * @date 2024/1/19
 */
@Slf4j
public class DynamicTasks {

    // 单实例
    private static DynamicTasks instance = null;

    /**
     * 任务实例集合
     */
    private final CopyOnWriteArrayList<TaskInstance> taskInstances = new CopyOnWriteArrayList<>();

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private  Map<String, ScheduledFutureHolder> scheduledFutureMap;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();


    /**
     * 获取TaskManager的实例
     * 如果实例不存在，则创建一个新的TaskManager实例并返回
     * @return TaskManager的实例
     */
    public static DynamicTasks getInstance() {
        if (instance == null) {
            instance = new DynamicTasks();
        }
        return instance;
    }

    /**
     * 私有构造方法，禁止外部实例化
     */
    private DynamicTasks() {
        init();
    }

    private void init() {
        this.scheduledFutureMap = new HashMap<>(1);
        if(threadPoolTaskScheduler == null) {
            this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            this.threadPoolTaskScheduler.setPoolSize(50);
            this.threadPoolTaskScheduler.initialize();
        }
    }

    /***
     * 创建定时执行器
     * @param cron
     * @param runnable
     * @return
     */
    private ScheduledFuture<?> createTaskScheduler(String cron, Runnable runnable) {
        ScheduledFuture<?> schedule = null;
        if (NumberUtil.isInteger(cron)) {
            schedule = threadPoolTaskScheduler.scheduleAtFixedRate(runnable, Duration.ofSeconds(Integer.parseInt(cron)));
        }
        else {
            schedule = threadPoolTaskScheduler.schedule(runnable, new CronTrigger(cron));
        }
        return schedule;
    }

    public void registerTask(String cron, String cronKey) {

        // 判断是否有
        ScheduledFutureHolder scheduledFutureHolder = scheduledFutureMap.get(cronKey);
        if (scheduledFutureHolder == null) {
            //将任务交给任务调度器执行
            ScheduledFuture<?> schedule = createTaskScheduler( cron, ()-> runTask(cronKey));
            //将任务包装成ScheduledFutureHolder
            scheduledFutureHolder = new ScheduledFutureHolder();
            scheduledFutureHolder.setScheduledFuture(schedule);
            scheduledFutureHolder.setCron(cron);
            scheduledFutureHolder.setCron(cronKey);
            scheduledFutureMap.put(cron, scheduledFutureHolder);
        }

    }

    /**
     * 运行任务
     *
     * @param cronKey 任务便利店
     */
    private void runTask(String cronKey) {
        // 同时通过cron和任务id去查找
        List<TaskInstance> list = taskInstances.stream().filter(it->it.getTaskConfig().getEnabled() && it.getTaskConfig().getCron().equals(cronKey)).collect(Collectors.toList());
        ScheduledFutureHolder scheduledFutureHolder = scheduledFutureMap.get(cronKey);
        // 如果不存在任务，则自毁
        if (list.isEmpty()) {
            ScheduledFuture<?> scheduledFuture = scheduledFutureHolder.getScheduledFuture();
            scheduledFuture.cancel(true);
            scheduledFutureMap.remove(cronKey);
            log.debug("表达式调度器由于没有可执行的任务，已经进行自毁:{}", cronKey);
            return;
        }
        log.debug("表达式调度器:{} 开始执行任务， 任务数:{}", cronKey, list.size());
        for (TaskInstance taskInstance: list) {
            executorService.submit(() -> {
                try {
                    // 查找任务模板
                    TaskTemplate taskTemplate = TaskTemplateManager.getInstance().getTaskTemplate(taskInstance.getTaskConfig().getTemplate());
                    // 依次运行Action
                    for (String actionName: taskTemplate.actions()) {
                        TaskAction taskAction = TaskActionManager.getInstance().getTemplate(actionName);
                        taskAction.execute(taskInstance.getTaskContext());
                    }
                    // 发送成功通知
                    if (taskInstance.getTaskConfig().getSuccessNotifyChannels() != null && taskInstance.getTaskConfig().getSuccessNotifyChannels().size() > 0) {
                        for (String notifyName: taskInstance.getTaskConfig().getSuccessNotifyChannels()) {
                            // 寻找通知实例
                            NotifyConfig notifyConfig = AppCommandLineRunner.appConfig.getNotifies().stream().filter(it->it.getName().equals(notifyName)).findFirst().orElse(null);
                            if (notifyConfig != null) {
                                NotifyTemplate notifyTemplate = NotifyTemplateManager.getInstance().get(notifyConfig.getType());
                                notifyTemplate.send("任务执行成功", "任务名称:" + taskInstance.getTaskConfig().getName(), notifyConfig.getProperties());
                            }

                        }
                    }

                }
                catch (Exception e) {
                    log.error("任务执行失败, 任务名称:{}, 异常信息", taskInstance.getTaskConfig().getName(), e);
                }
            });
        }


    }

    /**
     * 添加任务
     *
     * @param config 任务配置对象
     */
    public void addTask(TaskConfig config) {
        TaskInstance task = new TaskInstance();
        task.setTaskConfig(config);
        // 初始化上下文
        TaskContext context = new TaskContext();
        // 将properties加入进来
        context.setVariable("properties", config.getProperties());
        task.setTaskContext(context);
        taskInstances.add(task);
        // 注册任务
        registerTask(config.getCron(), config.getCron());
    }

}
