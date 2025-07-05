package com.apartment.demo.controller;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Engineer;
import com.apartment.demo.service.EngineerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/engineer")
public class EngineerController {

    @Resource
    private EngineerService engineerService;

    @GetMapping("/allEngineer")
    public Result<?> allEngineer(){
        return engineerService.allEngineer();
    }

    @GetMapping("/allroom/{adminId}")
    public Result<?> allroom(@PathVariable Integer adminId){
        return engineerService.allEngineerRoom(adminId);
    }

    @GetMapping("/all")
    public Result<?> all(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize,
                         @RequestParam(defaultValue = "0") String state,
                         @RequestParam(defaultValue = "") String search,
                         @RequestParam(defaultValue = "") Integer adminId){
        return  engineerService.findAllEngineerList(pageNum, pageSize, state, adminId, search);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Engineer engineer) {
        return engineerService.addEngineer(engineer);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Engineer engineer) {
        return engineerService.updateEngineer(engineer);
    }

    /**
     * 统计数据
     *
     * @return
     */
    @GetMapping("/count")
    public Result<?> count() {
        return engineerService.CountState();
    }

    @PostMapping("/findself")
    public Result<?> findself(@RequestParam Integer userId){
        return engineerService.findself(userId);
    }

}
