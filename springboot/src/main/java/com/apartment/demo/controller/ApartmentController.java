package com.apartment.demo.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.apartment.demo.common.Result;
import com.apartment.demo.service.ApartmentService;
import com.apartment.demo.entity.Apartment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    @Resource
    private ApartmentService apartmentService;

    @GetMapping("/all")
    public Result<?> all(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize,
                         @RequestParam(defaultValue = "") String search,
                         @RequestParam(defaultValue = "") Integer adminId){
        return apartmentService.FindAllApartment(pageNum, pageSize, search, adminId);
    }

    @GetMapping("/allList")
    public Result<?> allList(){
        return apartmentService.FindAllApartmentList();
    }

    @GetMapping("/{adminId}")
    public Result<?> all(@PathVariable Integer adminId){
        return apartmentService.FindAllApartmentById(adminId);
    }

    @PostMapping
    public Result<?> save(@RequestBody Apartment apartment) {
        return apartmentService.SaveApartment(apartment);
    }

    @PutMapping
    public Result<?> update(@RequestBody Apartment apartment) {
        return apartmentService.UpdateApartment(apartment);
    }

    @GetMapping("/random")
    public Result<?> random(){
        return apartmentService.FindApartmentByRandom();
    }


    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return apartmentService.DeleteApartment(id);
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
        writer.write(apartmentService.ExportApartment(), true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("酒店信息", "UTF-8");
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
        return apartmentService.UploadApartment(file);
    }
}
