package com.apartment.demo.controller;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Contract;
import com.apartment.demo.entity.User;
import com.apartment.demo.service.ContractService;
import com.apartment.demo.utils.ExportWordUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/contract")
public class ContractController {

    @Resource
    private ContractService contractService;

    @GetMapping("/alluser")
    public Result<?> alluser(){
        return contractService.alluser();
    }

    @GetMapping("/allroom/{adminId}")
    public Result<?> allroom(@PathVariable Integer adminId){
        return contractService.allroom(adminId);
    }

    @GetMapping("/all")
    public Result<?> all(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize,
                         @RequestParam(defaultValue = "") String search,
                         @RequestParam(defaultValue = "") Integer adminId){
        return contractService.findAllContract(pageNum, pageSize, search, adminId);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id){
        return contractService.leaveApartment(id);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Contract contract) {
        return contractService.addContract(contract);
    }

    @PostMapping("/findself")
    public Result<?> findself(@RequestParam Integer userId){
        return contractService.findself(userId);
    }

    @PostMapping("/findselfhaspic")
    public Result<?> findselfHasPic(@RequestBody User user){
        return contractService.findselfHasPic(user.getId());
    }

    /**
     * 统计数据
     *
     * @return
     */
    @GetMapping("/count")
    public Result<?> count() {
        return contractService.CountContract();
    }

    @GetMapping("/demo/export/{id}")
    public void export(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> params = contractService.download(id);
        ExportWordUtils.exportWord("files/contract.docx","D:/test","房屋租赁合同.docx",params,request,response);
    }

}
