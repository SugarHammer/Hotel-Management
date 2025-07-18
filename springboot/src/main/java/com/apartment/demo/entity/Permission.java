package com.apartment.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@TableName("permission")
@Data
public class Permission {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String path;
    private String comment;
    private String icon;
    private Integer pid;
    private List<Permission> childrenList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        if(path == null || that.path == null){
            return childrenList.size() == that.childrenList.size();
        } else {
            return path.equals(that.path);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
