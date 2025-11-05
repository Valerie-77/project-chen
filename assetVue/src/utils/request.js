import axios from "axios"
import { ElMessage } from 'element-plus'
const service = axios.create({
    baseURL: "/api",
    timeout: 1000
})

service.interceptors.request.use((config) => {
    return config;
}, (err) => {
    return Promise.reject(err)
});
service.interceptors.response.use((res) => {
    if (res.data.code == 200) {
        return res.data;
    } else {
        ElMessage({
            message: res.data.msg,
            type: 'error',
            duration:2000
        })
    }

}, err => {
    return Promise.reject(err);
});


export default service;