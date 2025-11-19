<script setup>
import service from "@/utils/request"
import { ElMessage, ElMessageBox } from "element-plus"
import { onMounted, ref, nextTick } from 'vue'

const treeData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const treeRef = ref()
const currentParentId = ref(0)

const form = ref({
    id: null,
    deptCode: '',
    deptName: '',
    parentId: 0,
    level: 1,
    path: '',
    managerId: 0,
    contact: '',
    status: 1
})

const onContainerClick = (event) => {
    const isTreeNode = event.target.closest('.el-tree-node__content')
    const isButton = event.target.closest('.el-button')

    if (!isTreeNode && !isButton) {
        if (treeRef.value) {
            treeRef.value.setCurrentKey(null)
            currentParentId.value = 0
        }
    }
}

const handleNodeClick = (nodeData) => {
    currentParentId.value = nodeData.id
}

const loadDepartments = async () => {
    const res = await service.get('/parent/list/1/1000')
    if (res.code === 200) {
        treeData.value = buildTree(res.data.data)
    }
}

const buildTree = (list) => {
    const map = {}, roots = []
    list.forEach(item => map[item.id] = {
        ...item,
        children: []
    })
    list.forEach(item => {
        if (item.parentId === 0) {
            roots.push(map[item.id])
        } else if (map[item.parentId]) {
            map[item.parentId].children.push(map[item.id])
        }
    })
    return roots
}

const showDialog = (data = null) => {
    dialogVisible.value = true
    if (data) {
        dialogTitle.value = '修改部门'
        nextTick(() => {
            form.value = {
                id: data.id,
                deptCode: data.deptCode,
                deptName: data.deptName,
                parentId: data.parentId,
                level: data.level,
                path: data.path,
                managerId: data.managerId,
                contact: data.contact,
                status: data.status
            }
        })
    } else {
        dialogTitle.value = '添加部门'
        form.value = {
            id: null,
            deptCode: '',
            deptName: '',
            parentId: currentParentId.value || 0,
            level: 1,
            path: '',
            managerId: 0,
            contact: '',
            status: 1
        }
    }
}

const save = () => {
    formRef.value.validate(async valid => {
        if (!valid) return

        try {
            const res = form.value.id
                ? await service.put('/parent/update', form.value)
                : await service.post('/parent/add', form.value)

            if (res.code === 200) {
                ElMessage.success(res.msg)
                dialogVisible.value = false
                loadDepartments()
                currentParentId.value = 0
            } else {
                ElMessage.warning(res.msg)
            }
        } catch (error) {
            ElMessage.error('操作失败')
        }
    })
}

const confirmDelete = (row) => {
    ElMessageBox({
        title: '确认删除',
        message: `
      <div class="cyber-confirm-content">
        <div class="warning-icon">⚠️</div>
        <div class="warning-text">您确定要删除部门 <span class="highlight">${row.deptName}</span> 吗？</div>
        <div class="warning-subtext">该操作不可撤销，请谨慎操作</div>
      </div>
    `,
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        showCancelButton: true,
        customClass: 'cyber-message-box',
        dangerouslyUseHTMLString: true,
        beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
                instance.confirmButtonLoading = true
                service.delete(`/parent/del/${row.id}`).then(res => {
                    instance.confirmButtonLoading = false
                    if (res.code === 200) {
                        ElMessage.success(res.msg)
                        loadDepartments()
                    } else {
                        ElMessage.warning(res.msg)
                    }
                    done()
                }).catch(() => {
                    instance.confirmButtonLoading = false
                    ElMessage.error('删除失败')
                    done()
                })
            } else {
                done()
            }
        }
    }).catch(() => {
        ElMessage.info('已取消删除操作')
    })
}

const getLevelType = (level) => {
    const levelTypes = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'danger'
    }
    return levelTypes[level] || 'info'
}

const getLevelText = (level) => {
    const levels = {
        1: '一级部门',
        2: '二级部门',
        3: '三级部门',
        4: '四级部门'
    }
    return levels[level] || `${level}级部门`
}

const getStatusText = (status) => {
    return status === 1 ? '启用' : '停用'
}

onMounted(loadDepartments)
</script>

<template>
    <div class="user-container" @click="onContainerClick">
        <div class="page-header">
            <div class="header-glitch" data-text="部门管理">部门管理</div>
            <div class="header-line"></div>
        </div>

        <div class="operation-bar">
            <el-button class="cyber-button primary" size="small" @click="showDialog(null)">
                <el-icon>
                    <CirclePlus />
                </el-icon>
                <span>新增部门</span>
                <div class="button-glitch"></div>
            </el-button>
        </div>

        <div class="table-container">
            <el-tree ref="treeRef" :data="treeData" node-key="id" default-expand-all class="cyber-tree"
                :highlight-current="true" @node-click="handleNodeClick">
                <template #default="{ node, data }">
                    <div class="tree-node">
                        <div class="node-info">
                            <span class="node-name">{{ data.deptName }}</span>
                            <span class="node-code">({{ data.deptCode }})</span>
                            <el-tag size="small" :type="data.status === 1 ? 'success' : 'danger'">
                                {{ getStatusText(data.status) }}
                            </el-tag>
                            <el-tag size="small" :type="getLevelType(data.level)">
                                {{ getLevelText(data.level) }}
                            </el-tag>
                            <span v-if="data.contact" class="node-contact">联系人: {{ data.contact }}</span>
                        </div>
                        <div class="node-actions">
                            <el-button class="cyber-button edit" size="small" @click="showDialog(data)">
                                <el-icon>
                                    <Edit />
                                </el-icon>
                                编辑
                            </el-button>
                            <el-button class="cyber-button danger" size="small" @click.stop="confirmDelete(data)">
                                <el-icon>
                                    <Delete />
                                </el-icon>
                                删除
                            </el-button>
                        </div>
                    </div>
                </template>
            </el-tree>
        </div>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600" class="cyber-dialog"
            :before-close="() => { formRef.value?.resetFields(); dialogVisible = false; currentParentId.value = 0 }">
            <el-form :model="form" ref="formRef" :rules="{
                deptName: [{ required: true, message: '请输入部门名称' }],
                level: [{ required: true, message: '请选择部门层级' }]
            }" class="cyber-form">
                <el-form-item label="上级部门">
                    <el-tree-select v-model="form.parentId" :data="treeData"
                        :props="{ value: 'id', label: 'deptName', children: 'children' }" placeholder="请选择上级部门"
                        clearable filterable class="cyber-input" />
                </el-form-item>

                <el-form-item label="部门名称" prop="deptName">
                    <el-input v-model="form.deptName" placeholder="请输入部门名称" class="cyber-input" />
                </el-form-item>

                <el-form-item label="部门代码" v-if="form.id">
                    <el-input v-model="form.deptCode" placeholder="部门代码" class="cyber-input" disabled />
                    <div class="form-tip">部门代码由系统自动生成</div>
                </el-form-item>

                <el-form-item label="部门层级" prop="level">
                    <el-select v-model="form.level" placeholder="请选择部门层级" class="cyber-input">
                        <el-option label="一级部门" :value="1" />
                        <el-option label="二级部门" :value="2" />
                        <el-option label="三级部门" :value="3" />
                        <el-option label="四级部门" :value="4" />
                    </el-select>
                </el-form-item>

                <el-form-item label="负责人ID">
                    <el-input-number v-model="form.managerId" :min="0" placeholder="负责人ID"
                        class="cyber-input cyber-input-number" controls-position="right" />
                </el-form-item>

                <el-form-item label="联系人">
                    <el-input v-model="form.contact" placeholder="请输入联系人信息" class="cyber-input" />
                </el-form-item>

                <el-form-item label="状态">
                    <el-radio-group v-model="form.status" class="cyber-radio">
                        <el-radio :value="1">启用</el-radio>
                        <el-radio :value="0">停用</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogVisible = false; currentParentId.value = 0"
                        class="cyber-button cancel">取消</el-button>
                    <el-button type="primary" @click="save" class="cyber-button primary">确定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped>
.user-container {
    padding: 20px;
    position: relative;
    z-index: 2;
    cursor: default;
}

.page-header {
    margin: 0 0 30px 0;
    position: relative;
}

.operation-bar {
    margin: 0 0 20px 0;
}

.tree-node {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 8px 0;
    border-bottom: 1px solid var(--cyber-border);
}

.node-info {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
}

.node-name {
    color: var(--cyber-text);
    font-weight: bold;
    font-size: 14px;
}

.node-code {
    color: var(--cyber-text-muted);
    font-size: 12px;
    background: var(--cyber-bg-light);
    padding: 2px 6px;
    border-radius: 4px;
}

.node-contact {
    color: var(--cyber-text-muted);
    font-size: 12px;
}

.node-actions {
    display: flex;
    gap: 8px;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}

.form-tip {
    font-size: 12px;
    color: var(--cyber-text-muted);
    margin-top: 4px;
}

.cyber-tree {
    background: var(--cyber-bg-card);
    border: 1px solid var(--cyber-border);
    border-radius: 8px;
    padding: 16px;
}

/* 只需要添加数字输入框的特殊样式 */
:deep(.cyber-input-number .el-input-number__decrease),
:deep(.cyber-input-number .el-input-number__increase) {
    background: linear-gradient(135deg, var(--cyber-bg), var(--cyber-bg-dark)) !important;
    border: 1px solid var(--cyber-border) !important;
    color: var(--cyber-primary) !important;
}

:deep(.cyber-input-number .el-input-number__decrease:hover),
:deep(.cyber-input-number .el-input-number__increase:hover) {
    background: linear-gradient(135deg, var(--cyber-bg), rgba(0, 240, 255, 0.2)) !important;
    border-color: var(--cyber-primary) !important;
}
</style>