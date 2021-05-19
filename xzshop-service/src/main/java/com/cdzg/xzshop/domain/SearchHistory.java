package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * 搜索历史记录
 */
@ApiModel(value = "搜索历史记录")
@Data
@Builder
@TableName(value = "search_history")
public class SearchHistory implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 关键词
     */
    @TableField(value = "key_word")
    @ApiModelProperty(value = "关键词")
    private String keyWord;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 搜索次数
     */
    @TableField(value = "`count`")
    @ApiModelProperty(value = "搜索次数")
    private Long count;

    private static final long serialVersionUID = 1L;
}