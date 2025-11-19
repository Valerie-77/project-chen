package com.system.asset05.mapper;


import com.system.asset05.entity.Parent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParentMapper {
    List<Parent> selectAllParents(@Param("page") int page, @Param("pageSize") int pageSize);

    int selectParentTotal();

    int addParent(Parent parent);

    int updateParent(Parent parent);

    int deleteParent(int id);

}
