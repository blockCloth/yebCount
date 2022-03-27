package com.yeb.centre.controller;


import com.yeb.centre.pojo.Position;
import com.yeb.centre.pojo.RespBean;
import com.yeb.centre.service.IPositionService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2022-01-21
 */
@RestController
@RequestMapping("/system/basic/pot")
public class PositionController {
    @Autowired
    private IPositionService positionService;


    @ApiOperation(value = "查询职位信息")
    @GetMapping("/")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "新增职位信息")
    @PostMapping("/")
    public RespBean insertPosistion(@RequestBody Position position){
        //设置日期格式
        position.setCreateDate(LocalDate.now());
        if (positionService.save(position)){
            return RespBean.success("新增成功！");
        }
        return RespBean.error("新增失败！");
    }

    @ApiOperation(value = "修改职位信息")
    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position){
        if (positionService.updateById(position)){
            return RespBean.success("更新成功！");
        }
        if (positionService.updateById(position)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除职位信息")
    @DeleteMapping("/{id}")
    public RespBean deletePosition(@PathVariable Integer id){
        if (positionService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除职位信息")
    @DeleteMapping
    public RespBean batchDeletePosition(Integer[] ids){
        if (positionService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return RespBean.success("删除失败！");
    }

}
