<script setup>
import { ElLoading } from 'element-plus'
import service from "@/utils/request"
import { reactive, ref } from 'vue';
import { useRouter } from "vue-router"
const router = useRouter();
const loginRef = ref();
let loginData = reactive({
    username: "",
    password: ""
})
const login = (formEl) => {
    formEl.validate((valid) => {
        if (valid) {
            const loading = ElLoading.service({
                lock: true,
                text: '正在登陆中...',
                background: 'rgba(0, 0, 0, 0.7)',
            })
            service.post("/user/login", {
                username: loginData.username,
                password: loginData.password
            }).then(res => {
                if (res) {
                    setTimeout(() => {
                        loading.close();
                        sessionStorage.setItem("user",JSON.stringify(res.data))
                        router.push("/home")
                    }, 2000)

                }

                
            })
        } else {
            console.error("submit is error")
        }
    })
}

</script>
<template>
    <div id="login">
        <main>
            <h3>登录</h3>
            <el-form ref="loginRef" label-width="auto" :model="loginData">
                <el-form-item label="账户" prop="username" :rules="[
                    { required: true, message: '请输入账户' }]">
                    <el-input type="username" autocomplete="off" v-model="loginData.username" placeholder="请输入账户" />
                </el-form-item>
                <el-form-item label="密码" prop="password" :rules="[
                    { required: true, message: '请输入密码' }]">
                    <el-input type="password" @keyup.enter="login(loginRef)" autocomplete="off" v-model="loginData.password" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary"  @click="login(loginRef)">立即登录</el-button>
                </el-form-item>
            </el-form>
        </main>
    </div>
</template>
<!-- scoped 私有样式 -->
<style scoped>
#login {
    width: 100%;
    height: 100vh;
    background: url("../../pic/p1.jpg") no-repeat;
    background-size: 100% 100%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

#login main {
    width: 380px;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 10px;
    margin-right: 35px;
    padding: 20px;
    box-sizing: border-box;
}

#login main h3 {
    margin-bottom: 30px;
}

.el-button {
    width: 200px;
    margin: 0 auto;
}
</style>