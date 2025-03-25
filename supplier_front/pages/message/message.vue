<template>
	<view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		},
		mounted() {
			
		},
		onLoad() {
			const count = 20
			uni.setTabBarBadge({
			        index: 1,
			        text: count > 99 ? "99+" : `${count}`
			      })
			if(count === 0) uni.removeTabBarBadge({ index: 1 })
		}
	}
</script>

<style>

</style>
