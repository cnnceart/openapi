package cn.yiidii.openapi.task.controller;

import cn.yiidii.openapi.task.TaskComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 多点任务补偿
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-27 23:52
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("duodian")
public class DuoDianController {

    private final TaskComponent taskComponent;

    @GetMapping("checkIn")
    public Object checkIn() {
        taskComponent.duodianChenkIn();
        return "手动补偿多点成功";
    }

}
