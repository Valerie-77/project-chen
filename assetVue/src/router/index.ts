import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            name: 'Login',
            component: () => import('@/views/login.vue'),
            beforeEnter: (to, from, next) => {
                const user = sessionStorage.getItem('user')
                if (user) {
                    next('/home')
                } else {
                    next()
                }
            }
        },
        {
            path: '/lenis',
            name: 'Lenis',
            component: () => import('@/views/lenis/LenisPage.vue')
        },
        {
            path: '/home',
            name: 'home',
            component: () => import('@/views/home/home.vue'),
            beforeEnter: (to, from, next) => {
                const user = sessionStorage.getItem('user')
                if (user) {
                    next()
                } else {
                    next('/login')
                }
            },
            children: [
                {
                    path: 'user',
                    name: 'User',
                    component: () => import('@/views/home/user.vue')
                },
                {
                    path: 'categories',
                    name: 'categories',
                    component: () => import('@/views/categories/categories.vue'),
                    meta: { title: '分类管理' }
                },
                {
                    path: 'parent',
                    name: 'parent',
                    component: () => import('@/views/parent/parent.vue'),
                    meta: { title: '分类管理' }
                }

            ]
        }
    ]
})

export default router