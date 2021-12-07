package top.meethigher.logmonitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author chenchuancheng
 * @since 2021/12/8 0:32
 */
@ApiModel("分页查询响应实体")
public class BasePageResponse<T> {
    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Integer number;

    /**
     * 页码大小
     */
    @ApiModelProperty("页码大小")
    private Integer size;

    /**
     * 页码总数
     */
    @ApiModelProperty("页码总数")
    private Integer totalPages;

    /**
     * 总数据
     */
    @ApiModelProperty("总数据")
    private Long totalElements;

    /**
     * data
     */
    @ApiModelProperty("业务数据")
    private T content;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}

