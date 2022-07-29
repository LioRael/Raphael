import { createRouter, createWebHistory } from "vue-router";

const Home = () => import("../pages/home/index.vue");
const Login = () => import("../pages/auth/login/login.vue");
const Register = () => import("../pages/auth/register/register.vue");
const Repository = () => import("../pages/repository/index.vue");
const Dashboard = () => import("../pages/dashboard/index.vue");

const routes = [
  { name: "home", path: "/", component: Home },
  { name: "login", path: "/login", component: Login },
  { name: "register", path: "/register", component: Register },
  {
    name: "workspace",
    path: "/:workspace",
    component: Repository,
  },
  {
    name: "repository",
    path: "/:workspace/:repository",
    component: Repository,
    children: [
      {
        name: "document",
        path: ":document",
        component: Repository,
        children: [
          {
            name: "document-edit",
            path: "edit",
            component: Repository,
          },
        ],
      },
      {
        name: "createDoc",
        path: "create/:type/:parent",
        component: Repository,
      },
    ],
  },
  { name: "dashboard", path: "/dashboard", component: Dashboard },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
