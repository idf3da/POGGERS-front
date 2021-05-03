<template>
	<v-main>
		<v-card class="d-flex justify-center mb-6 grey lighten-3" flat tile min-width="600px">
			<v-card class="pa-2" outlined tile min-width="600px" max-width="600px">
				<v-card>
					<v-text-field label="Title" v-model="newPost.title"></v-text-field>
					<v-text-field label="Descriptor" v-model="newPost.descriptorID"></v-text-field>
					<v-textarea color="teal" v-model="newPost.description">
						<template v-slot:label>
							<div>Bio <small>(optional)</small></div>
						</template>
					</v-textarea>
					<v-tooltip bottom transition="None">
						<template v-slot:activator="{ on, attrs }">
							<v-hover v-slot="{ hover }" open-delay="0" close-delay="0">
								<v-btn v-on:click="createPost">
									<v-icon v-bind="attrs" v-on="on" size="big" :color="hover ? 'rgb(80, 80, 255)' : 'rgb(180, 180, 180)'">
										mdi-send
									</v-icon>
								</v-btn>
							</v-hover>
						</template>
						<span>Upload post</span>
					</v-tooltip>
				</v-card>
			</v-card>
		</v-card>
	</v-main>
</template>

<script>
	import axios from "axios";

	export default {
		data() {
			return {
				errors: [],
				newPost: { title: "", descriptorid: "", description: "" },
			};
		},
		methods: {
			createPost() {
				// axios
				// 	.post("http://localhost:9090/api/post/create", {
				// 		body: { title: "LOOOOOL", descriptorID: "LOOOOOL", description: "LOOOOOL" },
				// 		headers: {
				// 			"Content-Type": "application/json",
				// 			"Access-Control-Allow-Origin": "*",
				// 		},
				// 	})
				// 	.then((response) => {
				// 		console.log(response.statusCode);
				// 	})
				// 	.catch((e) => {
				// 		this.errors.push(e);
				// 	});
				axios({
					method: "POST",
					url: "http://localhost:9090/api/post/create",
					data: { title: "", descriptorid: "", description: "" },
					headers: {
						"Content-Type": "application/json",
						"Access-Control-Allow-Origin": "*",
					},
				});
			},
		},
	};
</script>
