import {createRouter, createWebHistory} from 'vue-router'
import Layout from "@/layout/Layout";

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import("@/views/Index")
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("@/views/Register")
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

// 在刷新页面的时候重置当前路由
activeRouter()

function activeRouter() {
    const userStr = sessionStorage.getItem("user")
    if (userStr) {
        const user = JSON.parse(userStr)
        let root = {
            path: '/hindex',
            name: 'Layout',
            component: Layout,
            redirect: "/hindex",
            children:[]
        }
        if(user.permissions){
            user.permissions.forEach(p => {
                if(p.childrenList.length > 0){
                    p.childrenList.forEach(c => {
                        let cobj = {
                            path: c.path,
                            name: c.comment,
                            component: () => import("@/views/" + c.name),
                        };
                        root.children.push(cobj)
                    });
                }
                else {
                    let obj = {
                        path: p.path,
                        name: p.comment,
                        component: () => import("@/views/" + p.name),
                    };
                    root.children.push(obj)
                }
            })
        }
        if (router) {
            router.addRoute(root)
        }
    }
}

router.beforeEach((to, from, next) => {
    if (to.path === '/login' || to.path === '/register' || to.path === '/') {
        next()
        return
    }
    let user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    //路由权限表，供拦截器拦截无效或者越权的url
    const permissionList = [];
    if(user.permissions){
        user.permissions.forEach(p => {
            if(p.childrenList.length > 0){
                p.childrenList.forEach(c => {
                    permissionList.push(c.path)
                })
            }
            else {
                permissionList.push(p.path)
            }
        });
    }
    if (!user.permissions || !user.permissions.length) {
        next('/login')
    } else if (!permissionList.find(p => p === to.path)) {
        next('/login')
    } else {
        next()
    }
})


export default router
