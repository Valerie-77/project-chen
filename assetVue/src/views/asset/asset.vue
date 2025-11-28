<script setup>
import service from "@/utils/request"
import { ElMessage, ElMessageBox } from "element-plus"
import { onMounted, ref, nextTick } from 'vue'
import { CirclePlus, Search, Plus } from '@element-plus/icons-vue'

const searchKeyword = ref('')
const tableData = ref([])
const dialogVisible = ref(false)
let dialogTitle = ref('')
const formRef = ref()

// 树形数据
const categoryTree = ref([])
const locationTree = ref([])
const deptTree = ref([])
const userOptions = ref([])

// 表单
let form = ref({
    id: null,
    assetCode: '',
    assetName: '',
    model: '',
    categoryId: null,
    locationId: null,
    deptId: null,
    unit: '',
    userId: null,
    image: '',
    remark: ''
})

// 安全查找树节点名称
const getNodeName = (treeRef, id, labelKey) => {
    if (!id) return '-'
    const tree = treeRef && treeRef.value ? treeRef.value : Array.isArray(treeRef) ? treeRef : []
    if (!Array.isArray(tree)) return '-'
    for (const node of tree) {
        if (node.id === id) return node[labelKey] || node.name || node.deptName || '-'
        if (node.children && Array.isArray(node.children)) {
            const found = getNodeName(node.children, id, labelKey)
            if (found !== '-') return found
        }
    }
    return '-'
}

// 构建树
const buildTree = (list) => {
    const map = {}, roots = []
    list.forEach(item => map[item.id] = { ...item, children: [] })
    list.forEach(item => {
        if (item.parentId === 0 || item.parentId == null) {
            roots.push(map[item.id])
        } else if (map[item.parentId]) {
            map[item.parentId].children.push(map[item.id])
        }
    })
    return roots
}

// 唯一加载下拉数据函数
const loadSelectOptions = async () => {
    try {
        const [catRes, locRes, deptRes, userRes] = await Promise.all([
            service.get('/categories/list'),
            service.get('/location/list'),
            service.get('/parent/list/1/1000'),
            service.get('/user/list/1/1000')
        ])

        if (catRes.code === 200) categoryTree.value = buildTree(catRes.data.data || catRes.data || [])
        if (locRes.code === 200) locationTree.value = buildTree(locRes.data.data || locRes.data || [])
        if (deptRes.code === 200) deptTree.value = buildTree(deptRes.data.data || deptRes.data || [])
        if (userRes && userRes.code === 200) {
            const list = userRes.data.data || []
            userOptions.value = list.map(u => ({
                value: Number(u.id),
                label: `${u.realName || u.username} (${u.username})`
            }))

        }
    } catch (error) {
        console.error('下拉数据加载失败', error)
    }
}

const loadData = async () => {
    try {
        const res = searchKeyword.value
            ? await service.get(`/asset/search?keyword=${encodeURIComponent(searchKeyword.value)}`)
            : await service.get('/asset/list')
        if (res.code === 200) {
            tableData.value = res.data.data || res.data || []
        }
    } catch (error) {
        console.error('资产加载失败', error)
    }
}

onMounted(() => {
    loadData()
    loadSelectOptions()
})

const dialogIsShow = (row = null) => {
    dialogVisible.value = true
    dialogTitle.value = row ? "修改资产" : "添加资产"
    if (row) {
        nextTick(() => Object.assign(form.value, { ...row }))
    } else {
        form.value = {
            id: null, assetCode: '', assetName: '', model: '',
            categoryId: null, locationId: null, deptId: null,
            unit: '', userId: null, image: '', remark: ''
        }
    }
}

const sendAsset = (formEl) => {
    formEl.validate(async (valid) => {
        if (!valid) return
        const res = form.value.id
            ? await service.put("/asset/update", form.value)
            : await service.post("/asset/add", form.value)
        if (res.code === 200) {
            ElMessage.success(res.msg || "操作成功")
            formEl.resetFields()
            dialogVisible.value = false
            loadData()
        }
    })
}

// 和 user.vue 完全一样的赛博风删除确认框
const confirmDelete = (id, assetName = '此资产') => {
    ElMessageBox({
        title: '确认删除',
        message: `
          <div class="cyber-confirm-content">
            <div class="warning-icon">Warning</div>
            <div class="warning-text">您确定要删除资产 <span class="highlight">${assetName}</span> 吗？</div>
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
                service.delete(`/asset/del/${id}`).then(res => {
                    if (res.code === 200) {
                        ElMessage.success('删除成功')
                        loadData()
                    }
                }).finally(() => {
                    instance.confirmButtonLoading = false
                    done()
                })
            } else {
                done()
            }
        }
    }).catch(() => {
        ElMessage.info('已取消删除')
    })
}

// 上传相关（和 user.vue 完全一致）
const uploadSuccess = (response) => {
    console.log('上传响应:', response)
    if (response.code === 200) {
        form.value.image = response.data
        ElMessage.success('上传成功')
    } else {
        ElMessage.error(response.msg || '上传失败')
    }
}

const beforeUpload = (file) => {
    const isImage = file.type.startsWith('image/')
    const isLt10M = file.size / 1024 / 1024 < 10
    if (!isImage) { ElMessage.error('只能上传图片文件!'); return false }
    if (!isLt10M) { ElMessage.error('图片大小不能超过 10MB!'); return false }
    return true
}

const customUpload = (options) => {
    const formData = new FormData()
    formData.append('file', options.file)
    service.post('/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    }).then(response => {
        if (response.code === 200) options.onSuccess(response)
        else options.onError(new Error(response.msg || '上传失败'))
    }).catch(error => options.onError(error))
}
</script>

<template>
    <div class="operation-bar">
        <el-input v-model="searchKeyword" placeholder="搜索资产名称/编号/型号" style="width: 240px; margin-right: 12px" clearable
            @clear="loadData" @keyup.enter="loadData">
            <template #prepend>
                <el-button @click="loadData"><el-icon>
                        <Search />
                    </el-icon></el-button>
            </template>
        </el-input>
    </div>

    <div class="user-container">
        <div class="page-header">
            <div class="header-glitch" data-text="资产清单">资产清单</div>
            <div class="header-line"></div>
        </div>

        <div class="operation-bar">
            <el-button class="cyber-button primary" size="small" @click="dialogIsShow()">
                <el-icon>
                    <CirclePlus />
                </el-icon>
                <span>新增资产</span>
                <div class="button-glitch"></div>
            </el-button>
        </div>

        <div class="table-container">
            <el-table :data="tableData" class="cyber-table" size="small">
                <el-table-column prop="assetCode" label="资产编号" width="140" />
                <el-table-column prop="assetName" label="资产名称" width="180" />
                <el-table-column prop="model" label="型号" width="120" />
                <el-table-column label="分类" width="130">
                    <template #default="{ row }">
                        {{ getNodeName(categoryTree, row.categoryId, 'name') }}
                    </template>
                </el-table-column>
                <el-table-column label="位置" width="130">
                    <template #default="{ row }">
                        {{ getNodeName(locationTree, row.locationId, 'name') }}
                    </template>
                </el-table-column>
                <el-table-column label="部门" width="130">
                    <template #default="{ row }">
                        {{ getNodeName(deptTree, row.deptId, 'deptName') }}
                    </template>
                </el-table-column>
                <el-table-column prop="unit" label="单位" width="80" />
                <el-table-column label="领用人" width="160">
                    <template #default="{ row }">
                        {{userOptions.find(u => u.value == row.userId)?.label || '-'}}
                    </template>
                </el-table-column>
                <el-table-column label="照片" width="100">
                    <template #default="{ row }">
                        <el-image v-if="row.image" :src="'http://localhost:8080/upload/' + row.image"
                            style="width:50px;height:50px;object-fit:cover" fit="cover"
                            :preview-src-list="['http://localhost:8080/upload/' + row.image]" />
                        <span v-else>-</span>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" show-overflow-tooltip />
                <el-table-column label="操作" width="160" fixed="right">
                    <template #default="scope">
                        <el-button class="cyber-button edit" size="small"
                            @click="dialogIsShow(scope.row)">编辑</el-button>
                        <el-button class="cyber-button danger" size="small"
                            @click="confirmDelete(scope.row.id, scope.row.assetName)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 添加/编辑弹窗 -->
        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600" class="cyber-dialog">
            <el-form :model="form" ref="formRef" :rules="{
                assetName: [{ required: true, message: '必填' }],
                model: [{ required: true, message: '必填' }],
                categoryId: [{ required: true, message: '必选' }],
                locationId: [{ required: true, message: '必选' }],
                deptId: [{ required: true, message: '必选' }]
            }">
                <el-form-item label="资产编号"><el-input v-model="form.assetCode" disabled
                        class="cyber-input" /></el-form-item>
                <el-form-item label="资产名称" prop="assetName"><el-input v-model="form.assetName"
                        class="cyber-input" /></el-form-item>
                <el-form-item label="型号" prop="model"><el-input v-model="form.model"
                        class="cyber-input" /></el-form-item>
                <el-form-item label="资产分类" prop="categoryId">
                    <el-tree-select v-model="form.categoryId" :data="categoryTree"
                        :props="{ label: 'name', children: 'children' }" node-key="id" check-strictly show-checkbox
                        placeholder="请选择分类" clearable filterable class="cyber-input" style="width:100%" />
                </el-form-item>
                <el-form-item label="存放位置" prop="locationId">
                    <el-tree-select v-model="form.locationId" :data="locationTree"
                        :props="{ label: 'name', children: 'children' }" node-key="id" check-strictly show-checkbox
                        placeholder="请选择位置" clearable filterable class="cyber-input" style="width:100%" />
                </el-form-item>
                <el-form-item label="所在部门" prop="deptId">
                    <el-tree-select v-model="form.deptId" :data="deptTree"
                        :props="{ label: 'deptName', children: 'children' }" node-key="id" check-strictly show-checkbox
                        placeholder="请选择部门" clearable filterable class="cyber-input" style="width:100%" />
                </el-form-item>
                <el-form-item label="计量单位"><el-input v-model="form.unit" class="cyber-input" /></el-form-item>
                <el-form-item label="领用人">
                    <el-select v-model="form.userId" placeholder="请选择领用人" clearable filterable class="cyber-input"
                        style="width:100%">
                        <el-option v-for="item in userOptions" :key="item.value" :label="item.label"
                            :value="item.value" />
                    </el-select>
                </el-form-item>
                <el-form-item label="资产照片">
                    <el-upload class="cyber-upload" :http-request="customUpload" :show-file-list="false"
                        :before-upload="beforeUpload" :on-success="uploadSuccess">
                        <div class="upload-area">
                            <img v-if="form.image" class="avatar-preview"
                                :src="'http://localhost:8080/upload/' + form.image" />
                            <div v-else class="upload-placeholder">
                                <el-icon class="upload-icon">
                                    <Plus />
                                </el-icon>
                                <div class="upload-text">点击上传照片</div>
                            </div>
                        </div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" type="textarea" :rows="4" class="cyber-input" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogVisible = false" class="cyber-button cancel">取消</el-button>
                    <el-button type="primary" @click="sendAsset(formRef)" class="cyber-button primary">确定</el-button>
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
}

.page-header {
    margin: 0 0 30px 0;
    position: relative;
}

.operation-bar {
    margin: 0 0 20px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.table-container {
    background: var(--cyber-bg-card);
    border: 1px solid var(--cyber-border);
    border-radius: 8px;
    padding: 16px;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}

/* 上传样式完全一致 */
.upload-area {
    width: 100px;
    height: 100px;
    border: 2px dashed var(--el-border-color);
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;
}

.upload-area:hover {
    border-color: var(--cyber-primary);
}

.avatar-preview {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 8px;
}

.upload-placeholder {
    text-align: center;
    color: var(--el-text-color-secondary);
}

.upload-icon {
    font-size: 28px;
    margin-bottom: 8px;
}

.upload-text {
    font-size: 12px;
}

/* 备注框发光边框 */
:deep(.el-textarea__inner) {
    background: transparent !important;
    border: 1px solid #00f0ff !important;
    box-shadow: 0 0 10px rgba(0, 240, 255, 0.5) !important;
}

:deep(.el-textarea__inner:focus) {
    box-shadow: 0 0 15px rgba(0, 240, 255, 0.8) !important;
}
</style>