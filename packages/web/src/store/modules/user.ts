import { defineStore } from "pinia";
import store from "../index";
import request from "../../utils/request";
import { Message } from "@arco-design/web-vue";

export const useUserStore = defineStore("user", {
  state: () => {
    return {
      // @ts-ignore
      user: JSON.parse(localStorage.getItem("user") ?? null),
      token: localStorage.getItem("token"),
    };
  },
  getters: {
    getUser(state) {
      return state.user;
    },
  },
  actions: {
    clear() {
      // @ts-ignore
      this.token = null;
      // @ts-ignore
      this.user = null;
    },
    login(email: string, password: string, router: any, reload: any) {
      request("/user/auth/login", {
        method: "POST",
        params: {
          email,
          password,
        },
      }).then((response) => {
        if (response.data.code === 200) {
          Message.success(response.data.message);
          localStorage.setItem("token", response.data.data.token);
          // @ts-ignore
          this.token = response.data.data.token;
          request("/user/info").then((response) => {
            if (response.data.code === 200) {
              // @ts-ignore
              this.user = response.data.data;
              localStorage.setItem("user", JSON.stringify(response.data.data));
              router.push("/");
              reload();
            }
          });
        } else {
          Message.error(response.data.message);
        }
      });
    },
    logout() {
      request("/user/auth/logout").then((response) => {
        if (response.data.code === 200) {
          Message.success(response.data.message);
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          // @ts-ignore
          this.token = null;
          // @ts-ignore
          this.user = null;
        } else {
          Message.error(response.data.message);
        }
      });
    },
    register(
      email: string,
      username: string,
      password: string,
      router: any,
      reload: any
    ) {
      request("/user/auth/register", {
        method: "POST",
        params: {
          email: email,
          username: username,
          password: password,
        },
      }).then((response) => {
        if (response.data.code === 200) {
          Message.success(response.data.message);
          localStorage.setItem("token", response.data.data.token);
          // @ts-ignore
          this.token = response.data.data.token;
          request("/user/info").then((response) => {
            if (response.data.code === 200) {
              // @ts-ignore
              this.user = response.data.data;
              localStorage.setItem("user", JSON.stringify(response.data.data));
            }
          });
          router.push("/");
          reload();
        } else {
          Message.error(response.data.message);
        }
      });
    },
  },
});

export const getUserStore = () => {
  return useUserStore(store);
};
