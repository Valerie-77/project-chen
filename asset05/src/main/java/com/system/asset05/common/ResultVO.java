package com.system.asset05.common;

import lombok.Data;
    
@Data
public class ResultVO {
    private int code;
    private String msg;
    private Object data;

    public static ResultVO success(Object data) {
        ResultVO vo = new ResultVO();
        vo.setCode(200);
        vo.setMsg("成功");
        vo.setData(data);
        return vo;
    }

    public static ResultVO success(String msg) {
        ResultVO vo = new ResultVO();
        vo.setCode(200);
        vo.setMsg(msg);
        vo.setData(null);
        return vo;
    }

    public static ResultVO error(int code, String msg) {
        ResultVO vo = new ResultVO();
        vo.setCode(code);
        vo.setMsg(msg);
        vo.setData(null);
        return vo;
    }
}