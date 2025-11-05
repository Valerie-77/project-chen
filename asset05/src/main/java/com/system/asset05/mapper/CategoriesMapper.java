package com.system.asset05.mapper;

import com.system.asset05.entity.Categories;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoriesMapper {

    List<Categories> getCategories();

    int addCategories(Categories categories);

    int updateCategories(Categories categories);

    int deleteCategories(int id);

    int countChildrenByParentId(Integer parentId);
}