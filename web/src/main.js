import Vue from "vue";
import App from "./App";
import router from "./router";
import store from "./store";
import VueResource from "vue-resource";

import Vuetify from "vuetify";
import vuetify from "./plugins/vuetify";

import axios from "axios";
import VueAxios from "vue-axios";

import VueIpfs from "./plugins/vue-ipfs";

import "./assets/css/style.css";

Vue.config.productionTip = false;

Vue.use(VueAxios, axios);
Vue.use(VueIpfs);

new Vue({
	Vuetify,
	router,
	store,
	vuetify,
	render: (h) => h(App),
}).$mount("#app");

Vue.use(VueResource);
