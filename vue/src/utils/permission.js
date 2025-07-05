import router from "@/router";
import Layout from "@/layout/Layout";

// 注意：这个文件是设置动态路由的
// permissions是一个资源的数组
export function activeRouter() {
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
        if (router) {
            router.addRoute(root)
        }
    }
}


