package com.system.asset05.entity;

public class Parent {
    private int id;
    private String deptCode;
    private String deptName;
    private int parentId;
    private int level;
    private String path;
    private int manager_id;  // 保持原有属性名
    private String contact;
    private int status;

    // 添加 managerId 的 getter 和 setter（驼峰命名）
    public int getManagerId() {
        return manager_id;
    }

    public void setManagerId(int managerId) {
        this.manager_id = managerId;
    }

    // 保持原有的 manager_id 的 getter/setter
    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    // 其他已有的 getter/setter 保持不变...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", deptCode='" + deptCode + '\'' +
                ", deptName='" + deptName + '\'' +
                ", parentId=" + parentId +
                ", level=" + level +
                ", path='" + path + '\'' +
                ", manager_id=" + manager_id +
                ", contact='" + contact + '\'' +
                ", status=" + status +
                '}';
    }
}