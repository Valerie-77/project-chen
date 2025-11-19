package com.system.asset05.common;

public class ResultVO {
    private int code;
    private String msg;
    private Object data;

    // 手动添加 getter 和 setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

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

    public static ResultVO success(String msg, Object data) {
        ResultVO vo = new ResultVO();
        vo.setCode(200);
        vo.setMsg(msg);
        vo.setData(data);
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