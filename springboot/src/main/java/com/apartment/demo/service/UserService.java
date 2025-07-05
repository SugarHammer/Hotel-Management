package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {

    Result<?> LoginUser(User userParam);

    Result<?> Register(User user);

    Result<?> SaveUser(User user);

    Result<?> UpdateUser(User user);

    Result<?> ChangePwd(Map<String, Object> map);

    Result<?> DeleteUser(Long id);

    Result<?> ChangeRole(User user);

    Result<?> FindUserById(Long id);

    Result<?> FindAllUser();

    Result<?> FindAdmin();

    Result<?> CountUser();

    Result<?> FindUserByPage(Integer pageNum, Integer pageSize, String search, Integer userId);

    List<Map<String, Object>> ExportUser() throws IOException;

    Result<?> UploadUser(MultipartFile file) throws IOException;

    Result<?> MatchId(User user);
}
