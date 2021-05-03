<template>
	<div>
		<v-card class="d-flex justify-center grey lighten-3 rounded-4" flat tile min-width="600px">
			<v-card class="pa-2 grey lighten-3" outlined tile min-width="600px" max-width="600px">
				<v-card class="mb-3 rounded" flat v-for="post in posts" :key="post">
					<v-card-title>
						<v-avatar class="mr-3"><img src="ec.jpg" alt="John" /> </v-avatar>
						<v-col class="pa-0 mb-1">
							<a href="url" class="subtitle-2 ">{{ getUserInfo(post.creatorid) }}</a>
							<div class="caption mt-n2">two hours ago</div>
						</v-col>
						<!-- {{ getUserInfo(post.creatorid) }} {{ post.title }} -->
					</v-card-title>

					{{ post["description"] }}
					<v-img :src="'https://ipfs.io/ipfs/' + post['descriptorid']"></v-img>
				</v-card>
			</v-card>
		</v-card>
	</div>
</template>

<script>
	export default {
		components: {},
		data: () => ({
			posts: "",
			users: {},
		}),
		mounted() {
			this.axios.get("http://localhost:9090/api/post/recent").then((response) => {
				this.posts = response["data"];
			});
			this.getUserInfo(56749);
		},
		methods: {
			getUserInfo(creatorid) {
				if (!(creatorid in this.users)) {
					console.log("No" + creatorid + "in dict");
					this.axios.get("http://localhost:9090/api/user/" + creatorid.toString()).then((response) => {
						this.users[creatorid] = response["data"];
					});
					return this.users[creatorid].username;
				} else {
					console.log("Found" + creatorid + "in dict");
					return this.users[creatorid].username;
				}
			},
		},
	};
</script>

<style lang="scss">
	@import "~vuetify/src/styles/styles.sass";

	.v-avatar.outlined {
		border: 3px solid blue;
	}
	.v-card-title {
		font-size: 10rem;
	}
	a {
		text-decoration: none;
	}
</style>
