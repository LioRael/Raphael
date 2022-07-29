import { defineStore } from "pinia";
import store from "../index";

export const useDashboardStore = defineStore("dashboard", {
  state: () => {
    return {
      workspace: localStorage.getItem("workspace"),
    };
  },
  getters: {
    getWorkspace(state) {
      return state.workspace;
    },
  },
  actions: {
    setWorkspace(workspace: any) {
      this.workspace = workspace;
      localStorage.setItem("workspace", workspace);
    },
  },
});

export const getDashboardStore = () => {
  return useDashboardStore(store);
};
