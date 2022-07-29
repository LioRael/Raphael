import { createApp } from "vue";
import App from "./App.vue";
import ArcoVue from "@arco-design/web-vue";
import "./styles/index.less";
import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import router from "./router";
import { screens } from "vue-screen-utils";
import { VueQueryPlugin } from "vue-query";

const app = createApp(App);
app.use(ArcoVue);
app.use(router);
app.use(VueQueryPlugin);
app.use(ArcoVueIcon);
app.use(screens, {
  sm: "640px",
  md: "768px",
});
app.mount("#app");
