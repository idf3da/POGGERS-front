<template>
	<v-app>
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

		<v-main>
			<Main></Main>
			<Upload></Upload>
		</v-main>
	</v-app>
</template>

<script>
	import Main from "./components/Main.vue";
	import Upload from "./components/Upload.vue";

	export default {
		components: {
			Main,
			Upload,
		},
		data: () => ({}),
		created() {},
	};
</script>

<style lang="scss">
	@import "~vuetify/src/styles/styles.sass";

	.v-avatar.outlined {
		border: 3px solid blue;
	}
	.v-card-title {
		font-size: 1em;
	}
</style>
