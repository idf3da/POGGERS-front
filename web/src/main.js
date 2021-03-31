import Vue from "vue";
import App from "./App";
import router from "./router";
import store from "./store";
import VueResource from "vue-resource";
import Vuetify from "vuetify";
import IPFS from "ipfs";
import axios from "axios";
import vuetify from "./plugins/vuetify";
import "./assets/css/style.css";

Vue.config.productionTip = false;
let ipfsPromise = IPFS.create();

Vue.component("my-image-component", {
	props: {
		hash: { type: String, required: true },
	},
	data: () => {
		return {
			img: "data:image/png;base64,",
		};
	},
	template: `<v-img contain :src="'data:image/png;base64,'+img"></v-img>`,

	created() {
		this.loadImage();
	},
	methods: {
		loadImage: async function() {
			let ipfs = await ipfsPromise;
			try {
				let img = "";
				for await (const chunk of ipfs.cat(this.hash)) {
					img += chunk.toString("base64");
				}
				this.img = img;
			} catch (error) {
				throw new Error(error);
			}
		},
	},
});

new Vue({
	Vuetify,
	router,
	store,
	IPFS,
	axios,
	vuetify,
	render: (h) => h(App),
}).$mount("#app");

Vue.use(VueResource);
