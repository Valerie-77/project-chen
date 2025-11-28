<script setup>
import service from "@/utils/request"
import { ElMessage, ElMessageBox } from "element-plus"
import { onMounted, ref, nextTick } from 'vue'

const searchKeyword = ref('')

const loadData = async () => {
    try {
        const res = searchKeyword.value
            ? await service.get(`/location/search?keyword=${encodeURIComponent(searchKeyword.value)}`)
            : await service.get('/location/list')
        if (res.code === 200) {
            treeData.value = buildTree(res.data.data || res.data)
        }
    } catch (error) {
        console.log('位置加载失败', error)
    }
}

// 页面加载和搜索时都调用这个
onMounted(() => {
    loadData()
})

const treeData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const treeRef = ref()
const currentParentId = ref(0)

const form = ref({
    id: null,
    parentId: 0,
    name: '',
    remark: ''
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

const loadLocations = async () => {
    const res = await service.get('/location/list')
    if (res.code === 200) {
        treeData.value = buildTree(res.data.data || res.data)
    }
}

const buildTree = (list) => {
    const map = {}, roots = []
    list.forEach(item => map[item.id] = { ...item, children: [] })
    list.forEach(item => {
        if (item.parentId === 0) roots.push(map[item.id])
        else if (map[item.parentId]) map[item.parentId].children.push(map[item.id])
    })
    return roots
}

const showDialog = (data = null) => {
    dialogVisible.value = true
    if (data) {
        dialogTitle.value = '修改位置'
        nextTick(() => Object.assign(form.value, data))
    } else {
        dialogTitle.value = '添加位置'
        form.value = {
            id: null,
            parentId: currentParentId.value || 0,
            name: '',
            remark: ''
        }
    }
}

const save = () => {
    formRef.value.validate(async valid => {
        if (!valid) return
        const res = form.value.id
            ? await service.put('/location/update', form.value)
            : await service.post('/location/add', form.value)
        if (res.code === 200) {
            ElMessage.success(res.msg)
            dialogVisible.value = false
            loadLocations()
            currentParentId.value = 0
        }
    })
}

const confirmDelete = (row) => {
    ElMessageBox({
        title: '确认删除',
        message: `
      <div class="cyber-confirm-content">
        <div class="warning-icon">Warning</div>
        <div class="warning-text">您确定要删除位置 <span class="highlight">${row.name}</span> 吗？</div>
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
                service.delete(`/location/del/${row.id}`).then(res => {
                    instance.confirmButtonLoading = false
                    if (res.code === 200) {
                        ElMessage.success(res.msg)
                        loadLocations()
                    } else {
                        ElMessage.warning(res.msg)
                    }
                    done()
                }).catch(() => {
                    instance.confirmButtonLoading = false
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

onMounted(loadLocations)
</script>

<template>
    <div class="operation-bar">
        <el-input v-model="searchKeyword" placeholder="请输入关键词搜索" style="width: 240px; margin-right: 12px" clearable
            @clear="loadData" @keyup.enter="loadData">
            <template #prepend>
                <el-button @click="loadData">
                    <el-icon>
                        <Search />
                    </el-icon>
                </el-button>
            </template>
        </el-input>
    </div>
    <div class="user-container" @click="onContainerClick">
        <div class="page-header">
            <div class="header-glitch" data-text="资产位置管理">资产位置管理</div>
            <div class="header-line"></div>
        </div>

        <div class="operation-bar">
            <el-button class="cyber-button primary" size="small" @click="showDialog(null)">
                <el-icon>
                    <CirclePlus />
                </el-icon>
                <span>新增位置</span>
                <div class="button-glitch"></div>
            </el-button>
        </div>

        <div class="table-container">
            <el-tree ref="treeRef" :data="treeData" node-key="id" default-expand-all class="cyber-tree"
                :highlight-current="true" @node-click="handleNodeClick">
                <template #default="{ data }">
                    <div class="tree-node">
                        <div class="node-info">
                            <span class="node-name">{{ data.name }}</span>
                            <span v-if="data.remark" class="node-remark">（{{ data.remark }}）</span>
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

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500" class="cyber-dialog"
            :before-close="() => { formRef.value?.resetFields(); dialogVisible = false; currentParentId.value = 0 }">
            <el-form :model="form" ref="formRef" :rules="{ name: [{ required: true, message: '请输入位置名称' }] }"
                class="cyber-form">
                <el-form-item label="上级位置">
                    <el-tree-select v-model="form.parentId" :data="treeData"
                        :props="{ value: 'id', label: 'name', children: 'children' }" placeholder="请选择上级位置" clearable
                        filterable class="cyber-input" />
                </el-form-item>
                <el-form-item label="位置名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入位置名称" class="cyber-input" />
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" placeholder="选填" class="cyber-input" />
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

<!-- 完全复用你分类页面的样式 -->
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
}

.node-info {
    display: flex;
    align-items: center;
    gap: 8px;
}

.node-name {
    color: var(--cyber-text);
    font-weight: bold;
    font-size: 14px;
}

.node-remark {
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
</style>