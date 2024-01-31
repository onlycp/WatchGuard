package com.zek.tools.guard.core;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.zek.tools.guard.core.config.AppConfig;
import com.zek.tools.guard.core.instance.DynamicTasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

/**
 * @author chenp
 * @date 2024/1/31
 */
@Slf4j
@Component
public class AppCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String config = Files.readString(new File("app.json").toPath());
        AppConfig appConfig = JSONUtil.toBean(config, AppConfig.class);
        appConfig.getTasks().forEach(taskConfig -> {
            log.info("register task: {}", taskConfig);
            DynamicTasks.getInstance().addTask(taskConfig);
        });
    }
}
