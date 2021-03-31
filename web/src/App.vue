<template>
	<v-app id="inspire">
		<v-app-bar app color="white" flat>
			<v-row align="center">
				<v-col class="pl-6">
					<v-row align="center">
						<v-img src="POGGERS_logo.png" max-height="40" max-width="40"></v-img>
						<v-toolbar-title>POGGERS</v-toolbar-title>
					</v-row>
				</v-col>

				<v-spacer></v-spacer>

				<v-col class="text-right">
					<v-tooltip class="pr-7" bottom transition="None">
						<template v-slot:activator="{ on, attrs }">
							<v-hover v-slot="{ hover }" open-delay="0" close-delay="0">
								<v-icon class="pr-7" v-bind="attrs" v-on="on" size="big" :color="hover ? 'rgb(80, 80, 255)' : 'rgb(180, 180, 180)'">
									mdi-bell
								</v-icon>
							</v-hover>
						</template>
						<span>Notifications</span>
					</v-tooltip>

					<v-tooltip class="pr-7" bottom transition="None">
						<template v-slot:activator="{ on, attrs }">
							<v-hover v-slot="{ hover }" open-delay="0" close-delay="0">
								<v-icon class="pr-7" v-bind="attrs" v-on="on" size="big" :color="hover ? 'rgb(80, 80, 255)' : 'rgb(180, 180, 180)'">
									mdi-account-group
								</v-icon>
							</v-hover>
						</template>
						<span>Community</span>
					</v-tooltip>

					<v-hover v-slot="{ hover }" open-delay="0" close-delay="0">
						<v-avatar v-bind="attrs" v-on="on" :class="hover ? 'outlined' : ''">
							<img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
						</v-avatar>
					</v-hover>
				</v-col>
			</v-row>
		</v-app-bar>

		<v-main class="grey lighten-3">
			<v-container>
				<v-row justify="center">
					<v-col cols="2" class="ml-8">
						<v-sheet rounded="lg">
							<v-list color="transparent">
								<v-list-item link>
									<v-list-item-content>
										<v-list-item-title>Home</v-list-item-title>
									</v-list-item-content>
								</v-list-item>
								<v-list-item link>
									<v-list-item-content>
										<v-list-item-title>Community</v-list-item-title>
									</v-list-item-content>
								</v-list-item>
								<v-list-item link>
									<v-list-item-content>
										<v-list-item-title>Messages</v-list-item-title>
									</v-list-item-content>
								</v-list-item>
								<v-list-item link>
									<v-list-item-content>
										<v-list-item-title>Friends</v-list-item-title>
									</v-list-item-content>
								</v-list-item>

								<!-- <v-divider class="my-2"></v-divider> -->
							</v-list>
						</v-sheet>
					</v-col>
					<v-col cols="5">
						<v-sheet min-height="90vh" rounded="lg">
							<v-col>
								<v-card v-for="post in posts" :key="post" class="ma-8" min-height="100px">
									<v-card-title v-if="post.title">
										{{ post.title }}
									</v-card-title>
									{{ post.description }}
									<my-image-component :hash="post.descriptorid"></my-image-component>
									<v-btn icon>
										<v-icon>mdi-heart</v-icon>
									</v-btn>
									<v-btn icon>
										<v-icon>mdi-comment</v-icon>
									</v-btn>
								</v-card>
							</v-col>
						</v-sheet>
					</v-col>

					<v-col cols="2" class="mr-8">
						<v-sheet min-height="90vh" rounded="lg">
							TEXT
							{{ posts }}
						</v-sheet>
					</v-col>
				</v-row>
			</v-container>
		</v-main>
	</v-app>
</template>

<script>
	import axios from "axios";

	// const ipfs = VueIpfs.create();

	export default {
		data: () => ({
			posts: {},
		}),
		created() {
			axios.get("http://localhost:9090/api/post/recent").then((response) => {
				this.posts = JSON.parse(response["data"]);
				// this.img = this.loadImage(this.posts[0]["descriptorid"]);
			});

			// //IPFS START
			// const repoPath = "ipfs" + Math.random();
			// const ipfs = new IPFS({ repo: repoPath });

			// ipfs.on("ready", () => {
			// 	let ipfsPath = this.input;
			// 	ipfs.files.cat(ipfsPath, function(err, file) {
			// 		if (err) {
			// 			throw err;
			// 		}
			// 		let img_data = file.toString("base64");
			// 		this.img = "data:image/png;base64," + img_data;
			// 	});

			// 	this.log = (line) => {
			// 		document.getElementById("output").appendChild(document.createTextNode(`${line}\r\n`));
			// 	};
			// });

			// (async () => {
			// 	const node = await IPFS.create();
			// 	node.bootstrap.add("/ip4/127.0.0.1/tcp/4001/p2p/12D3KooWD9BNsXhBvMiinAydUy4nk2SssytkndwBBQ2YGUqCnWxi");

			// 	const stream = node.cat(this.posts[0]["descriptorid"]);

			// 	for await (const chunk of stream) {
			// 		this.data += chunk.toString();
			// 	}

			// 	this.img = "data:image/png;base64," + this.data.toString("base64");
			// })().catch(console.error);
		},
		// methods: {
		// 	loadImage: async function(hash) {
		// 		try {
		// 			const ipfs = await IPFS.create();
		// 			let img = "data:image/png;base64,";
		// 			for await (const chunk of ipfs.cat(hash)) {
		// 				img = chunk.toString();
		// 			}
		// 			console.log(img);
		// 			return img;
		// 		} catch (error) {
		// 			throw new Error(error);
		// 		}
		// 	},
		// },
	};
</script>

<style lang="scss">
	@import "~vuetify/src/styles/styles.sass";

	.v-avatar.outlined {
		border: 3px solid blue;
	}
</style>
