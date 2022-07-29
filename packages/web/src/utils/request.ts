import axios from "axios";
import { useUserStore } from "../store";
import { Message } from "@arco-design/web-vue";
import router from "../router";

const instance = axios.create({
  baseURL: "http://127.0.0.1:8080/api/",
});

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    // @ts-ignore
    config.headers.common["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

instance.interceptors.response.use((response) => {
  if (response.data.code === 401) {
    router.push("/login");
    Message.error("登录已过期，请重新登录。");
    useUserStore().clear();
  }
  return response;
});

export default instance;
