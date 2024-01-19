package com.zek.tools.guard.task;

import com.zek.tools.guard.action.Action;

/**
 * @author chenp
 * @date 2024/1/19
 */
public interface TaskTemplate {

    /**
     *
     * @return
     */
    String name();

    Action[] actions();


}
