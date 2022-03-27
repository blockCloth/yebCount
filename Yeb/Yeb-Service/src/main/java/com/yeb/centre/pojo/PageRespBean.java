package com.yeb.centre.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dai
 * @create 2022-02-2022/2/3  16-48-21
 * 分页公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRespBean {
    /**
     * 总条数
     */
    private Long total;
    /**
     * 返回list对象
     */
    private List<?> data;
}
