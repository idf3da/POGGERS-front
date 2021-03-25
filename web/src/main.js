import Vue from 'vue'
import App from "./App";
import router from './router'
import store from './store'
import VueResource from 'vue-resource';
import Vuetify from "vuetify"
import axios from "axios";
import vuetify from './plugins/vuetify';
import './assets/css/style.css'

Vue.config.productionTip = false

new Vue({
  Vuetify,
  router,
  store,
  axios,
  vuetify,
  render: h => h(App)
}).$mount("#app");

Vue.use(VueResource);