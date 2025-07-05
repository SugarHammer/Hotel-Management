package com.apartment.demo.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.User;
import com.apartment.demo.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User userParam) {
        return userService.LoginUser(userParam);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return userService.Register(user);
    }

    @PostMapping("/id")
    public Result<?> matchId(@RequestBody User user) {
        return userService.MatchId(user);
    }


    @PostMapping
    public Result<?> save(@RequestBody User user) {
        return userService.SaveUser(user);
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        return userService.UpdateUser(user);
    }

    @PutMapping("/pass")
    public Result<?> pass(@RequestBody Map<String, Object> map) {
        return userService.ChangePwd(map);
    }

    // 改变权限接口
    @PutMapping("/changeRole")
    public Result<?> changeRole(@RequestBody User user) {
        return userService.ChangeRole(user);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return userService.DeleteUser(id);
    }

    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable Long id) {
        return userService.FindUserById(id);
    }

    @GetMapping("/all")
    public Result<?> findAll() {
        return userService.FindAllUser();
    }

    @GetMapping("/admin")
    public Result<?> findAdmin() {
        return userService.FindAdmin();
    }


    /**
     * 统计数据
     *
     * @return
     */
    @GetMapping("/count")
    public Result<?> count() {
        return userService.CountUser();
    }

    /**
     * 用户分页列表查询
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") Integer userId) {
        return userService.FindUserByPage(pageNum,pageSize,search,userId);
    }

    /**
     * Excel导出
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(userService.ExportUser(), true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    /**
     * Excel导入
     * 导入的模板可以使用 Excel导出的文件
     *
     * @param file Excel
     * @return
     * @throws IOException
     */
    @PostMapping("/import")
    public Result<?> upload(MultipartFile file) throws IOException {
        return userService.UploadUser(file);
    }

}
