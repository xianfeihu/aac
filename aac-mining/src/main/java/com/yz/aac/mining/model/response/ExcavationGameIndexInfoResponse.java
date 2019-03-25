package com.yz.aac.mining.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Data
@NoArgsConstructor
@ApiModel("挖矿游戏首页信息响应对象")
public class ExcavationGameIndexInfoResponse {

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "开始时间", position = 2)
    private Long beginTime;

    @ApiModelProperty(value = "结束时间", position = 3)
    private Long endTime;

    @ApiModelProperty(value = "进入等级限制", position = 4)
    private String levelLimit;

    @ApiModelProperty(value = "是否解锁（true-已解锁，false-未解锁）", position = 5)
    private Boolean isUnLock;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String userLevelName;

    public String getLevelLimit() {
        if (levelLimit==null) return "不限等级";
        String[] split = levelLimit.split(",");
        if (split.length == 1) return levelLimit;
        return split[0]+"-"+split[split.length-1];
    }

    public Boolean getIsUnLock() {
        if (levelLimit==null) return true;
        Arrays.stream(levelLimit.split(",")).forEach(x -> {
            if (x.equals(userLevelName)) {
                isUnLock=true;
                return;
            }
        });
        return isUnLock!=null ? isUnLock : false;
    }

    public ExcavationGameIndexInfoResponse(Long id, Long beginTime, Long endTime, String levelLimit) {
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.levelLimit = levelLimit;
    }
}
