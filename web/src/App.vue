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
								<v-card class="ma-8" min-height="100px">
									<v-card-title>
										Title
									</v-card-title>
									<v-img contain src="placeholder_img.png"></v-img>

									<v-btn icon>
										<v-icon>mdi-heart</v-icon>
									</v-btn>
									<v-btn icon>
										<v-icon>mdi-comment</v-icon>
									</v-btn>
								</v-card>

								<v-card class="ma-8" min-height="100px">
									<v-card-title>
										Title
									</v-card-title>
									<v-img contain src="placeholder_img.png"></v-img>
									<v-btn icon>
										<v-icon>mdi-heart</v-icon>
									</v-btn>
									<v-btn icon>
										<v-icon>mdi-comment</v-icon>
									</v-btn>
								</v-card>
								<v-card class="ma-8" min-height="100px">
									<v-card-title>
										Title
									</v-card-title>
									<v-img contain src="placeholder_img.png"></v-img>
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
							{{ data["data"] }}
						</v-sheet>
					</v-col>
				</v-row>
			</v-container>
		</v-main>
	</v-app>
</template>

<script>
	import axios from "axios";

	export default {
		data: () => ({
			data: "Empty",
		}),
		mounted() {
			axios.get("http://localhost:8080/api/comments/for_post/1").then((response) => {
				this.data = response;
			});
		},
	};
</script>

<style lang="scss">
	@import "~vuetify/src/styles/styles.sass";

	.v-avatar.outlined {
		border: 3px solid blue;
	}
</style>
