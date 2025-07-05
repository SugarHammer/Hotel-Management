package com.apartment.demo.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.apartment.demo.common.Result;
import com.apartment.demo.entity.User;
import com.apartment.demo.service.RoomService;
import com.apartment.demo.entity.Room;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomService roomService;

    @GetMapping("/all")
    public Result<?> all(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize,
                         @RequestParam(defaultValue = "") String search,
                         @RequestParam(defaultValue = "") Integer adminId,
                         @RequestParam(defaultValue = "") Integer apartmentId,
                         @RequestParam(defaultValue = "是") String hasPeople){
        return roomService.FindAllRoom(pageNum, pageSize, search, adminId, apartmentId, hasPeople);
    }

    @GetMapping("/{id}")
    public Result<?> one(@PathVariable Integer id){
        return roomService.FindRoomByRoomId(id);
    }

    @GetMapping("/random")
    public Result<?> random(){
        return roomService.FindRoomByRandom();
    }

    @PostMapping
    public Result<?> save(@RequestBody Room room) {
        return roomService.SaveRoom(room);
    }

    @PutMapping
    public Result<?> update(@RequestBody Room room) {
        return roomService.UpdateRoom(room);
    }

    @PutMapping("/pass")
    public Result<?> pass(@RequestBody Map<String, Object> map) {
        return roomService.ChangePwd(map);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return roomService.DeleteRoom(id);
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
        writer.write(roomService.ExportRoom(), true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("房间信息", "UTF-8");
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
        return roomService.UploadRoom(file);
    }
}
