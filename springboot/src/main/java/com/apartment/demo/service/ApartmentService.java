package com.apartment.demo.service;

import com.apartment.demo.common.Result;
import com.apartment.demo.entity.Apartment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApartmentService {

    Result<?> SaveApartment(Apartment apartment);

    Result<?> UpdateApartment(Apartment apartment);

    Result<?> DeleteApartment(Long id);

    Result<?> FindAllApartment(Integer pageNum, Integer pageSize, String search, Integer adminId);

    Result<?> FindAllApartmentList();

    Result<?> FindAllApartmentById(Integer adminId);

    Result<?> FindApartmentByRandom();

    List<Map<String, Object>> ExportApartment() throws IOException;

    Result<?> UploadApartment(MultipartFile file) throws IOException;
}
