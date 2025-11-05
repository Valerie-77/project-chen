<script setup>
import service from "@/utils/request"
import { ElMessage, ElMessageBox } from "element-plus";
import { onMounted, ref, nextTick } from 'vue';

const userData = ref({});
const tableData = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);

// 添加/编辑用户相关
const userRef = ref()
let dialogTitle = ref()
let UData = ref({
  username: "",
  password: "",
  realName: "",
  gender: 1,
  userType: "1",
  email: "",
  mobile: "",
  status: 1,
  avatar: ""
})
let dialogVisible = ref(false)

//获取数据
const getUser = async (page) => {
  try {
    const result = await service.get(`/user/list/${page}/${pageSize.value}`);
    console.log(result)
    userData.value = result.data;
    tableData.value = result.data.data || [];
    currentPage.value = page;
  } catch (error) {
    console.error("获取用户数据失败:", error);
  }
}

const handlePageChange = (page) => {
  getUser(page);
}

//分页
let activePage = ref(1);//当前选中的页码
const pagination = (p) => {
  activePage.value = p
  getUser(p)
}

onMounted(() => {
  getUser(activePage.value);
})

//删除用户
const delUser = async (id) => {
  const result = await service.delete(`/user/del/${id}`)
  if (result.code == 200) {
    ElMessage.success(result.msg)
    getUser(activePage.value);
  }
}

// 自定义删除确认对话框
const confirmDelete = (id, username) => {
  ElMessageBox({
    title: '确认删除',
    message: `
      <div class="cyber-confirm-content">
        <div class="warning-icon">⚠️</div>
        <div class="warning-text">您确定要删除用户 <span class="highlight">${username}</span> 吗？</div>
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
        instance.confirmButtonLoading = true;
        delUser(id).finally(() => {
          instance.confirmButtonLoading = false;
          done();
        });
      } else {
        done();
      }
    }
  }).then(() => {
    // 确认删除后的逻辑已经在 beforeClose 中处理
  }).catch(() => {
    ElMessage.info('已取消删除操作');
  });
}

//添加用户 && 修改用户
const dialogIsShow = (u) => {
  dialogVisible.value = true;
  if (u != null) {
    //更新
    dialogTitle.value = "修改用户"
    nextTick(() => {
      Object.assign(UData.value, u)
    })
  } else {
    //添加
    dialogTitle.value = "添加用户"
    Object.assign(UData.value, {
      username: "",
      password: "",
      realName: "",
      gender: 1,
      userType: "1",
      email: "",
      mobile: "",
      status: 1,
      avatar: ""
    })
  }
}

const beforeColse = (done) => {
  if (userRef.value) {
    userRef.value.resetFields()
  }
  done()
}

const sendUser = (formEl) => {
  formEl.validate(async (valid) => {
    let result = null;
    if (valid) {
      if (UData.value?.id) {
        result = await service.put("/user/update", UData.value)
      } else {
        result = await service.post("/user/add", UData.value)
      }
      if (result.code == 200) {
        ElMessage.success(result.msg)
        formEl.resetFields();
        dialogVisible.value = false;
        getUser(activePage.value);
      }
    } else {
      console.log("submit is error")
    }
  })
}

// 头像上传成功处理
const uploadSuccess = (response) => {
  console.log('上传响应:', response);
  if (response.code === 200) {
    UData.value.avatar = response.data;
    ElMessage.success('头像上传成功');
  } else {
    ElMessage.error(response.msg || '头像上传失败');
  }
}

// 头像上传前的验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt10M = file.size / 1024 / 1024 < 10;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB!');
    return false;
  }
  return true;
}

// 头像上传错误处理
const uploadError = (error) => {
  console.error('上传错误:', error);
  ElMessage.error('头像上传失败，请重试');
}


const customUpload = (options) => {
  const formData = new FormData();
  formData.append('file', options.file);


  service.post('/api/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(response => {
    if (response.code === 200) {
      options.onSuccess(response);
    } else {
      options.onError(new Error(response.msg || '上传失败'));
    }
  }).catch(error => {
    options.onError(error);
  });
}
</script>
<template>
  <div class="user-container">
    <!-- 标题区域 -->
    <div class="page-header">
      <div class="header-glitch" data-text="用户管理">用户管理</div>
      <div class="header-line"></div>
    </div>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button class="cyber-button primary" size="small" @click="dialogIsShow(null)">
        <el-icon>
          <CirclePlus />
        </el-icon>
        <span>新增用户</span>
        <div class="button-glitch"></div>
      </el-button>
    </div>

    <!-- 表格 -->
    <div class="table-container">
      <el-table :data="tableData" class="cyber-table" size="small" empty-text="No Data">
        <el-table-column prop="id" label="编号" width="120" align="center" />
        <el-table-column prop="userCode" label="用户编码" width="150" align="center" />
        <el-table-column prop="username" label="用户名称" align="center" />
        <el-table-column prop="realName" label="真实姓名" width="150" align="center" />
        <el-table-column prop="avatar" label="用户头像" width="120" align="center">
          <template #default="scope">
            <el-avatar v-if="scope.row.avatar" :src="'http://localhost:8080/upload/' + scope.row.avatar" size="small" />
            <el-avatar v-else size="small">
              <span class="default-avatar">{{ scope.row.realName?.charAt(0) || scope.row.username?.charAt(0) || 'U'
              }}</span>
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="用户性别" width="100" align="center">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : scope.row.gender === 2 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="userType" label="用户类型" width="120" align="center">
          <template #default="scope">
            <el-text type="danger">{{ scope.row.userType == 0 ? '管理员' : '用户' }}</el-text>
          </template>
        </el-table-column>

        <el-table-column prop="email" label="电子邮箱" width="200" align="center" />
        <el-table-column prop="mobile" label="联系方式" width="150" align="center" />
        <el-table-column prop="status" label="用户状态" width="120" align="center">
          <template #default="scope">
            <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="2" inline-prompt active-text="开启"
              inactive-text="禁用" style="
        --el-switch-on-color: rgba(0, 240, 255, 0.3);
        --el-switch-off-color: rgba(255, 0, 102, 0.3);
        --el-switch-border-color: #00f0ff30;
      " class="cyber-switch" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button class="cyber-button edit" size="small" @click="dialogIsShow(scope.row)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑
            </el-button>
            <el-button class="cyber-button danger" size="small"
              @click="confirmDelete(scope.row.id, scope.row.username)">
              <el-icon>
                <Delete />
              </el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background layout="prev, pager, next" :total="userData.total || 0" :page-size="pageSize"
        :current-page="currentPage" @current-change="handlePageChange" class="cyber-pagination" @change="pagination" />
    </div>

    <!-- 添加/编辑用户对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500" :before-close="beforeColse"
      class="cyber-dialog">
      <el-form :model="UData" ref="userRef" class="cyber-form">
        <el-form-item label="用户名称" prop="username" :rules="[{ required: true, message: '请输入用户名称' }]">
          <el-input placeholder="请输入用户名称" v-model="UData.username" class="cyber-input" />
        </el-form-item>
        <el-form-item label="用户密码" prop="password" :rules="[{ required: true, message: '请输入用户密码' }]">
          <el-input placeholder="请输入用户密码" v-model="UData.password" type="password" class="cyber-input" />
        </el-form-item>
        <el-form-item label="用户姓名" prop="realName" :rules="[{ required: true, message: '请输入用户姓名' }]">
          <el-input placeholder="请输入用户姓名" v-model="UData.realName" class="cyber-input" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email" :rules="[{ required: true, message: '请输入电子邮箱' }]">
          <el-input placeholder="请输入电子邮箱" v-model="UData.email" class="cyber-input" />
        </el-form-item>
        <el-form-item label="联系方式" prop="mobile" :rules="[{ required: true, message: '请输入联系方式' }]">
          <el-input placeholder="请输入联系方式" v-model="UData.mobile" class="cyber-input" />
        </el-form-item>
        <el-form-item label="用户性别" prop="gender">
          <el-radio-group v-model="UData.gender" class="cyber-radio-group">
            <el-radio :value="1" size="large" class="cyber-radio">男</el-radio>
            <el-radio :value="2" size="large" class="cyber-radio">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-radio-group v-model="UData.userType" class="cyber-radio-group">
            <el-radio value="0" size="large" class="cyber-radio">管理员</el-radio>
            <el-radio value="1" size="large" class="cyber-radio">用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户状态" prop="status">
          <el-switch inline-prompt v-model="UData.status" :active-value="1" :inactive-value="2" active-text="开启"
            inactive-text="禁用" class="cyber-switch-dialog" />
        </el-form-item>
        <el-form-item label="用户头像">
          <el-upload class="cyber-upload" :http-request="customUpload" :show-file-list="false"
            :before-upload="beforeUpload" :on-success="uploadSuccess" :on-error="uploadError">
            <div class="upload-area">
              <img v-if="UData.avatar" class="avatar-preview" :src="'http://localhost:8080/upload/' + UData.avatar" />
              <div v-else class="upload-placeholder">
                <el-icon class="upload-icon">
                  <Plus />
                </el-icon>
                <div class="upload-text">点击上传头像</div>
              </div>
            </div>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cyber-button cancel">取消</el-button>
          <el-button type="primary" @click="sendUser(userRef)" class="cyber-button primary">确定</el-button>
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
  margin: 0 0 var(--cyber-gap-header) 0;
  position: relative;
}

.operation-bar {
  margin: 0 0 var(--cyber-gap-section) 0;
}

.default-avatar {
  color: var(--cyber-primary);
  font-weight: bold;
  text-shadow: 0 0 5px var(--cyber-primary);
}
</style>